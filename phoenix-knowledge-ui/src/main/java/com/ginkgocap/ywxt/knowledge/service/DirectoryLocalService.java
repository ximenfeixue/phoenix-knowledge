package com.ginkgocap.ywxt.knowledge.service;

import com.ginkgocap.parasol.directory.exception.DirectorySourceServiceException;
import com.ginkgocap.parasol.directory.model.Directory;
import com.ginkgocap.parasol.directory.model.DirectorySource;
import com.ginkgocap.parasol.directory.service.DirectoryService;
import com.ginkgocap.parasol.directory.service.DirectorySourceService;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeDetail;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeUtil;
import com.ginkgocap.ywxt.knowledge.service.common.KnowledgeBaseService;
import com.gintong.frame.util.dto.CommonResultCode;
import com.gintong.frame.util.dto.InterfaceResult;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * Created by gintong on 2016/6/14.
 */
public class DirectoryLocalService implements KnowledgeBaseService
{
    private Logger logger = LoggerFactory.getLogger(DirectoryLocalService.class);

    @Autowired
    private DirectorySourceService directorySourceService;

    @Autowired
    private DirectoryService directoryService;

    /*
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


    public InterfaceResult batchCatalogs(long userId,String requestJson) throws Exception {
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
                List<Long> directoryIds = (List<Long>)map.get("tagIds");

                //Update knowledge Detail
                KnowledgeDetail knowledgeDetail = knowledgeMongoDao.getByIdAndColumnId(knowledgeId, (short)-1);
                if (knowledgeDetail != null) {
                    knowledgeDetail.setCategoryIds(directoryIds);
                    knowledgeMongoDao.update(knowledgeDetail);
                    logger.info("add knowledge directory to Mongo knowledgeId: {}", knowledgeId);
                } else {
                    logger.error("can't find this knowledge by Id: {}", knowledgeId);
                }

                if (knowledgeDetail != null) {
                    for (Long directoryId : directoryIds) {
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
                else {
                    logger.error("can't find this knowledge detail info, so skip add directory to this knowledge, knowledgeId: {}", knowledgeId);
                }
            }
        } catch (DirectorySourceServiceException ex) {
            ex.printStackTrace();
        }

        return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
    }*/
}
