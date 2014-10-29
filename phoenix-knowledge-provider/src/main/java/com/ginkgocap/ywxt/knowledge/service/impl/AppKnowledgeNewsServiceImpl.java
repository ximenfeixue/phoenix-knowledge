package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeNews;
import com.ginkgocap.ywxt.knowledge.service.AppKnowledgeNewsService;

public class AppKnowledgeNewsServiceImpl implements AppKnowledgeNewsService {

	@Resource
	private MongoTemplate mongoTemplate;
	
	@Override
	public void updateKnowledge(KnowledgeNews knowledgeNews) {
		
		mongoTemplate.save(knowledgeNews, "KnowledgeNews");
		
	}

	@Override
	public List<KnowledgeNews> selectKnowledgeByUserId(Long userId) {
		
		Criteria criteria = new Criteria().is(userId);
		Query query = new Query(criteria);
		query.sort().on("createtime", Order.DESCENDING);
		
		return mongoTemplate.find(query, KnowledgeNews.class, "KnowledgeNews");
	}

	@Override
	public KnowledgeNews selectKnowledgeByKnowledgeId(Long knowledgeId) {
		
		Criteria criteria = Criteria.where("_id").is(knowledgeId);
		Query query = new Query(criteria);
		
		return mongoTemplate.findOne(query, KnowledgeNews.class, "KnowledgeNews");
	}

}
