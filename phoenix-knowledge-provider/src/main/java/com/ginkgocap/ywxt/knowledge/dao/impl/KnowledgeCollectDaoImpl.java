package com.ginkgocap.ywxt.knowledge.dao.impl;

import com.ginkgocap.ywxt.knowledge.dao.KnowledgeCollectDao;
import com.ginkgocap.ywxt.knowledge.dao.KnowledgeMongoDao;
import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeCollect;
import com.ginkgocap.ywxt.knowledge.model.common.Constant;
import com.ginkgocap.ywxt.knowledge.service.common.KnowledgeCommonService;
import com.gintong.frame.util.dto.CommonResultCode;
import com.gintong.frame.util.dto.InterfaceResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by gintong on 2016/8/17.
 */
@Repository("knowledgeCollectDao")
public class KnowledgeCollectDaoImpl extends BaseDao implements KnowledgeCollectDao
{
    private final Logger logger = LoggerFactory.getLogger(KnowledgeCollectDaoImpl.class);

    @Resource
    private MongoTemplate mongoTemplate;

    @Autowired
    private KnowledgeCommonService knowledgeCommonService;

    @Autowired
    KnowledgeMongoDao knowledgeMongoDao;

    private int maxCount = 100;
    private int maxSize = 10;

    @Override
    public InterfaceResult collectKnowledge(long userId,long knowledgeId, int typeId,short privated) {
        Knowledge detail = knowledgeMongoDao.getByIdAndColumnId(knowledgeId, typeId);
        if (detail == null) {
            logger.error("can't get knowledge detail. userId: " + userId +" knowledgeId: " + knowledgeId + " type: " + typeId);
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
        }

        Query query = knowledgeColumnIdAndOwnerId(userId, knowledgeId, typeId);
        if (mongoTemplate.findOne(query, KnowledgeCollect.class, Constant.Collection.KnowledgeCollect) == null) {
            KnowledgeCollect collect = new KnowledgeCollect();
            collect.setId(knowledgeCommonService.getKnowledgeSequenceId());
            collect.setKnowledgeId(knowledgeId);
            collect.setType((short)typeId);
            collect.setColumnId(typeId);
            collect.setCreateTime(System.currentTimeMillis());
            collect.setKnowledgeTitle(detail.getTitle());
            collect.setOwnerId(userId);
            collect.setPrivated(privated);

            mongoTemplate.save(collect, Constant.Collection.KnowledgeCollect);
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
        }

        return InterfaceResult.getSuccessInterfaceResultInstance("该知识已经被收藏");
    }

    @Override
    public InterfaceResult deleteCollectedKnowledge(long ownerId,long knowledgeId,int typeId)
    {
        Query query = knowledgeColumnIdAndOwnerId(ownerId, knowledgeId, typeId);
        KnowledgeCollect collect = mongoTemplate.findAndRemove(query, KnowledgeCollect.class, Constant.Collection.KnowledgeCollect);
        if (collect == null) {
            logger.error("this knowledge is not collected: ownerId: "+ ownerId+ " knowledgeId: " + knowledgeId + " typeId: "+typeId);
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION,"该知识没有被收藏!");
        }
        return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
    }

    @Override
    public boolean updateCollectedKnowledge(final KnowledgeCollect collect)
    {
        if (collect != null && collect.getId() > 0) {
            try {
                mongoTemplate.save(collect, Constant.Collection.KnowledgeCollect);
            } catch (Exception ex) {
                logger.error("update collected knowledge failed. usserId: " + collect.getOwnerId() + " error: "  + ex.getMessage());
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public KnowledgeCollect getCollectedKnowledge(long userId,long knowledgeId, int typeId)
    {
        Query query = knowledgeColumnIdAndOwnerId(userId, knowledgeId, typeId);
        return mongoTemplate.findOne(query, KnowledgeCollect.class, Constant.Collection.KnowledgeCollect);
    }

    @Override
    public boolean isCollectedKnowledge(long userId,long knowledgeId, int typeId)
    {
        KnowledgeCollect collect = getCollectedKnowledge(userId, knowledgeId, typeId);
        return collect != null;
    }

    @Override
    public List<KnowledgeCollect> myCollectKnowledge(final long userId,final int typeId,int page, int size,final String keyword)
    {
        Query query = new Query();
        Criteria criteria = Criteria.where(Constant.OwnerId).is(userId);
        if (StringUtils.isNotBlank(keyword) && !"null".equals(keyword)) {
            criteria.and("knowledgeTitle").regex(".*?\\" +keyword+ ".*");
        }
        query.addCriteria(criteria);

        if (typeId > 0) {
            query.addCriteria(Criteria.where(Constant.ColumnId).is(typeId));
        }
        long count = mongoTemplate.count(query, KnowledgeCollect.class, Constant.Collection.KnowledgeCollect);
        final int start = page * size;
        if (start >= count) {
            logger.error("start exceed to end, so return null. userId: " + userId + " start: " + start + " page: " + page + " count: " + count);
            return null;
        }
        if (size > maxSize) {
            size = maxSize;
        }
        if (start+size > count) {
            size = (int)count - start;
        }

        query.with(new Sort(Sort.Direction.DESC, Constant.createTime));
        query.skip(start);
        query.limit(size);
        List<KnowledgeCollect> collectedItem = mongoTemplate.find(query, KnowledgeCollect.class, Constant.Collection.KnowledgeCollect);

        return collectedItem;
    }

    @Override
    public List<KnowledgeCollect> getAllCollectKnowledge(final int page, final int size)
    {
        Query query = new Query();
        long count = mongoTemplate.count(query, KnowledgeCollect.class, Constant.Collection.KnowledgeCollect);
        final int start = page * size;
        if (start >= count) {
            logger.error("start exceed to end, so return null. page: " + page + " size: " + size);
            return null;
        }
        query.with(new Sort(Sort.Direction.DESC, Constant.createTime));
        query.skip(start);
        query.limit(size);
        List<KnowledgeCollect> collectedItem = mongoTemplate.find(query, KnowledgeCollect.class, Constant.Collection.KnowledgeCollect);

        return collectedItem;
    }

    @Override
    public long myCollectKnowledgeCount(long userId)
    {
        Query query = new Query();
        query.addCriteria(Criteria.where(Constant.OwnerId).is(userId));

        return mongoTemplate.count(query, KnowledgeCollect.class, Constant.Collection.KnowledgeCollect);
    }
}
