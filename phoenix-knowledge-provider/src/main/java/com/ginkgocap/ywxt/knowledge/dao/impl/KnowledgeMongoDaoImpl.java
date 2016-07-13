package com.ginkgocap.ywxt.knowledge.dao.impl;

import com.ginkgocap.ywxt.knowledge.dao.KnowledgeMongoDao;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeDetail;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeUtil;
import com.ginkgocap.ywxt.knowledge.model.common.Constant;
import com.ginkgocap.ywxt.knowledge.service.common.KnowledgeCommonService;
import com.mongodb.BasicDBObject;
import com.mongodb.WriteResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicUpdate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Repository("knowledgeMongoDao")
public class KnowledgeMongoDaoImpl implements KnowledgeMongoDao {

    private Logger logger = LoggerFactory.getLogger(KnowledgeMongoDaoImpl.class);
	@Resource
	private MongoTemplate mongoTemplate;

    @Autowired
    private KnowledgeCommonService knowledgeCommonService;

    private final int maxSize = 20;
	
	@Override
	public KnowledgeDetail insert(KnowledgeDetail knowledgeDetail) throws Exception {
		if(knowledgeDetail == null) {
            throw new IllegalArgumentException("knowledgeDetail is null");
        }

		long currentDate = new Date().getTime();
        knowledgeDetail.setCreateTime(currentDate);
		
		String currCollectionName = getCollectionName(knowledgeDetail.getColumnId());
        knowledgeDetail.setId(knowledgeCommonService.getKnowledgeSequenceId());
		mongoTemplate.insert(knowledgeDetail,currCollectionName);
		
		return knowledgeDetail;
	}
	
	@Override
	public List<KnowledgeDetail> insertList(List<KnowledgeDetail> knowledgeDetailList) throws Exception {
		if(knowledgeDetailList != null && knowledgeDetailList.size() > 0) {
			for(KnowledgeDetail knowledgeDetail : knowledgeDetailList) {
                knowledgeDetail.setId(knowledgeCommonService.getKnowledgeSequenceId());
			}
		}
        mongoTemplate.insert(knowledgeDetailList, KnowledgeDetail.class);

		return knowledgeDetailList;
	}

	@Override
	public KnowledgeDetail update(KnowledgeDetail knowledgeDetail) {

		if(knowledgeDetail == null) {
            throw new IllegalArgumentException("knowledgel is null");
        }

        if (knowledgeDetail.getId() <= 0) {
            throw new IllegalArgumentException("knowledge Id is invalidated, id: "+knowledgeDetail.getId());
        }

        if (knowledgeDetail.getColumnId() <= 0) {
            throw new IllegalArgumentException("knowledge columnId is invalidated, columnId: "+knowledgeDetail.getColumnId());
        }

        long currentDate = new Date().getTime();
        knowledgeDetail.setModifyTime(currentDate);
        Query query = knowledgeColumnIdQuery(knowledgeDetail.getId(), knowledgeDetail.getColumnId());
        String currCollectionName = getCollectionName(knowledgeDetail.getColumnId());
        KnowledgeDetail existValue = mongoTemplate.findOne(query, KnowledgeDetail.class, currCollectionName);
        if (existValue != null) {
            mongoTemplate.save(knowledgeDetail, currCollectionName);
        }
        else {
            logger.error("can't find this knowledge, so skip update. knowledgeId: {}",knowledgeDetail.getId());
            return null;
        }
		
		return knowledgeDetail;
	}


	@Override
	public int deleteByIdAndColumnId(long id,int columnId) throws Exception
    {
		Query query = knowledgeColumnIdQuery(id, columnId);
        WriteResult result = mongoTemplate.remove(query, getCollectionName(columnId));
        if (result.getN() <=0 ) {
            return -1;
        }
		return 0;
	}

	@Override
	public int deleteByIdsAndColumnId(List<Long> ids,int columnId) throws Exception
    {
        Query query = new Query(Criteria.where(Constant._ID).in(ids));
        if (columnId > 0) {
            query.addCriteria(Criteria.where(Constant.ColumnId).is(columnId));
        }

        WriteResult result = mongoTemplate.remove(query, getCollectionName(columnId));
        if (result.getN() <=0 ) {
            return -1;
        }
		return 0;
	}

	@Override
	public int deleteByCreateUserIdAndColumnId(long createUserId,int columnId) throws Exception
    {
		Query query = new Query(Criteria.where(Constant.OwnerId).is(createUserId));
        if (columnId > 0) {
            query.addCriteria(Criteria.where(Constant.ColumnId).is(columnId));
        }

        WriteResult result = mongoTemplate.remove(query, getCollectionName(columnId));
        if (result.getN() <=0 ) {
            return -1;
        }
		return 0;
	}

    @Override
    public boolean deleteKnowledgeDirectory(long knowledgeId,int columnId,long directoryId)
    {
        KnowledgeDetail detail = getByIdAndColumnId(knowledgeId, columnId);
        List<Long> directoryIds = detail.getCategoryIds();
        if (directoryIds != null && directoryIds.contains(directoryId)) {
            directoryIds.remove(directoryId);
            detail.setCategoryIds(directoryIds);
            update(detail);
            return true;
        }
        else {
            logger.error("can't find the knowledge by, knowledgeId: {} columnId: {}", knowledgeId, columnId);
            return false;
        }
    }

	@Override
	public KnowledgeDetail getByIdAndColumnId(long id,int columnId) {
		Query query = knowledgeColumnIdQuery(id, columnId);
		return mongoTemplate.findOne(query,KnowledgeDetail.class, getCollectionName(columnId));
	}

    @Override
    public KnowledgeDetail insertAfterDelete(KnowledgeDetail knowledgeDetail) throws Exception {
        long knowledgeId = knowledgeDetail.getId();
        if(knowledgeDetail == null || knowledgeId <= 0) {
            throw new IllegalArgumentException("knowledgeDetail is null or invalidated!");
        }

        KnowledgeDetail oldValue = this.getByIdAndColumnId(knowledgeId,knowledgeDetail.getColumnId());
        if (oldValue == null) {
            try {
                this.insert(knowledgeDetail);
            } catch (Exception ex) {
                 throw ex;
            }
        }

        return oldValue;
    }

    @Override
	public List<KnowledgeDetail> getByIdsAndColumnId(List<Long> ids,int columnId) throws Exception
    {
		Query query = new Query(Criteria.where(Constant._ID).in(ids));
        if (columnId > 0) {
            query.addCriteria(Criteria.where(Constant.ColumnId).is(columnId));
        }
		
		return mongoTemplate.find(query,KnowledgeDetail.class, getCollectionName(columnId));
	}

    public List<KnowledgeDetail> getNoDirectory(long userId,int start,int size)
    {
        Query query = new Query(Criteria.where(Constant.OwnerId).is(userId));
        query.addCriteria(Criteria.where(Constant.categoryIds).exists(false));
        final String collectName = getCollectionName((short)0);
        long count = mongoTemplate.count(query, KnowledgeDetail.class, collectName);
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

    /*
	private String getCollectionName(short columnId) throws Exception {
		
		StringBuffer strBuf = new StringBuffer();
		strBuf.append(KNOWLEDGE_COLLECTION_NAME);
		
		//从缓存中获取系统栏目
		List<ColumnSys> columnSysList = new ArrayList<ColumnSys>();
		Iterator<ColumnSys> it = columnSysList.iterator();
		boolean columnCodeNotExistflag = true;
		
		while(it.hasNext()) {
			ColumnSys columnSys = it.next();
			if(columnId == columnSys.getId()) {
				if(StringUtils.isEmpty(columnSys.getColumnCode())) {
					break;
				}
				columnCodeNotExistflag = false;
				strBuf.append(columnSys.getColumnCode());
				break;
			}
		}
		
		if(columnCodeNotExistflag) {
			strBuf.append(KNOWLEDGE_COLLECTION_USERSELF_NAME);
		}
		
		return strBuf.toString();
	}*/
	
	private String getCollectionName(int columnId) {
		return KnowledgeUtil.getKnowledgeCollectionName(columnId);
	}

    private Class getKnowledgeClassType(int columnId)
    {
        return KnowledgeUtil.getKnowledgeClassType(columnId);
    }
	
	private Update getUpdate(KnowledgeDetail knowledgeDetail) {
        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObject.put("$set", knowledgeDetail);
        Update update = new BasicUpdate(basicDBObject);
		
		return update;
	}

    private Query knowledgeColumnIdQuery(long knowledgeId,int columnId)
    {
        Query query = new Query();
        query.addCriteria(Criteria.where(Constant._ID).is(knowledgeId));
        //columnId < 0, it means we query knowledge only by knowledgeId
        if (columnId > 0) {
            query.addCriteria(Criteria.where(Constant.ColumnId).is(columnId));
        }
        return query;
    }
}