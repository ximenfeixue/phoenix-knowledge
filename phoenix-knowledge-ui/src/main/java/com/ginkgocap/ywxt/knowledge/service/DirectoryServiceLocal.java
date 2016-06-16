package com.ginkgocap.ywxt.knowledge.service;

import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.ginkgocap.parasol.directory.exception.DirectoryServiceException;
import com.ginkgocap.parasol.directory.exception.DirectorySourceServiceException;
import com.ginkgocap.parasol.directory.model.Directory;
import com.ginkgocap.parasol.directory.model.DirectorySource;
import com.ginkgocap.parasol.directory.service.DirectoryService;
import com.ginkgocap.parasol.directory.service.DirectorySourceService;
import com.ginkgocap.ywxt.knowledge.model.DataCollection;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeBase;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeDetail;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeUtil;
import com.ginkgocap.ywxt.knowledge.service.common.KnowledgeBaseService;
import com.gintong.frame.util.dto.CommonResultCode;
import com.gintong.frame.util.dto.InterfaceResult;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by gintong on 2016/6/14.
 */
@Service("directoryServiceLocal")
public class DirectoryServiceLocal extends BaseServiceLocal implements KnowledgeBaseService
{
    private Logger logger = LoggerFactory.getLogger(DirectoryServiceLocal.class);

    @Resource
    private DirectorySourceService directorySourceService;

    @Resource
    private DirectoryService directoryService;

    //@Resource
    //private KnowledgeService knowledgeService;


    public List<Long> createDirectory(long userId,short type,String directoryName) throws Exception
    {
        List<Long> directoryIds = new ArrayList<Long>();
        List<Directory> directoryList = directoryService.getDirectorysForRoot(APPID, userId, 3933392601350164L);
        if (directoryList != null && directoryList.size() >= 5) {
            for (Directory directory : directoryList) {
                directoryIds.add(directory.getId());
            }
            return directoryIds;
        }

        Directory directory = new Directory();
        directory.setUserId(userId);
        directory.setAppId(APPID);
        directory.setName(directoryName);
        directory.setTypeId(3933392601350164L);//This get from DB
        Long directoryId =  directoryService.createDirectoryForRoot(userId, directory);
        directoryIds.add(directoryId);
        return directoryIds;
    }

    public InterfaceResult batchCatalogs(KnowledgeService knowledgeService,long userId,String requestJson) throws Exception {
        logger.info("batchTags: {}", requestJson );
        if(StringUtils.isEmpty(requestJson)) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION);
        }

        List<LinkedHashMap<String, Object>> directoryItems =  KnowledgeUtil.readValue(List.class, requestJson);
        if (directoryItems == null || directoryItems.size() <= 0) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION);
        }

        try {
            for (int index = 0; index < directoryItems.size(); index++) {
                Map<String, Object> map = directoryItems.get(index);
                //Set<String> set = map.keySet();
                String title = map.get("title").toString();
                long knowledgeId = Long.parseLong(map.get("id").toString());
                List<String> directoryIds = (List<String>)map.get("tagIds");

                //Update knowledge Detail
                //Update knowledge Detail
                DataCollection data = knowledgeService.getKnowledge(knowledgeId,(short)-1);
                if (data == null) {
                    logger.error("can't find this knowledge detail info, so skip add directory to this knowledge, knowledgeId: {}", knowledgeId);
                    continue;
                }

                List<Long> newDirectoryIds = convertStToLong(directoryIds);
                KnowledgeDetail knowledgeDetail = data.getKnowledgeDetail();
                knowledgeDetail.setTags(newDirectoryIds);

                //Update knowledge base
                KnowledgeBase knowledgeBase = data.getKnowledge();

                String tagStr = directoryIds.toString();
                tagStr = tagStr.substring(1, tagStr.length()-1);
                if (tagStr.length() > 255) {
                    int lastIndex = tagStr.lastIndexOf(",");
                    tagStr = tagStr.substring(0, lastIndex-1);
                }
                knowledgeBase.setTags(tagStr);

                knowledgeService.updateKnowledge(new DataCollection(knowledgeBase, knowledgeDetail));
                logger.info("batch tags to knowledge success!  knowledgeId: {}", knowledgeId);

                for (Long directoryId : newDirectoryIds) {
                    DirectorySource directorySource = new DirectorySource();
                    directorySource.setUserId(userId);
                    directorySource.setDirectoryId(directoryId);
                    directorySource.setAppId(APPID);
                    directorySource.setSourceId(knowledgeId);
                    directorySource.setSourceTitle(title);
                    //source type 为定义的类型id:exp(用户为1,人脉为2,知识为3,需求为4,事务为5)
                    directorySource.setSourceType((int) sourceType);
                    directorySource.setCreateAt(new Date().getTime());
                    directorySourceService.createDirectorySources(directorySource);
                    logger.info("create directory source, directoryId:" + directoryId + " knowledgeId: " + knowledgeId);
                }
            }
        } catch (DirectorySourceServiceException ex) {
            ex.printStackTrace();
        }

        return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
    }

    public InterfaceResult getDirectorySourceCountByIds(long userId,List<Long> directoryIds) throws Exception
    {
        if (directoryIds == null || directoryIds.size() <= 0) {
            return InterfaceResult.getSuccessInterfaceResultInstance("directory List is null or size is 0!");
        }
        Map<Long,Integer> sourceMap = new HashMap<Long,Integer>(directoryIds.size());
        for (long directoryId : directoryIds) {
            int count = directorySourceService.countDirectorySourcesByDirectoryId(APPID, userId, directoryId);
            sourceMap.put(directoryId, count);
        }

        return InterfaceResult.getSuccessInterfaceResultInstance(sourceMap);
    }

    public MappingJacksonValue getDirectoryListByIds(long userId,List<Long> directoryIds)
    {
        List<Directory> directoryList = null;
        try {
            directoryList = directoryService.getDirectoryList(APPID, -1L, directoryIds);
        } catch (DirectoryServiceException e) {
            logger.error("Get directory list failed！reason："+e.getMessage());
            e.printStackTrace();
        }
        if (directoryList != null && directoryList.size() > 0) {
            MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(directoryList);
            SimpleFilterProvider filterProvider = KnowledgeUtil.directoryFilterProvider(Directory.class.getName());
            mappingJacksonValue.setFilters(filterProvider);
            return mappingJacksonValue;
        }

        logger.error("Can't get any tags with given tag id");
        return new MappingJacksonValue(InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS));
    }

    public boolean saveDirectorySource(long userId, KnowledgeDetail knowledgeDetail)
    {
        //save directory
        List<Long> categorysList = knowledgeDetail.getCategoryIds();
        if (categorysList == null || categorysList.size() <= 0) {
            logger.error("directory List is empty, so skip to save..");
            return false;
        }

        try {
            for (int index = 0; index < categorysList.size(); index++) {
                logger.info("directoryId: {}", categorysList.get(index));
                long directoryId = Long.valueOf(categorysList.get(index));
                if (directoryId > 0) {
                    DirectorySource directorySource = createDirectorySource(userId, directoryId, knowledgeDetail);
                    directorySourceService.createDirectorySources(directorySource);
                }
                logger.info("directoryId:" + directoryId);
            }
        } catch (DirectorySourceServiceException ex) {
            ex.printStackTrace();
        }
        return true;
    }

    public boolean updateDirectorySource(long userId, KnowledgeDetail knowledgeDetail)
    {
        long knowledgeId = knowledgeDetail.getId();
        try{
            boolean removeDirectoryFlag = directorySourceService.removeDirectorySourcesBySourceId(userId, APPID, sourceType, knowledgeId);
            if(removeDirectoryFlag){
                List<Long> categoryList = knowledgeDetail.getCategoryIds();
                if(categoryList != null && categoryList.size() > 0){
                    for(Long directoryId : categoryList){
                        if(directoryId >= 0) {
                            DirectorySource directorySource = createDirectorySource(userId, directoryId, knowledgeDetail);
                            directorySourceService.createDirectorySources(directorySource);
                        }
                        logger.info("directoryId:" + directoryId);
                    }
                }
            }
            else{
                logger.error("update category remove failed...userId=" + userId+ ", knowledgeId=" + knowledgeId);
                return false;
            }
        }catch(DirectorySourceServiceException e1){
            logger.error("update category remove failed...userId=" + userId + ", knowledgeId=" + knowledgeId);
            return false;
        }
        return true;
    }

    public boolean deleteDirectory(long userId, long knowledgeId)
    {
        try{
            boolean removeDirectoryFlag = directorySourceService.removeDirectorySourcesBySourceId(userId, APPID, sourceType, knowledgeId);
            if(!removeDirectoryFlag){
                logger.error("category remove failed...userId=" + userId + ", knowledgeId=" + knowledgeId);
                return false;
            }
        }catch(DirectorySourceServiceException ex){
            logger.error("category remove failed...userId=" + userId + ", knowledgeId=" + knowledgeId + "error: "+ex.getMessage());
            return false;
        }
        return true;
    }

    public List<Long> getKowledgeIdListByDirectoryId(long userId,long directoryId,int start,int size)
    {
        Object[] parameter = new Object[]{userId, APPID, sourceType, directoryId};
        List<DirectorySource> directorySources = null;
        try {
            directorySources = directorySourceService.getSourcesByDirectoryIdAndSourceType(start, size, parameter);
        } catch (DirectorySourceServiceException e) {
            logger.error("get directory Source failed: "+e.getMessage());
            e.printStackTrace();
        }
        List<Long> knowledgeIds = new ArrayList<Long>(directorySources.size());
        if (directorySources != null && directorySources.size() > 0) {
            for (DirectorySource source : directorySources) {
                knowledgeIds.add(source.getSourceId());
                logger.info("add knowledge source: "+source.getSourceId());
            }
        }

        return knowledgeIds;
    }


    private DirectorySource createDirectorySource(long userId, long directoryId,KnowledgeDetail knowledge)
    {
        DirectorySource directorySource = new DirectorySource();
        directorySource.setUserId(userId);
        directorySource.setDirectoryId(directoryId);
        directorySource.setAppId(APPID);
        directorySource.setSourceId(knowledge.getId());
        directorySource.setSourceTitle(knowledge.getTitle());
        //source type 为定义的类型id:exp(用户为1,人脉为2,知识为3,需求为4,事务为5)
        directorySource.setSourceType(sourceType);
        directorySource.setCreateAt(new Date().getTime());

        return directorySource;
    }
}
