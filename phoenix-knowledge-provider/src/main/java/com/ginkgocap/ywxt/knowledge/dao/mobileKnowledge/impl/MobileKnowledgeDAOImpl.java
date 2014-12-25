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

import javax.annotation.Resource;

import net.sf.json.JSONArray;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.ginkgocap.ywxt.knowledge.dao.mobileKnowledge.MobileKnowledgeDAO;
import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.util.Constants;

@Component("mobileKnowledgeDAO")
public class MobileKnowledgeDAOImpl   implements MobileKnowledgeDAO {
	
 
	@Resource
	private MongoTemplate mongoTemplate;
 

	@Override
	public List<Knowledge> getKnowledge(String[] columnID,long user_id,String type,int offset,int limit) {
		String obj = Constants.getTableName(type);
		String mongo_collection = obj.substring(obj.lastIndexOf(".") + 1, obj.length());
		List<String> list = new ArrayList<String>(columnID.length);
		for(int i = 0; i < columnID.length; i++) {
			list.add(columnID[i]);
		}
		Query qu = query(where("uid").is(user_id).and("columnid").in(list).and("status").is(4)).skip(offset).limit(limit); 
		qu.sort().on("createtime", Order.DESCENDING);
		return mongoTemplate.find(qu, Knowledge.class, mongo_collection);
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
	
	//{ "$or" : [ { "uid" : 13511} , { "uid" : 0}] , "$and" : [ { "cpathid" : { "$regex" : "^投融工具.*$" , "$options" : ""}} , { "status" : 4}]}
	//{"$or":[{"_id" : { "$in" : [ 166475 , 316198]}}], "$and" : [ { "cpathid" : { "$regex" : "^投融工具.*$" } , { "status" : 4}]}
	@Override
	public List<Knowledge> fetchFriendKw(long[] kid,int type,int offset,int limit) {
		String class_name =Constants.getTableName(type + "");
		String collection_name = class_name.substring(class_name.lastIndexOf(".") + 1, class_name.length());
		
		String column = Constants.getKnowledgeTypeName(String.valueOf(type));
		
		if(StringUtils.isEmpty(column)) {
			System.out.println(String.format("column=%s,type=%d", column,type));
			return null;
		}
			
		/*	List<Long> list = new ArrayList<Long>(kid.length);
			for(int i = 0; i < kid.length; i++) {
				list.add(kid[i]);
			}
			Criteria ctri = new Criteria("cpathid");
			ctri.regex("^" + column + ".*$").and("status").is("4");*/
		if(kid == null || kid.length == 0) return null;
		List<Long> list = new ArrayList<Long>(kid.length);
		for(int i = 0; i < kid.length; i++) {
			list.add(kid[i]);
		}
		Criteria ctri = Criteria.where("_id").in(list).and("cpathid").regex("^" + column + ".*$").and("status").is(4);
		Query query_n = new Query(ctri);
		return mongoTemplate.find(query_n.limit(limit),Knowledge.class,collection_name);
/*		String knowledge = (kid == null || kid.length == 0) ? "[]" : JSONArray.fromObject(kid).toString();
		String result = String.format("{\"_id\":{ \"$in\":%s},\"$and\":[{\"cpathid\":{ \"$regex\":\"^%s.*$\"}},{\"status\":4}]}", knowledge,column);
		BasicQuery query = new BasicQuery(result);
		List<Knowledge> kn =  mongoTemplate.find(query,Knowledge.class,collection_name);
		return kn;*/
		
	}
	
	
	//{"_id":{ "$in":[]},"$and":[{"cpathid":{ "$regex":"^资讯.*$"}},{"status":4}]}
	@Override
	public long fetchFriendKwCount(long[] kid,int type) {
		String class_name =Constants.getTableName(type + "");
		String collection_name = class_name.substring(class_name.lastIndexOf(".") + 1, class_name.length());
		
		String column = Constants.getKnowledgeTypeName(String.valueOf(type));
		if(StringUtils.isEmpty(column)) {
			System.out.println(String.format("column=%s,type=%d", column,type));
			return 0L;
		}
		/*	
		List<Long> list = new ArrayList<Long>(kid.length);
		for(int i = 0; i < kid.length; i++) {
			list.add(kid[i]);
		}
		
		Criteria ctri = new Criteria("cpathid");
		ctri.regex("^" + column + ".*$").and("status").is("4");
		return mongoTemplate.count(query(where("_id").in(list).andOperator(ctri)), collection_name);*/
		String knowledge = (kid == null || kid.length == 0) ? "[]" : JSONArray.fromObject(kid).toString();
		String result = String.format("{\"_id\":{ \"$in\":%s},\"$and\":[{\"cpathid\":{ \"$regex\":\"^%s.*$\"}},{\"status\":4}]}", knowledge,column);
		BasicQuery query = new BasicQuery(result);
		long count = mongoTemplate.count(query,collection_name);
		return count;
	}

}
