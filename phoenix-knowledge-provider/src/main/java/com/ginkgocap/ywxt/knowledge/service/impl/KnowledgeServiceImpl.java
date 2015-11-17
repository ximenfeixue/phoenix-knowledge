package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.ArrayList;
import java.util.Date;
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

import com.ginkgocap.ywxt.file.model.FileIndex;
import com.ginkgocap.ywxt.file.service.FileIndexService;
import com.ginkgocap.ywxt.knowledge.dao.knowledge.KnowledgeDao;
import com.ginkgocap.ywxt.knowledge.dao.knowledgecategory.KnowledgeCategoryDAO;
import com.ginkgocap.ywxt.knowledge.dao.news.KnowledgeNewsDAO;
import com.ginkgocap.ywxt.knowledge.entity.Column;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeBase;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCategory;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeDraft;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeRecycle;
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
import com.ginkgocap.ywxt.knowledge.service.DataCenterService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeCategoryService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeCommentService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeConnectInfoService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeDraftService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeMainService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeMongoIncService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeRecycleService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeStaticsService;
import com.ginkgocap.ywxt.knowledge.service.UserCategoryService;
import com.ginkgocap.ywxt.knowledge.service.UserPermissionService;
import com.ginkgocap.ywxt.knowledge.thread.NoticeThreadPool;
import com.ginkgocap.ywxt.knowledge.util.Constants;
import com.ginkgocap.ywxt.knowledge.util.DateUtil;
import com.ginkgocap.ywxt.knowledge.util.HtmlToText;
import com.ginkgocap.ywxt.knowledge.util.JsonUtil;
import com.ginkgocap.ywxt.knowledge.util.KnowledgeUtil;
import com.ginkgocap.ywxt.metadata.service.SensitiveWordService;
import com.ginkgocap.ywxt.user.form.EtUserInfo;
import com.ginkgocap.ywxt.user.form.ReceiversInfo;
import com.ginkgocap.ywxt.user.model.User;
import com.ginkgocap.ywxt.user.model.UserFeed;
import com.ginkgocap.ywxt.user.service.DiaryService;
import com.ginkgocap.ywxt.user.service.DynamicNewsService;
import com.ginkgocap.ywxt.user.service.FriendsRelationService;
import com.ginkgocap.ywxt.user.service.UserFeedService;
import com.ginkgocap.ywxt.util.DateFunc;
import com.ginkgocap.ywxt.util.MakePrimaryKey;
import com.ginkgocap.ywxt.util.MakeTaskId;
import com.ginkgocap.ywxt.util.PageUtil;

@Service("knowledgeService")
public class KnowledgeServiceImpl extends BaseServiceImpl implements KnowledgeService {

	private final static String dule = "1";

	private static final Logger logger = LoggerFactory.getLogger(KnowledgeServiceImpl.class);

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

	@Autowired
	private SensitiveWordService sensitiveWordService;

	@Autowired
	private KnowledgeMainService knowledgeMainService;

	@Autowired
	private UserFeedService userFeedService;

	@Autowired
	private DiaryService diaryService;

	@Autowired
	private KnowledgeStaticsService knowledgeStaticsService;

	@Autowired
	private DataCenterService dataCenterService;

	@Autowired
	private NoticeThreadPool noticeThreadPool;

	@Autowired
	private KnowledgeCommentService knowledgeCommentService;

	@Autowired
	private FileIndexService fileIndexService;

	@Autowired
	private KnowledgeConnectInfoService knowledgeConnectInfoService;

	@Autowired
	private FriendsRelationService friendsRelationService;

	@Autowired
	private DynamicNewsService dynamicNewsService;

	//
	@Deprecated
	@Override
	public Map<String, Object> deleteKnowledge(String knowledgeids, long catetoryid, String types, String titles, User user) {

		Map<String, Object> result = new HashMap<String, Object>();
		long[] knowledgeid = KnowledgeUtil.convertionToLong(knowledgeids.substring(0, knowledgeids.length() - 1).split(","));
		String[] type = types.split(",");
		String[] title = titles.split(",");
		for (int i = 0; i < knowledgeid.length; i++) {

			String obj = Constants.getTableName(type[i]);

			String collectionName = obj.substring(obj.lastIndexOf(".") + 1, obj.length());

			Criteria criteria = Criteria.where("_id").in(knowledgeid[i]);
			Query query = new Query(criteria);
			Update update = new Update();
			update.set("status", Constants.Status.recycle.v());
			mongoTemplate.updateFirst(query, update, collectionName);

			// 知识存入回收站
			int RecycleCount = knowledgeRecycleService.insertKnowledgeRecycle(knowledgeid[i], title[i], type[i], user.getId(), catetoryid);
			if (RecycleCount == 0) {
				logger.error("删除知识到回收站失败，知识ID:{}", knowledgeid);
				result.put(Constants.status, Constants.ResultType.fail.v());
				return result;
			}
			// 删除知识目录中的信息
			int categoryCount = knowledgeCategoryService.updateKnowledgeCategory(knowledgeid[i], catetoryid);
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
	public Map<String, Object> deleteKnowledgeNew(String knowledgeids, long catetoryid, long userid) {
		Map<String, Object> result = new HashMap<String, Object>();
		long[] knowledgeid = null;

		// 检查是否为标准数据
		try {
			if (knowledgeids.endsWith(",")) {
				knowledgeid = KnowledgeUtil.convertionToLong(knowledgeids.substring(0, knowledgeids.length() - 1).split(","));
			} else {
				knowledgeid = KnowledgeUtil.convertionToLong(knowledgeids.split(","));
			}
		} catch (Exception e) {
			result.put(Constants.status, Constants.ResultType.fail.v());
			return result;
		}

		// 批量删除
		for (int i = 0; i < knowledgeid.length; i++) {
			KnowledgeBase bl = knowledgeBaseMapper.selectByPrimaryKey(knowledgeid[i]);
			String title = "";
			long ct = 0;
			if (bl != null) {
				title = bl.getTitle();
				ct = bl.getColumnType();
			} else {
				continue;
			}
			String obj = Constants.getTableName(ct + "");
			String collectionName = obj.substring(obj.lastIndexOf(".") + 1, obj.length());
			Criteria criteria = Criteria.where("_id").in(knowledgeid[i]);
			Query query = new Query(criteria);
			Update update = new Update();
			update.set("status", Constants.Status.recycle.v());

			// 删除动态长观点信息
			if (ct == Constants.KnowledgeType.Opinion.v()) {
				userFeedService.deleteDynamicKnowledge(knowledgeid[i]);
			}
			// 删除更新关联表
			try {
				mongoTemplate.updateFirst(query, update, collectionName);
				// 多个目录下同一知识，如果回收站里有，不插入回收站
				KnowledgeRecycle recycle = knowledgeRecycleService.selectByKnowledgeId(knowledgeid[i]);
				if (recycle == null) {

					knowledgeRecycleService.insertKnowledgeRecycle(knowledgeid[i], title, ct + "", userid, catetoryid);
				}
				List<KnowledgeCategory> list = knowledgeCategoryService.selectKnowledgeCategory(knowledgeid[i]);
				if (list != null && list.size() > 0) {
					for (KnowledgeCategory knowledgeCategory : list) {
						knowledgeCategoryService.updateKnowledgeCategory(knowledgeid[i], knowledgeCategory.getCategoryId());
					}
				}
			} catch (Exception e) {
				result.put(Constants.status, Constants.ResultType.fail.v());
			}

			// 删除统计表信息
			knowledgeStaticsService.deleteKnowledgeStatics(knowledgeid[i]);

			// 删除该知识评论信息
			knowledgeCommentService.deleteCommentByknowledgeId(knowledgeid[i], userid);
			noticeDataCenter(ct + "", knowledgeid[i], "del");
		}
		return result;
	}

	@Override
	public Map<String, Object> updateKnowledge(KnowledgeNewsVO vo, User user) {
		Map<String, Object> result = new HashMap<String, Object>();

		// 查询知识内容敏感词
		List<String> listword = sensitiveWordService.sensitiveWord(vo.getContent());
		if (listword != null && listword.size() > 0) {
			logger.warn("发布的知识内容存在敏感词，参数为：{}", listword.toString());
			result.put(Constants.status, Constants.ResultType.sensitiveWords.v());
			result.put(Constants.errormessage, Constants.ErrorMessage.sensitiveWord.c());
			result.put("listword", listword);
			return result;
		}
		setVoValues(vo, user);
		insertOrUpdateKnowledge(vo, user);
		// 添加知识到权限表
		result = insertUserPermissions(vo, user);
		Integer status = Integer.parseInt(result.get(Constants.status) + "");
		if (status != 1) {
			result.put(Constants.errormessage, Constants.ErrorMessage.paramNotValid.c());
			return result;
		}

		// 添加知识到目录知识表
		result = insertCatalogueIds(vo, user);
		status = Integer.parseInt(result.get(Constants.status) + "");

		if (status != 1) {
			result.put(Constants.errormessage, Constants.ErrorMessage.addKnowledgeCatalogueIds.c());
			return result;
		}

		saveFeed(vo, user);
		// 大数据通知接口
		noticeDataCenter(vo.getColumnType(), vo.getkId(), "upd");
		result.put(Constants.status, Constants.ResultType.success.v());
		result.put("knowledgeid", vo.getkId());
		result.put("type", vo.getColumnType());
		logger.info("编辑知识成功,知识ID:{}", vo.getkId());
		return result;

	}

	@Override
	public List<String> insertUserShare(KnowledgeNewsVO vo, User user) {
		List<String> permList = JsonUtil.getPermissionList(vo.getSelectedIds());
		// 大乐全平台分享
		userPermissionService.insertUserShare(permList, vo.getkId(), vo, user);
		return permList;
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
		return mongoTemplate.findOne(query, Knowledge.class, obj.substring(obj.lastIndexOf(".") + 1, obj.length()));

	}

	@Deprecated
	@Override
	public List<KnowledgeNews> selectByParam(Long columnid, long source, Long userid, List<Long> ids, int page, int size) {
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

	@Deprecated
	@Override
	public void deleteKnowledgeByid(long knowledgeid) {

		KnowledgeDraft knowledgeDraft = knowledgeDraftService.selectByKnowledgeId(knowledgeid);

		String obj = Constants.getTableName(knowledgeDraft.getType());

		String collectionName = obj.substring(obj.lastIndexOf(".") + 1, obj.length());
		Criteria criteria = Criteria.where("_id").is(knowledgeid);
		Query query = new Query(criteria);
		mongoTemplate.remove(query, collectionName);
	}

	@Override
	public Map<String, Object> insertknowledge(KnowledgeNewsVO vo, User user) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (user == null) {
			result.put(Constants.status, Constants.ResultType.fail.v());
			result.put(Constants.errormessage, Constants.ErrorMessage.userNotLogin.c());
			return result;
		}
		logger.info("开始新建知识,知识类型为：{},创建用户:{}", vo.getColumnType(), user.getId());
		// 查询知识内容敏感词
		List<String> listword = sensitiveWordService.sensitiveWord(vo.getContent());
		if (listword != null && listword.size() > 0) {
			logger.warn("发布的知识内容存在敏感词，参数为：{}", listword.toString());
			result.put(Constants.status, Constants.ResultType.sensitiveWords.v());
			result.put(Constants.errormessage, Constants.ErrorMessage.sensitiveWord.c());
			result.put("listword", listword);
			return result;
		}
		setVoValues(vo, user);
		insertOrUpdateKnowledge(vo, user); // 插入Or修改知识
		// 添加知识到权限表
		result = insertUserPermissions(vo, user);
		Integer status = Integer.parseInt(result.get(Constants.status) + "");
		if (status != 1) {
			result.put(Constants.errormessage, Constants.ErrorMessage.paramNotValid.c());
			return result;
		}

		// 添加知识到目录知识表
		result = insertCatalogueIds(vo, user);
		status = Integer.parseInt(result.get(Constants.status) + "");

		if (status != 1) {
			result.put(Constants.errormessage, Constants.ErrorMessage.addKnowledgeCatalogueIds.c());
			return result;
		}
		saveFeed(vo, user); // 动态存观点
		noticeDataCenter(vo.getColumnType(), vo.getkId(), "add");
		result.put("knowledgeid", vo.getkId());
		result.put("type", vo.getColumnType());
		result.put(Constants.status, Constants.ResultType.success.v());
		logger.info("创建知识成功,知识ID:{}", vo.getkId());
		return result;
	}

	/**
	 * @param vo
	 * @param user
	 */
	private void setVoValues(KnowledgeNewsVO vo, User user) {
		Map<String, Object> result;
		long kId = knowledgeMongoIncService.getKnowledgeIncreaseId();
		result = getColumnidAndColumnPath(vo, user.getId()); // 获得该用户的栏目路径
		vo.setColumnid(result.get("columnid") + "");
		vo.setColumnPath(result.get("columnPath") + "");
		if (this.isAsso(vo.getAsso())) {
			vo.setAsso("");
		}

		if (vo.getKnowledgeid() != null && !"".equals(vo.getKnowledgeid())) {
			vo.setkId(Long.parseLong(vo.getKnowledgeid()));
		} else {
			vo.setkId(kId);
		}
		vo.setDesc(getDesc(vo)); // 如果是行业，投融工具，经典案例，不从内容取简介
		vo.setStatus(Constants.KnowledgeCategoryStatus.effect.v() + "");
		vo.setCreatetime(DateUtil.formatWithYYYYMMDDHHMMSS(new Date()));
		vo.setEssence(vo.getEssence() != null ? StringUtils.equals(vo.getEssence(), "on") ? "1" : "0" : "0");
		vo.setKnowledgestatus(Constants.Status.checked.v());
	}

	@Deprecated
	@Override
	public void restoreKnowledgeByid(long knowledgeid, long userid) {

		KnowledgeRecycle knowledgerecycle = knowledgeRecycleService.selectByKnowledgeId(knowledgeid);

		String obj = Constants.getTableName(knowledgerecycle.getType());

		try {
			String collectionName = obj.substring(obj.lastIndexOf(".") + 1, obj.length());

			Criteria criteria = Criteria.where("_id").is(knowledgeid);

			Query query = new Query(criteria);

			Update update = new Update();
			update.set("status", Constants.Status.checked.v());
			mongoTemplate.updateFirst(query, update, collectionName);

			// //回收站恢复，如果是app端，回收站知识不恢复到目录
			List<KnowledgeCategory> listcategory = knowledgeCategoryService.selectKnowledgeCategory(knowledgeid);
			if (listcategory != null && listcategory.size() > 0) {
				for (KnowledgeCategory knowledgeCategory : listcategory) {
					UserCategory usercategory = userCategoryService.selectByPrimaryKey(knowledgeCategory.getCategoryId());

					if (usercategory != null) {
						knowledgeCategoryService.updateKnowledgeCategorystatus(knowledgeid, knowledgeCategory.getCategoryId());
					} else {
						// 查询该用户下的未分组目录ID
						List<UserCategory> list = userCategoryService.selectNoGroup(userid, Constants.unGroupSortId, (byte) 0);
						if (list != null) {
							// 查询未分组知识，如果未分组中有该知识，则该知识不添加到未分组
							UserCategory category = list.get(0);
							List<KnowledgeCategory> listnogroup = knowledgeCategoryService.selectKnowledgeCategory(knowledgeid, category.getId(),
									Constants.KnowledgeCategoryStatus.effect.v() + "");
							if (listnogroup.size() == 0) {
								knowledgeCategoryService.insertKnowledgeCategoryNogroup(knowledgeid, category.getId());
							}
						}
					}
				}  
			} else {
				// 查询该用户下的未分组目录ID
				List<UserCategory> list = userCategoryService.selectNoGroup(userid, Constants.unGroupSortId, (byte) 0);
				if (list != null) {
					// 查询未分组知识，如果未分组中有该知识，则该知识不添加到未分组
					UserCategory category = list.get(0);
					List<KnowledgeCategory> listnogroup = knowledgeCategoryService.selectKnowledgeCategory(knowledgeid, category.getId(),
							Constants.KnowledgeCategoryStatus.effect.v() + "");
					if (listnogroup.size() == 0) {
						knowledgeCategoryService.insertKnowledgeCategoryNogroup(knowledgeid, category.getId());
					}
				}
			}
			// 知识恢复的同时，将统计信息也恢复
			// knowledgeStaticsService.initKnowledgeStatics(knowledgeid,
			// knowledgerecycle.getTitle(),
			// Short.parseShort(knowledgerecycle.getType()));
			// 大数据通知接口
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("oper", "add");
			params.put("type", knowledgerecycle.getType());
			params.put("kId", knowledgeid);
			noticeThreadPool.noticeDataCenter(Constants.noticeType.knowledge.v(), params);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Deprecated
	@Override
	public void deleteforeverKnowledge(long knowledgeid) {

		KnowledgeRecycle knowledgerecycle = knowledgeRecycleService.selectByKnowledgeId(knowledgeid);

		String obj = Constants.getTableName(knowledgerecycle.getType());

		String collectionName = obj.substring(obj.lastIndexOf(".") + 1, obj.length());
		Criteria criteria = Criteria.where("_id").is(knowledgeid);
		Query query = new Query(criteria);
		Update update = new Update();
		update.set("status", Constants.Status.foreverdelete.v());
		mongoTemplate.updateFirst(query, update, collectionName);

	}

	@Override
	public Map<String, Object> insertUserPermissions(KnowledgeNewsVO vo, User user) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<String> permList = null;
		// 删除知识之前的权限
		userPermissionService.deleteUserPermission(vo.getkId(), user.getId());
		// 创建知识，生成动态
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("type", "10");
		param.put("lowType", vo.getColumnType() + "");
		param.put("createrId", user.getId() + "");
		param.put("title", vo.getTitle() + "");
		param.put("content", vo.getDesc() + "");
		param.put("createrName", user.getName() + "");
		param.put("targetId", vo.getkId() + "");
		param.put("imgPath", vo.getPic() + "");
		param.put("picPath", user.getPicPath() + "");
		param.put("forwardingContent", "");
		param.put("gender", user.getSex());
		param.put("createType", user.isVirtual() ? "2" : "1");
		if (StringUtils.isNotBlank(vo.getSelectedIds()) && !vo.getSelectedIds().equals(dule)) {
			// 获取知识权限,大乐（2）：用户ID1，用户ID2...&中乐（3）：用户ID1，用户ID2...&小乐（4）：用户ID1，用户ID2...
			Boolean dule = JsonUtil.checkKnowledgePermission(vo.getSelectedIds());
			if (dule == null) {
				logger.error("解析权限信息失败，参数为：{}", vo.getSelectedIds());
				result.put(Constants.status, Constants.ResultType.fail.v());
				result.put(Constants.errormessage, Constants.ErrorMessage.paramNotValid.c());
				return result;
			}
			if (!dule) {
				// 获取用户
				permList = userPermissionService.userPermissionAll(vo.getSelectedIds(), user);
				// 大乐全平台分享
				userPermissionService.insertUserShare(permList, vo.getkId(), vo, user);
				// 分享到金桐脑
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("userId", user.getId());
				params.put("vo", vo);
				noticeThreadPool.noticeDataCenter(Constants.noticeType.shareToJinTN.v(), params);
				// 判断基础信息来源
				boolean flag = userPermissionService.checkUserSource(permList);
				result.put("flag", flag);
				int pV = userPermissionService.insertUserPermission(permList, vo.getkId(), user.getId(), vo.getShareMessage(),
						Short.parseShort(vo.getColumnType()), Long.parseLong(vo.getColumnid()));
				if (pV == 0) {
					logger.error("创建知识未全部完成,添加知识到用户权限信息失败，知识ID:{},目录ID:{}", vo.getkId());
				}
				param.put("receiverIds", changeRelation(permList));
			}
		} else {
			// 独乐也生成动态
			param.put("receiverIds", null);
		}
		// 新建知识均生成动态
		dynamicNewsService.insertNewsAndRelationByParam(param);
		if (vo.isNeedUpdate()) {
			updateKnowledgeByPermission(vo);
		}
		noticeDataCenter(vo.getColumnType(),vo.getkId(), "upd");
		result.put(Constants.status, Constants.ResultType.success.v());
		return result;
	}

	private Map<String, List<Long>> changeRelation(List<String> permList) {

		Map<String, List<Long>> map = new HashMap<String, List<Long>>();

		if (permList == null || permList.size() == 0)
			return null;
		for (String str : permList) {
			String[] temp = str.split(":");
			// 未分享给用户
			if (temp.length < 1)
				continue;
			List<Long> ids = new ArrayList<Long>();
			String tem = temp[1];
			if ("[]".equals(tem) || "".equals(tem))
				continue;
			String[] strIds = tem.substring(1, tem.length() - 1).split(",");
			for (String id : strIds) {
				ids.add(Long.valueOf(id.trim()));
			}
			// 如果为大乐
			if ("2".equals(temp[0])) {
				map.put("dale", ids);
			}// 如果为中乐
			else if ("3".equals(temp[0])) {
				map.put("zhongle", ids);
			}
		}
		return map;
	}

	/**
	 * @param getkId
	 * @param columnType
	 * @param selectedIds
	 */
	private void updateKnowledgeByPermission(KnowledgeNewsVO vo) {
		logger.info("进入修改知识权限请求,id:{},type{}", vo.getkId(), vo.getColumnType());
		String obj = Constants.getTableName(vo.getColumnType());
		try {
			Criteria criteria = Criteria.where("_id").is(vo.getkId());
			Query query = new Query(criteria);
			Update update = new Update();
			update.set("selectedIds", vo.getSelectedIds());

			mongoTemplate.updateFirst(query, update, obj.substring(obj.lastIndexOf(".") + 1, obj.length()));

		} catch (Exception e) {
			e.printStackTrace();
			logger.info("修改知识权限请求失败,id:{},type{}", vo.getkId(), vo.getColumnType());
		}

		logger.info("修改知识权限请求成功,id:{},type{}", vo.getkId(), vo.getColumnType());

	}

	@Deprecated
	@Override
	public void updateKnowledgeForInvestment(Long id, String pic, String refrenceData, String imageBookData, String content, String desc, Long userId) {
		knowledgeDao.updateInvestment(id, pic, refrenceData, imageBookData, content, desc);
		knowledgeDraftService.deleteKnowledgeSingalDraft(id, "2", userId);
		// 大数据通知接口
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("oper", "upd");
		params.put("type", "2");
		params.put("kId", id);
		noticeThreadPool.noticeDataCenter(Constants.noticeType.knowledge.v(), params);
	}

	public void updateByPrimaryKey(KnowledgeBase kb) {
		knowledgeBaseMapper.modifyKnowledgeId(kb);
	}

	@Override
	public Map<String, Object> saveKnowledge(KnowledgeNewsVO vo, User user) {

		Map<String, Object> result = new HashMap<String, Object>();
		if (user == null) {
			result.put(Constants.status, Constants.ResultType.fail.v());
			result.put(Constants.errormessage, Constants.ErrorMessage.userNotLogin.c());
			return result;
		}
		logger.info("开始保存知识,知识类型为：{},创建用户:{}", vo.getColumnType(), user.getId());
		// 保存，通过知识ID 查找需要保存的信息
		if (vo.getkId() != 0) {
			String taskId = MakeTaskId.getTaskId();
			Knowledge knowledge = selectKnowledge(vo.getkId(), vo.getColumnType());
			long kId = knowledgeMongoIncService.getKnowledgeIncreaseId();
			String columnid = StringUtils.isBlank(knowledge.getColumnid()) ? "0" : knowledge.getColumnid();
			// 判断用户是否选择栏目
			String columnPath = null;
			if (Long.parseLong(columnid) != 0) {
				columnPath = columnService.getColumnPathById(Long.parseLong(columnid));
			} else {
				columnPath = "";
			}
			if (isAsso(vo.getAsso())) {
				vo.setAsso("");
			}
			saveFileIndex(knowledge.getTaskid(), user);
			// 知识入Mongo
			vo.setkId(kId);
			vo.setColumnPath(columnPath);
			vo.setColumnid(columnid);
			vo.setContent(knowledge.getContent());
			vo.setTaskId(taskId);
			if (Integer.parseInt(vo.getColumnType()) == Constants.Type.Opinion.v()) {
				vo.setColumnid(Constants.KnowledgeType.Opinion.v() + "");
				columnPath = Constants.KnowledgeType.Opinion.c();
				vo.setColumnPath(Constants.KnowledgeType.Opinion.c());

			}

			if (Integer.parseInt(vo.getColumnType()) == Constants.Type.Article.v()) {
				vo.setColumnid(Constants.KnowledgeType.Article.v() + "");
				columnPath = Constants.KnowledgeType.Article.c();
				vo.setColumnPath(Constants.KnowledgeType.Article.c());

			}
			vo.setDesc(knowledge.getDesc());
			vo.setStatus(Constants.KnowledgeCategoryStatus.effect.v() + "");
			vo.setCreatetime(DateUtil.formatWithYYYYMMDDHHMMSS(new Date()));
			vo.setEssence(knowledge.getEssence() + "");
			vo.setKnowledgestatus(Constants.Status.checked.v());
			vo.setPic(knowledge.getPic());
			insertOrUpdateKnowledge(vo, user);
			// 添加知识到权限表
			result = insertUserPermissions(vo, user);
			Integer status = Integer.parseInt(result.get(Constants.status) + "");
			if (status != 1) {
				result.put(Constants.errormessage, Constants.ErrorMessage.paramNotValid.c());
				return result;
			}
			// 添加知识到目录知识表
			result = insertCatalogueIds(vo, user);
			status = Integer.parseInt(result.get(Constants.status) + "");
			if (status != 1) {
				result.put(Constants.errormessage, Constants.ErrorMessage.addKnowledgeCatalogueIds.c());
				return result;
			}
			saveFeed(vo, user);
		}
		noticeDataCenter(vo.getColumnType(), vo.getkId(), "add");
		result.put("knowledgeid", vo.getkId());
		result.put("type", vo.getColumnType());
		result.put(Constants.status, Constants.ResultType.success.v());
		logger.info("保存知识成功,知识ID:{}", vo.getkId());
		return result;

	}

	@Override
	public Map<String, Object> insertCatalogueIds(KnowledgeNewsVO vo, User user) {

		Map<String, Object> result = new HashMap<String, Object>();

		// 删除该知识下的所有目录
		knowledgeCategoryService.deleteKnowledgeCategory(vo.getkId());
		// 删除该知识的基本信息
		knowledgeBaseMapper.deleteByPrimaryKey(vo.getkId());

		long[] cIds = null;
		// 添加知识到知识目录表
		if (StringUtils.isBlank(vo.getCatalogueIds())) { // 如果目录ID为空,默认添加到未分组目录中.
			UserCategoryExample example = new UserCategoryExample();
			com.ginkgocap.ywxt.knowledge.entity.UserCategoryExample.Criteria criteria = example.createCriteria();
			criteria.andSortidEqualTo(Constants.unGroupSortId);
			criteria.andUserIdEqualTo(user.getId());
			criteria.andCategoryTypeEqualTo((short) Constants.CategoryType.common.v());
			List<UserCategory> list = userCategoryMapper.selectByExample(example);
			if (list != null && list.size() > 0) {// 创建空指针异常
				cIds = new long[1];
				cIds[0] = list.get(0).getId();
			}
		} else {
			cIds = KnowledgeUtil.formatString(vo.getCatalogueIds());
		}

		int categoryV = knowledgeCategoryService.insertKnowledgeRCategory(vo.getkId(), cIds, user.getId(), user.getName(), vo.getColumnPath(), vo);
		if (categoryV == 0) {
			logger.error("创建知识未全部完成,添加知识到知识目录信息失败，知识ID:{},目录ID:{}", vo.getkId(), cIds);
			result.put(Constants.status, Constants.ResultType.fail.v());
			result.put(Constants.errormessage, Constants.ErrorMessage.addKnowledgeFail.c());
			return result;
		}
		result.put(Constants.status, Constants.ResultType.success.v());
		return result;
	}

	public boolean isAsso(String asso) {
		if (asso.contains("{\"r\":[],\"p\":[],\"o\":[],\"k\":[]}")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 根据知识knowledgeId 来判断 insert Or update
	 * 
	 * @param vo
	 * @param user
	 */
	public void insertOrUpdateKnowledge(KnowledgeNewsVO vo, User user) {
		if (StringUtils.isNotBlank(vo.getKnowledgeid())) {
			vo.setkId(Long.parseLong(vo.getKnowledgeid()));
			// 关联信息存入mysql中
			if (StringUtils.isNotBlank(vo.getAsso())) {
				knowledgeConnectInfoService.insertKnowledgeConnectInfo(vo.getAsso(), vo.getkId(), user.getId()); // 关联信息存入mysql中
			}
			knowledgeNewsDAO.updateKnowledge(vo, user);
		} else {
			if (StringUtils.isNotBlank(vo.getAsso())) {
				knowledgeConnectInfoService.insertKnowledgeConnectInfo(vo.getAsso(), vo.getkId(), user.getId());
			}
			knowledgeNewsDAO.insertknowledge(vo, user);
		}
	}

	/**
	 * 观点添加到动态
	 * 
	 * @param vo
	 * @param user
	 */
	public void saveFeed(KnowledgeNewsVO vo, User user) {
		try {
			if (Integer.parseInt(vo.getColumnType()) == Constants.KnowledgeType.Opinion.v()) {
				UserFeed feed = new UserFeed();
				feed.setContent(vo.getContent());
				feed.setCreatedBy(user.getName());
				feed.setCreatedById(user.getId());
				feed.setCtime(DateFunc.getDate());
				feed.setGroupName("仅好友可见");
				feed.setScope(1);// 设置可见级别
				feed.setGroupName("");
				feed.setTargetId(vo.getkId());
				feed.setTitle(vo.getTitle());
				feed.setType(1);
				feed.setImgPath("");// 长观点地址
				feed.setDelstatus(0);// 删除状态
				List<ReceiversInfo> receivers = diaryService.getReceiversInfo(vo.getContent(), -1, user.getId());
				feed.setReceivers(receivers);// 获取接收人信息
				feed.setDiaryType(1);
				List<EtUserInfo> etInfo = new ArrayList<EtUserInfo>();// 被@的信息
				feed.setEtInfo(etInfo);
				userFeedService.saveOrUpdate(feed);
			}
		} catch (Exception e) {
			logger.error("动态观点存储失败,知识ID{}", vo.getkId());
			e.printStackTrace();
		}
	}

	/**
	 * 获取知识栏目Id和栏目路径
	 * 
	 * @param vo
	 * @param userId
	 * @return
	 */
	public Map<String, Object> getColumnidAndColumnPath(KnowledgeNewsVO vo, long userId) {
		Map<String, Object> result = new HashMap<String, Object>();
		String columnid = StringUtils.isBlank(vo.getColumnid()) ? "0" : vo.getColumnid();
		String columnPath = null;
		Column column = null;
		if (Long.parseLong(columnid) != 0) {
			columnPath = columnService.getColumnPathById(Long.parseLong(columnid));
		} else {
			column = columnService.getUnGroupColumnIdBySortId(userId);
			if (column == null) {
				columnService.checkNogroup(userId); // 没有未没分组栏目，添加
			} else {
				columnid = column.getId() + "";
			}
			columnPath = Constants.unGroupSortName;
		}
		if (Integer.parseInt(vo.getColumnType()) == Constants.Type.Opinion.v()) {
			columnid = Constants.KnowledgeType.Opinion.v() + "";
			columnPath = Constants.KnowledgeType.Opinion.c();
		}
		if (Integer.parseInt(vo.getColumnType()) == Constants.Type.Article.v()) {
			columnid = Constants.KnowledgeType.Article.v() + "";
			columnPath = Constants.KnowledgeType.Article.c();
		}
		result.put("columnid", columnid);
		result.put("columnPath", columnPath);
		return result;
	}

	/**
	 * 接知识类型 获取知识的desc
	 * 
	 * @param vo
	 * @return
	 */
	public String getDesc(KnowledgeNewsVO vo) {
		String content = "";
		if (Integer.parseInt(vo.getColumnType()) == Constants.Type.Investment.v()
				|| Integer.parseInt(vo.getColumnType()) == Constants.Type.Industry.v()
				|| Integer.parseInt(vo.getColumnType()) == Constants.Type.Case.v()) {
			if (StringUtils.isNotBlank(vo.getDesc())) {
				return getContent(vo.getDesc());
			} else {
				return getContent(vo.getContent());
			}
		} else {
			if (StringUtils.isBlank(vo.getDesc())) {
				return getContent(vo.getContent());
			}
		}
		return content;
	}

	/**
	 * 保存知识，通过taskId 获取原知识附件，保存到新知识内
	 * 
	 * @param taskId
	 * @param user
	 */
	public void saveFileIndex(String taskId, User user) {
		// 保存附件
		List<FileIndex> filelist = fileIndexService.selectByTaskId(taskId, Constants.KnowledgeCategoryStatus.effect.v() + "");
		if (filelist != null && filelist.size() > 0) {
			for (FileIndex fileIndex : filelist) {
				String fileId = MakePrimaryKey.getPrimaryKey();
				FileIndex fileIndex1 = new FileIndex();
				fileIndex1.setId(fileId);
				fileIndex1.setFilePath(fileIndex.getFilePath());
				fileIndex1.setFileTitle(fileIndex.getFileTitle());
				fileIndex1.setTaskId(taskId);
				fileIndex1.setFileSize(fileIndex.getFileSize());
				fileIndex1.setAuthor(user.getId());
				fileIndex1.setAuthorName(user.getName());
				fileIndex1.setMd5(fileIndex.getMd5());
				fileIndex1.setStatus(true);
				fileIndex1.setCtime(DateUtil.formatWithYYYYMMDDHHMMSS(new Date()));
				fileIndexService.insert(fileIndex1);
			}
		}
	}

	public String getContent(String strContent) {
		String content = HtmlToText.html2Text(strContent);
		if (StringUtils.isNotBlank(content)) {
			content = content.length() > 50 ? content.substring(0, 50) + "..." : content;
		}
		return content;
	}
}
