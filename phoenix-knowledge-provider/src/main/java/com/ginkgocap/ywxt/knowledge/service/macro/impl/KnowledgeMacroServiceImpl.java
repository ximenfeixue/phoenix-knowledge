package com.ginkgocap.ywxt.knowledge.service.macro.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.dao.knowledge.KnowledgeDao;
import com.ginkgocap.ywxt.knowledge.dao.knowledgecategory.KnowledgeCategoryDAO;
import com.ginkgocap.ywxt.knowledge.dao.macro.KnowledgeMacroDAO;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeMacro;
import com.ginkgocap.ywxt.knowledge.service.macro.KnowledgeMacroService;
import com.ibatis.sqlmap.client.SqlMapClient;

@Service("knowledgeMacroService")
public class KnowledgeMacroServiceImpl implements KnowledgeMacroService {

	@Autowired
	private KnowledgeDao knowledgeDao;

	@Autowired
	private KnowledgeMacroDAO knowledgeMacroDAO;

	@Autowired
	private KnowledgeCategoryDAO knowledgeBetweenDAO;

	@Autowired
	SqlMapClient sqlMapClient;

	@Override
	public KnowledgeMacro insertknowledge(KnowledgeMacro knowledge) {

		KnowledgeMacro knowledgeNews = knowledgeMacroDAO
				.insertknowledge(knowledge);
		if (knowledgeNews == null) {

			return null;
		} else {
			return knowledgeNews;
		}

	}

	@Override
	public void deleteKnowledge(long[] ids) {

		knowledgeMacroDAO.deleteKnowledge(ids);
	}

	@Override
	public void updateKnowledge(KnowledgeMacro knowledge) {

		int count = 0;
		knowledgeMacroDAO.updateKnowledge(knowledge);
		if (count > 0) {
			// if (categoryids != null && categoryids.length > 0) {
			//
			// count = knowledgeDao.deleteKnowledgeRCategory(
			// knowledge.getId(), categoryid);
			// if (count > 0) {
			// knowledgeBetweenDAO.insertKnowledgeRCategory(knowledge,
			// categoryids);
			// return 1;
			// }
			// }
			// return 1;
		}
	}

	@Override
	public KnowledgeMacro selectKnowledge(long knowledgeid) {

		return knowledgeMacroDAO.selectKnowledge(knowledgeid);
	}

}
