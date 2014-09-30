package com.ginkgocap.ywxt.knowledge.dao.columnknowledge.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.ginkgocap.ywxt.knowledge.dao.columnknowledge.ColumnKnowledgeDAO;
import com.ginkgocap.ywxt.knowledge.entity.ColumnKnowledge;
import com.ginkgocap.ywxt.knowledge.entity.ColumnKnowledgeExample;
import com.ginkgocap.ywxt.knowledge.entity.ColumnKnowledgeExample.Criteria;
import com.ginkgocap.ywxt.knowledge.mapper.ColumnKnowledgeMapper;
import com.ginkgocap.ywxt.knowledge.mapper.ColumnKnowledgeValueMapper;

/**
 * @author zhangwei
 * 
 */
@Component("columnknowledgeDAO")
public class ColumnKnowledgeDAOImpl implements ColumnKnowledgeDAO {

	@Resource
	private ColumnKnowledgeMapper columnKnowledgeMapper;

	@Resource
	private ColumnKnowledgeValueMapper columnKnowledgeValueMapper;

	@Override
	public int insertColumnKnowledge(long column_id, long knowledge_id,
			long user_id, int type) {

		ColumnKnowledge columnKnowledge = new ColumnKnowledge();
		columnKnowledge.setColumnId(column_id);
		columnKnowledge.setKnowledgeId(knowledge_id);
		columnKnowledge.setUserId(user_id);
		columnKnowledge.setType((short) type);
		return columnKnowledgeMapper.insertSelective(columnKnowledge);

	}

	@Override
	public int deleteColumnKnowledge(long[] knowledgeids, long columnid) {

		return columnKnowledgeValueMapper.deleteKnowledge(knowledgeids,
				columnid);
	}

	@Override
	public int updateColumnKnowledge(long column_id, long knowledge_id,
			long user_id, int type) {
		ColumnKnowledge columnKnowledge = new ColumnKnowledge();

		columnKnowledge.setColumnId(column_id);
		columnKnowledge.setType((short) type);
		
		ColumnKnowledgeExample example = new ColumnKnowledgeExample();
		Criteria criteria = example.createCriteria();
		criteria.andKnowledgeIdEqualTo(knowledge_id);
		return columnKnowledgeMapper.updateByExample(columnKnowledge, example);
	}

}
