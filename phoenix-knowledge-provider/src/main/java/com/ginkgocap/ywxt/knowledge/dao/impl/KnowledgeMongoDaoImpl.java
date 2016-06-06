package com.ginkgocap.ywxt.knowledge.dao.impl;

import com.ginkgocap.ywxt.knowledge.dao.KnowledgeMongoDao;
import com.ginkgocap.ywxt.knowledge.model.ColumnSys;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeDetail;
import com.ginkgocap.ywxt.knowledge.model.common.Constant;
import com.ginkgocap.ywxt.knowledge.service.common.KnowledgeCommonService;
import com.mongodb.BasicDBObject;
import com.mongodb.WriteResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicUpdate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Repository("knowledgeMongoDao")
public class KnowledgeMongoDaoImpl implements KnowledgeMongoDao {
	
	@Resource
	private MongoTemplate mongoTemplate;

    @Autowired
    private KnowledgeCommonService knowledgeCommontService;
	
	@Override
	public KnowledgeDetail insert(KnowledgeDetail knowledgeDetail, String... collectionName) throws Exception {
		if(knowledgeDetail == null) {
            throw new IllegalArgumentException("knowledgeDetail is null");
        }

		long currentDate = new Date().getTime();
        knowledgeDetail.setCreateTime(currentDate);
		
		String currCollectionName = getCollectionName(knowledgeDetail.getColumnId(),collectionName);
        knowledgeDetail.setId(knowledgeCommontService.getKnowledgeSeqenceId());
		mongoTemplate.insert(knowledgeDetail,currCollectionName);
		
		return knowledgeDetail;
	}
	
	@Override
	public List<KnowledgeDetail> insertList(List<KnowledgeDetail> knowledgeDetailList,  String... collectionName) throws Exception {
		if(knowledgeDetailList != null && knowledgeDetailList.size() > 0) {
			for(KnowledgeDetail knowledgeDetail : knowledgeDetailList) {
                knowledgeDetail.setId(knowledgeCommontService.getKnowledgeSeqenceId());
			}
		}
        mongoTemplate.insert(knowledgeDetailList, KnowledgeDetail.class);

		return knowledgeDetailList;
	}

	@Override
	public KnowledgeDetail update(KnowledgeDetail knowledgeDetail, String... collectionName)
			throws Exception {

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
        String currCollectionName = getCollectionName(knowledgeDetail.getColumnId(),collectionName);
		WriteResult result = mongoTemplate.updateFirst(query, getUpdate(knowledgeDetail), currCollectionName);
        if (result.getN()<=0) {
            return null;
        }
		
		return knowledgeDetail;
	}


	@Override
	public int deleteByIdAndColumnId(long id,short columnId, String...collectionName ) throws Exception
    {
		Query query = knowledgeColumnIdQuery(id, columnId);
        WriteResult result = mongoTemplate.remove(query, getCollectionName(columnId,collectionName));
        if (result.getN() <=0 ) {
            return -1;
        }
		return 0;
	}

	@Override
	public int deleteByIdsAndColumnId(List<Long> ids,short columnId,String... collectionName) throws Exception
    {
        Query query = new Query(Criteria.where(Constant._ID).in(ids));
        if (columnId > 0) {
            query.addCriteria(Criteria.where(Constant.ColumnId).is(columnId));
        }

        WriteResult result = mongoTemplate.remove(query, getCollectionName(columnId,collectionName));
        if (result.getN() <=0 ) {
            return -1;
        }
		return 0;
	}

	@Override
	public int deleteByCreateUserIdAndColumnId(long createUserId,short columnId,String... collectionName) throws Exception
    {
		Query query = new Query(Criteria.where(Constant.OwnerId).is(createUserId));
        query.addCriteria(Criteria.where(Constant.ColumnId).is(columnId));

        WriteResult result = mongoTemplate.remove(query, getCollectionName(columnId,collectionName));
        if (result.getN() <=0 ) {
            return -1;
        }
		return 0;
	}

	@Override
	public KnowledgeDetail getByIdAndColumnId(long id,short columnId,String... collectionName) throws Exception {
		Query query = knowledgeColumnIdQuery(id, columnId);
		
		return mongoTemplate.findOne(query,KnowledgeDetail.class, getCollectionName(columnId,collectionName));
	}

    @Override
    public KnowledgeDetail insertAfterDelete(KnowledgeDetail knowledgeDetail,String... collectionName) throws Exception {
        long knowledgeId = knowledgeDetail.getId();
        if(knowledgeDetail == null || knowledgeId <= 0) {
            throw new IllegalArgumentException("knowledgeDetail is null or invalidated!");
        }

        String currCollectionName = getCollectionName(knowledgeDetail.getColumnId(),collectionName);
        KnowledgeDetail oldValue = this.getByIdAndColumnId(knowledgeId,knowledgeDetail.getColumnId(),currCollectionName);
        if (oldValue == null) {
            try {
                this.insert(knowledgeDetail, currCollectionName);
            } catch (Exception ex) {
                 throw ex;
            }
        }

        return oldValue;
    }

    @Override
	public List<KnowledgeDetail> getByIdsAndColumnId(List<Long> ids,short columnId,String... collectionName) throws Exception
    {
		Query query = new Query(Criteria.where(Constant._ID).in(ids));
        query.addCriteria(Criteria.where(Constant.ColumnId).is(columnId));
		
		return mongoTemplate.find(query,KnowledgeDetail.class, getCollectionName(columnId,collectionName));
	}
	
	private String getCollectionName(long columnId) throws Exception {
		
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
	}
	
	private String getCollectionName(long columnId,String[] collectionName) throws Exception {
		return "Knowledge"; //ArrayUtils.isEmpty(collectionName) && StringUtils.isEmpty(collectionName[0]) ? getCollectionName(columnId) : collectionName[0];
	}
	
	private Update getUpdate(KnowledgeDetail knowledgeDetail) {
        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObject.put("$set", knowledgeDetail);
        Update update = new BasicUpdate(basicDBObject);
		
		return update;
	}

    private Query knowledgeColumnIdQuery(long knowledgeId,short columnId)
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