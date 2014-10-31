package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.dao.knowledge.KnowledgeDao;
import com.ginkgocap.ywxt.knowledge.dao.knowledgecategory.KnowledgeCategoryDAO;
import com.ginkgocap.ywxt.knowledge.dao.news.KnowledgeNewsDAO;
import com.ginkgocap.ywxt.knowledge.entity.Column;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeDraft;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeRecycle;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeStatics;
import com.ginkgocap.ywxt.knowledge.entity.UserCategory;
import com.ginkgocap.ywxt.knowledge.entity.UserCategoryExample;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeBaseMapper;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeStaticsMapper;
import com.ginkgocap.ywxt.knowledge.mapper.UserCategoryMapper;
import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNews;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNewsVO;
import com.ginkgocap.ywxt.knowledge.service.ColumnKnowledgeService;
import com.ginkgocap.ywxt.knowledge.service.ColumnService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeCategoryService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeDraftService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeMongoIncService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeNewsService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeRecycleService;
import com.ginkgocap.ywxt.knowledge.service.UserCategoryService;
import com.ginkgocap.ywxt.knowledge.service.UserPermissionService;
import com.ginkgocap.ywxt.knowledge.util.Constants;
import com.ginkgocap.ywxt.knowledge.util.JsonUtil;
import com.ginkgocap.ywxt.knowledge.util.KnowledgeUtil;
import com.ginkgocap.ywxt.user.model.User;
import com.ginkgocap.ywxt.util.PageUtil;

@Service("knowledgeNewsService")
public class KnowledgeNewsServiceImpl implements KnowledgeNewsService {

	private final static String dule = "1";

	private static final Logger logger = LoggerFactory
			.getLogger(KnowledgeNewsServiceImpl.class);

	@Autowired
	private KnowledgeDao knowledgeDao;

	@Autowired
	private KnowledgeNewsDAO knowledgeNewsDAO;

	@Autowired
	private KnowledgeCategoryDAO knowledgeBetweenDAO;

	@Resource
	private MongoTemplate mongoTemplate;

	@Resource
	private UserPermissionService userPermissionService;

	@Resource
	private KnowledgeCategoryService knowledgeCategoryService;

	@Resource
	private ColumnService columnService;

	@Resource
	private ColumnKnowledgeService columnKnowledgeService;

	@Resource
	private KnowledgeMongoIncService knowledgeMongoIncService;

	@Resource
	private KnowledgeStaticsMapper knowledgeStaticsMapper;

	@Resource
	private UserCategoryMapper userCategoryMapper;

	@Resource
	private KnowledgeRecycleService knowledgeRecycleService;

	@Resource
	private UserCategoryService userCategoryService;

	@Resource
	private KnowledgeDraftService knowledgeDraftService;

	@Resource
	private KnowledgeBaseMapper knowledgeBaseMapper;

	@Override
	public Map<String, Object> deleteKnowledge(String knowledgeids,
			long catetoryid, String types, String titles, User user) {

		Map<String, Object> result = new HashMap<String, Object>();
		long[] knowledgeid = KnowledgeUtil.convertionToLong(knowledgeids
				.substring(0, knowledgeids.length() - 1).split(","));
		String[] type = types.split(",");
		String[] title = titles.split(",");
		for (int i = 0; i < knowledgeid.length; i++) {

			String obj = Constants.getTableName(type[i]);

			String collectionName = obj.substring(obj.lastIndexOf(".") + 1,
					obj.length());

			Criteria criteria = Criteria.where("_id").in(knowledgeid[i]);
			Query query = new Query(criteria);
			Update update = new Update();
			update.set("status", Constants.Status.recycle.v());
			mongoTemplate.updateFirst(query, update, collectionName);

			// 知识存入回收站
			int RecycleCount = knowledgeRecycleService
					.insertKnowledgeRecycle(knowledgeid[i], title[i], type[i],
							user.getId(), catetoryid);
			if (RecycleCount == 0) {
				logger.error("删除知识到回收站失败，知识ID:{}", knowledgeid);
				result.put(Constants.status, Constants.ResultType.fail.v());
				return result;
			}
			// 删除知识目录中的信息
			int categoryCount = knowledgeCategoryService
					.updateKnowledgeCategory(knowledgeid[i], catetoryid);
			if (categoryCount == 0) {
				logger.error("修改知识目录失败，知识ID:{}", knowledgeid);
				result.put(Constants.status, Constants.ResultType.fail.v());
			}
		}
		result.put(Constants.status, Constants.ResultType.success.v());
		logger.info("删除知识成功,知识ID:{}", knowledgeid);

		return result;
	}

	@Override
	public Map<String, Object> updateKnowledge(KnowledgeNewsVO vo, User user) {

		Map<String, Object> result = new HashMap<String, Object>();

		knowledgeNewsDAO.updateKnowledge(vo, user);

		// TODO 判断用户是否选择栏目
		String columnPath = null;
		Column column = null;
		if (Long.parseLong(vo.getColumnid()) != 0) {
			columnPath = columnService.getColumnPathById(Long.parseLong(vo
					.getColumnid()));
		} else {
			column = columnService.getUnGroupColumnIdBySortId(user.getId());
			columnPath = Constants.unGroupSortName;
		}

		// 修改栏目知识关系
		int columnknowledgeCount = columnKnowledgeService.updateColumn(
				vo.getkId(), Long.parseLong(vo.getColumnid()));

		if (columnknowledgeCount == 0) {
			logger.error("修改知识栏目失败，知识ID:{}", vo.getkId());
			result.put(Constants.status, Constants.ResultType.fail.v());
			return result;
		}

		// 删除用户权限数据
		int userPermissionCount = userPermissionService.deleteUserPermission(vo
				.getkId());
		// 添加知识到权限表.若是独乐（1），不入权限,直接插入到mongodb中

		if (StringUtils.isNotBlank(vo.getSelectedIds())
				&& !vo.getSelectedIds().equals(dule)) {
			// 获取知识权限,大乐（2）：用户ID1，用户ID2...&中乐（3）：用户ID1，用户ID2...&小乐（4）：用户ID1，用户ID2...
			Boolean dule = JsonUtil.checkKnowledgePermission(vo
					.getSelectedIds());
			if (dule == null) {
				logger.error("解析权限信息失败，参数为：{}", vo.getSelectedIds());
				result.put(Constants.status, Constants.ResultType.fail.v());
				result.put(Constants.errormessage,
						Constants.ErrorMessage.paramNotValid.c());
				return result;
			}
			if (!dule) {
				// 格式化权限信息
				List<String> permList = JsonUtil.getPermissionList(vo
						.getSelectedIds());
				int pV = userPermissionService.insertUserPermission(
						permList,
						vo.getkId(),
						user.getId(),
						vo.getShareMessage(),
						Short.parseShort(vo.getColumnType()),
						Long.parseLong(vo.getColumnid()) != 0 ? Long
								.parseLong(vo.getColumnid()) : column.getId());
				if (pV == 0) {
					logger.error("创建知识未全部完成,添加知识到用户权限信息失败，知识ID:{},目录ID:{}",
							vo.getkId());
				}
			}
		}

		// 删除该知识下的所有目录
		int categoryCount = knowledgeCategoryService.deleteKnowledgeCategory(vo
				.getkId());
		// 删除该知识的基本信息
		knowledgeBaseMapper.deleteByPrimaryKey(vo.getkId());

		if (categoryCount > 0) {
			logger.error("删除该知识下的所有目录，知识ID:{}", vo.getkId());
			long[] cIds = null;
			// 添加知识到知识目录表
			if (StringUtils.isBlank(vo.getCatalogueIds())) { // 如果目录ID为空,默认添加到未分组目录中.
				UserCategoryExample example = new UserCategoryExample();
				com.ginkgocap.ywxt.knowledge.entity.UserCategoryExample.Criteria criteria = example
						.createCriteria();
				criteria.andSortidEqualTo(Constants.unGroupSortId);
				criteria.andUserIdEqualTo(user.getId());
				criteria.andCategoryTypeEqualTo((short) Constants.CategoryType.common
						.v());
				List<UserCategory> list = userCategoryMapper
						.selectByExample(example);
				if (list != null && list.size() == 1) {
					cIds = new long[1];
					cIds[0] = list.get(0).getId();

				}
			} else {
				cIds = KnowledgeUtil.formatString(vo.getCatalogueIds()
						.substring(1, vo.getCatalogueIds().length()));
			}
			int categoryV = knowledgeCategoryService.insertKnowledgeRCategory(
					vo.getkId(), cIds, user.getId(), user.getName(),
					columnPath, vo);
			if (categoryV == 0) {
				logger.error("创建知识未全部完成,添加知识到知识目录信息失败，知识ID:{},目录ID:{}",
						vo.getkId(), cIds);
				result.put(Constants.status, Constants.ResultType.fail.v());
				result.put(Constants.errormessage,
						Constants.ErrorMessage.addKnowledgeFail.c());
				return result;
			}
		}

		result.put(Constants.status, Constants.ResultType.success.v());
		logger.info("编辑知识成功,知识ID:{}", vo.getkId());
		return result;

	}

	@Override
	public Knowledge selectKnowledge(long knowledgeid, String type) {

		String obj = Constants.getTableName(type);

		Knowledge knowledge = null;
		Criteria criteria = Criteria.where("_id").is(knowledgeid);
		Query query = new Query(criteria);
		try {
			knowledge = (Knowledge) Class.forName(obj).newInstance();

		} catch (Exception e) {

			e.printStackTrace();
		}
		return mongoTemplate.findOne(query, Knowledge.class,
				obj.substring(obj.lastIndexOf(".") + 1, obj.length()));

	}

	@Override
	public List<KnowledgeNews> selectByParam(Long columnid, long source,
			Long userid, List<Long> ids, int page, int size) {
		Criteria criteria1 = new Criteria().is(userid);
		Criteria criteria2 = new Criteria();
		if (ids != null) {
			criteria2.and("_id").in(ids);
		}
		Criteria criteriaall = new Criteria();
		criteriaall.orOperator(criteria1, criteria2);
		Query query = new Query(criteriaall);
		query.sort().on("createtime", Order.DESCENDING);
		long count = mongoTemplate.count(query, KnowledgeNews.class);
		PageUtil p = new PageUtil((int) count, page, size);
		query.limit(p.getPageStartRow() - 1);
		query.skip(size);
		return mongoTemplate.find(query, KnowledgeNews.class, "KnowledgeNews");
	}

	@Override
	public void deleteKnowledgeByid(long knowledgeid) {

		KnowledgeDraft knowledgeDraft = knowledgeDraftService
				.selectByKnowledgeId(knowledgeid);

		String obj = Constants.getTableName(knowledgeDraft.getType());

		String collectionName = obj.substring(obj.lastIndexOf(".") + 1,
				obj.length());
		Criteria criteria = Criteria.where("_id").is(knowledgeid);
		Query query = new Query(criteria);
		mongoTemplate.remove(query, collectionName);
	}

	@Override
	public Map<String, Object> insertknowledge(KnowledgeNewsVO vo, User user) {
		logger.info("开始新建知识,知识类型为：{},创建用户:{}", vo.getColumnType(), user.getId());
		Map<String, Object> result = new HashMap<String, Object>();
		// 获取Session用户值
		long userId = user.getId();
		String username = user.getUserName();

		long kId = knowledgeMongoIncService.getKnowledgeIncreaseId();
		// TODO 判断用户是否选择栏目
		String columnPath = null;
		Column column = null;
		if (Long.parseLong(vo.getColumnid()) != 0) {
			columnPath = columnService.getColumnPathById(Long
					.parseLong(StringUtils.isBlank(vo.getColumnid()) ? "0" : vo
							.getColumnid()));
		} else {
			column = columnService.getUnGroupColumnIdBySortId(user.getId());
			columnPath = Constants.unGroupSortName;
		}
		// 知识入Mongo
		vo.setkId(kId);
		vo.setColumnPath(columnPath);

		knowledgeNewsDAO.insertknowledge(vo, user);

		// 添加知识到权限表.若是独乐（1），不入权限,直接插入到mongodb中
		String selectedIds = vo.getSelectedIds().replace("&quot;", "\"");
		if (StringUtils.isNotBlank(selectedIds) && !selectedIds.equals(dule)) {
			// 获取知识权限,大乐（2）：用户ID1，用户ID2...&中乐（3）：用户ID1，用户ID2...&小乐（4）：用户ID1，用户ID2...
			Boolean dule = JsonUtil.checkKnowledgePermission(selectedIds);
			if (dule == null) {
				logger.error("解析权限信息失败，参数为：{}", selectedIds);
				result.put(Constants.status, Constants.ResultType.fail.v());
				result.put(Constants.errormessage,
						Constants.ErrorMessage.paramNotValid.c());
				return result;
			}
			if (!dule) {
				// 格式化权限信息
				List<String> permList = JsonUtil.getPermissionList(selectedIds);
				int pV = userPermissionService
						.insertUserPermission(
								permList,
								kId,
								userId,
								vo.getShareMessage(),
								Short.parseShort(vo.getColumnType()),
								Long.parseLong(vo.getColumnid()) != 0 ? Long
										.parseLong(StringUtils.isBlank(vo
												.getColumnid()) ? "0" : vo
												.getColumnid()) : column
										.getId());
				if (pV == 0) {
					logger.error("创建知识未全部完成,添加知识到用户权限信息失败，知识ID:{},目录ID:{}", kId);
				}
			}
		}
		long[] cIds = null;
		// 添加知识到知识目录表
		if (StringUtils.isBlank(vo.getCatalogueIds().substring(1, 1))) { // 如果目录ID为空,默认添加到未分组目录中.
			UserCategoryExample example = new UserCategoryExample();
			com.ginkgocap.ywxt.knowledge.entity.UserCategoryExample.Criteria criteria = example
					.createCriteria();
			criteria.andSortidEqualTo(Constants.unGroupSortId);
			criteria.andUserIdEqualTo(userId);
			criteria.andCategoryTypeEqualTo((short) Constants.CategoryType.common
					.v());
			List<UserCategory> list = userCategoryMapper
					.selectByExample(example);
			if (list != null && list.size() == 1) {
				cIds = new long[1];
				cIds[0] = list.get(0).getId();

			}
		} else {
			cIds = KnowledgeUtil.formatString(vo.getCatalogueIds().substring(1,
					vo.getCatalogueIds().length()));
		}
		int categoryV = knowledgeCategoryService.insertKnowledgeRCategory(kId,
				cIds, userId, username, columnPath, vo);
		if (categoryV == 0) {
			logger.error("创建知识未全部完成,添加知识到知识目录信息失败，知识ID:{},目录ID:{}", kId, cIds);
			result.put(Constants.status, Constants.ResultType.fail.v());
			result.put(Constants.errormessage,
					Constants.ErrorMessage.addKnowledgeFail.c());
			return result;
		}
		// 添加知识到栏目知识表
		// int columnV = columnKnowledgeService.insertColumnKnowledge(
		// vo.getColumnid(), knowledge.getId(), userId,
		// Constants.Type.News.v());
		//
		// if (columnV == 0) {
		// logger.error("创建知识未全部完成,添加知识到栏目知识信息失败，知识ID:{},栏目ID:{}", kId,
		// vo.getColumnid());
		// result.put(Constants.status, Constants.ResultType.fail.v());
		// result.put(Constants.errormessage,
		// Constants.ErrorMessage.addKnowledgeFail.c());
		// return result;
		// }
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
		result.put("knowledgeid", kId);
		result.put("type", vo.getColumnType());
		result.put(Constants.status, Constants.ResultType.success.v());
		logger.info("创建知识成功,知识ID:{}", kId);
		return result;
	}

	@Override
	public void restoreKnowledgeByid(long knowledgeid) {

		KnowledgeRecycle knowledgerecycle = knowledgeRecycleService
				.selectByKnowledgeId(knowledgeid);

		String obj = Constants.getTableName(knowledgerecycle.getType());

		try {
			String collectionName = obj.substring(obj.lastIndexOf(".") + 1,
					obj.length());

			Criteria criteria = Criteria.where("_id").is(knowledgeid);

			Query query = new Query(criteria);

			Update update = new Update();
			update.set("status", Constants.Status.checked.v());
			mongoTemplate.updateFirst(query, update, collectionName);

			UserCategory usercategory = userCategoryService
					.selectByPrimaryKey(knowledgerecycle.getCatetoryid());

			if (usercategory != null) {
				knowledgeCategoryService.updateKnowledgeCategorystatus(
						knowledgeid, knowledgerecycle.getCatetoryid());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteforeverKnowledge(long knowledgeid) {

		KnowledgeRecycle knowledgerecycle = knowledgeRecycleService
				.selectByKnowledgeId(knowledgeid);

		String obj = Constants.getTableName(knowledgerecycle.getType());

		String collectionName = obj.substring(obj.lastIndexOf(".") + 1,
				obj.length());
		Criteria criteria = Criteria.where("_id").is(knowledgeid);
		Query query = new Query(criteria);
		Update update = new Update();
		update.set("status", Constants.Status.foreverdelete.v());
		mongoTemplate.updateFirst(query, update, collectionName);

	}
}
