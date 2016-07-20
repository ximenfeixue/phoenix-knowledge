package com.ginkgocap.ywxt.knowledge.service.impl;

import com.ginkgocap.ywxt.knowledge.dao.KnowledgeMongoDao;
import com.ginkgocap.ywxt.knowledge.dao.KnowledgeMysqlDao;
import com.ginkgocap.ywxt.knowledge.model.*;
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
    @Autowired
    private KnowledgeMysqlDao knowledgeMysqlDao;

    @Resource
    private MongoTemplate mongoTemplate;

    @Autowired
    private KnowledgeCommonService knowledgeCommonService;

    @Autowired
    KnowledgeMongoDao knowledgeMongoDao;

    private int maxCount = 100;
    private int maxSize = 10;

    @Override
    public InterfaceResult collectKnowledge(long userId,long knowledgeId, int columnId) throws Exception {
        Knowledge detail = knowledgeMongoDao.getByIdAndColumnId(knowledgeId, columnId);
        if (detail == null) {
            InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
        }

        Query query = knowledgeColumnIdAndOwnerId(userId, knowledgeId, columnId);
        if (mongoTemplate.findOne(query, KnowledgeCollect.class, Constant.Collection.KnowledgeCollect) == null) {
            KnowledgeCollect collect = new KnowledgeCollect();
            collect.setId(knowledgeCommonService.getKnowledgeSequenceId());
            collect.setKnowledgeId(knowledgeId);
            collect.setColumnId(columnId);
            collect.setCreateTime(System.currentTimeMillis());
            collect.setKnowledgeTitle(detail.getTitle());
            collect.setOwnerId(userId);

            mongoTemplate.save(collect, Constant.Collection.KnowledgeCollect);
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
        }

        return InterfaceResult.getSuccessInterfaceResultInstance("Have collected this knowledge!");

    }

    @Override
    public InterfaceResult deleteCollectedKnowledge(long ownerId,long knowledgeId,int columnId) throws Exception
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
    public boolean isCollectedKnowledge(long userId,long knowledgeId, int columnId)
    {
        Query query = knowledgeColumnIdAndOwnerId(userId, knowledgeId, columnId);
        return mongoTemplate.exists(query, KnowledgeCollect.class, Constant.Collection.KnowledgeCollect);
    }

    @Override
    public List<KnowledgeCollect> myCollectKnowledge(long userId,int columnId,int start, int size) throws Exception
    {
        Query query = new Query();
        query.addCriteria(Criteria.where(Constant.OwnerId).is(userId));
        if (columnId != -1) {
            query.addCriteria(Criteria.where(Constant.ColumnId).is(columnId));
        }
        long count = mongoTemplate.count(query, KnowledgeCollect.class, Constant.Collection.KnowledgeCollect);
        if (start >= count) {
            start = 0;
        }
        if (size > maxSize) {
            size = maxSize;
        }
        if (start+size > count) {
            size = (int)count - start;
        }

        query.skip(start);
        query.limit(size);
        List<KnowledgeCollect> collectedItem = mongoTemplate.find(query, KnowledgeCollect.class, Constant.Collection.KnowledgeCollect);

        return collectedItem;
    }

    @Override
    public long myCollectKnowledgeCount(long userId) throws Exception
    {
        Query query = new Query();
        query.addCriteria(Criteria.where(Constant.OwnerId).is(userId));

        return mongoTemplate.count(query, KnowledgeCollect.class, Constant.Collection.KnowledgeCollect);
    }

    @Override
    public InterfaceResult reportKnowledge(KnowledgeReport report) throws Exception
    {
        Query query = knowledgeColumnIdAndOwnerId(report.getUserId(), report.getKnowledgeId(), report.getColumnId());
        if (mongoTemplate.findOne(query, KnowledgeReport.class, Constant.Collection.KnowledgeReport) == null) {
            if (report.getCreateTime() <= 0) {
                report.setCreateTime(System.currentTimeMillis());
            }
            mongoTemplate.save(report, Constant.Collection.KnowledgeReport);
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
        }
        return InterfaceResult.getSuccessInterfaceResultInstance("Have reported this knowledge!");
    }

    private Query knowledgeColumnIdAndOwnerId(long ownerId,long knowledgeId,int columnId)
    {
        Query query = new Query();
        query.addCriteria(Criteria.where(Constant.OwnerId).is(ownerId));
        query.addCriteria(Criteria.where(Constant.KnowledgeId).is(knowledgeId));
        if (columnId != -1) {
            query.addCriteria(Criteria.where(Constant.ColumnId).is(columnId));
        }
        return query;
    }
}
