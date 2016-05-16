package com.ginkgocap.ywxt.knowledge.service.impl;

import com.ginkgocap.parasol.directory.exception.DirectorySourceServiceException;
import com.ginkgocap.parasol.directory.model.DirectorySource;
import com.ginkgocap.parasol.directory.service.DirectorySourceService;
import com.ginkgocap.parasol.tags.exception.TagSourceServiceException;
import com.ginkgocap.parasol.tags.model.TagSource;
import com.ginkgocap.parasol.tags.service.TagSourceService;
import com.ginkgocap.ywxt.knowledge.dao.KnowledgeMongoDao;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeCollect;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeDetail;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeReport;
import com.ginkgocap.ywxt.knowledge.model.TagItems;
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
import java.util.Date;
import java.util.List;

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
    private TagSourceService tagSourceService;

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
    public InterfaceResult reportKnowledge(KnowledgeReport report) throws Exception
    {
        mongoTemplate.save(report, Constant.Collection.KnowledgeReport);

        return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
    }

    public InterfaceResult batchTags(List<TagItems> tagItems, long userId) throws Exception {
        try {
            for (TagItems tagItem : tagItems) {
                for (Long tagId : tagItem.getTagIds()) {
                    TagSource tagSource = new TagSource();
                    tagSource.setUserId(userId);
                    tagSource.setAppId(APPID);
                    tagSource.setSourceId(tagItem.getKnowlegeId());
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

    public InterfaceResult batchCatalogs(List<TagItems> tagItems, long userId) throws Exception {
        try {
            for (TagItems tagItem : tagItems) {
                for (long directoryId : tagItem.getTagIds()) {
                    DirectorySource directorySource = new DirectorySource();
                    directorySource.setUserId(userId);
                    directorySource.setDirectoryId(directoryId);
                    directorySource.setAppId(APPID);
                    directorySource.setSourceId(tagItem.getKnowlegeId());
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

    private Query knowledgeColumnIdAndOwnerId(long ownerId,long knowledgeId,short columnId)
    {
        Query query = new Query();
        query.addCriteria(Criteria.where(Constant.OwnerId).is(ownerId));
        query.addCriteria(Criteria.where(Constant.KnowledgeId).is(knowledgeId));
        query.addCriteria(Criteria.where(Constant.ColumnId).is(columnId));
        return query;
    }
}
