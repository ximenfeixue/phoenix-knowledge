package com.ginkgocap.ywxt.knowledge.admin.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.admin.dao.KnowledgeInvestmentAdminDAO;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeInvestment;
import com.ginkgocap.ywxt.user.form.DataGridModel;


@Service("knowledgeInvestmentAdminService")
public class KnowledgeInvestmentAdminServiceImpl implements KnowledgeInvestmentAdminService {

	@Autowired
	private KnowledgeInvestmentAdminDAO knowledgeInvestmentAdminDAO;

	@Override
	public List<KnowledgeInvestment> selectAll() {
		List<KnowledgeInvestment> list = knowledgeInvestmentAdminDAO.findAll();
		return list;
	}

	@Override
	public Map<String, Object> selectByParam(DataGridModel dgm,
			Map<String, String> map) {
		
		Map<String ,Object> result =new HashMap<String ,Object> () ;
		Integer currentPage = dgm.getPage();
		Integer size = dgm.getRows();
		result = knowledgeInvestmentAdminDAO.selectKnowledgeInvestmentList(currentPage, size, map);
//		long total = knowledgeInvestmentAdminDAO.selectKnowledgeInvestmentListCount();
//		result.put("total", total);
//		result.put("rows", l);
		return result;
	}

	@Override
	public KnowledgeInvestment selectKnowledgeInvestmentById(long id) {
		KnowledgeInvestment news = knowledgeInvestmentAdminDAO.selectKnowledgeInvestmentById(id);
		return news;
	}

	@Override
	public void deleteKnowledgeInvestmentById(long id) {
		knowledgeInvestmentAdminDAO.deleteKnowledgeInvestmentById(id);
	}

	@Override
	public void checkKnowledgeInvestmentById(long id, int status) {
		knowledgeInvestmentAdminDAO.checkStatusById(id, status);
	}
	
	
}
