package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.ginkgocap.ywxt.knowledge.dao.reader.KnowledgeReaderDAO;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeStatics;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeReaderService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeStaticsService;
import com.ginkgocap.ywxt.knowledge.util.Constants;
import com.ginkgocap.ywxt.user.model.User;
import com.ginkgocap.ywxt.user.service.FriendsRelationService;
import com.ginkgocap.ywxt.user.service.UserService;

public abstract class KnowledgeReaderServiceImpl implements
		KnowledgeReaderService {

	private Logger logger = LoggerFactory
			.getLogger(KnowledgeReaderServiceImpl.class);

	@Resource
	private UserService userService;

	@Resource
	private KnowledgeStaticsService knowledgeStaticsService;

	@Resource
	private FriendsRelationService friendsRelationService;

	@Resource
	private MongoTemplate mongoTemplate;

	@Resource
	private KnowledgeReaderDAO knowledgeReaderDAO;

	@Override
	public User getUserInfo(long userid) {
		return userService.selectByPrimaryKey(userid);
	}

	@Override
	public KnowledgeStatics getKnowledgeStatusCount(long kid) {
		return knowledgeStaticsService.selectByknowledgeId(kid);
	}

	@Override
	public Map<String, Integer> isExistFriends(long loginuserid, long kuid) {
		Map<String, Integer> result = new HashMap<String, Integer>();
		if (kuid == 0) {
			result.put("relation", Constants.Relation.jintongnao.v());
		} else if (loginuserid == kuid) {
			result.put("relation", Constants.Relation.self.v());
		} else if (friendsRelationService.isExistFriends(loginuserid, kuid)) {
			result.put("relation", Constants.Relation.friends.v());
		} else {
			result.put("relation", Constants.Relation.none.v());
		}
		return result;
	}

	@Override
	public Map<String, Boolean> showHeadTag(long kid, int type) {
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		boolean download = false;
		boolean sourceFile = false;
		boolean category = false;
		boolean mark = false;

		if (type == Constants.Type.News.v()) {
			sourceFile = true;
		} else if (type == Constants.Type.Industry.v()) {
			category = true;
		} else if (type == Constants.Type.Asset.v()) {
			sourceFile = true;
		} else if (type == Constants.Type.BookReport.v()) {
			mark = true;
			download = true;
			category = true;
		} else if (type == Constants.Type.Example.v()) {
			download = true;
			category = true;
		} else if (type == Constants.Type.Investment.v()) {
			category = true;
		} else if (type == Constants.Type.Macro.v()) {
			sourceFile = true;
		}

		result.put("download", download);
		result.put("sourceFile", sourceFile);
		result.put("category", category);
		result.put("mark", mark);

		return result;
	}

	@Override
	public Map<String, Object> getKnowledgeContent(long kid, String type) {
		Map<String, Object> result = new HashMap<String, Object>();
		String content = null;
		try {
			content = knowledgeReaderDAO.findHtmlContentFromMongo(kid, type);

			result.put(Constants.status, Constants.ResultType.success.v());
			result.put("content", content);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			logger.error("未能根据类型:{}创建对象", type);
			result.put(Constants.status, Constants.ResultType.fail.v());
		}
		return result;
	}
}
