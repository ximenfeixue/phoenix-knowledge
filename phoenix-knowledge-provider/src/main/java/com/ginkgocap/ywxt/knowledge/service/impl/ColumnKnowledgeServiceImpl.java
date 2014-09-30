package com.ginkgocap.ywxt.knowledge.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.dao.columnknowledge.ColumnKnowledgeDAO;
import com.ginkgocap.ywxt.knowledge.entity.ColumnKnowledge;
import com.ginkgocap.ywxt.knowledge.service.ColumnKnowledgeService;

@Service("columnknowledgeService")
public class ColumnKnowledgeServiceImpl implements ColumnKnowledgeService {

	@Autowired
	private ColumnKnowledgeDAO columnKnowledgeDAO;

	@Override
	public int insertColumnKnowledge(long column_id, long knowledge_id,
			long user_id, int type) {
		return columnKnowledgeDAO.insertColumnKnowledge(column_id,
				knowledge_id, user_id, type);
	}

	@Override
	public int deleteColumnKnowledge(long[] knowledgeids, long columnid) {

		return columnKnowledgeDAO.deleteColumnKnowledge(knowledgeids, columnid);
	}

	@Override
	public int updateColumnKnowledge(long column_id, long knowledge_id,
			long user_id, int type) {

		return columnKnowledgeDAO.updateColumnKnowledge(column_id,
				knowledge_id, user_id, type);
	}

}
