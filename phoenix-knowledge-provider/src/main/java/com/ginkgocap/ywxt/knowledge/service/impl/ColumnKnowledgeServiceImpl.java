package com.ginkgocap.ywxt.knowledge.service.knowledge.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.dao.columnknowledge.ColumnKnowledgeDAO;
import com.ginkgocap.ywxt.knowledge.entity.ColumnKnowledge;
import com.ginkgocap.ywxt.knowledge.service.knowledge.ColumnKnowledgeService;

@Service("columnknowledgeService")
public class ColumnKnowledgeServiceImpl implements ColumnKnowledgeService {

	@Autowired
	private ColumnKnowledgeDAO columnKnowledgeDAO;

	@Override
	public int insertColumnKnowledge(ColumnKnowledge columnKnowledge) {
		return columnKnowledgeDAO.insertColumnKnowledge(columnKnowledge);
	}

	@Override
	public int deleteColumnKnowledge(long[] knowledgeids, long columnid) {

		return columnKnowledgeDAO.deleteColumnKnowledge(knowledgeids, columnid);
	}

}
