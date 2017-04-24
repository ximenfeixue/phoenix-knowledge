package com.ginkgocap.ywxt.knowledge.dao.impl;

import com.ginkgocap.ywxt.knowledge.dao.KnowledgeMongoDao;
import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeBase;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeBaseSync;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeUtil;
import com.ginkgocap.ywxt.knowledge.model.common.Constant;
import com.ginkgocap.ywxt.knowledge.model.common.DataCollect;
import com.ginkgocap.ywxt.knowledge.model.common.EModuleType;
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
import org.springframework.data.mongodb.core.query.BasicUpdate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
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

    private static final String topKnowledge = "TopKnowledge";

    private final int maxSize = 20;

    @Override
    public Knowledge insert(Knowledge knowledge) throws Exception {
        if (knowledge == null) {
            throw new IllegalArgumentException("Knowledge is null");
        }

        knowledge.setCreatetime(String.valueOf(System.currentTimeMillis()));
        final String currCollectionName = getCollectionName(knowledge.getColumnType());
        knowledge.setId(knowledgeCommonService.getKnowledgeSequenceId());
        mongoTemplate.save(knowledge, currCollectionName);
        return knowledge;
    }

    @Override
    public List<Knowledge> insertList(List<Knowledge> KnowledgeList, final int type) throws Exception {
        List<Knowledge> batchToSave = null;
        if(KnowledgeList != null && KnowledgeList.size() > 0) {
        	batchToSave = new ArrayList<Knowledge>(KnowledgeList.size());
            final String collectionName = getCollectionName(type);
            for(Knowledge detail : KnowledgeList) {
                if (detail != null) {
                    detail.setId(knowledgeCommonService.getKnowledgeSequenceId());
                    batchToSave.add(detail);
                }
                else {
                    logger.error("One knowledge is null...");
                }
            }
            mongoTemplate.insert(batchToSave, collectionName);
        }

        return batchToSave;
    }

    @Override
    public Knowledge update(Knowledge knowledge, int oldType) {

        if(knowledge == null) {
            throw new IllegalArgumentException("knowledge is null");
        }

        if (knowledge.getId() <= 0) {
            throw new IllegalArgumentException("knowledge Id is invalidated, id: " + knowledge.getId());
        }

        if (!validateColumnId(knowledge.getColumnid())) {
            throw new IllegalArgumentException("knowledge columnId is invalidated, columnId: " + knowledge.getColumnid());
        }

        knowledge.setModifytime(String.valueOf(System.currentTimeMillis()));
        Query query = knowledgeIdQuery(knowledge.getId());

        String columnType = oldType > 0 ? String.valueOf(oldType) : knowledge.getColumnType();
        String collectionName = getCollectionName(columnType);
        Knowledge existValue = mongoTemplate.findOne(query, Knowledge.class, collectionName);
        if (existValue != null) {
            if (existValue.getCid() != knowledge.getCid()) {
                logger.error("can't update knowledge, as owner is different. knowledgeId: " + knowledge.getId() +
                " exist cid :" + existValue.getCid() + " new cid: " + knowledge.getCid());
                return null;
            }
            if (oldType > 0) {
                boolean result = remove(query, collectionName);
                if (result) {
                    logger.info("delete old knowledge detail success, knowledgeId: " + knowledge.getId());
                }
                logger.info("update knowledge detail, old collectionName: " + collectionName);
                collectionName = getCollectionName(knowledge.getColumnType());
                logger.info("update knowledge detail, new collectionName: " + collectionName);
            }
            mongoTemplate.save(knowledge, collectionName);
        }
        else {
            logger.error("can't find this knowledge, so skip update. knowledgeId: " + knowledge.getId() + " type: " + columnType);
            return null;
        }

        return knowledge;
    }

    public boolean updatePrivated(final long knowledgeId, final short type, final short privated) {
        Query query = knowledgeIdQuery(knowledgeId);
        String collectionName = getCollectionName(type);
        Update update = Update.update("privated", privated);
        WriteResult result = mongoTemplate.updateMulti(query, update, Knowledge.class, collectionName);
        if (result.getN() <= 0) {
            logger.error("update knowledge privated failed, id: " + knowledgeId + " type: " + type);
            return false;
        }
        return true;
    }

    @Override
    public boolean addTag(final long userId, final long knowledgeId, final int type, final List<Long> tagIdList) {
        return addIds(userId, knowledgeId, type, tagIdList, EModuleType.ETag);
    }

    @Override
    public boolean addDirectory(final long userId, final long knowledgeId, final int type, final List<Long> directoryIdList) {
        return addIds(userId, knowledgeId, type, directoryIdList, EModuleType.EDirectory);
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
    public boolean logicDeleteByIdsType(List<Long> ids,int columnType)
    {
        Query query = new Query(Criteria.where(Constant._ID).in(ids));
        return logicDelete(query, getCollectionName(columnType));
    }

    @Override
    public boolean logicRecoveryByIdsType(List<Long> ids,int columnType)
    {
        Query query = new Query(Criteria.where(Constant._ID).in(ids));
        return logicRecovery(query, getCollectionName(columnType));
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
            update(detail, -1);
            return true;
        }
        else {
            logger.error("can't find the knowledge by, knowledgeId: " + knowledgeId+ " columnType: " + columnType);
            return false;
        }
    }

    @Override
    public boolean deleteTag(long userId, long knowledgeId, int type, List<Long> tagIdList) {
        return deleteIds(userId, knowledgeId, type, tagIdList, EModuleType.ETag);
    }

    @Override
    public boolean deleteDirectory(long userId, long knowledgeId, int type, List<Long> directoryIdList) {
        return deleteIds(userId, knowledgeId, type, directoryIdList, EModuleType.EDirectory);
    }

    @Override
    public Knowledge getByIdAndColumnId(long knowledgeId, int columnType)
    {
        Query query = knowledgeIdQuery(knowledgeId);
        return mongoTemplate.findOne(query, Knowledge.class, getCollectionName(columnType));
    }

    @Override
    public List<KnowledgeBase> getAllByIdsType(final List<Long> ids, final short type)
    {
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.and("status").is(4);
        criteria.and("_id").in(ids);
        query.addCriteria(criteria);

        final String collectionName = KnowledgeUtil.getKnowledgeCollectionName(type);
        final List<Knowledge> detailList = mongoTemplate.find(query, Knowledge.class, collectionName);
        return DataCollect.convertDetailToBaseList(detailList, type, true);
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

    @Override
    public void backupKnowledgeBase(KnowledgeBaseSync knowledgeSync)
    {
        if (knowledgeSync == null) {
            return;
        }
        mongoTemplate.save(knowledgeSync, Constant.Collection.KnowledgeBaseSync);
    }

    @Override
    public void deleteBackupKnowledgeBase(long knowledgeId)
    {
        if (knowledgeId < 0) {
            return;
        }
        Query query = new Query(Criteria.where(Constant._ID).is(knowledgeId));
        mongoTemplate.remove(query, KnowledgeBaseSync.class, Constant.Collection.KnowledgeBaseSync);
    }

    @Override
    public List<KnowledgeBaseSync> getBackupKnowledgeBase(int start, int size) {
        Query query = new Query();
        long count = mongoTemplate.count(query, KnowledgeBaseSync.class, Constant.Collection.KnowledgeBaseSync);
        if (start >= count) {
            return null;
        }
        if (size > maxSize) {
            size = maxSize;
        }
        if (size > count) {
            size = (int)count;
        }
        if (start > 0) {
            query.skip(start);
        }
        query.limit(size);
        return mongoTemplate.find(query, KnowledgeBaseSync.class, Constant.Collection.KnowledgeBaseSync);
    }

    @Override
    public boolean updateIds(final long userId, final long knowledgeId, final int type, final List<Long> idList, final EModuleType moduleType) {
        final String keyWord = moduleType.keyWord();
        if (CollectionUtils.isEmpty(idList)) {
            logger.error(keyWord + " is null, skip update. id: " + knowledgeId + " type: " + type);
            return false;
        }

        Query query = knowledgeIdQuery(knowledgeId);
        String collectionName = getCollectionName(type);
        Knowledge existValue = mongoTemplate.findOne(query, Knowledge.class, collectionName);
        if (existValue == null) {
            logger.error("knowledge not exist. id: " + knowledgeId + " type: " + type);
            return false;
        }
        if (existValue.getCid() != userId) {
            logger.error("no permission. id: " + knowledgeId + " type: " + type + " cid: " + existValue.getCid() + " userId: " + userId);
            return false;
        }

        return updateIdToDB(query, collectionName, keyWord, idList);
    }

    @Override
    public void addTopKnowledge(final List<Long> ids, final short type) {

        List<KnowledgeBase> baseList = this.getAllByIdsType(ids, type);
        if (CollectionUtils.isNotEmpty(baseList)) {
            mongoTemplate.insert(baseList, topKnowledge);
            logger.info("add top knowledge success..");
        } else {
            logger.error("add top knowledge failed..");
        }
    }

    @Override
    public void deleteTopKnowledge(final List<Long> ids, final short type) {
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.and("type").is(type);
        criteria.and("_id").in(ids);
        query.addCriteria(criteria);

        WriteResult result = mongoTemplate.remove(query, topKnowledge);
        if (result.getN() > 0) {
            logger.info("delete top knowledge success..");
        } else {
            logger.error("delete top knowledge failed..");
        }
    }

    @Override
    public List<KnowledgeBase> getTopKnowledgeByPage(final short type, final int page, int size) {
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.and("type").is(type);
        query.addCriteria(criteria);

        long count = mongoTemplate.count(query, KnowledgeBase.class, topKnowledge);
        final int start = page * size;
        if (start >= count) {
            return null;
        }
        if (size > maxSize) {
            size = maxSize;
        }
        if (size > count) {
            size = (int)count;
        }
        query.skip(start);
        query.limit(size);

        return mongoTemplate.find(query, KnowledgeBase.class, topKnowledge);
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

    private boolean logicDelete(Query query, final String collectionName)
    {
        Update update = Update.update("status", 5);
        WriteResult result = mongoTemplate.updateMulti(query, update, collectionName);
        if (result.getN() <= 0) {
            return false;
        }
        return true;
    }

    private boolean logicRecovery(Query query, final String collectionName)
    {
        Update update = Update.update("status", 4);
        WriteResult result = mongoTemplate.updateMulti(query, update, collectionName);
        if (result.getN() <= 0) {
            return false;
        }
        return true;
    }

    private boolean addIds(final long userId, final long knowledgeId, final int type, final List<Long> idList, final EModuleType moduleType) {
        final String keyWord = moduleType.keyWord();
        if (CollectionUtils.isEmpty(idList)) {
            logger.error(keyWord + " is null, skip update. id: " + knowledgeId + " type: " + type);
            return false;
        }

        Query query = knowledgeIdQuery(knowledgeId);
        String collectionName = getCollectionName(type);
        Knowledge existValue = mongoTemplate.findOne(query, Knowledge.class, collectionName);
        if (existValue == null) {
            logger.error("knowledge not exist. id: " + knowledgeId + " type: " + type);
            return false;
        }
        if (existValue.getCid() != userId) {
            logger.error("no permission. id: " + knowledgeId + " type: " + type + " cid: " + existValue.getCid() + " userId: " + userId);
            return false;
        }
        List<Long> existIds = null;
        if (moduleType == EModuleType.ETag) {
            existIds = existValue.getTagList();
        } else if (moduleType == EModuleType.EDirectory) {
            existIds = existValue.getDirectorys();
        } else {
            logger.error("moduleType not match. keyWord: " + keyWord + "id: " + knowledgeId + " type: " + type);
            return false;
        }

        if (CollectionUtils.isEmpty(existIds)) {
            existIds = idList;
        } else {
            existIds.addAll(idList);
        }

        return updateIdToDB(query, collectionName, keyWord, existIds);
        /*
        Update update = new Update();
        update.set(keyWord, existIds);
        WriteResult result = mongoTemplate.upsert(query, update, collectionName);
        if (result.getN() > 0) {
            return true;
        }
        return false;*/
    }

    private boolean deleteIds(final long userId, final long knowledgeId, final int type, final List<Long> idList, final EModuleType moduleType) {
        final String keyWord = moduleType.keyWord();
        if (CollectionUtils.isEmpty(idList)) {
            logger.error(keyWord + " idList is null, skip update. id: " + knowledgeId + " type: " + type);
            return false;
        }

        Query query = knowledgeIdQuery(knowledgeId);
        String collectionName = getCollectionName(type);
        Knowledge existValue = mongoTemplate.findOne(query, Knowledge.class, collectionName);
        if (existValue == null) {
            logger.error("knowledge not exist. id: " + knowledgeId + " type: " + type);
            return false;
        }
        if (existValue.getCid() != userId) {
            logger.error("no permission. id: " + knowledgeId + " type: " + type + " cid: " + existValue.getCid() + " userId: " + userId);
            return false;
        }

        List<Long> existIds = null;
        if (moduleType == EModuleType.ETag) {
            existIds = existValue.getTagList();
        } else if (moduleType == EModuleType.EDirectory) {
            existIds = existValue.getDirectorys();
        } else {
            logger.error("moduleType not match. keyWord: " + keyWord + "id: " + knowledgeId + " type: " + type);
            return false;
        }

        if (CollectionUtils.isEmpty(existIds)) {
            logger.error(keyWord + "no id exist. knowledgeId: " + knowledgeId + " type: " + type);
            return false;
        } else {
            existIds.removeAll(idList);
        }

        /*
        Update update = new Update();
        update.set(keyWord, existIds);
        WriteResult result = mongoTemplate.upsert(query, update, collectionName);
        if (result.getN() > 0) {
            return true;
        }
        return false;*/

        return updateIdToDB(query, collectionName, keyWord, existIds);
    }

    private boolean updateIdToDB(final Query query, final String collectionName, final String colName, final List<Long> ids) {
        Update update = new Update();
        update.set(colName, ids);
        WriteResult result = mongoTemplate.upsert(query, update, collectionName);
        if (result.getN() > 0) {
            return true;
        }
        return false;
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
