package com.ginkgocap.ywxt.knowledge.service.knowledgestatics.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.dao.statics.KnowledgeStaticsDAO;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeStatics;
import com.ginkgocap.ywxt.knowledge.service.knowledgestatics.KnowledgeStaticsService;

@Service("knowledgeStaticsService")
public class KnowledgeStaticsServiceImpl implements KnowledgeStaticsService {

	@Autowired
	private KnowledgeStaticsDAO knowledgeStaticsDAO;

	@Override
	public int insertKnowledgeStatics(KnowledgeStatics knowledgeStatics) {
		return knowledgeStaticsDAO.insertKnowledgeStatics(knowledgeStatics);
	}

	@Override
	public KnowledgeStatics selectByknowledgeId(long knowledgeid) {

		return knowledgeStaticsDAO.selectByknowledgeId(knowledgeid);
	}

}
