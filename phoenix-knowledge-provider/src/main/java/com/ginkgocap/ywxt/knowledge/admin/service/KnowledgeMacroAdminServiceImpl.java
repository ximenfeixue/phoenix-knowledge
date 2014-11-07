package com.ginkgocap.ywxt.knowledge.admin.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.admin.dao.KnowledgeMacroAdminDAO;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeMacro;
import com.ginkgocap.ywxt.user.form.DataGridModel;


@Service("knowledgeMacroAdminService")
public class KnowledgeMacroAdminServiceImpl implements KnowledgeMacroAdminService {

	@Autowired
	private KnowledgeMacroAdminDAO knowledgeMacroAdminDAO;
	
	@Override
	public List<KnowledgeMacro> selectAll() {
		List<KnowledgeMacro> list = knowledgeMacroAdminDAO.findAll();
		return list;
	}

	@Override
	public Map<String, Object> selectByParam(DataGridModel dgm,
			Map<String, String> map) {
		
		Map<String ,Object> result =new HashMap<String ,Object> () ;
		Integer currentPage = dgm.getPage();
		Integer size = dgm.getRows();
		result = knowledgeMacroAdminDAO.selectKnowledgeMacroList(currentPage, size, map);
//		long total = knowledgeMacroAdminDAO.selectKnowledgeMacroListCount();
//		result.put("total", total);
//		result.put("rows", l);
		return result;
	}

	@Override
	public KnowledgeMacro selectKnowledgeMacroById(long id) {
		KnowledgeMacro news = knowledgeMacroAdminDAO.selectKnowledgeMacroById(id);
		return news;
	}

	@Override
	public void deleteKnowledgeMacroById(long id) {
		knowledgeMacroAdminDAO.deleteKnowledgeMacroById(id);
	}

	@Override
	public void checkKnowledgeMacroById(long id, int status) {
		knowledgeMacroAdminDAO.checkStatusById(id, status);
	}
	
	
}
