package com.ginkgocap.ywxt.knowledge.dao.mobileKnowledge.impl;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

import com.ginkgocap.ywxt.knowledge.dao.mobileKnowledge.MobileKnowledgeDAO;
import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.util.Constants;
import com.ibatis.sqlmap.client.SqlMapClient;

@Component("mobileKnowledgeDAO")
public class MobileKnowledgeDAOImpl extends SqlMapClientDaoSupport implements MobileKnowledgeDAO {
	
	@Autowired
	SqlMapClient sqlMapClient;

	@Resource
	private MongoTemplate mongoTemplate;

	@PostConstruct
	public void initSqlMapClient() {
		super.setSqlMapClient(sqlMapClient);
	}

	@Override
	public List<Knowledge> getKnowledge(String[] columnID,long user_id,String type,int offset,int limit) {
		String obj = Constants.getTableName(type);
		String mongo_collection = obj.substring(obj.lastIndexOf(".") + 1, obj.length());
		List<String> list = new ArrayList<String>(columnID.length);
		for(int i = 0; i < columnID.length; i++) {
			list.add(columnID[i]);
		}
		return mongoTemplate.find(query(where("uid").is(user_id).and("columnid").in(list).and("status").is(4)).skip(offset).limit(limit), Knowledge.class, mongo_collection);
	}
	
	@Override//{"uid":0,"status":4,"columnid": 1212}
	public long getKnowledgeByUserIdAndColumnID(String[] columnID,long user_id,String type) {
		String obj = Constants.getTableName(type);
		String mongo_collection = obj.substring(obj.lastIndexOf(".") + 1, obj.length());
		List<String> list = new ArrayList<String>(columnID.length);
		for(int i = 0; i < columnID.length; i++) {
			list.add(columnID[i]);
		}
		return mongoTemplate.count(query(where("uid").is(user_id).and("columnid").in(list).and("status").is(4)), mongo_collection);
	}

	@Override
	public List<Knowledge> getMixKnowledge(String columnID, long user_id,String type, int offset, int limit) {
		String class_name =Constants.getTableName(type);
		String collection_name = class_name.substring(class_name.lastIndexOf(".") + 1, class_name.length());
		return mongoTemplate.find(query(where("columnid").is(columnID).and("uid").in(Arrays.asList(new Long[]{0L,user_id}))).skip(offset).limit(limit), Knowledge.class,collection_name);
	
	}

	@Override
	public long getMixKnowledgeCount(String columnID, long user_id,
			String type) {
		String class_name =Constants.getTableName(type);
		String collection_name = class_name.substring(class_name.lastIndexOf(".") + 1, class_name.length());
		return mongoTemplate.count(query(where("columnid").is(columnID).and("uid").in(Arrays.asList(new Long[]{0L,user_id}))), collection_name);
	}

	@Override
	public List<Knowledge> fileKnowledge(Map<Long, Integer> map) {
		if(map == null || map.size() == 0) return null;
		Set<Entry<Long,Integer>> set = map.entrySet();
		Iterator<Entry<Long,Integer>> it = set.iterator();
		List<Knowledge> result = new ArrayList<Knowledge>();
		while(it.hasNext()) {
			Entry<Long,Integer> entry = it.next();
			long kid = entry.getKey();
			int value = entry.getValue();
			String class_name =Constants.getTableName(value + "");
			String collection_name = class_name.substring(class_name.lastIndexOf(".") + 1, class_name.length());
			Knowledge knowledge = mongoTemplate.findOne(query(where("_id").is(kid)),Knowledge.class,collection_name);
			if(knowledge != null) result.add(knowledge);
			
		}
		return result;
	}
	
	
	
	

}
