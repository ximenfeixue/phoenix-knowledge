package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.dao.knowledge.KnowledgeDraftDAO;
import com.ginkgocap.ywxt.knowledge.dao.news.KnowledgeNewsDAO;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeDraft;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeDraftExample;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeDraftExample.Criteria;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeStatics;
import com.ginkgocap.ywxt.knowledge.entity.UserCategory;
import com.ginkgocap.ywxt.knowledge.entity.UserCategoryExample;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeDraftMapper;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeDraftValueMapper;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeStaticsMapper;
import com.ginkgocap.ywxt.knowledge.mapper.UserCategoryMapper;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNewsVO;
import com.ginkgocap.ywxt.knowledge.service.ColumnService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeCategoryService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeDraftService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeMongoIncService;
import com.ginkgocap.ywxt.knowledge.service.UserPermissionService;
import com.ginkgocap.ywxt.knowledge.util.Constants;
import com.ginkgocap.ywxt.knowledge.util.KnowledgeUtil;
import com.ginkgocap.ywxt.knowledge.util.Page;
import com.ginkgocap.ywxt.user.model.User;

@Service("knowledgeDraftService")
public class KnowledgeDraftServiceImpl implements KnowledgeDraftService {

	private final static String dule = "1";

	private static final Logger logger = LoggerFactory
			.getLogger(KnowledgeNewsServiceImpl.class);

	@Resource
	private KnowledgeDraftMapper knowledgeDraftMapper;

	@Resource
	private KnowledgeDraftValueMapper knowledgeDraftValueMapper;

	@Autowired
	private KnowledgeDraftDAO knowledgeDraftDAO;

	@Autowired
	private KnowledgeMongoIncService knowledgeMongoIncService;

	@Autowired
	private ColumnService columnService;

	@Autowired
	private KnowledgeNewsDAO knowledgeNewsDAO;

	@Autowired
	private UserPermissionService userPermissionService;

	@Autowired
	private UserCategoryMapper userCategoryMapper;

	@Autowired
	private KnowledgeCategoryService knowledgeCategoryService;

	@Autowired
	private KnowledgeStaticsMapper knowledgeStaticsMapper;

	@Override
	public Map<String, Object> insertKnowledgeDraft(KnowledgeNewsVO vo,
			User user) {
		Map<String, Object> result = new HashMap<String, Object>();
		// 获取Session用户值
		long userId = user.getId();
		String username = user.getUserName();

		long kId = knowledgeMongoIncService.getKnowledgeIncreaseId();

		// TODO 判断用户是否选择栏目
		String columnPath = columnService.getColumnPathById(vo.getColumnid());
		// 知识入Mongo
		vo.setkId(kId);
		vo.setColumnPath(columnPath);

		knowledgeNewsDAO.insertknowledgeDraft(vo, user);

		knowledgeDraftDAO.insertKnowledge(kId, vo.getTitle(), vo.getColumnType(),
				vo.getColumnType(), userId);

		// 添加知识到权限表.若是独乐（1），不入权限,直接插入到mongodb中

		if (StringUtils.isNotBlank(vo.getSelectedIds())
				&& !vo.getSelectedIds().equals(dule)) {

			// 获取知识权限,大乐（2）：用户ID1，用户ID2...&中乐（3）：用户ID1，用户ID2...&小乐（4）：用户ID1，用户ID2...
			List<String> permList = KnowledgeUtil.getPermissionList(vo
					.getSelectedIds());

			int pV = userPermissionService.insertUserPermission(permList, kId,
					userId, vo.getShareMessage(),
					Short.parseShort(vo.getColumnType()), vo.getColumnid());
			if (pV == 0) {
				logger.error("创建知识未全部完成,添加知识到用户权限信息失败，知识ID:{},目录ID:{}", kId);
			}
		}
		long[] cIds = null;
		// 添加知识到知识目录表
		if (StringUtils.isBlank(vo.getCatalogueIds())) { // 如果目录ID为空,默认添加到未分组目录中.
			UserCategoryExample example = new UserCategoryExample();
			com.ginkgocap.ywxt.knowledge.entity.UserCategoryExample.Criteria criteria = example
					.createCriteria();
			criteria.andSortidEqualTo(Constants.unGroupSortId);
			criteria.andUserIdEqualTo(userId);
			List<UserCategory> list = userCategoryMapper
					.selectByExample(example);
			if (list != null && list.size() == 1) {
				cIds = new long[1];
				cIds[0] = list.get(0).getId();

			} else {
				// 如果没有未分组目录,创建未分组目录.
				// TODO
			}
		} else {
			cIds = KnowledgeUtil.formatString(vo.getCatalogueIds());
		}
		if (StringUtils.isNotBlank(vo.getCatalogueIds())) {

			int categoryV = knowledgeCategoryService.insertKnowledgeRCategory(
					kId, cIds, userId, username, columnPath, vo);
			if (categoryV == 0) {
				logger.error("创建知识未全部完成,添加知识到知识目录信息失败，知识ID:{},目录ID:{}", kId,
						cIds);
				result.put(Constants.status, Constants.ResultType.fail.v());
				result.put(Constants.errormessage,
						Constants.ErrorMessage.addKnowledgeFail.c());
				return result;
			}
		}
		// 初始化知识统计信息
		KnowledgeStatics statics = new KnowledgeStatics();
		statics.setClickcount(0l);
		statics.setCollectioncount(0l);
		statics.setCommentcount(0l);
		statics.setKnowledgeId(kId);
		statics.setSharecount(0l);
		statics.setTitle(vo.getTitle());
		statics.setType(Short.parseShort(vo.getColumnType()));
		int sV = knowledgeStaticsMapper.insertSelective(statics);
		if (sV == 0) {
			logger.error("创建知识未全部完成,添加知识到知识统计信息失败，知识ID:{},栏目类型:{}", kId,
					vo.getColumnType());
			result.put(Constants.status, Constants.ResultType.fail.v());
			result.put(Constants.errormessage,
					Constants.ErrorMessage.addKnowledgeFail.c());
			return result;
		}

		result.put(Constants.status, Constants.ResultType.success.v());
		logger.info("添加草稿箱成功,知识ID:{}", kId);
		return result;
	}

	@Override
	public Page<KnowledgeDraft> selectKnowledgeDraft(Page<KnowledgeDraft> page,
			long userid, String type) {
		List<KnowledgeDraft> list = knowledgeDraftDAO.selectKnowledgeDraft(
				userid, type, (page.getPageNo() - 1) * page.getPageSize(),
				page.getPageSize());

		int count = knowledgeDraftDAO.countKnowledgeDraft(userid, type);

		page.setTotalItems(count);
		page.setResult(list);

		return page;
	}

	@Override
	public int deleteKnowledgeDraft(long[] knowledgeids, long userid) {

		return knowledgeDraftValueMapper.deleteKnowledge(knowledgeids, userid);
	}

	@Override
	public KnowledgeDraft selectByKnowledgeId(long knowledgeid) {

		return knowledgeDraftMapper.selectByPrimaryKey(knowledgeid);
	}

	@Override
	public List<KnowledgeDraft> selectKnowledgeDraft(long userid, String type,
			int pageno, int pagesize) {

		KnowledgeDraftExample example = new KnowledgeDraftExample();
		Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(type)) {
			criteria.andDrafttypeEqualTo(type);
		}
		criteria.andUseridEqualTo(userid);
		example.setOrderByClause("createtime desc");
		example.setLimitStart(pageno);
		example.setLimitEnd(pagesize);
		return knowledgeDraftMapper.selectByExample(example);
	}

	@Override
	public int countKnowledgeDraft(long userid, String type) {

		KnowledgeDraftExample example = new KnowledgeDraftExample();
		Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(type)) {
			criteria.andDrafttypeEqualTo(type);
		}
		criteria.andUseridEqualTo(userid);
		return knowledgeDraftMapper.countByExample(example);
	}

	@Override
	public int deleteKnowledgeDraft(long knowledgeid) {

		return 0;
	}

}
