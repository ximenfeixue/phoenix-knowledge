package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ginkgocap.ywxt.knowledge.dao.knowledge.KnowledgeDraftDAO;
import com.ginkgocap.ywxt.knowledge.dao.news.KnowledgeNewsDAO;
import com.ginkgocap.ywxt.knowledge.entity.Column;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeDraft;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeDraftExample;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeDraftExample.Criteria;
import com.ginkgocap.ywxt.knowledge.entity.UserCategory;
import com.ginkgocap.ywxt.knowledge.entity.UserCategoryExample;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeBaseMapper;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeDraftMapper;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeDraftValueMapper;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeStaticsMapper;
import com.ginkgocap.ywxt.knowledge.mapper.UserCategoryMapper;
import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNewsVO;
import com.ginkgocap.ywxt.knowledge.service.ColumnService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeCategoryService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeConnectInfoService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeDraftService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeMainService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeMongoIncService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeService;
import com.ginkgocap.ywxt.knowledge.service.UserPermissionService;
import com.ginkgocap.ywxt.knowledge.util.Constants;
import com.ginkgocap.ywxt.knowledge.util.DateUtil;
import com.ginkgocap.ywxt.knowledge.util.JsonUtil;
import com.ginkgocap.ywxt.knowledge.util.KnowledgeUtil;
import com.ginkgocap.ywxt.knowledge.util.Page;
import com.ginkgocap.ywxt.metadata.service.SensitiveWordService;
import com.ginkgocap.ywxt.user.model.User;

@Deprecated
@Service("knowledgeDraftService")
public class KnowledgeDraftServiceImpl implements KnowledgeDraftService {

	private final static String dule = "1";

	private static final Logger logger = LoggerFactory
			.getLogger(KnowledgeDraftServiceImpl.class);

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

	@Autowired
	private SensitiveWordService sensitiveWordService;

	@Autowired
	private KnowledgeMainService knowledgeMainService;

	@Resource
	private KnowledgeBaseMapper knowledgeBaseMapper;

	@Autowired
	private KnowledgeService knowledgeService;

	@Autowired
	private KnowledgeConnectInfoService knowledgeConnectInfoService;

	@Override
	public Map<String, Object> insertKnowledgeDraft(KnowledgeNewsVO vo,
			User user) {
		Map<String, Object> result = new HashMap<String, Object>();
		// 获取Session用户值
		long userId = user.getId();
		String username = user.getName();

		// long kId = knowledgeMongoIncService.getKnowledgeIncreaseId();
		String columnid = StringUtils.isBlank(vo.getColumnid()) ? "0" : vo
				.getColumnid();
		// TODO 判断用户是否选择栏目
		String columnPath = null;
		Column column = null;
		if (Long.parseLong(columnid) != 0) {
			columnPath = columnService.getColumnPathById(Long
					.parseLong(columnid));
		} else {
			column = columnService.getUnGroupColumnIdBySortId(user.getId());
			if (column == null) {
				// 没有未没分组栏目，添加
				columnService.checkNogroup(user.getId());
			} else {
				columnid = column.getId() + "";
			}

			columnPath = Constants.unGroupSortName;
		}

		if (vo.getAsso().equals("{\"r\":[],\"p\":[],\"o\":[],\"k\":[]}")) {
			vo.setAsso("");
		}
		// 知识入Mongo
		// vo.setkId(kId);
		vo.setColumnPath(columnPath);
		vo.setColumnid(columnid);
		vo.setStatus(Constants.KnowledgeCategoryStatus.uneffect.v() + "");
		vo.setCreatetime(DateUtil.formatWithYYYYMMDDHHMMSS(new Date()));
		vo.setEssence(vo.getEssence() != null ? StringUtils.equals(
				vo.getEssence(), "on") ? "1" : "0" : "0");
		// 查询知识内容敏感词
		List<String> listword = sensitiveWordService.sensitiveWord(vo
				.getContent());
		if (listword != null && listword.size() > 0) {
			logger.error("发布的知识内容存在敏感词，参数为：{}", vo.getkId());
			result.put(Constants.status,
					Constants.ResultType.sensitiveWords.v());
			result.put(Constants.errormessage,
					Constants.ErrorMessage.sensitiveWord.c());
			result.put("listword", listword);
			return result;
		}
		if (StringUtils.isNotBlank(vo.getKnowledgeid())) {

			vo.setkId(Long.parseLong(vo.getKnowledgeid()));
			Knowledge k = getDraftByMainIdAndUser(vo.getkId(),
					vo.getColumnType(), user.getId());
			if (k != null && k.getId() > 0) {
				vo.setKnowledgeMainId(vo.getkId());
				vo.setkId(k.getId());
				// 关联信息存入mysql中
				if (StringUtils.isNotBlank(vo.getAsso())) {
					knowledgeConnectInfoService.insertKnowledgeConnectInfo(
							vo.getAsso(), vo.getkId(), user.getId());
				}
				knowledgeNewsDAO.updateKnowledgeDraft(vo, user);
			} else {
				long kId = knowledgeMongoIncService.getKnowledgeIncreaseId();
				vo.setKnowledgeMainId(vo.getkId());
				vo.setkId(kId);
				// 关联信息存入mysql中
				if (StringUtils.isNotBlank(vo.getAsso())) {
					knowledgeConnectInfoService.insertKnowledgeConnectInfo(
							vo.getAsso(), vo.getkId(), user.getId());
				}
				knowledgeNewsDAO.insertknowledgeDraft(vo, user);
			}

			KnowledgeDraft knowledgeDraft = this.selectByKnowledgeId(vo
					.getKnowledgeMainId());

			if (knowledgeDraft != null) {
				this.updateKnowledgeDaraft(vo.getKnowledgeMainId(),
						vo.getTitle(), vo.getColumnName(), userId,
						vo.getColumnType());
			} else {

				knowledgeDraftDAO.insertKnowledge(vo.getKnowledgeMainId(),
						vo.getTitle(), vo.getColumnName(), vo.getColumnType(),
						userId);
			}
		} else {
			// id=0 相当新增
			long draftKId = knowledgeMongoIncService.getKnowledgeIncreaseId();
			long kId = knowledgeMongoIncService.getKnowledgeIncreaseId();
			vo.setkId(draftKId);
			// 关联信息存入mysql中
			if (StringUtils.isNotBlank(vo.getAsso())) {
				knowledgeConnectInfoService.insertKnowledgeConnectInfo(
						vo.getAsso(), vo.getkId(), user.getId());
			}
			knowledgeNewsDAO.insertknowledgeDraft(vo, user); // 插入到正式库假装当作草稿防止被查询出来
			vo.setKnowledgeMainId(draftKId);// 草稿中存放真正知识的ID
			vo.setkId(kId);// 插入草稿ID
			// 关联信息存入mysql中
			if (StringUtils.isNotBlank(vo.getAsso())) {
				knowledgeConnectInfoService.insertKnowledgeConnectInfo(
						vo.getAsso(), vo.getkId(), user.getId());
			}
			
			knowledgeNewsDAO.insertknowledgeDraft(vo, user); // 插入到正式库并当作真实的知识草稿
			knowledgeDraftDAO.insertKnowledge(draftKId, vo.getTitle(),
					vo.getColumnName(), vo.getColumnType(), userId);
			vo.setkId(kId-1);

		}
		// 添加知识到目录知识表
		result = knowledgeService.insertCatalogueIds(vo, user);
		Integer status = Integer.parseInt(result.get(Constants.status) + "");

		if (status != 1) {
			result.put(Constants.errormessage,
					Constants.ErrorMessage.addKnowledgeCatalogueIds.c());
			return result;
		}
		result.put(Constants.status, Constants.ResultType.success.v());
		result.put("knowledgeid", vo.getKnowledgeMainId());
		logger.info("添加草稿箱成功,知识ID:{}", vo.getkId());
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
	@Transactional
	public int deleteKnowledgeDraft(long[] knowledgeids, long userid) {

		return knowledgeDraftValueMapper.deleteKnowledge(knowledgeids, userid);
	}

	@Override
	public KnowledgeDraft selectByKnowledgeId(long knowledgeid) {

		return knowledgeDraftMapper.selectByPrimaryKey(knowledgeid);
	}

	@Override
	public List<KnowledgeDraft> selectKnowledgeDraft(long userid, String type,
			String keyword, int pageno, int pagesize) {

		KnowledgeDraftExample example = new KnowledgeDraftExample();
		Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(type)) {
			criteria.andDrafttypeEqualTo(type);
		}
		if (StringUtils.isNotBlank(keyword)) {
			criteria.andDraftnameLike("%" + keyword + "%");
		}
		criteria.andUseridEqualTo(userid);
		example.setOrderByClause("createtime desc");
		example.setLimitStart(pageno);
		example.setLimitEnd(pagesize);
		return knowledgeDraftMapper.selectByExample(example);
	}

	@Override
	public int countKnowledgeDraft(long userid, String type, String keyword) {

		KnowledgeDraftExample example = new KnowledgeDraftExample();
		Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(type)) {
			criteria.andDrafttypeEqualTo(type);
		}
		if (StringUtils.isNotBlank(keyword)) {
			criteria.andDraftnameLike("%" + keyword + "%");
		}
		criteria.andUseridEqualTo(userid);
		return knowledgeDraftMapper.countByExample(example);
	}

	@Override
	@Transactional
	public int deleteKnowledgeDraft(long knowledgeid) {

		return knowledgeDraftMapper.deleteByPrimaryKey(knowledgeid);
	}

	@Override
	@Transactional
	public int updateKnowledgeDaraft(long knowledgeid, String draftname,
			String drafttype, long userid, String type) {

		KnowledgeDraft knowledgedraft = new KnowledgeDraft();
		knowledgedraft.setKnowledgeId(knowledgeid);
		knowledgedraft.setDraftname(draftname);
		knowledgedraft.setDrafttype(drafttype);
		knowledgedraft.setUserid(userid);
		knowledgedraft.setType(type);
		knowledgedraft.setCreatetime(new Date());
		return knowledgeDraftMapper.updateByPrimaryKeySelective(knowledgedraft);
	}

	@Override
	public Map<String, Object> insertKnowledgeDraftNew(KnowledgeNewsVO vo,
			User user) {
		return this.insertKnowledgeDraftNew(vo, user, false);
	}

	@Override
	public int deleteKnowledgeSingalDraft(Long knowledgeMainId, String type) {
		return deleteKnowledgeSingalDraft(knowledgeMainId, type, null);
	}

	@Override
	@Transactional
	public int deleteKnowledgeSingalDraft(Long knowledgeMainId, String type,
			Long userId) {

		Knowledge k = getDraftByMainIdAndUser(knowledgeMainId, type, userId);
		if (k == null) {
			return 0;
		}
		knowledgeNewsDAO.deleteKnowledgeById(k.getId(), type);

		if (userId == null) {
			return knowledgeDraftMapper.deleteByPrimaryKey(k.getId());
		} else {
			KnowledgeDraft knowledgeDraft = new KnowledgeDraft();
			knowledgeDraft.setKnowledgeId(k.getKnowledgeMainId());
			knowledgeDraft.setUserid(userId);
			return knowledgeDraftMapper
					.deleteByPrimaryKeyAndUserId(knowledgeDraft);
		}
	}

	@Override
	public Knowledge getDraftByMainId(Long knowledgeId, String type) {
		return this.getDraftByMainIdAndUser(knowledgeId, type, null);
	}

	@Override
	public Knowledge getDraftByMainIdAndUser(Long knowledgeId, String type,
			Long userId) {
		return knowledgeNewsDAO.getDraftByMainIdAndUser(knowledgeId, type,
				userId);
	}

	@Override
	public Map<String, Object> insertKnowledgeDraftNew(KnowledgeNewsVO vo,
			User user, boolean isUpdate) {
		Map<String, Object> result = new HashMap<String, Object>();
		// 获取Session用户值
		long userId = user.getId();
		String username = user.getName();

		String columnid = StringUtils.isBlank(vo.getColumnid()) ? "0" : vo
				.getColumnid();
		// TODO 判断用户是否选择栏目
		String columnPath = null;
		Column column = null;
		if (Long.parseLong(columnid) != 0) {
			columnPath = columnService.getColumnPathById(Long
					.parseLong(columnid));
		} else {
			column = columnService.getUnGroupColumnIdBySortId(user.getId());
			if (column == null) {
				// 没有未没分组栏目，添加
				columnService.checkNogroup(user.getId());
			} else {
				columnid = column.getId() + "";
			}

			columnPath = Constants.unGroupSortName;
		}
		// 知识入Mongo
		vo.setColumnPath(columnPath);
		vo.setColumnid(columnid);
		vo.setStatus(Constants.KnowledgeCategoryStatus.uneffect.v() + "");
		vo.setCreatetime(DateUtil.formatWithYYYYMMDDHHMMSS(new Date()));
		vo.setEssence(vo.getEssence() != null ? StringUtils.equals(
				vo.getEssence(), "on") ? "1" : "0" : "0");
		// 查询知识内容敏感词
		List<String> listword = sensitiveWordService.sensitiveWord(vo
				.getContent());
		if (listword != null && listword.size() > 0) {
			logger.error("发布的知识内容存在敏感词，参数为：{}", vo.getkId());
			result.put(Constants.status,
					Constants.ResultType.sensitiveWords.v());
			result.put(Constants.errormessage,
					Constants.ErrorMessage.sensitiveWord.c());
			result.put("listword", listword);
			return result;
		}
		// 法律法规名称不可重复
		if (Integer.parseInt(vo.getColumnType()) == Constants.Type.Law.v()) {
			int count = knowledgeMainService.checkLawNameRepeat(vo.getTitle());
			if (count == 0) {
				logger.error("法律法规名称重复，参数为：{}", vo.getTitle());
				result.put(Constants.status,
						Constants.ResultType.sameNameError.v());
				result.put(Constants.errormessage,
						Constants.ErrorMessage.hasName.c());

				return result;
			}
		}
		if (vo.getkId() > 0) {
			Knowledge k = getDraftByMainIdAndUser(vo.getkId(),
					vo.getColumnType(), user.getId());
			if (k != null && k.getId() > 0) {
				vo.setKnowledgeMainId(vo.getkId());
				vo.setkId(k.getId());
				// 关联信息存入mysql中
				// if (StringUtils.isNotBlank(vo.getAsso())) {
				// knowledgeConnectInfoService.insertKnowledgeConnectInfo(
				// vo.getAsso(), vo.getkId());
				// }
				knowledgeNewsDAO.updateKnowledgeDraft(vo, user);
			} else {
				long kId = knowledgeMongoIncService.getKnowledgeIncreaseId();
				vo.setKnowledgeMainId(vo.getkId());
				vo.setkId(kId);
				// 关联信息存入mysql中
				// if (StringUtils.isNotBlank(vo.getAsso())) {
				// knowledgeConnectInfoService.insertKnowledgeConnectInfo(
				// vo.getAsso(), vo.getkId());
				// }
				knowledgeNewsDAO.insertknowledgeDraft(vo, user);
			}
			// 判断是否是投融工具更新
			if (!isUpdate) {
				if (Integer.parseInt(vo.getColumnType()) != Constants.Type.Law
						.v()) {// 法律法规只有独乐，不入权限表

				}

				// 删除该知识下的所有目录
				int categoryCount = knowledgeCategoryService
						.deleteKnowledgeCategory(vo.getkId());
				// 删除该知识的基本信息
				knowledgeBaseMapper.deleteByPrimaryKey(vo.getkId());

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
					if (list != null && list.size() > 0) {//创建空指针异常
						cIds = new long[1];
						cIds[0] = list.get(0).getId();
					}
				} else {
					cIds = KnowledgeUtil.formatString(vo.getCatalogueIds());
				}
				int categoryV = knowledgeCategoryService
						.insertKnowledgeRCategory(vo.getkId(), cIds,
								user.getId(), user.getName(), columnPath, vo);
				if (categoryV == 0) {
					logger.error("创建知识未全部完成,添加知识到知识目录信息失败，知识ID:{},目录ID:{}",
							vo.getkId(), cIds);
					result.put(Constants.status, Constants.ResultType.fail.v());
					result.put(Constants.errormessage,
							Constants.ErrorMessage.addKnowledgeFail.c());
					return result;
				}
			}
			KnowledgeDraft knowledgeDraft = this.selectByKnowledgeId(vo
					.getKnowledgeMainId());

			if (knowledgeDraft != null) {
				this.updateKnowledgeDaraft(vo.getKnowledgeMainId(),
						vo.getTitle(), vo.getColumnName(), userId,
						vo.getColumnType());
			} else {

				knowledgeDraftDAO.insertKnowledge(vo.getKnowledgeMainId(),
						vo.getTitle(), vo.getColumnName(), vo.getColumnType(),
						userId);
			}
		} else {
			// id=0 相当新增
			long draftKId = knowledgeMongoIncService.getKnowledgeIncreaseId();
			long kId = knowledgeMongoIncService.getKnowledgeIncreaseId();
			vo.setkId(draftKId);
			knowledgeNewsDAO.insertknowledgeDraft(vo, user); // 插入到正式库假装当作草稿防止被查询出来
			vo.setKnowledgeMainId(draftKId);// 草稿中存放真正知识的ID
			vo.setkId(kId);// 插入草稿ID
			knowledgeNewsDAO.insertknowledgeDraft(vo, user); // 插入到正式库并当作真实的知识草稿
			knowledgeDraftDAO.insertKnowledge(draftKId, vo.getTitle(),
					vo.getColumnName(), vo.getColumnType(), userId);
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

				int categoryV = knowledgeCategoryService
						.insertKnowledgeRCategory(kId, cIds, userId, username,
								columnPath, vo);
				if (categoryV == 0) {
					logger.error("创建知识未全部完成,添加知识到知识目录信息失败，知识ID:{},目录ID:{}",
							kId, cIds);
					result.put(Constants.status, Constants.ResultType.fail.v());
					result.put(Constants.errormessage,
							Constants.ErrorMessage.addKnowledgeFail.c());
					return result;
				}
			}
			// // 初始化知识统计信息
			// KnowledgeStatics statics = new KnowledgeStatics();
			// statics.setClickcount(0l);
			// statics.setCollectioncount(0l);
			// statics.setCommentcount(0l);
			// statics.setKnowledgeId(kId);
			// statics.setSharecount(0l);
			// statics.setTitle(vo.getTitle());
			// statics.setType(Short.parseShort(vo.getColumnType()));
			// int sV = knowledgeStaticsMapper.insertSelective(statics);
			// if (sV == 0) {
			// logger.error("创建知识未全部完成,添加知识到知识统计信息失败，知识ID:{},栏目类型:{}", kId,
			// vo.getColumnType());
			// result.put(Constants.status, Constants.ResultType.fail.v());
			// result.put(Constants.errormessage,
			// Constants.ErrorMessage.addKnowledgeFail.c());
			// return result;
			// }

		}
		result.put(Constants.status, Constants.ResultType.success.v());
		result.put("kId", vo.getKnowledgeMainId());
		logger.info("添加草稿箱成功,草稿知识ID:{}", vo.getkId());
		return result;
	}

}
