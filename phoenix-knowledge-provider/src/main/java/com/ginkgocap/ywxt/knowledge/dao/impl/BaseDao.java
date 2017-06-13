package com.ginkgocap.ywxt.knowledge.dao.impl;

import com.ginkgocap.ywxt.knowledge.model.*;
import com.ginkgocap.ywxt.knowledge.model.common.Constant;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
/**
 * Created by gintong on 2016/8/17.
 */
public abstract class BaseDao {

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
}
