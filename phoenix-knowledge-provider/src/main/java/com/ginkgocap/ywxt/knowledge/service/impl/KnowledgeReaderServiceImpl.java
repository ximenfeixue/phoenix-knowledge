package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ginkgocap.ywxt.knowledge.dao.reader.KnowledgeReaderDAO;
import com.ginkgocap.ywxt.knowledge.entity.Column;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCollectionExample;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCollectionExample.Criteria;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeStatics;
import com.ginkgocap.ywxt.knowledge.entity.UserPermissionExample;
import com.ginkgocap.ywxt.knowledge.mapper.ColumnMapper;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeCollectionMapper;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeStaticsMapperManual;
import com.ginkgocap.ywxt.knowledge.mapper.UserPermissionMapper;
import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeLaw;
import com.ginkgocap.ywxt.knowledge.service.AttachmentService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeReaderService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeStaticsService;
import com.ginkgocap.ywxt.knowledge.util.Constants;
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

	@Resource
	private UserPermissionMapper userPermissionMapper;

	@Resource
	private ColumnMapper columnMapper;

	@Resource
	private KnowledgeStaticsMapperManual knowledgeStaticsMapperManual;

	@Override
	public User getUserInfo(long userid) {
		return userService.selectByPrimaryKey(userid);
	}

	@Override
	public KnowledgeStatics getKnowledgeStatusCount(long kid) {
		return knowledgeStaticsService.selectByknowledgeId(kid);
	}

	@Override
	public Map<String, Integer> authorAndLoginUserRelation(User user, long kUid) {
		logger.info("进入获取用户关系请求,登陆用户ID:{},知识所属用户ID:{}", user == null ? "未登陆用户"
				: user.getId(), kUid);
		Map<String, Integer> result = new HashMap<String, Integer>();
		// 未登陆用户显示加为好友状态
		if (user == null) {
			if (kUid == 0) {
				result.put("relation", Constants.Relation.jinTN.v());
			} else {
				result.put("relation", Constants.Relation.notFriends.v());
			}

		} else {

			if (user.getId() == -1) {
				result.put("relation", Constants.Relation.platform.v());
			} else if (kUid == 0) {
				result.put("relation", Constants.Relation.jinTN.v());
			} else if (user.getId() == kUid) {
				result.put("relation", Constants.Relation.self.v());
			} else {
				int r = friendsRelationService.getFriendsStatus(user.getId(),
						kUid);
				// (-1=是自己 or 0=不是好友 or 1=好友等待中 or 2=已是好友)
				if (r == 0) {
					result.put("relation", Constants.Relation.notFriends.v());
				} else if (r == 1) {
					result.put("relation", Constants.Relation.waitAgree.v());
				} else if (r == 2) {
					result.put("relation", Constants.Relation.friends.v());
				}

			}

		}
		logger.info("获取用户关系请求成功,登陆用户ID:{},知识所属用户ID:{}", user == null ? "未登陆用户"
				: user.getId(), kUid);
		return result;
	}

	@Override
	public Map<String, Boolean> showHeadMenu(long kid, String type) {
		logger.info("进入获取阅读器功能列表请求,知识id:{},type:{}", kid, type);
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		boolean download = false;
		boolean category = false;
		boolean mark = false;

		if (Integer.parseInt(type) == Constants.Type.Industry.v()) {
			category = true;
		} else if (Integer.parseInt(type) == Constants.Type.BookReport.v()) {
			mark = true;
			download = true;
			category = true;
		} else if (Integer.parseInt(type) == Constants.Type.Example.v()) {
			download = true;
			category = true;
		} else if (Integer.parseInt(type) == Constants.Type.Investment.v()) {
			category = true;
		}
		result.put("download", download);
		result.put("category", category);
		result.put("mark", mark);
		logger.info("获取阅读器功能列表请求成功!,知识id:{},type:{}", kid, type);
		return result;
	}

	@Override
	public Map<String, Boolean> showHeadMenu(long kid, String type, User user,
			long authorId) {
		Map<String, Boolean> result = new HashMap<String, Boolean>();

		result.putAll(showHeadMenu(kid, type));

		result.put("edit", user == null ? false : user.getId() == authorId);

		result.put("share", user == null ? false : user.getId() == authorId);

		result.put("sourceFile", authorId == Constants.Ids.jinTN.v());

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
		if (Integer.parseInt(type) == Constants.Type.Law.v()) {
			KnowledgeLaw law = (KnowledgeLaw) knowledge;

			result.put("postUnit", law.getPostUnit());
			result.put("titanic", law.getTitanic());
			result.put("submitTime", law.getSubmitTime());
			result.put("performTime", law.getPerformTime());

		}

		result.put("title", knowledge.getTitle());
		result.put(
				"content",
				StringUtils.isBlank(knowledge.getHcontent()) ? knowledge
						.getContent() : knowledge.getHcontent());

		result.put(
				"createtime",
				StringUtils.isBlank(knowledge.getSysTime()) ? knowledge
						.getCreatetime() : knowledge.getSysTime());
		result.put("author", knowledge.getUname());
		result.put("source",
				knowledge.getSource() == null ? "" : knowledge.getSource());
		result.put("asso",
				knowledge.getAsso() == null ? "" : knowledge.getAsso());

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
	public Map<String, Object> getReaderHeadMsg(long kid, long authorId,
			User user, String type) {
		logger.info("进入查询阅读器头部内容请求,知识ID:{},当前登陆用户:{}", kid,
				user == null ? "未登陆" : user.getId());
		Map<String, Object> result = new HashMap<String, Object>();

		// 获取统计信息
		KnowledgeStatics statics = getKnowledgeStatusCount(kid);
		if (statics != null) {
			result.put("commentCount", statics.getCommentcount());
			result.put("clickCount", statics.getClickcount());
			result.put("collCount", statics.getCollectioncount());
			result.put("shareCount", statics.getSharecount());
		} else {
			boolean succ = addKnowledgeStatics(kid, authorId, type);
			if (succ) {
				result.put("commentCount", 0);
				result.put("clickCount", 0);
				result.put("collCount", 0);
				result.put("shareCount", 0);
			}
		}
		// 存储登陆用户与知识作者关系
		result.putAll(authorAndLoginUserRelation(user, authorId));
		// 存储阅读器功能菜单
		result.putAll(showHeadMenu(kid, type, user, authorId));
		// 查询用户基本信息并存储用户名、头像、用户ID
		User kUser = userService.selectByPrimaryKey(authorId);
		result.put("user", kUser);
		result.put("isColl", getIsCollStatus(kid, user));

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
	@Transactional
	@Override
	public Boolean addKnowledgeStatics(long kId, long kUId, String type) {
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
		knowledgeStatics.setType(Short.parseShort(type));

		int v = knowledgeStaticsService
				.insertKnowledgeStatics(knowledgeStatics);

		return v > 0;
	}

	private Boolean getIsCollStatus(long kid, User user) {
		if (user == null)
			return false;
		KnowledgeCollectionExample example = new KnowledgeCollectionExample();
		Criteria criteria = example.createCriteria();
		criteria.andKnowledgeIdEqualTo(kid);
		criteria.andUseridEqualTo(user.getId());

		int v = knowledgeCollectionMapper.countByExample(example);

		return v > 0;
	}

	public Map<String, Object> getKnowledgeDetail(long kid, User user,
			String type) {
		logger.info("--进入查询知识详细信息请求,知识ID:{},当前登陆用户:{}--", kid,
				user != null ? user.getId() : "未登陆");
		Map<String, Object> result = new HashMap<String, Object>();

		Knowledge knowledge = getKnowledgeById(kid, type);
		// 查询知识信息
		if (knowledge == null) {
			logger.error("没有找到知识,知识ID:{},知识类型:{}", kid, type);
			result.put(Constants.status, Constants.ResultType.fail.v());
			result.put(Constants.errormessage,
					Constants.ErrorMessage.artNotExsit.c());
			return result;
		}
		// 查询用户对文章的查看权限
		Map<String, Object> perMap = getArticlePermission(knowledge, user);
		if (Integer.parseInt(perMap.get(Constants.status) + "") != Constants.ResultType.success
				.v())
			return perMap;

		// 存储阅读器头部信息
		result.putAll(getReaderHeadMsg(kid, knowledge.getUid(), user, type));
		// 添加点击数(放到请求头部信息之后，若有不存在的知识会添加一条)
		knowledgeStaticsMapperManual.updateStatics(kid, 0, 0, 0,
				Constants.StaticsValue.clickCount.v());
		// 存储正文内容
		result.putAll(getKnowledgeContent(knowledge, type));
		// 查询附件
		result.putAll(attachmentService.queryAttachmentByTaskId(knowledge
				.getTaskid()));
		// 查询面包导航
		result.put("columnId", knowledge.getColumnid());
		Map<String, Object> cnMap = getKnowledgePath(knowledge);
		String columnType = cnMap.get("columnType") + "";
		result.put("columnName", StringUtils.isBlank(columnType) ? ""
				: Constants.getKnowledgeTypeName(columnType));
		result.put("kid", kid);
		result.put("type", type);
		result.put("sourceAddr",
				knowledge.getS_addr() == null ? "" : knowledge.getS_addr());
		// 权限设置
		result.put("selectIds", knowledge.getSelectedIds() == null ? ""
				: knowledge.getSelectedIds());
		// 生态圈使用
		result.put("ecosphereType", Constants.MATERIAL_KNOWLEDGE);
		logger.info("--查询知识详细信息请求成功,知识ID:{},当前登陆用户:{}--", kid,
				user != null ? user.getId() : "未登陆");
		return result;
	}

	// private boolean getArticlePermission(Knowledge knowledge) {
	// return getArticlePermission(knowledge, null);
	// }
	/** 获取知识所在全路径ID */
	private Map<String, Object> getKnowledgePath(Knowledge knowledge) {
		if (knowledge == null) {
			return null;
		}
		return getKnowledgePath(knowledge.getColumnid());
	}

	private Map<String, Object> getKnowledgePath(String columnId) {
		Map<String, Object> result = new HashMap<String, Object>();
		// Knowledge
		if (StringUtils.isBlank(columnId)) {
			return null;
		}
		Column column = columnMapper.selectByPrimaryKey(Long
				.parseLong(columnId));
		if (column == null) {
			return null;
		}
		result.put("columnType", column.getType());

		return result;
	}

	private Map<String, Object> getArticlePermission(Knowledge knowledge,
			User user) {
		Map<String, Object> result = new HashMap<String, Object>();
		// 判断如果知识被永久性删除
		if (knowledge.getStatus() == Constants.Status.foreverdelete.v()) {
			result.put(Constants.status, Constants.ResultType.fail.v());
			result.put(Constants.errormessage,
					Constants.ErrorMessage.artNotExsit.c());
			return result;
		}
		// 判断如果文章被删除或被屏蔽
		if (knowledge.getStatus() != Constants.Status.checked.v()) {

			if (user != null && user.getId() == knowledge.getUid()) { // 如果用户不空，且登陆用户==作者，则可以访问
				result.put(Constants.status, Constants.ResultType.success.v());
			} else {

				result.put(Constants.status, Constants.ResultType.fail.v());
				result.put(Constants.errormessage,
						Constants.ErrorMessage.artNotExsit.c());
			}
			return result;
		}
		// 如果作者为金桐脑
		if (knowledge.getUid() == Constants.Ids.jinTN.v()) {
			result.put(Constants.status, Constants.ResultType.success.v());
			return result;
		}
		// 判断用户对此文章权限
		UserPermissionExample example = new UserPermissionExample();
		com.ginkgocap.ywxt.knowledge.entity.UserPermissionExample.Criteria criteria = example
				.createCriteria();
		if (user == null) {
			criteria.andReceiveUserIdEqualTo((long) Constants.Ids.platform.v());
			criteria.andKnowledgeIdEqualTo(knowledge.getId());

		} else {
			if (user.getId() == knowledge.getUid()) {
				result.put(Constants.status, Constants.ResultType.success.v());
				return result;
			}
			List<Long> idList = new ArrayList<Long>();
			idList.add((long) Constants.Ids.platform.v());
			idList.add(user.getId());
			criteria.andKnowledgeIdEqualTo(knowledge.getId());
			criteria.andReceiveUserIdIn(idList);
			criteria.andTypeNotEqualTo(Constants.PermissionType.xiaoles.v());
		}
		int v = userPermissionMapper.countByExample(example);
		if (v > 0) {
			result.put(Constants.status, Constants.ResultType.success.v());
		} else {
			result.put(Constants.status, Constants.ResultType.fail.v());
			result.put(Constants.errormessage,
					Constants.ErrorMessage.artPermissionNotFound.c());
		}
		return result;
	}
}
