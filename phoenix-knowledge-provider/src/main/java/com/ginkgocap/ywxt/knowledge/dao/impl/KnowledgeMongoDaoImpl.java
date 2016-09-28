package com.ginkgocap.ywxt.knowledge.dao.impl;

import com.ginkgocap.ywxt.knowledge.dao.KnowledgeMongoDao;
import com.ginkgocap.ywxt.knowledge.model.*;
import com.ginkgocap.ywxt.knowledge.model.common.Constant;
import com.ginkgocap.ywxt.knowledge.service.common.KnowledgeCommonService;
import com.ginkgocap.ywxt.knowledge.utils.KnowledgeConstant;
import com.mongodb.BasicDBObject;
import com.mongodb.WriteResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by gintong on 2016/7/19.
 */
@Repository("knowledgeMongoDao")
public class KnowledgeMongoDaoImpl implements KnowledgeMongoDao {

    private final Logger logger = LoggerFactory.getLogger(KnowledgeMongoDaoImpl.class);
    @Resource
    private MongoTemplate mongoTemplate;

    @Autowired
    private KnowledgeCommonService knowledgeCommonService;

    private final int maxSize = 20;

    @Override
    public Knowledge insert(Knowledge knowledge) throws Exception {
        if(knowledge == null) {
            throw new IllegalArgumentException("Knowledge is null");
        }

        knowledge.setCreatetime(String.valueOf(System.currentTimeMillis()));
        final String currCollectionName = getCollectionName(knowledge.getColumnType());
        knowledge.setId(knowledgeCommonService.getKnowledgeSequenceId());
        mongoTemplate.save(knowledge, currCollectionName);

        return knowledge;
    }

    @Override
    public List<Knowledge> insertList(List<Knowledge> KnowledgeList) throws Exception {
        if(KnowledgeList != null && KnowledgeList.size() > 0) {
            for(Knowledge Knowledge : KnowledgeList) {
                Knowledge.setId(knowledgeCommonService.getKnowledgeSequenceId());
            }
        }
        mongoTemplate.insert(KnowledgeList, Knowledge.class);

        return KnowledgeList;
    }

    @Override
    public Knowledge update(Knowledge knowledge) {

        if(knowledge == null) {
            throw new IllegalArgumentException("knowledge is null");
        }

        if (knowledge.getId() <= 0) {
            throw new IllegalArgumentException("knowledge Id is invalidated, id: "+knowledge.getId());
        }

        if (!validateColumnId(knowledge.getColumnid())) {
            throw new IllegalArgumentException("knowledge columnId is invalidated, columnId: "+knowledge.getColumnid());
        }

        knowledge.setModifytime(String.valueOf(System.currentTimeMillis()));
        Query query = knowledgeIdQuery(knowledge.getId());
        int columnType = parserColumnId(knowledge.getColumnType());
        final String collectionName = getCollectionName(columnType);
        Knowledge existValue = mongoTemplate.findOne(query, Knowledge.class, collectionName);
        if (existValue != null) {
            mongoTemplate.save(knowledge, collectionName);
        }
        else {
            logger.error("can't find this knowledge, so skip update. knowledgeId: " + knowledge.getId());
            return null;
        }

        return knowledge;
    }


    @Override
    public boolean deleteByIdAndColumnId(long knowledgeId,int columnType)
    {
        Query query = knowledgeIdQuery(knowledgeId);
        return remove(query, getCollectionName(columnType));
        //return findAndRemove(query, KnowledgeType.knowledgeType(columnType).cls(), getCollectionName(columnType));
    }

    @Override
    public boolean deleteByIdsAndColumnType(List<Long> ids,int columnType) throws Exception
    {
        Query query = new Query(Criteria.where(Constant._ID).in(ids));
        return remove(query, getCollectionName(columnType));
        //return findAndRemove(query, KnowledgeType.knowledgeType(columnType).cls(), getCollectionName(columnType));
    }

    @Override
    public boolean deleteByUserIdAndColumnType(long createUserId,int columnType) throws Exception
    {
        Query query = new Query(Criteria.where(Constant.cid).is(createUserId));
        //addColumnIdToQuery(query, columnType);

        return remove(query, getCollectionName(columnType));
    }

    @Override
    public boolean deleteKnowledgeDirectory(long knowledgeId,int columnType,long directoryId)
    {
        Knowledge detail = getByIdAndColumnId(knowledgeId, columnType);
        List<Long> directoryIds = detail.getDirectorys();
        if (directoryIds != null && directoryIds.contains(directoryId)) {
            directoryIds.remove(directoryId);
            detail.setDirectorys(directoryIds);
            update(detail);
            return true;
        }
        else {
            logger.error("can't find the knowledge by, knowledgeId: {} columnType: {}", knowledgeId, columnType);
            return false;
        }
    }

    @Override
    public Knowledge getByIdAndColumnId(long knowledgeId, int columnType)
    {
        Query query = knowledgeIdQuery(knowledgeId);
        return mongoTemplate.findOne(query, Knowledge.class, getCollectionName(columnType));
    }

    @Override
    public Knowledge insertAfterDelete(Knowledge knowledge) throws Exception {

        if(knowledge == null || knowledge.getId() <= 0) {
            throw new IllegalArgumentException("Knowledge is null or invalidated!");
        }

        long knowledgeId = knowledge.getId();
        int columnId = parserColumnId(knowledge.getColumnid());
        Knowledge oldValue = this.getByIdAndColumnId(knowledgeId,columnId);
        if (oldValue == null) {
            try {
                this.insert(knowledge);
            } catch (Exception ex) {
                throw ex;
            }
        }

        return oldValue;
    }

    @Override
    public List<Knowledge> getByIdsAndColumnId(List<Long> ids,int columnType) throws Exception
    {
        Query query = new Query(Criteria.where(Constant._ID).in(ids));

        return mongoTemplate.find(query,Knowledge.class, getCollectionName(columnType));
    }

    public List<Knowledge> getNoDirectory(long userId,int start,int size)
    {
        Query query = new Query(Criteria.where(Constant.cid).is(userId));
        query.addCriteria(Criteria.where(Constant.categoryIds).exists(false));
        final String collectName = getCollectionName((short)0);
        long count = mongoTemplate.count(query, Knowledge.class, collectName);
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
        Class classType = getKnowledgeClassType((short)0);
        return mongoTemplate.find(query, classType, collectName);
    }

    // 首页主页
    @SuppressWarnings("unchecked")
    @Override
    public List<Knowledge> selectIndexByParam(short type,int page, int size,List<Long> ids)
    {
        logger.info("type: {} page: {} size: {}", type, page, size);
        String collectionName = getCollectionName(type);
        Criteria criteria = Criteria.where("status").is(4);
        Criteria criteriaPj = new Criteria();
        Criteria criteriaUp = new Criteria();
        Criteria criteriaGt = new Criteria();

        criteriaGt.and("uid").is(KnowledgeConstant.SOURCE_GINTONG_BRAIN_ID);
        // 查询栏目大类下的数据：全平台
        // 查询资讯
        Query query = null;
        if (ids != null && ids.size() > 0) {
            criteriaUp.and("_id").in(ids);
            criteriaPj.orOperator(criteriaUp, criteriaGt);
            criteriaPj.andOperator(criteria);
            query = new Query(criteriaPj);
        } else {
            criteria.andOperator(criteriaGt);
            query = new Query(criteria);
        }
        String str = "" + KnowledgeUtil.writeObjectToJson(criteria);
        logger.info("MongoObject:" + collectionName + ",Query:" + str);
        query.with(new Sort(Sort.Direction.DESC, Constant._ID));
        long count;
        try {
            // count = mongoTemplate.count(query, names[length - 1]);
            // PageUtil p = new PageUtil((int) count, page, size);
            query.limit(size);
            query.skip(0);
            return mongoTemplate.find(query, Knowledge.class, collectionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void backupKnowledgeBase(KnowledgeBaseSync knowledgeSync)
    {
        if (knowledgeSync == null) {
            return;
        }
        mongoTemplate.save(knowledgeSync, Constant.Collection.KnowledgeBaseSync);
    }

    public void deleteBackupKnowledgeBase(long knowledgeId)
    {
        if (knowledgeId < 0) {
            return;
        }
        Query query = new Query(Criteria.where(Constant._ID).is(knowledgeId));
        mongoTemplate.remove(query, KnowledgeBaseSync.class, Constant.Collection.KnowledgeBaseSync);
    }

    public List<KnowledgeBaseSync> getBackupKnowledgeBase(int start, int size)
    {
        Query query = new Query();
        long count = mongoTemplate.count(query, KnowledgeBaseSync.class, Constant.Collection.KnowledgeBaseSync);
        if (start >= count) {
            return null;
        }
        if (size > maxSize) {
            size = maxSize;
        }
        if (start+size > count) {
            size = (int)(count - (long)start);
        }

        query.skip(start);
        query.limit(size);
        return mongoTemplate.find(query, KnowledgeBaseSync.class, Constant.Collection.KnowledgeBaseSync);
    }

    private String getCollectionName(String columnId) {
        int type = KnowledgeUtil.parserColumnId(columnId);
        return KnowledgeUtil.getKnowledgeCollectionName(type);
    }

    private String getCollectionName(int columnId) {
        return KnowledgeUtil.getKnowledgeCollectionName(columnId);
    }


    private Class getKnowledgeClassType(int columnId)
    {
        return KnowledgeUtil.getKnowledgeClassType(columnId);
    }

    private Update getUpdate(Knowledge Knowledge) {
        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObject.put("$set", Knowledge);
        Update update = new BasicUpdate(basicDBObject);

        return update;
    }

    private Query knowledgeIdQuery(long knowledgeId)
    {
        Query query = new Query();
        query.addCriteria(Criteria.where(Constant._ID).is(knowledgeId));
        //columnId < 0, it means we query knowledge only by knowledgeId
        return query;
    }

    private void addColumnIdToQuery(Query query, int columnId)
    {
        if (columnId > 0) {
            query.addCriteria(Criteria.where(Constant.columnid).is(String.valueOf(columnId)));
        }
    }

    private boolean validateColumnId(String columnId)
    {
        if (StringUtils.isEmpty(columnId)) {
            return false;
        }

        int newColumnId = 0;
        try {
            newColumnId = Integer.parseInt(columnId);
        } catch (Exception ex) {
            return false;
        }

        return newColumnId > 0;
    }

    private int parserColumnId(String columnId)
    {
        return KnowledgeUtil.parserColumnId(columnId);
    }

    private boolean remove(Query query, final String collectionName)
    {
        WriteResult result = mongoTemplate.remove(query, collectionName);
        if (result.getError() != null) {
            return false;
        }
        return true;
    }

    private <T> boolean findAndRemove(Query query, Class<T> entityClass, final String collectionName)
    {
        List<T> deletedItems = mongoTemplate.findAllAndRemove(query, entityClass, collectionName);
        if (CollectionUtils.isEmpty(deletedItems)) {
            return false;
        }
        return true;
    }
}
