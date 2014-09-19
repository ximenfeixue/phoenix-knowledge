package com.ginkgocap.ywxt.knowledge.service.knowledge.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.dao.statics.KnowledgeStaticsDAO;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeStatics;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeStaticsMapper;
import com.ginkgocap.ywxt.knowledge.service.knowledge.KnowledgeStaticsService;

@Service("knowledgeStaticsService")
public class KnowledgeStaticsServiceImpl implements KnowledgeStaticsService {

	@Autowired
	private KnowledgeStaticsDAO knowledgeStaticsDAO;
	
	@Autowired
	private KnowledgeStaticsMapper knowledgeStaticsMapper;

	@Override
	public int insertKnowledgeStatics(KnowledgeStatics knowledgeStatics) {
		return knowledgeStaticsDAO.insertKnowledgeStatics(knowledgeStatics);
	}

	@Override
	public KnowledgeStatics selectByknowledgeId(long knowledgeid) {
		return knowledgeStaticsMapper.selectByPrimaryKey(knowledgeid);
	}

}
