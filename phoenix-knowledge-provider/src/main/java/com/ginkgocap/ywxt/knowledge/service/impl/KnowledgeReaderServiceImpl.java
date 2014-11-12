package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.dao.reader.KnowledgeReaderDAO;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCollectionExample;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCollectionExample.Criteria;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeStatics;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeCollectionMapper;
import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.service.AttachmentService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeReaderService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeStaticsService;
import com.ginkgocap.ywxt.knowledge.util.Constants;
import com.ginkgocap.ywxt.knowledge.util.DateUtil;
import com.ginkgocap.ywxt.knowledge.util.JsonUtil;
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

	@Resource
	private MongoTemplate mongoTemplate;

	@Resource
	private AttachmentService attachmentService;

	@Resource
	private KnowledgeCollectionMapper knowledgeCollectionMapper;

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
			long kUid) {
		logger.info("进入获取用户关系请求,登陆用户ID:{},知识所属用户ID:{}", loginuserid, kUid);
		Map<String, Integer> result = new HashMap<String, Integer>();
		if (loginuserid == -1) {
			result.put("relation", Constants.Relation.platform.v());
		} else if (kUid == 0) {
			result.put("relation", Constants.Relation.jinTN.v());
		} else if (loginuserid == kUid) {
			result.put("relation", Constants.Relation.self.v());
		} else {
			int r = friendsRelationService.getFriendsStatus(loginuserid, kUid);
			// (-1=是自己 or 0=不是好友 or 1=好友等待中 or 2=已是好友)
			if (r == 0 || r == 1) {
				result.put("relation", Constants.Relation.notFriends.v());
			} else if (r == 2) {
				result.put("relation", Constants.Relation.friends.v());
			}

		}
		logger.info("获取用户关系请求成功,登陆用户ID:{},知识所属用户ID:{}", loginuserid, kUid);
		return result;
	}

	@Override
	public Map<String, Boolean> showHeadMenu(long kid, String type) {
		logger.info("进入获取阅读器功能列表请求,知识id:{},type:{}", kid, type);
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
		logger.info("获取阅读器功能列表请求成功!,知识id:{},type:{}", kid, type);
		return result;
	}

	@Override
	public Map<String, Object> getKnowledgeContent(Knowledge knowledge,
			String type) {
		logger.info("进入获取知识内容请求,知识type:{}", type);
		Map<String, Object> result = new HashMap<String, Object>();

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

		result.put("createtime",
				DateUtil.formatWithYYYYMMDDHHMMSS(knowledge.getCreatetime()));
		result.put("author", knowledge.getUname());
		result.put("source", knowledge.getSource());

		List<String> tagsList = JsonUtil.parseTags(knowledge.getTags());
		result.put("tagsList", tagsList);
		result.put("type", type);

		result.put(Constants.status, Constants.ResultType.success.v());
		logger.info("获取知识内容请求成功,知识type:{}", type);
		return result;
	}

	@Override
	public Knowledge getKnowledgeById(long kid, String type) {
		logger.info("进入获取知识实体请求,知识ID:{},类型为:{}", kid, type);
		Knowledge knowledge = null;
		try {
			String obj = Constants.getTableName(type);
			knowledge = (Knowledge) mongoTemplate.findById(kid,
					Class.forName(obj),
					obj.substring(obj.lastIndexOf(".") + 1, obj.length()));

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			logger.error("获取知识实体请求失败,知识ID:{},类型为:{}", kid, type);
		}
		logger.info("获取知识实体请求成功,知识ID:{},类型为:{}", kid, type);
		return knowledge;
	}

	@Override
	public Map<String, Object> getReaderHeadMsg(long kid, long kUId,
			long userId, String type) {
		logger.info("进入查询阅读器头部内容请求,知识ID:{},当前登陆用户:{}", kid,
				userId == -1 ? "未登陆" : userId);
		Map<String, Object> result = new HashMap<String, Object>();

		// 获取统计信息
		KnowledgeStatics statics = getKnowledgeStatusCount(kid);
		if (statics != null) {
			result.put("commentCount", statics.getCommentcount());
			result.put("clickCount", statics.getClickcount());
			result.put("collCount", statics.getCollectioncount());
			result.put("shareCount", statics.getSharecount());
		} else {
			boolean succ = addKnowledgeStatics(kid, kUId, type);
			if (succ) {
				result.put("commentCount", 0);
				result.put("clickCount", 0);
				result.put("collCount", 0);
				result.put("shareCount", 0);
			}
		}
		// 存储登陆用户与知识作者关系
		result.putAll(authorAndLoginUserRelation(userId, kUId));
		// 存储阅读器功能菜单
		result.putAll(showHeadMenu(kid, type));
		// 查询用户基本信息并存储用户名、头像、用户ID
		User kUser = userService.selectByPrimaryKey(kUId);
		result.put("user", kUser);
		result.put("isColl", getIsCollStatus(kid, userId));

		return result;
	}

	/**
	 * 添加统计信息
	 * 
	 * @param kId
	 *            用户ID
	 * @param kUId
	 *            知识作者ID
	 * @param type
	 *            知识类型
	 * @return
	 */
	private Boolean addKnowledgeStatics(long kId, long kUId, String type) {
		Knowledge knowledge = getKnowledgeById(kId, type);
		if (knowledge == null) {
			logger.error("没有找到知识,知识ID:{},知识类型:{}", kId, type);
			return false;
		}
		KnowledgeStatics knowledgeStatics = new KnowledgeStatics();
		knowledgeStatics.setClickcount(0l);
		knowledgeStatics.setCollectioncount(0l);
		knowledgeStatics.setCommentcount(0l);
		knowledgeStatics.setKnowledgeId(kId);
		knowledgeStatics.setSharecount(0l);
		if (kUId == 0 || kUId == -1) {
			knowledgeStatics.setSource((short) Constants.StaticsType.sys.v());
		} else {
			knowledgeStatics.setSource((short) Constants.StaticsType.user.v());
		}
		knowledgeStatics.setTitle(knowledge.getTitle());
		knowledgeStatics.setType(Short.parseShort(type));

		int v = knowledgeStaticsService
				.insertKnowledgeStatics(knowledgeStatics);

		return v > 0;
	}

	private Boolean getIsCollStatus(long kid, long userId) {
		KnowledgeCollectionExample example = new KnowledgeCollectionExample();
		Criteria criteria = example.createCriteria();
		criteria.andKnowledgeIdEqualTo(kid);
		criteria.andUseridEqualTo(userId);

		int v = knowledgeCollectionMapper.countByExample(example);

		return v > 0;
	}

	public Map<String, Object> getKnowledgeDetail(long kid, User user,
			String type) {
		logger.info("--进入查询知识详细信息请求,知识ID:{},当前登陆用户:{}--", kid,
				user != null ? user.getId() : "未登陆");
		//TODO查询权限
		Map<String, Object> result = new HashMap<String, Object>();
		// 查询知识信息
		Knowledge knowledge = getKnowledgeById(kid, type);
		if (knowledge == null) {
			logger.error("没有找到知识,知识ID:{},知识类型:{}", kid, type);
			result.put(Constants.status, Constants.ResultType.fail.v());
			result.put(Constants.errormessage,
					Constants.ErrorMessage.artNotExsit.c());
			return result;
		}
		// 存储阅读器头部信息
		result.putAll(getReaderHeadMsg(kid, knowledge.getUid(), user.getId(),
				type));
		// 存储正文内容
		result.putAll(getKnowledgeContent(knowledge, type));
		// 查询附件
		result.putAll(attachmentService.queryAttachmentByTaskId(knowledge
				.getTaskid()));
		result.put("kid", kid);
		logger.info("--查询知识详细信息请求成功,知识ID:{},当前登陆用户:{}--", kid,
				user != null ? user.getId() : "未登陆");
		return result;
	}
}
