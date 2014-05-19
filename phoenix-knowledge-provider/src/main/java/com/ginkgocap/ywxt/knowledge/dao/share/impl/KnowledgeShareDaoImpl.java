package com.ginkgocap.ywxt.knowledge.dao.share.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.ginkgocap.ywxt.knowledge.dao.share.KnowledgeShareDao;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeShare;
import com.ginkgocap.ywxt.util.DateFunc;
import com.ginkgocap.ywxt.util.MakePrimaryKey;
@Component("knowledgeShareDao")
public class KnowledgeShareDaoImpl implements KnowledgeShareDao {

	@Resource
	private MongoTemplate mongoTemplate;
	
	@Override
	public KnowledgeShare save(KnowledgeShare knowledgeShare) {
		Criteria criteria = Criteria.where("knowledgeId").is(knowledgeShare.getKnowledgeId()).and("userId").is(knowledgeShare.getUserId());
		Query query = new Query(criteria);
		KnowledgeShare ks = mongoTemplate.findOne(query, KnowledgeShare.class);
		if(ks!=null){
			Update update =  new Update();
			update.set("receiverId", knowledgeShare.getReceiverId());
			update.set("receiverName", knowledgeShare.getReceiverName());
			update.set("ctime", DateFunc.getDate());
			update.set("title", knowledgeShare.getTitle());
			update.set("friends", knowledgeShare.getFriends());
			mongoTemplate.updateMulti(query, update, KnowledgeShare.class);
			return ks;
		}
		knowledgeShare.setCtime(DateFunc.getDate());
		knowledgeShare.setId(MakePrimaryKey.getPrimaryKey());
		mongoTemplate.save(knowledgeShare);
		return knowledgeShare;
	}

	@Override
	public List<KnowledgeShare> findMyShare(long userId, int start, int end, String title) {
		Criteria criteria = Criteria.where("userId").is(userId);
		if(StringUtils.isNotBlank(title)){
			criteria.and("title").regex(".*?"+title+".*");
		}
		Query query = new Query(criteria);
		query.sort().on("ctime", Order.DESCENDING);
		query.skip(start);
		query.limit(end);
		return mongoTemplate.find(query, KnowledgeShare.class);
	}

	@Override
	public int findMyShareCount(long userId, String title) {
		Criteria criteria = Criteria.where("userId").is(userId);
		if(StringUtils.isNotBlank(title)){
			criteria.and("title").regex(".*?"+title+".*");
		}
		Query query = new Query(criteria);
		return (int) mongoTemplate.count(query, KnowledgeShare.class);
	}

	@Override
	public List<KnowledgeShare> findShareMe(long userId, int start, int end, String title) {
		Criteria criteria = Criteria.where("receiverId").is(userId);
		criteria.and("friends.userId").is(userId).and("friends.status").ne(-1);
		if(StringUtils.isNotBlank(title)){
			criteria.and("title").regex(".*?"+title+".*");
		}
		Query query = new Query(criteria);
		query.sort().on("ctime", Order.DESCENDING);
		query.skip(start);
		query.limit(end);
		return mongoTemplate.find(query, KnowledgeShare.class);
	}

	@Override
	public int findShareMeCount(long userId, String title) {
		Criteria criteria = Criteria.where("receiverId").is(userId);
		criteria.and("friends.userId").is(userId).and("friends.status").ne(-1);
		if(StringUtils.isNotBlank(title)){
			criteria.and("title").regex(".*?"+title+".*");
		}
		Query query = new Query(criteria);
		return (int) mongoTemplate.count(query, KnowledgeShare.class);
	}

	@Override
	public void deleteShareInfoByKnowledgeId(long knowledgeId) {
		Criteria criteria = Criteria.where("knowledgeId").is(knowledgeId);
		Query query = new Query(criteria);
		mongoTemplate.remove(query, KnowledgeShare.class);
	}

	@Override
	public KnowledgeShare findMyShareOne(long userId, long knowledgeId) {
		Criteria criteria = Criteria.where("knowledgeId").is(knowledgeId).and("userId").is(userId);
		Query query = new Query(criteria);
		return mongoTemplate.findOne(query, KnowledgeShare.class);
	}

	@Override
	public KnowledgeShare findShareMeOne(long userId, long knowledgeId) {
		Criteria criteria = Criteria.where("knowledgeId").is(knowledgeId).and("receiverId").is(userId);
		Query query = new Query(criteria);
		return mongoTemplate.findOne(query, KnowledgeShare.class);
	}

	@Override
	public void updateMyShareTitle(long userId, long knowledgeId, String title) {
		Criteria criteria = Criteria.where("knowledgeId").is(knowledgeId).and("userId").is(userId);
		Query query = new Query(criteria);
		Update update = new Update();
		update.set("title", title);
		mongoTemplate.updateFirst(query, update, KnowledgeShare.class);
	}

	@Override
	public void updateShareMeTitle(long userId, long knowledgeId, String title) {
		Criteria criteria = Criteria.where("knowledgeId").is(knowledgeId).and("receiverId").is(userId);
		Query query = new Query(criteria);
		Update update = new Update();
		update.set("title", title);
		mongoTemplate.updateFirst(query, update, KnowledgeShare.class);
	}

	@Override
	public void deleteMyShare(long userId, long knowledgeId) {
		Criteria criteria = Criteria.where("knowledgeId").is(knowledgeId).and("userId").is(userId);
		Query query = new Query(criteria);
		mongoTemplate.remove(query, KnowledgeShare.class);
		
	}

	@Override
	public void deleteShareMe(long userId, long knowledgeId) {
		Criteria criteria = Criteria.where("knowledgeId").is(knowledgeId).and("receiverId").is(userId);
		Query query = new Query(criteria);
		Update update = new Update();
		update.set("friends.$.status", -1);
		mongoTemplate.updateMulti(query, update, KnowledgeShare.class);
	}




	

}
