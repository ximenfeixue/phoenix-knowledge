package com.ginkgocap.ywxt.knowledge.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.entity.ColumnKnowledge;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCategory;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCategoryExample;
import com.ginkgocap.ywxt.knowledge.mapper.ColumnKnowledgeMapper;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeCategoryMapper;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeCommonService;

@Service("knowledgeCommonService")
public class KnowledgeCommonServiceImpl implements KnowledgeCommonService {

	@Resource
	private ColumnKnowledgeMapper columnKnowledgeMapper;

	@Resource
	private KnowledgeCategoryMapper knowledgeCategoryMapper;

	@Override
	public ColumnKnowledge getKnowledgeColumn(long kid) {
		return columnKnowledgeMapper.selectByPrimaryKey(kid);
	}

	@Override
	public KnowledgeCategory getKnowledgeCategory(long kid) {
		return null;
	}

}
