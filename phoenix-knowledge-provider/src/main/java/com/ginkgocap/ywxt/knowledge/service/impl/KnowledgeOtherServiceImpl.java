package com.ginkgocap.ywxt.knowledge.service.impl;

import com.ginkgocap.ywxt.knowledge.dao.KnowledgeMongoDao;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeCollect;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeDetail;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeReport;
import com.ginkgocap.ywxt.knowledge.model.common.Constant;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeOtherService;
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

/**
 * Created by Chen Peifeng on 2016/4/15.
 */

@Service("knowledgeOtherService")
public class KnowledgeOtherServiceImpl implements KnowledgeOtherService
{
    private Logger logger = LoggerFactory.getLogger(KnowledgeOtherServiceImpl.class);
    @Resource
    private MongoTemplate mongoTemplate;

    @Autowired
    private KnowledgeCommonService knowledgeCommonService;

    @Autowired
    KnowledgeMongoDao knowledgeMongoDao;

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

    private Query knowledgeColumnIdAndOwnerId(long ownerId,long knowledgeId,short columnId)
    {
        Query query = new Query();
        query.addCriteria(Criteria.where(Constant.OwnerId).is(ownerId));
        query.addCriteria(Criteria.where(Constant.KnowledgeId).is(knowledgeId));
        query.addCriteria(Criteria.where(Constant.ColumnId).is(columnId));
        return query;
    }
}
