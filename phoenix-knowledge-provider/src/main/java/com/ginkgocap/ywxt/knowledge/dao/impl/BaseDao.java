package com.ginkgocap.ywxt.knowledge.dao.impl;

import com.ginkgocap.ywxt.knowledge.model.common.Constant;
import com.ginkgocap.ywxt.knowledge.utils.KnowledgeUtil;
import com.gintong.frame.cache.redis.RedisCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by gintong on 2016/8/17.
 */
public abstract class BaseDao {

    @Resource
    protected MongoTemplate mongoTemplate;

    @Autowired
    private RedisCacheService redisCacheService;

    private final int cacheTTL = 60 * 60 * 2;

    protected Query knowledgeColumnIdAndOwnerId(long ownerId, long knowledgeId, int typeId) {
        Query query = new Query();
        query.addCriteria(Criteria.where(Constant.OwnerId).is(ownerId));
        query.addCriteria(Criteria.where(Constant.KnowledgeId).is(knowledgeId));
        if (typeId != -1) {
            query.addCriteria(Criteria.where(Constant.type).is(typeId));
        }
        return query;
    }

    protected Query knowledgeTypeIdQuery(long knowledgeId, int typeId) {
        Query query = new Query();
        query.addCriteria(Criteria.where(Constant.KnowledgeId).is(knowledgeId));
        if (typeId != -1) {
            query.addCriteria(Criteria.where(Constant.type).is(typeId));
        }
        return query;
    }

    protected Query idType(long knowledgeId, int typeId) {
        Query query = new Query();
        query.addCriteria(Criteria.where(Constant.KnowledgeId).is(knowledgeId));
        if (typeId != -1) {
            query.addCriteria(Criteria.where(Constant.type).is(typeId));
        }
        return query;
    }

    protected void saveToMongo(final Object objectToSave, final String tableName) {
        this.mongoTemplate.insert(objectToSave, tableName);
    }

    protected <T> void batchSaveToMongo(final List<T> batchToSave, final String tableName) {
        this.mongoTemplate.insert(batchToSave, tableName);
    }

    protected void updateToMongo(final Object objectToSave, final String tableName) {
        this.mongoTemplate.save(objectToSave, tableName);
    }

    protected String getCollectionName(final String columnId) {
        final int type = KnowledgeUtil.parserColumnId(columnId);
        return KnowledgeUtil.getKnowledgeCollectionName(type);
    }

    protected String getCollectionName(final int columnId) {
        return KnowledgeUtil.getKnowledgeCollectionName(columnId);
    }


    protected void saveToCache(String key, Object value) {
        try {
            redisCacheService.setRedisCacheByKey(key, value);
            redisCacheService.expireRedisCacheByKey(key, cacheTTL);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void saveToCache(String key, Object value, Integer expiredTime) {
        try {
            redisCacheService.setRedisCacheByKey(key, value);
            redisCacheService.expireRedisCacheByKey(key, expiredTime);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected Object getFromCache(String key) {
        try {
            return redisCacheService.getRedisCacheByKey(key);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    protected void removeFromCache(String key) {
        try {
            redisCacheService.deleteRedisCacheByKey(key);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
