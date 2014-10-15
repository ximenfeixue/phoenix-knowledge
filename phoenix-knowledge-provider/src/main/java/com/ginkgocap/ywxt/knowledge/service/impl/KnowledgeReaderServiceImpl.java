package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.dao.reader.KnowledgeReaderDAO;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeStatics;
import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeReaderService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeStaticsService;
import com.ginkgocap.ywxt.knowledge.util.Constants;
import com.ginkgocap.ywxt.knowledge.util.DateUtil;
import com.ginkgocap.ywxt.user.model.User;
import com.ginkgocap.ywxt.user.service.FriendsRelationService;
import com.ginkgocap.ywxt.user.service.UserService;

@Service("knowledgeReaderService")
public class KnowledgeReaderServiceImpl implements KnowledgeReaderService {

	private Logger logger = LoggerFactory
			.getLogger(KnowledgeReaderServiceImpl.class);

	@Resource
	private UserService userService;

	@Resource
	private KnowledgeStaticsService knowledgeStaticsService;

	@Resource
	private FriendsRelationService friendsRelationService;

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
	public Map<String, Integer> authorAndLoginUserRelation(long loginuserid,
			long kuid) {
		Map<String, Integer> result = new HashMap<String, Integer>();
		if (loginuserid == -1) {
			result.put("relation", Constants.Relation.none.v());
		} else if (kuid == 0) {
			result.put("relation", Constants.Relation.jinTN.v());
		} else if (loginuserid == kuid) {
			result.put("relation", Constants.Relation.self.v());
		} else if (friendsRelationService.isExistFriends(loginuserid, kuid)) {
			result.put("relation", Constants.Relation.friends.v());
		}
		return result;
	}

	@Override
	public Map<String, Boolean> showHeadTag(long kid, String type) {
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		boolean download = false;
		boolean sourceFile = false;
		boolean category = false;
		boolean mark = false;

		if (Integer.parseInt(type) == Constants.Type.News.v()) {
			sourceFile = true;
		} else if (Integer.parseInt(type) == Constants.Type.Industry.v()) {
			category = true;
		} else if (Integer.parseInt(type) == Constants.Type.Asset.v()) {
			sourceFile = true;
		} else if (Integer.parseInt(type) == Constants.Type.BookReport.v()) {
			mark = true;
			download = true;
			category = true;
		} else if (Integer.parseInt(type) == Constants.Type.Example.v()) {
			download = true;
			category = true;
		} else if (Integer.parseInt(type) == Constants.Type.Investment.v()) {
			category = true;
		} else if (Integer.parseInt(type) == Constants.Type.Macro.v()) {
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
			Knowledge knowledge = knowledgeReaderDAO.findHtmlContentFromMongo(
					kid, type);

			if (knowledge == null) {
				result.put(Constants.status, Constants.ResultType.fail.v());
				result.put(Constants.errormessage,
						Constants.ErrorMessage.artNotExsit.c());
				return result;
			}
			result.put("title", knowledge.getTitle());
			result.put(
					"content",
					StringUtils.isBlank(knowledge.getHcontent()) ? knowledge
							.getContent() : knowledge.getHcontent());

			result.put("createtime", DateUtil
					.formatWithYYYYMMDDHHMMSS(knowledge.getCreatetime()));
			result.put("author", knowledge.getUname());
			result.put("source", knowledge.getSource());

			result.put("tags", knowledge.getTags());
			result.put("type", type);

			result.put(Constants.status, Constants.ResultType.success.v());

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			logger.error("未能根据类型:{}创建对象", type);
			result.put(Constants.status, Constants.ResultType.fail.v());
			result.put(Constants.errormessage,
					Constants.ErrorMessage.artNotExsit.c());
		}
		return result;
	}

	@Override
	public Map<String, Object> addCollection(long kid, long userid,
			String type, String source, long columnid, long categoryid) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (knowledgeReaderDAO.addCollection(kid, userid, type, source,
				columnid, categoryid) == 0) {
			result.put(Constants.status, Constants.ResultType.fail.v());
			result.put(Constants.errormessage,
					Constants.ErrorMessage.addCollFail.c());
		} else {
			// 存储收藏数
			knowledgeReaderDAO.addStaticsCount(kid, 0, 0,
					Constants.StaticsValue.collCount.v(), 0);
			result.put(Constants.status, Constants.ResultType.success.v());
		}
		return result;
	}

}
