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
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.dao.knowledge.KnowledgeDao;
import com.ginkgocap.ywxt.knowledge.dao.knowledgecategory.KnowledgeCategoryDAO;
import com.ginkgocap.ywxt.knowledge.dao.news.KnowledgeNewsDAO;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNews;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNewsVO;
import com.ginkgocap.ywxt.knowledge.service.ColumnKnowledgeService;
import com.ginkgocap.ywxt.knowledge.service.ColumnService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeCategoryService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeMongoIncService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeNewsService;
import com.ginkgocap.ywxt.knowledge.service.UserPermissionService;
import com.ginkgocap.ywxt.knowledge.util.Constants;
import com.ginkgocap.ywxt.knowledge.util.KnowledgeUtil;
import com.ginkgocap.ywxt.user.model.User;

@Service("knowledgeNewsService")
public class KnowledgeNewsServiceImpl implements KnowledgeNewsService {

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

	@Override
	public void deleteKnowledge(long[] ids) {

		knowledgeNewsDAO.deleteKnowledge(ids);
	}

	@Override
	public void updateKnowledge(String title, long userid, String uname,
			long cid, String cname, String cpath, String content, String pic,
			String desc, String essence, String taskid, String tags,
			long knowledgeid) {

		knowledgeNewsDAO.updateKnowledge(title, userid, uname, cid, cname,
				cpath, content, pic, desc, essence, taskid, tags, knowledgeid);
	}

	@Override
	public KnowledgeNews selectKnowledge(long knowledgeid) {

		return knowledgeNewsDAO.selectKnowledge(knowledgeid);
	}

	@Override
	public List<KnowledgeNews> selectByParam(Long columnid, long source,
			Long userid, List<Long> ids, int page, int size) {
		return knowledgeNewsDAO.selectByParam(columnid, source, userid, ids,
				page, size);
	}

	@Override
	public void deleteKnowledgeByid(long knowledgeid) {
		knowledgeNewsDAO.deleteKnowledgeByid(knowledgeid);
	}

	@Override
	public Map<String, Object> insertknowledge(KnowledgeNewsVO vo, User user) {
		logger.info("开始新建知识,知识类型为：{},创建用户:{}", vo.getColumnType(), user.getId());
		Map<String, Object> result = new HashMap<String, Object>();
		// 获取Session用户值
		long userId = user.getId();
		String username = user.getUserName();

		long kId = knowledgeMongoIncService.getKnowledgeIncreaseId();
		String columnPath = columnService.columnname(vo.getColumnid());
		// 知识入Mongo
		KnowledgeNews knowledge = knowledgeNewsDAO.insertknowledge(kId, vo,
				columnPath, userId, username);

		if (knowledge == null) {
			logger.info("创建知识失败,知识ID:{}", kId);
			result.put(Constants.status, Constants.ResultType.fail.v());
			result.put(Constants.errormessage,
					Constants.ErrorMessage.addKnowledgeFail.c());
			return result;
		}
		// 添加知识到权限表

		if (StringUtils.isNotBlank(vo.getSelectedIds())) {

			// 获取知识权限,大乐（2）：用户ID1，用户ID2...&中乐（3）：用户ID1，用户ID2...&小乐（4）：用户ID1，用户ID2...
			List<String> permList = KnowledgeUtil.getPermissionList(vo
					.getSelectedIds());

			userPermissionService.insertUserPermission(permList,
					knowledge.getId(), userId, Constants.Type.News.v(),
					vo.getShareMessage(), (short) Constants.Type.News.v(),
					vo.getColumnid());
		}
		// 添加知识到知识目录表
		long[] cIds = KnowledgeUtil.formatString(vo.getCatalogueIds(), 1, vo
				.getCatalogueIds().length() - 1);
		int categoryV = knowledgeCategoryService.insertKnowledgeRCategory(
				knowledge.getId(), cIds, userId, username, columnPath, vo);
		if (categoryV == 0) {
			logger.error("创建知识未全部完成,添加知识到知识目录信息失败，知识ID:{},目录ID:{}", kId, cIds);
			result.put(Constants.status, Constants.ResultType.fail.v());
			result.put(Constants.errormessage,
					Constants.ErrorMessage.addKnowledgeFail.c());
			return result;
		}
		// 添加知识到栏目知识表
		int columnV = columnKnowledgeService.insertColumnKnowledge(
				vo.getColumnid(), knowledge.getId(), userId,
				Constants.Type.News.v());

		if (columnV == 0) {
			logger.error("创建知识未全部完成,添加知识到栏目知识信息失败，知识ID:{},栏目ID:{}", kId,
					vo.getColumnid());
			result.put(Constants.status, Constants.ResultType.fail.v());
			result.put(Constants.errormessage,
					Constants.ErrorMessage.addKnowledgeFail.c());
			return result;
		}

		result.put(Constants.status, Constants.ResultType.success.v());
		logger.info("创建知识成功,知识ID:{}", kId);
		return result;
	}

	@Override
	public void restoreKnowledgeByid(long knowledgeid) {
		knowledgeNewsDAO.restoreKnowledgeByid(knowledgeid);

	}

	@Override
	public void deleteforeverKnowledge(long knowledgeid) {

		Criteria criteria = Criteria.where("_id").is(knowledgeid);
		Query query = new Query(criteria);
		KnowledgeNews kdnews = mongoTemplate.findOne(query,
				KnowledgeNews.class, "KnowledgeNews");
		if (kdnews != null) {
			Update update = new Update();
			update.set("status", Constants.Status.foreverdelete.v());
			mongoTemplate.updateFirst(query, update, "KnowledgeNews");
		}

	}
}
