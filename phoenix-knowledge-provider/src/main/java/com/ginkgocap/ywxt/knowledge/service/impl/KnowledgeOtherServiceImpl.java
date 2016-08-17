package com.ginkgocap.ywxt.knowledge.service.impl;

import com.ginkgocap.ywxt.knowledge.dao.KnowledgeCollectDao;
import com.ginkgocap.ywxt.knowledge.dao.KnowledgeMongoDao;
import com.ginkgocap.ywxt.knowledge.dao.KnowledgeMysqlDao;
import com.ginkgocap.ywxt.knowledge.dao.KnowledgeReportDao;
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

    @Resource
    private MongoTemplate mongoTemplate;

    @Autowired
    private KnowledgeCommonService knowledgeCommonService;

    @Autowired
    private KnowledgeMongoDao knowledgeMongoDao;

    @Autowired
    private KnowledgeReportDao knowledgeReportDao;

    @Autowired
    private KnowledgeCollectDao knowledgeCollectDao;

    private int maxCount = 100;
    private int maxSize = 10;

    @Override
    public InterfaceResult collectKnowledge(long userId,long knowledgeId, int type) throws Exception {
        return knowledgeCollectDao.collectKnowledge(userId, knowledgeId, type);
    }

    @Override
    public InterfaceResult deleteCollectedKnowledge(long ownerId,long knowledgeId,int columnId) throws Exception
    {
        return knowledgeCollectDao.deleteCollectedKnowledge(ownerId, knowledgeId, columnId);
    }

    @Override
    public boolean isCollectedKnowledge(long userId,long knowledgeId, int columnId)
    {
        return isCollectedKnowledge(userId, knowledgeId, columnId);
    }

    @Override
    public List<KnowledgeCollect> myCollectKnowledge(long userId,int columnId,int start, int size) throws Exception
    {
        Query query = new Query();
        query.addCriteria(Criteria.where(Constant.OwnerId).is(userId));
        if (columnId > 0) {
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
        return knowledgeReportDao.reportKnowledge(report);
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
