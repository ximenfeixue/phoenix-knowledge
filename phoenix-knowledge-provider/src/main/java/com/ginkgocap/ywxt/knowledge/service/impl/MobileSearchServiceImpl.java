package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.mapper.MobileKnowledgeMapper;
import com.ginkgocap.ywxt.knowledge.model.UserPermissionMongo;
import com.ginkgocap.ywxt.knowledge.service.MobileSearchService;
import com.ginkgocap.ywxt.knowledge.util.HTTPUtil;
import com.ginkgocap.ywxt.util.PageUtil;

@Service("mobileSearchService")
public class MobileSearchServiceImpl implements MobileSearchService {

	private static final Logger logger = LoggerFactory
			.getLogger(KnowledgeCollectionServiceImpl.class);
	@Resource
	private MongoTemplate mongoTemplate;
	@Resource
	private MobileKnowledgeMapper mobileKnowledgeMapper;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Map<String, Object> searchByKeywords(Long userid, String keywords,
			String scope, String pno, String psize) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("user_id", userid + "");
		params.put("keywords", keywords);
		params.put("scope", scope);
		params.put("pno", pno);
		params.put("psize", psize);

		String str = HTTPUtil.post("knowledge/keyword/search.json", params);

		ObjectMapper mapper = new ObjectMapper();
		Map result = null;
		try {
			result = mapper.readValue(str, Map.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	@Override
	public Map<String, Object> selectKnowledgeByTagsAndkeywords(Long userid,
			String keywords, String scope, String tag, int page, int size) {
		logger.info(
				"com.ginkgocap.ywxt.knowledge.service.impl.MobileSearchService.selectKnowledgeByTagsAndkeywords:{},",
				userid);
		logger.info(
				"com.ginkgocap.ywxt.knowledge.service.impl.MobileSearchService.selectKnowledgeByTagsAndkeywords:{},",
				keywords);
		logger.info(
				"com.ginkgocap.ywxt.knowledge.service.impl.MobileSearchService.selectKnowledgeByTagsAndkeywords:{},",
				scope);
		logger.info(
				"com.ginkgocap.ywxt.knowledge.service.impl.MobileSearchService.selectKnowledgeByTagsAndkeywords:{},",
				tag);
		logger.info(
				"com.ginkgocap.ywxt.knowledge.service.impl.MobileSearchService.selectKnowledgeByTagsAndkeywords:{},",
				page);
		logger.info(
				"com.ginkgocap.ywxt.knowledge.service.impl.MobileSearchService.selectKnowledgeByTagsAndkeywords:{},",
				size);
		int start = (page - 1) * size;
		/** 判断是否传的是默认值 */
		start = start < 0 ? 0 : start;
		int count = mobileKnowledgeMapper
				.selectCountKnowledgeByTagsAndKeyWords(userid, tag, keywords);
		List<?> kcl = mobileKnowledgeMapper.selectKnowledgeByTagsAndKeyWords(
				userid, tag, keywords, start, size);
		PageUtil p = new PageUtil(count, page, size);
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("page", p);
		m.put("list", kcl);
		return m;
	}

	@Override
	public Map<String, Object> selectKnowledgeByMyCollectionAndkeywords(
			Long userid, String keywords, String scope, int page, int size) {
		logger.info(
				"com.ginkgocap.ywxt.knowledge.service.impl.MobileSearchService.selectKnowledgeByMyCollectionAndkeywords:{},",
				userid);
		logger.info(
				"com.ginkgocap.ywxt.knowledge.service.impl.MobileSearchService.selectKnowledgeByMyCollectionAndkeywords:{},",
				keywords);
		logger.info(
				"com.ginkgocap.ywxt.knowledge.service.impl.MobileSearchService.selectKnowledgeByMyCollectionAndkeywords:{},",
				scope);
		logger.info(
				"com.ginkgocap.ywxt.knowledge.service.impl.MobileSearchService.selectKnowledgeByMyCollectionAndkeywords:{},",
				page);
		logger.info(
				"com.ginkgocap.ywxt.knowledge.service.impl.MobileSearchService.selectKnowledgeByMyCollectionAndkeywords:{},",
				size);
		int start = (page - 1) * size;
		/** 判断是否传的是默认值 */
		start = start < 0 ? 0 : start;
		int count = mobileKnowledgeMapper
				.selectCountKnowledgeByMyCollectionAndKeyWords(userid, keywords);
		List<?> kcl = mobileKnowledgeMapper
				.selectKnowledgeByMyCollectionAndKeyWords(userid, keywords,
						start, size);
		PageUtil p = new PageUtil(count, page, size);
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("page", p);
		m.put("list", kcl);
		return m;
	}

	@Override
	public Map<String, Object> selectShareMeByKeywords(Long userid,
			String keywords, String scope, int start, int size) {
		List<UserPermissionMongo> lt = null;
		PageUtil page = null;
		String pattern = "^.*" + keywords + ".*$"; 
		Criteria c = Criteria.where("receiveUserId").is(userid);
		c.andOperator(new Criteria("title").regex(pattern));
		Query query = new Query(c);
		long count = mongoTemplate.count(query, UserPermissionMongo.class);
		page = new PageUtil((int) count, start, size);
		query = new Query(c);
		query.sort().on("createtime", Order.DESCENDING);
		if (size > 0) {
			query.skip((start - 1) * size);
			query.limit(size);
		}
		lt = mongoTemplate.find(query, UserPermissionMongo.class);
		if (lt == null) {
			lt = new ArrayList<UserPermissionMongo>();
		}
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("pageUtil", page);
		returnMap.put("list", lt);

		return returnMap;
	}

	@Override
	public Map<String, Object> selectKnowledgeBySourceAndColumn(Long userid, long columnId, String scope, int page, int size) {
		logger.info(
				"com.ginkgocap.ywxt.knowledge.service.impl.MobileSearchService.selectKnowledgeBySourceAndColumn:{},",
				userid);
		logger.info(
				"com.ginkgocap.ywxt.knowledge.service.impl.MobileSearchService.selectKnowledgeBySourceAndColumn:{},",
				columnId);
		logger.info(
				"com.ginkgocap.ywxt.knowledge.service.impl.MobileSearchService.selectKnowledgeBySourceAndColumn:{},",
				scope);
		logger.info(
				"com.ginkgocap.ywxt.knowledge.service.impl.MobileSearchService.selectKnowledgeBySourceAndColumn:{},",
				page);
		logger.info(
				"com.ginkgocap.ywxt.knowledge.service.impl.MobileSearchService.selectKnowledgeBySourceAndColumn:{},",
				size);
		int start = (page - 1) * size;
		/** 判断是否传的是默认值 */
		start = start < 0 ? 0 : start;
		int count = mobileKnowledgeMapper
				.selectCountKnowledgeBySourceAndColumn(columnId, userid);
		List<?> kcl = mobileKnowledgeMapper
				.selectKnowledgeBySourceAndColumn(columnId, userid,
						start, size);
		PageUtil p = new PageUtil(count, page, size);
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("page", p);
		m.put("list", kcl);
		return m;
	}

	@Override
	public Map<String, Object> selectMyFriendKnowledgeByKeywords(
			String friends, long columnId, String scope, int page, int size) {
		logger.info(
				"com.ginkgocap.ywxt.knowledge.service.impl.MobileSearchService.selectMyFriendKnowledgeByKeywords:{},",
				columnId);
		logger.info(
				"com.ginkgocap.ywxt.knowledge.service.impl.MobileSearchService.selectMyFriendKnowledgeByKeywords:{},",
				friends);
		logger.info(
				"com.ginkgocap.ywxt.knowledge.service.impl.MobileSearchService.selectMyFriendKnowledgeByKeywords:{},",
				scope);
		logger.info(
				"com.ginkgocap.ywxt.knowledge.service.impl.MobileSearchService.selectMyFriendKnowledgeByKeywords:{},",
				page);
		logger.info(
				"com.ginkgocap.ywxt.knowledge.service.impl.MobileSearchService.selectMyFriendKnowledgeByKeywords:{},",
				size);
		int start = (page - 1) * size;
		/** 判断是否传的是默认值 */
		start = start < 0 ? 0 : start;
		/** 临时数组 */
		String[] temp = friends.split(",");
		/** 传递数据 */
		long[] tempFriends = new long[temp.length];
		/** 替换 */
		for (int i = 0; i < tempFriends.length; i++) {
			tempFriends[i] = Long.parseLong(temp[i]);
		}
		int count = mobileKnowledgeMapper
				.selectCountForMyFriendKnowledgeByKeyWords(tempFriends, columnId);
		List<?> kcl = mobileKnowledgeMapper
				.selectMyFriendKnowledgeByKeyWords(tempFriends, columnId,
						start, size);
		PageUtil p = new PageUtil(count, page, size);
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("page", p);
		m.put("list", kcl);
		return m;
	}

}
