package com.ginkgocap.ywxt.knowledge.admin.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.admin.dao.KnowledgeIndustryAdminDAO;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeIndustry;
import com.ginkgocap.ywxt.user.form.DataGridModel;


@Service("knowledgeIndustryAdminService")
public class KnowledgeIndustryAdminServiceImpl implements KnowledgeIndustryAdminService {

	@Autowired
	private KnowledgeIndustryAdminDAO knowledgeIndustryAdminDAO;
	
	@Override
	public List<KnowledgeIndustry> selectAll() {
		List<KnowledgeIndustry> list = knowledgeIndustryAdminDAO.findAll();
		return list;
	}

	@Override
	public Map<String, Object> selectByParam(DataGridModel dgm,
			Map<String, String> map) {
		
		Map<String ,Object> result =new HashMap<String ,Object> () ;
		Integer currentPage = dgm.getPage();
		Integer size = dgm.getRows();
		result = knowledgeIndustryAdminDAO.selectKnowledgeIndustryList(currentPage, size, map);
//		long total = knowledgeIndustryAdminDAO.selectKnowledgeIndustryListCount();
//		result.put("total", total);
//		result.put("rows", l);
		return result;
	}

	@Override
	public KnowledgeIndustry selectKnowledgeIndustryById(long id) {
		KnowledgeIndustry news = knowledgeIndustryAdminDAO.selectKnowledgeIndustryById(id);
		return news;
	}

	@Override
	public void deleteKnowledgeIndustryById(long id) {
		knowledgeIndustryAdminDAO.deleteKnowledgeIndustryById(id);
	}

	@Override
	public void checkKnowledgeIndustryById(long id, int status) {
		knowledgeIndustryAdminDAO.checkStatusById(id, status);
	}
	
	
}
