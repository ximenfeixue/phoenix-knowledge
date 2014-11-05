package com.ginkgocap.ywxt.knowledge.admin.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.admin.dao.KnowledgeNewsAdminDAO;
import com.ginkgocap.ywxt.knowledge.dao.knowledge.KnowledgeDao;
import com.ginkgocap.ywxt.knowledge.dao.knowledgecategory.KnowledgeCategoryDAO;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeBaseMapper;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeStaticsMapper;
import com.ginkgocap.ywxt.knowledge.mapper.UserCategoryMapper;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNews;
import com.ginkgocap.ywxt.knowledge.service.ColumnKnowledgeService;
import com.ginkgocap.ywxt.knowledge.service.ColumnService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeCategoryService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeDraftService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeMongoIncService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeRecycleService;
import com.ginkgocap.ywxt.knowledge.service.UserCategoryService;
import com.ginkgocap.ywxt.knowledge.service.UserPermissionService;
import com.ginkgocap.ywxt.user.form.DataGridModel;


@Service("knowledgeNewsAdminService")
public class KnowledgeNewsAdminServiceImpl implements KnowledgeNewsAdminService {

	private final static String dule = "1";

	private static final Logger logger = LoggerFactory
			.getLogger(KnowledgeNewsAdminServiceImpl.class);

	@Autowired
	private KnowledgeDao knowledgeDao;

	@Autowired
	private KnowledgeNewsAdminDAO knowledgeNewsAdminDAO;

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
	public List<KnowledgeNews> selectAll() {
		List<KnowledgeNews> list = knowledgeNewsAdminDAO.findAll();
		return list;
	}

	@Override
	public Map<String, Object> selectAll(DataGridModel dgm,
			Map<String, Object> map) {
		
		Map<String ,Object> result =new HashMap<String ,Object> () ;
		Integer start = (dgm.getPage() - 1) * dgm.getRows();
		Integer size = dgm.getRows();
		List<KnowledgeNews> l = knowledgeNewsAdminDAO.selectKnowledgeNewsList(start, size);
		long total = knowledgeNewsAdminDAO.selectKnowledgeNewsListCount();
		result.put("total", total);
		result.put("rows", l);
		return result;
	}

	@Override
	public KnowledgeNews selectKnowledgeNewsById(long id) {
		KnowledgeNews news = knowledgeNewsAdminDAO.selectKnowledgeNewsById(id);
		return news;
	}

	@Override
	public void deleteKnowledgeNewsById(long id) {
		knowledgeNewsAdminDAO.deleteKnowledgeNewsById(id);
	}

	@Override
	public void checkKnowledgeNewsById(long id, int status) {
		knowledgeNewsAdminDAO.checkStatusById(id, status);
	}
	
	
}
