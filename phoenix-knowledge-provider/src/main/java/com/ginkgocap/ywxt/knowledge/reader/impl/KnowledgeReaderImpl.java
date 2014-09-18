package com.ginkgocap.ywxt.knowledge.reader.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.ginkgocap.ywxt.knowledge.entity.KnowledgeStatics;
import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.reader.KnowledgeReader;
import com.ginkgocap.ywxt.knowledge.service.knowledgestatics.KnowledgeStaticsService;
import com.ginkgocap.ywxt.knowledge.util.Constants;
import com.ginkgocap.ywxt.knowledge.util.MongoUtils;
import com.ginkgocap.ywxt.user.model.User;
import com.ginkgocap.ywxt.user.service.FriendsRelationService;
import com.ginkgocap.ywxt.user.service.UserService;

public abstract class KnowledgeReaderImpl implements KnowledgeReader {

	@Resource
	private UserService userService;

	@Resource
	private KnowledgeStaticsService knowledgeStaticsService;

	@Resource
	private FriendsRelationService friendsRelationService;

	@Resource
	private MongoTemplate mongoTemplate;

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
	public String getKnowledgeContent(long kid, String type) {
		Knowledge knowledge = null;
		try {
			MongoUtils util = new MongoUtils();
			String c = util.getTableName(type);
			knowledge = (Knowledge) mongoTemplate.findById(kid,
					Class.forName(c), util.getCollectionName(c));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return StringUtils.isBlank(knowledge.getHcontent()) ? knowledge
				.getContent() : knowledge.getHcontent();
	}

}
