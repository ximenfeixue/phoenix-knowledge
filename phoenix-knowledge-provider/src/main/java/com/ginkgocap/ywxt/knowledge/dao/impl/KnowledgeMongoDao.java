package com.ginkgocap.ywxt.knowledge.dao.impl;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.ginkgocap.ywxt.knowledge.dao.IKnowledgeMongoDao;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeMongo;
import com.ginkgocap.ywxt.user.model.User;
import com.mongodb.WriteResult;

@Repository("knowledgeMongoDao")
public class KnowledgeMongoDao implements IKnowledgeMongoDao {
	
	@Resource
	private MongoTemplate mongoTemplate;

	@Override
	public KnowledgeMongo insert(KnowledgeMongo KnowledgeMongo,
			long knowledgeId, User user) throws Exception {
		
		if(KnowledgeMongo == null || knowledgeId <= 0)
			return null;
		
		KnowledgeMongo.setId(knowledgeId);
		
		mongoTemplate.insert(KnowledgeMongo,KNOWLEDGE_COLLECTION_NAME);
		
		return this.getById(knowledgeId);
	}

	@Override
	public KnowledgeMongo update(KnowledgeMongo KnowledgeMongo, User user)
			throws Exception {

		if(KnowledgeMongo == null)
			return null;
		
		long knowledgeId = KnowledgeMongo.getId();
		
		if(knowledgeId <= 0) {
			return this.insert(KnowledgeMongo, knowledgeId, user);
		}
		
		Criteria criteria = Criteria.where("_id").is(knowledgeId);
		Query query = new Query(criteria);
		
		Update update = new Update();
		
		JSONObject json = JSONObject.fromObject(KnowledgeMongo);
		
		Iterator<String> it = json.keys();
		
		while(it.hasNext()) {
			String key = it.next();
			update.update(key, json.get(key));
		}
		
		WriteResult result = mongoTemplate.updateFirst(query, update, KNOWLEDGE_COLLECTION_NAME);
		
		return this.getById(knowledgeId);
	}

	@Override
	public KnowledgeMongo insertAfterDelete(KnowledgeMongo KnowledgeMongo,
			long knowledgeId, User user) throws Exception {
		
		if(KnowledgeMongo == null || knowledgeId <= 0)
			return null;
		
		KnowledgeMongo oldValue = this.getById(knowledgeId);
		
		this.deleteById(knowledgeId);
		
		try {
			
			this.insert(KnowledgeMongo, knowledgeId, user);
			
		} catch (Exception e) {
			
			if(oldValue != null)
				this.insert(oldValue, knowledgeId, user);
			
			throw e;
		}
		
		
		return this.getById(knowledgeId);
	}

	@Override
	public int deleteById(long id) throws Exception {
		
		Criteria criteria = Criteria.where("_id").is(id);
		Query query = new Query(criteria);
		
		mongoTemplate.remove(query, KNOWLEDGE_COLLECTION_NAME);
		
		return 0;
	}

	@Override
	public int deleteByIds(List<Long> ids) throws Exception {
		
		Criteria criteria = Criteria.where("_id").in(ids);
		Query query = new Query(criteria);
		
		mongoTemplate.remove(query, KNOWLEDGE_COLLECTION_NAME);
		
		return 0;
	}

	@Override
	public int deleteByCreateUserId(long createUserId) throws Exception {
		
		Criteria criteria = Criteria.where("createUserId").is(createUserId);
		Query query = new Query(criteria);
		
		mongoTemplate.remove(query, KNOWLEDGE_COLLECTION_NAME);
		
		return 0;
	}

	@Override
	public KnowledgeMongo getById(long id) throws Exception {
		
		Criteria criteria = Criteria.where("_id").is(id);
		Query query = new Query(criteria);
		
		return mongoTemplate.findOne(query,KnowledgeMongo.class, KNOWLEDGE_COLLECTION_NAME);
		
	}
	
	
	
}