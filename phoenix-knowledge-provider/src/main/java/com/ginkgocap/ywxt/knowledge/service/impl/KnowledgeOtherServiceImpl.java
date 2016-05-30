package com.ginkgocap.ywxt.knowledge.service.impl;

import com.ginkgocap.parasol.directory.exception.DirectorySourceServiceException;
import com.ginkgocap.parasol.directory.model.Directory;
import com.ginkgocap.parasol.directory.model.DirectorySource;
import com.ginkgocap.parasol.directory.service.DirectoryService;
import com.ginkgocap.parasol.directory.service.DirectorySourceService;
import com.ginkgocap.parasol.tags.exception.TagSourceServiceException;
import com.ginkgocap.parasol.tags.model.Tag;
import com.ginkgocap.parasol.tags.model.TagSource;
import com.ginkgocap.parasol.tags.service.TagService;
import com.ginkgocap.parasol.tags.service.TagSourceService;
import com.ginkgocap.ywxt.knowledge.dao.KnowledgeMongoDao;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeCollect;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeDetail;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeReport;
import com.ginkgocap.ywxt.knowledge.model.common.Constant;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeOtherService;
import com.ginkgocap.ywxt.knowledge.service.common.KnowledgeBaseService;
import com.ginkgocap.ywxt.knowledge.service.common.KnowledgeCommonService;
import com.gintong.frame.util.dto.CommonResultCode;
import com.gintong.frame.util.dto.InterfaceResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by Chen Peifeng on 2016/4/15.
 */

@Service("knowledgeOtherService")
public class KnowledgeOtherServiceImpl implements KnowledgeOtherService, KnowledgeBaseService
{
    private Logger logger = LoggerFactory.getLogger(KnowledgeOtherServiceImpl.class);
    @Resource
    private MongoTemplate mongoTemplate;

    @Autowired
    private KnowledgeCommonService knowledgeCommonService;

    @Autowired
    KnowledgeMongoDao knowledgeMongoDao;

    @Autowired
    private DirectorySourceService directorySourceService;

    @Autowired
    private DirectoryService directoryService;

    @Autowired
    private TagSourceService tagSourceService;

    @Autowired
    private TagService tagService;

    private int maxCount = 100;

    @Override
    public InterfaceResult collectKnowledge(long userId,long knowledgeId, short columnId) throws Exception
    {
        KnowledgeDetail knowledgeDetail = knowledgeMongoDao.getByIdAndColumnId(knowledgeId, columnId);
        if (knowledgeDetail == null) {
            InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
        }

        KnowledgeCollect collect = new KnowledgeCollect();
        collect.setId(knowledgeCommonService.getKnowledgeSeqenceId());
        collect.setKnowledgeId(knowledgeId);
        collect.setColumnId(columnId);
        collect.setCreateTime(System.currentTimeMillis());
        collect.setKnowledgeTitle(knowledgeDetail.getTitle());
        collect.setOwnerId(userId);

        mongoTemplate.save(collect, Constant.Collection.KnowledgeCollect);

        return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
    }

    @Override
    public InterfaceResult deleteCollectedKnowledge(long ownerId,long knowledgeId,short columnId) throws Exception
    {
        Query query = knowledgeColumnIdAndOwnerId(ownerId, knowledgeId, columnId);
        KnowledgeCollect collect = mongoTemplate.findAndRemove(query, KnowledgeCollect.class, Constant.Collection.KnowledgeCollect);
        if (collect == null) {
            logger.error("delete knowledge collect error, no this knowledge collect or no permission to delete: ownerId: "+ ownerId+ " knowledgeId: " + knowledgeId + " columnId: "+columnId);
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
        }
        return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
    }

    @Override
    public List<KnowledgeCollect> myCollectKnowledge(long userId,short columnId) throws Exception
    {
        Query query = new Query();
        query.addCriteria(Criteria.where(Constant.OwnerId).is(userId));
        if (columnId != -1) {
            query.addCriteria(Criteria.where(Constant.ColumnId).is(columnId));
        }
        List<KnowledgeCollect> collectedItem = mongoTemplate.find(query, KnowledgeCollect.class, Constant.Collection.KnowledgeCollect);

        return collectedItem;
    }

    @Override
    public InterfaceResult reportKnowledge(KnowledgeReport report) throws Exception
    {
        mongoTemplate.save(report, Constant.Collection.KnowledgeReport);

        return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
    }

    public InterfaceResult batchTags(List<LinkedHashMap<String, Object>> tagItems, long userId) throws Exception {
        try {
            for (int index = 0; index < tagItems.size(); index++) {
                Map<String, Object> map = tagItems.get(index);
                Set<String> set = map.keySet();
                List<Long> tagIds = (List<Long>)map.get("tagIds");
                for (long tagId : tagIds) {
                    TagSource tagSource = new TagSource();
                    tagSource.setUserId(userId);
                    tagSource.setAppId(APPID);
                    //tagSource.setTitle();
                    tagSource.setSourceId(Long.parseLong(map.get("id").toString()));
                    //source type 为定义的类型id:exp(用户为1,人脉为2,知识为3,需求为4,事务为5)
                    tagSource.setSourceType(sourceType);
                    tagSource.setTagId(tagId);
                    tagSource.setCreateAt(new Date().getTime());
                    tagSourceService.createTagSource(tagSource);
                    logger.info("tagId:" + tagId);
                }
            }
        } catch (TagSourceServiceException ex) {
            ex.printStackTrace();
        }

        return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
    }

    public InterfaceResult batchCatalogs(List<LinkedHashMap<String, Object>> tagItems, long userId) throws Exception {
        try {
            for (int index = 0; index < tagItems.size(); index++) {
                Map<String, Object> map = tagItems.get(index);
                Set<String> set = map.keySet();
                List<Long> tagIds = (List<Long>)map.get("tagIds");
                for (long directoryId : tagIds) {
                    DirectorySource directorySource = new DirectorySource();
                    directorySource.setUserId(userId);
                    directorySource.setDirectoryId(directoryId);
                    directorySource.setAppId(APPID);
                    directorySource.setSourceId(Long.parseLong(map.get("id").toString()));
                    //source type 为定义的类型id:exp(用户为1,人脉为2,知识为3,需求为4,事务为5)
                    directorySource.setSourceType((int) sourceType);
                    directorySource.setCreateAt(new Date().getTime());
                    directorySourceService.createDirectorySources(directorySource);
                    logger.info("dircetoryId:" + directoryId);
                }
            }
        } catch (DirectorySourceServiceException ex) {
            ex.printStackTrace();
        }

        return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
    }

    public InterfaceResult getTagListByIds(List<Long> tagIds,long userId) throws Exception
    {
        List<Tag> tags = tagService.getTags(-1L,tagIds);
        if (tags != null && tags.size() > 0) {
            return InterfaceResult.getSuccessInterfaceResultInstance(tags);
        }

        return InterfaceResult.getSuccessInterfaceResultInstance("Can't get any tags with given tag id");
    }

    public InterfaceResult getDirectoryListByIds(List<Long> directoryIds,long userId) throws Exception
    {
        List<Directory> directoryList = directoryService.getDirectoryList(APPID, -1L, directoryIds);
        if (directoryList != null && directoryList.size() > 0) {
            return InterfaceResult.getSuccessInterfaceResultInstance(directoryList);
        }

        return InterfaceResult.getSuccessInterfaceResultInstance("Can't get any tags with given tag id");
    }


    private Query knowledgeColumnIdAndOwnerId(long ownerId,long knowledgeId,short columnId)
    {
        Query query = new Query();
        query.addCriteria(Criteria.where(Constant.OwnerId).is(ownerId));
        query.addCriteria(Criteria.where(Constant.KnowledgeId).is(knowledgeId));
        query.addCriteria(Criteria.where(Constant.ColumnId).is(columnId));
        return query;
    }
}
