package com.ginkgocap.ywxt.knowledge.service.knowledgecategory.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.dao.knowledgecategory.KnowledgeCategoryDAO;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNews;
import com.ginkgocap.ywxt.knowledge.service.knowledgecategory.KnowledgeCategoryService;

@Service("knowledgeCategoryService")
public class KnowledgeCategoryServiceImpl implements KnowledgeCategoryService {

	@Autowired
	private KnowledgeCategoryDAO knowledgeBetweenDAO;

	@Override
	public void insertKnowledgeRCategory(KnowledgeNews knowledge,
			long[] categoryid) {
		knowledgeBetweenDAO.insertKnowledgeRCategory(knowledge, categoryid);
	}

	@Override
	public int deleteKnowledgeRCategory(long[] knowledgeids, long categoryid) {

		return knowledgeBetweenDAO.deleteKnowledgeRCategory(knowledgeids,
				categoryid);
	}

}
