package com.ginkgocap.ywxt.knowledge.admin.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.admin.dao.KnowledgeNewsAdminDAO;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNews;
import com.ginkgocap.ywxt.user.form.DataGridModel;


@Service("knowledgeNewsAdminService")
public class KnowledgeNewsAdminServiceImpl implements KnowledgeNewsAdminService {

	@Autowired
	private KnowledgeNewsAdminDAO knowledgeNewsAdminDAO;

	@Override
	public List<KnowledgeNews> selectAll() {
		List<KnowledgeNews> list = knowledgeNewsAdminDAO.findAll();
		return list;
	}

	@Override
	public Map<String, Object> selectByParam(DataGridModel dgm,
			Map<String, String> map) {
		
		Map<String ,Object> result =new HashMap<String ,Object> () ;
		Integer currentPage = dgm.getPage();
		Integer size = dgm.getRows();
		result = knowledgeNewsAdminDAO.selectKnowledgeNewsList(currentPage, size, map);
//		long total = knowledgeNewsAdminDAO.selectKnowledgeNewsListCount();
//		result.put("total", total);
//		result.put("rows", l);
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
