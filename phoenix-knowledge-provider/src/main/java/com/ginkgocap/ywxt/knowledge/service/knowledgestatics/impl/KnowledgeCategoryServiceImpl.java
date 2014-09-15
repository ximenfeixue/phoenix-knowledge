package com.ginkgocap.ywxt.knowledge.service.knowledgestatics.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.dao.statics.KnowledgeStaticsDAO;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeStatics;
import com.ginkgocap.ywxt.knowledge.service.knowledgestatics.KnowledgeStaticsService;

@Service("knowledgeStaticsService")
public class KnowledgeCategoryServiceImpl implements KnowledgeStaticsService {

	@Autowired
	private KnowledgeStaticsDAO knowledgeStaticsDAO;
	
	@Override
	public void insertKnowledgeStatics(KnowledgeStatics knowledgeStatics) {
		knowledgeStaticsDAO.insertKnowledgeStatics(knowledgeStatics);
	}

}
