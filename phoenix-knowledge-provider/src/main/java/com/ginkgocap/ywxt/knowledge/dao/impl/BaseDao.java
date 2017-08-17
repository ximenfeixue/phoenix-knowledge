package com.ginkgocap.ywxt.knowledge.dao.impl;

import com.ginkgocap.ywxt.knowledge.model.*;
import com.ginkgocap.ywxt.knowledge.model.common.Constant;

import com.ginkgocap.ywxt.knowledge.utils.KnowledgeUtil;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * Created by gintong on 2016/8/17.
 */
public abstract class BaseDao {

    @Resource
    protected MongoTemplate mongoTemplate;

    protected Query knowledgeColumnIdAndOwnerId(long ownerId, long knowledgeId, int typeId) {
        Query query = new Query();
        query.addCriteria(Criteria.where(Constant.OwnerId).is(ownerId));
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
}
