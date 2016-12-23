package com.ginkgocap.ywxt.knowledge.service;

import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.ginkgocap.parasol.directory.exception.DirectoryServiceException;
import com.ginkgocap.parasol.directory.exception.DirectorySourceServiceException;
import com.ginkgocap.parasol.directory.model.Directory;
import com.ginkgocap.parasol.directory.model.DirectorySource;
import com.ginkgocap.parasol.directory.service.DirectoryService;
import com.ginkgocap.parasol.directory.service.DirectorySourceService;
import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeUtil;
import com.ginkgocap.ywxt.knowledge.model.common.DataCollect;
import com.ginkgocap.ywxt.knowledge.model.common.ResItem;
import com.ginkgocap.ywxt.knowledge.service.common.KnowledgeBaseService;
import com.gintong.frame.util.dto.CommonResultCode;
import com.gintong.frame.util.dto.InterfaceResult;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by gintong on 2016/7/19.
 */
@Repository("directoryServiceLocal")
public class DirectoryServiceLocal extends BaseServiceLocal implements KnowledgeBaseService
{
    private Logger logger = LoggerFactory.getLogger(DirectoryServiceLocal.class);

    @Resource
    private DirectorySourceService directorySourceService;

    @Resource
    private DirectoryService directoryService;

    //Just for Unit test
    public long createDirectory(long userId,String directoryName)
    {
        try {
            Directory directory = new Directory();
            directory.setUserId(userId);
            directory.setAppId(APPID);
            directory.setName(directoryName);
            directory.setTypeId(sourceType);//This get from DB
            return directoryService.createDirectoryForRoot(userId, directory);
        } catch (Exception ex) {
            logger.error("create directory failed. userId: {} directoryName: {}, error: {}", userId, directoryName, ex.getMessage());
        }
        return -1L;
    }

    public List<Directory> getDirectoryList(long userId)
    {
        try {
            return directoryService.getDirectorysForRoot(APPID, userId, (long) sourceType);
        } catch (Exception ex) {
            logger.error("Can't get directoryList by userId: {}, parentId: {} error: {}", userId, 0, ex.getMessage());
        }

        return null;
    }
    //end

    public boolean updateDirectorySource(long userId, Knowledge knowledgeDetail)
    {
        List<Long> directoryIds = knowledgeDetail.getDirectorys();
        if(directoryIds == null || directoryIds.size() <= 0) {
            return false;
        }

        long knowledgeId = knowledgeDetail.getId();
        try {
            if (!directorySourceService.removeDirectorySourcesBySourceId(userId, APPID, sourceType, knowledgeId)) {
                logger.error("delete category failed...userId: " + userId+ ", knowledgeId: " + knowledgeId);
            }
        } catch (Exception ex) {
            logger.error("delete category failed...userId: " + userId+ ", knowledgeId: " + knowledgeId + "error: "+ex.getMessage());
        }

        for(Long directoryId : directoryIds){
            if(directoryId >= 0) {
                try {
                    DirectorySource directorySource = newDirectorySourceObject(userId, directoryId, knowledgeDetail);
                    long directorySourceId = directorySourceService.createDirectorySources(directorySource);
                    logger.info("create directory success. userId: " + userId + ", knowledgeId: " + knowledgeId + "directorySourceId: " + directorySourceId);
                } catch (Exception ex) {
                    logger.error("create category failed...userId: " + userId + ", knowledgeId: " + knowledgeId + "error: " + ex.getMessage());
                }
            }
        }

        return true;
    }

    public InterfaceResult batchDirectory(KnowledgeService knowledgeService,long userId,List<ResItem> directoryItems) throws Exception
    {
        logger.info("batchDirectory: {}", directoryItems );
        if(directoryItems == null || directoryItems.size() <= 0) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION);
        }

        for (ResItem item : directoryItems) {
            long knowledgeId = item.getResId();
            List<Long> directoryIds = item.getIds();

            if (directoryIds == null || directoryIds.size() <= 0) {
                logger.error("Directory list is null or empty, so skip to add. knowledgeId: {}", knowledgeId);
            }

            //Update knowledge Detail
            DataCollect data = null;
            try {
                short columnType = item.getType();
                data = knowledgeService.getKnowledge(knowledgeId, columnType);
            } catch (Exception ex) {
                logger.error("find knowledge failed, knowledgeId: {} error: {}", knowledgeId, ex.getMessage());
                continue;
            }
            if (data == null) {
                logger.error("can't knowledge, so skip add directory, knowledgeId: {}", knowledgeId);
                continue;
            }

            Knowledge knowledgeDetail = data.getKnowledgeDetail();
            if (data.getKnowledgeDetail() == null) {
                logger.error("can't find this knowledge detail, so skip add directory, knowledgeId: {}", knowledgeId);
                continue;
            }

            if (!deleteDirectorySource(userId, knowledgeId)) {
                logger.error("delete old directory source failed, userId:" + userId + " knowledgeId: " + knowledgeId);
            }

            //Batch create directory
            List<Long> successIds = createDirectorySource(userId, directoryIds, knowledgeDetail);

            //Update knowledge detail
            knowledgeDetail.setDirectorys(successIds);

            knowledgeService.updateKnowledge(new DataCollect(null, knowledgeDetail));
            logger.info("batch Directory to knowledge success!  knowledgeId: {}", knowledgeId);
        }

        return InterfaceResult.getSuccessInterfaceResultInstance("创建目录成功.");
    }

    public InterfaceResult batchDirectory(KnowledgeService knowledgeService,long userId,String requestJson) throws Exception {
        logger.info("batchDirectory: {}", requestJson );
        if(StringUtils.isEmpty(requestJson)) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION);
        }

        List<LinkedHashMap<String, Object>> directoryItems =  KnowledgeUtil.readValue(List.class, requestJson);
        if (directoryItems == null || directoryItems.size() <= 0) {
            logger.error("directory list is null. userId: {}", userId);
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION);
        }

        int successResult = 0;
        int failedResult = 0;
        boolean overMaxLimit = false;
        for (int index = 0; index < directoryItems.size(); index++) {
            Map<String, Object> map = directoryItems.get(index);
            Object resId = map.get("resId");
            if (resId == null) {
                logger.error("Can't get knowledge Id, so skip..");
                continue;
            }
            Object type = map.get("type");
            if (type == null) {
                logger.error("Can't get knowledge type, so skip..");
                //continue;
                //TODO: need remove
                type = "1";
            }
            Object ids = map.get("ids");
            if (ids == null) {
                logger.error("Can't get tag ids, so skip..");
                continue;
            }

            long knowledgeId = KnowledgeUtil.parserStringIdToLong(resId.toString());
            List<Object> directoryIds = (List<Object>)ids;

            List<Long> newDirectoryIds = convertObjectListToLongList(directoryIds);
            if (newDirectoryIds == null || newDirectoryIds.size() <= 0) {
                logger.error("There is no valid and safe Directory Id, so skip. knowledgeId: {}", knowledgeId);
                continue;
            }

            DataCollect data = null;
            try {
                short columnType = KnowledgeUtil.parserShortType(type.toString());
                data = knowledgeService.getKnowledge(knowledgeId, columnType);
            } catch (Exception ex) {
                logger.error("find knowledge failed, knowledgeId: {} error: {}", knowledgeId, ex.getMessage());
                continue;
            }
            if (data == null) {
                logger.error("can't get knowledge, so skip add directory, knowledgeId: {}", knowledgeId);
                continue;
            }

            Knowledge knowledgeDetail = data.getKnowledgeDetail();
            if (data.getKnowledgeDetail() == null) {
                logger.error("can't find this knowledge detail, so skip add directory, knowledgeId: {}", knowledgeId);
                continue;
            }

            //Delete existing directory items
            /*if (!deleteDirectorySource(userId, knowledgeId)) {
                logger.error("delete old directory source failed, userId:" + userId + " knowledgeId: " + knowledgeId);
            }*/

            //Add new directory items
            List<Long> successIds = createDirectorySource(userId, newDirectoryIds, knowledgeDetail);

            //Update knowledge Detail
            List<Long> existDirectoryIds = knowledgeDetail.getDirectorys();
            if (existDirectoryIds == null || existDirectoryIds.size() <= 0) {
                existDirectoryIds = successIds;
            } else if (successIds != null && successIds.size() > 0) {
                existDirectoryIds.addAll(successIds);
            }
            knowledgeDetail.setDirectorys(existDirectoryIds);

            knowledgeService.updateKnowledge(new DataCollect(null, knowledgeDetail));
            logger.info("batch Directory to knowledge success!  knowledgeId: {}", knowledgeId);
            successResult = + successIds.size();
            failedResult = + newDirectoryIds.size() - successIds.size();
        }
        return batchResult(successResult, failedResult, overMaxLimit);
    }

    public InterfaceResult getDirectorySourceCountByIds(long userId,List<Long> directoryIds) throws Exception
    {
        if (directoryIds == null || directoryIds.size() <= 0) {
            logger.error("directory List is null or size is 0.");
            return InterfaceResult.getSuccessInterfaceResultInstance("目录Id列表是空，请仔细检查!");
        }
        Map<Long,Integer> sourceMap = new HashMap<Long,Integer>(directoryIds.size());
        for (long directoryId : directoryIds) {
            int count = directorySourceService.countDirectorySourcesByDirectoryId(APPID, userId, directoryId);
            sourceMap.put(directoryId, count);
        }
        logger.info(".......Get directory count success......");
        return InterfaceResult.getSuccessInterfaceResultInstance(sourceMap);
    }

    public MappingJacksonValue getDirectoryListByIds(long userId,List<Long> directoryIds)
    {
        List<Directory> directoryList = getDirectoryList(userId, directoryIds);
        if (directoryList != null && directoryList.size() > 0) {
            InterfaceResult result = InterfaceResult.getSuccessInterfaceResultInstance(directoryList);
            MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
            SimpleFilterProvider filterProvider = KnowledgeUtil.directoryFilterProvider(Directory.class.getName());
            mappingJacksonValue.setFilters(filterProvider);
            logger.info(".......Get directory list success......");
            return mappingJacksonValue;
        }

        logger.error("Can't get any directory with given directory id");
        return new MappingJacksonValue(InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS));
    }

    public List<Directory> getDirectoryList(long userId,List<Long> directoryIds)
    {
        try {
            return directoryService.getDirectoryList(APPID, -1L, directoryIds);
        } catch (Exception e) {
            logger.error("Get directory list failed！reason："+e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public List<Long> saveDirectorySource(long userId, Knowledge knowledgeDetail)
    {
        //save directory
        List<Long> directoryIds = knowledgeDetail.getDirectorys();
        if (directoryIds == null || directoryIds.size() <= 0) {
            logger.error("directory List is empty, so skip to save..");
            return null;
        }

        return createDirectorySource(userId, directoryIds, knowledgeDetail);
    }

    public boolean deleteDirectory(long userId, long knowledgeId)
    {
        try{
            boolean removeDirectoryFlag = directorySourceService.removeDirectorySourcesBySourceId(userId, APPID, sourceType, knowledgeId);
            if(!removeDirectoryFlag){
                logger.error("delete category failed...userId: " + userId + ", knowledgeId: " + knowledgeId);
                return false;
            }
        }catch(Exception ex){
            logger.error("delete category failed...userId: " + userId + ", knowledgeId: " + knowledgeId + "error: "+ex.getMessage());
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public List<Long> getKnowledgeIdListByDirectoryId(final long userId, final long directoryId, final int start, final int size)
    {
        Object[] parameter = new Object[]{userId, APPID, sourceType, directoryId};
        List<DirectorySource> directorySources = null;
        try {
            directorySources = directorySourceService.getSourcesByDirectoryIdAndSourceType(start, size, parameter);
        } catch (Exception e) {
            logger.error("get directory Source failed: "+e.getMessage());
            e.printStackTrace();
        }

        List<Long> knowledgeIds = null;
        if (directorySources != null && directorySources.size() >0) {
            logger.debug("directorySources size: {}", directorySources.size());
            knowledgeIds = new ArrayList<Long>(directorySources.size());
            if (directorySources != null && directorySources.size() > 0) {
                for (DirectorySource source : directorySources) {
                    if (source != null) {
                        knowledgeIds.add(source.getSourceId());
                        logger.info("add knowledge source: " + source.getSourceId());
                    } else {
                        logger.error("these is a directory Source is null, please check the reason..");
                    }
                }
            }
        } else {
            logger.error("no knowledge under the directory: id: {}", directoryId);
        }

        return knowledgeIds;
    }

    private DirectorySource newDirectorySourceObject(long userId, long directoryId,Knowledge detail)
    {
        DirectorySource directorySource = new DirectorySource();
        directorySource.setUserId(userId);
        directorySource.setDirectoryId(directoryId);
        directorySource.setAppId(APPID);
        directorySource.setSourceId(detail.getId());
        directorySource.setSourceTitle(detail.getTitle());
        directorySource.setSourceType(sourceType);
        directorySource.setCreateAt(new Date().getTime());//source type 为定义的类型id:exp(用户为1,人脉为2,需求为7,知识为8,事务为5)
        return directorySource;
    }

    private boolean deleteDirectorySource(long userId, long knowledgeId)
    {
        try {
            if (!directorySourceService.removeDirectorySourcesBySourceId(userId, APPID, sourceType, knowledgeId)) {
                logger.error("delete category failed...userId: " + userId+ ", knowledgeId: " + knowledgeId);
                return false;
            }
        } catch (Exception ex) {
            logger.error("delete category failed...userId: " + userId+ ", knowledgeId: " + knowledgeId + "error: "+ex.getMessage());
            return false;
        }
        return true;
    }

    private List<Long> createDirectorySource(final long userId, final List<Long> directoryIds, final Knowledge detail)
    {
        long knowledgeId = detail.getId();
        int size = directoryIds.size();
        List<Long> successIds = new ArrayList<Long>(size);
        for (int index = 0; index < size; index++) {
            logger.info("directoryId: {}", directoryIds.get(index));
            if (directoryIds.get(index) == null || !(directoryIds.get(index) instanceof Long)) {
                logger.error("directoryId: {} is invalidated, so skip to add to knowledge, id: {}", directoryIds.get(index), detail.getId());
                continue;
            }

            Long directoryId = directoryIds.get(index);
            try {
                DirectorySource directorySource = newDirectorySourceObject(userId, directoryId, detail);
                logger.info("before create directory source. directoryId:" + directoryId + " knowledgeId: " + knowledgeId);
                directorySourceService.createDirectorySources(directorySource);
                successIds.add(directoryId);
                logger.info("create directory source success. directoryId:" + directoryId + " knowledgeId: " + knowledgeId);
            } catch (Exception ex) {
                logger.info("create directory source, failed. userId: {}, knowledgeId: {}, directoryId: {} error: {}",
                        userId, knowledgeId, directoryId, ex.getMessage());
            }
        }
        return successIds;
    }

    public MappingJacksonValue convertToMappingJacksonValue(List<Directory> directoryList)
    {
        InterfaceResult result = InterfaceResult.getSuccessInterfaceResultInstance(directoryList);
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
        SimpleFilterProvider filterProvider = KnowledgeUtil.directoryFilterProvider(Directory.class.getName());
        mappingJacksonValue.setFilters(filterProvider);
        logger.info(".......Get directory list success......");
        return mappingJacksonValue;
    }
}