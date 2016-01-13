package com.ginkgocap.ywxt.knowledge.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.ginkgocap.ywxt.knowledge.dao.IKnowledgeMongoDao;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeMongo;
import com.ginkgocap.ywxt.knowledge.utils.DateUtil;
import com.ginkgocap.ywxt.user.model.User;
import com.mongodb.WriteResult;

@Repository("knowledgeMongoDao")
public class KnowledgeMongoDao implements IKnowledgeMongoDao {
	
	@Resource
	private MongoTemplate mongoTemplate;
	
	@Override
	public KnowledgeMongo insert(KnowledgeMongo knowledgeMongo, User user,String... collectionName) throws Exception {
		
		if(knowledgeMongo == null || knowledgeMongo.getId() <= 0)
			return null;
		
		String currentDate = DateUtil.formatWithYYYYMMDDHHMMSS(new Date());
		
		if(knowledgeMongo.getCreateUserId() <= 0)
			knowledgeMongo.setCreateUserId(user.getId());
		if(StringUtils.isBlank(knowledgeMongo.getCreateDate()))
			knowledgeMongo.setCreateDate(currentDate);
		knowledgeMongo.setModifyUserId(user.getId());
		knowledgeMongo.setModifyDate(currentDate);
		
		String currCollectionName = getCollectionName(knowledgeMongo.getColumnId(),collectionName);
		
		mongoTemplate.insert(knowledgeMongo,currCollectionName);
		
		return this.getByIdAndColumnId(knowledgeMongo.getId(),knowledgeMongo.getColumnId(),currCollectionName);
	}
	
	@Override
	public List<KnowledgeMongo> insertList(List<KnowledgeMongo> knowledgeMongoList, User user,String... collectionName) throws Exception {
		
		List<KnowledgeMongo> returnList = new ArrayList();
		
		if(knowledgeMongoList != null && !knowledgeMongoList.isEmpty()) {
			Iterator<KnowledgeMongo> it = knowledgeMongoList.iterator();
			while(it.hasNext()) {
				returnList.add(this.insert(it.next(), user, collectionName));
			}
		}
		
		return returnList;
	}

	@Override
	public KnowledgeMongo update(KnowledgeMongo knowledgeMongo, User user,String... collectionName)
			throws Exception {

		if(knowledgeMongo == null)
			return null;
		
		long knowledgeId = knowledgeMongo.getId();
		
		if(knowledgeId <= 0) {
			return this.insert(knowledgeMongo, user);
		}
		
		String currentDate = DateUtil.formatWithYYYYMMDDHHMMSS(new Date());

		knowledgeMongo.setModifyUserId(user.getId());
		knowledgeMongo.setModifyDate(currentDate);
		
		Criteria criteria = Criteria.where("_id").is(knowledgeId);
		Query query = new Query(criteria);
		
		String currCollectionName = getCollectionName(knowledgeMongo.getColumnId(),collectionName);
		
		WriteResult result = mongoTemplate.updateFirst(query, getUpdate(knowledgeMongo,user), currCollectionName);
		
		return this.getByIdAndColumnId(knowledgeId,knowledgeMongo.getColumnId(),currCollectionName);
	}

	@Override
	public KnowledgeMongo insertAfterDelete(KnowledgeMongo knowledgeMongo,
			long knowledgeId, User user,String... collectionName) throws Exception {
		
		String currCollectionName = getCollectionName(knowledgeMongo.getColumnId(),collectionName);
		
		if(knowledgeMongo == null || knowledgeId <= 0)
			return null;
		
		KnowledgeMongo oldValue = this.getByIdAndColumnId(knowledgeId,knowledgeMongo.getColumnId(),currCollectionName);
		
		this.deleteByIdAndColumnId(knowledgeId,knowledgeMongo.getColumnId(),currCollectionName);
		
		try {
			
			this.insert(knowledgeMongo, user,currCollectionName);
			
		} catch (Exception e) {
			
			if(oldValue != null)
				this.insert(oldValue, user,currCollectionName);
			
			throw e;
		}
		
		
		return this.getByIdAndColumnId(knowledgeId,knowledgeMongo.getColumnId());
	}

	@Override
	public int deleteByIdAndColumnId(long id,long columnId, String...collectionName ) throws Exception {
		
		Criteria criteria = Criteria.where("_id").is(id);
		Query query = new Query(criteria);
		
		mongoTemplate.remove(query, getCollectionName(columnId,collectionName));
		
		return 0;
	}

	@Override
	public int deleteByIdsAndColumnId(List<Long> ids,long columnId,String... collectionName) throws Exception {
		
		Criteria criteria = Criteria.where("_id").in(ids);
		Query query = new Query(criteria);
		
		mongoTemplate.remove(query, getCollectionName(columnId,collectionName));
		
		return 0;
	}

	@Override
	public int deleteByCreateUserIdAndColumnId(long createUserId,long columnId,String... collectionName) throws Exception {
		
		Criteria criteria = Criteria.where("createUserId").is(createUserId);
		Query query = new Query(criteria);
		
		mongoTemplate.remove(query, getCollectionName(columnId,collectionName));
		
		return 0;
	}

	@Override
	public KnowledgeMongo getByIdAndColumnId(long id,long columnId,String... collectionName) throws Exception {
		
		Criteria criteria = Criteria.where("_id").is(id);
		Query query = new Query(criteria);
		
		return mongoTemplate.findOne(query,KnowledgeMongo.class, getCollectionName(columnId,collectionName));
		
	}
	
	@Override
	public List<KnowledgeMongo> getByIdsAndColumnId(List<Long> ids,long columnId,String... collectionName) throws Exception {
		
		Criteria criteria = Criteria.where("_id").in(ids);
		Query query = new Query(criteria);
		
		return mongoTemplate.find(query,KnowledgeMongo.class, getCollectionName(columnId,collectionName));
		
	}
	
	private String getCollectionName(long columnId) throws Exception {
		
		StringBuffer strBuf = new StringBuffer();
		
		strBuf.append(KNOWLEDGE_COLLECTION_NAME);
		
		//从缓存中获取系统栏目
		List columnSysList = new ArrayList();
		
		Iterator it = columnSysList.iterator();
		
		boolean columnCodeNotExistflag = true;
		
		while(it.hasNext()) {
			//ColulmnSys columnSys = it.next();
			//if(columnId == columnSys.getId()) {
				//if(StringUtils.isEmpty(columnSys.getColumnCode())) {
					//break;
				//}
				columnCodeNotExistflag = false;
				//strBuf.append(columnSys.getColumnCode());
				break;
			//}
		}
		
		if(columnCodeNotExistflag) {
			strBuf.append(KNOWLEDGE_COLLECTION_USERSELF_NAME);
		}
		
		return strBuf.toString();
		
	}
	
	private String getCollectionName(long columnId,String[] collectionName) throws Exception {
		return ArrayUtils.isEmpty(collectionName) && StringUtils.isEmpty(collectionName[0]) ? getCollectionName(columnId) : collectionName[0];
	}
	
	private Update getUpdate(KnowledgeMongo knowledgeMongo, User user) {
		
		//构建更新字段，目前默认是全字段更新
		Update update = new Update();
		
		JSONObject json = JSONObject.fromObject(knowledgeMongo);
		
		Iterator<String> it = json.keys();
		
		while(it.hasNext()) {
			String key = it.next();
			update.update(key, json.get(key));
		}
		
		return update;
		
	}
}