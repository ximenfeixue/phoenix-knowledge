package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.dao.columnknowledge.ColumnKnowledgeDAO;
import com.ginkgocap.ywxt.knowledge.entity.ColumnKnowledge;
import com.ginkgocap.ywxt.knowledge.entity.ColumnKnowledgeExample;
import com.ginkgocap.ywxt.knowledge.entity.ColumnKnowledgeExample.Criteria;
import com.ginkgocap.ywxt.knowledge.mapper.ColumnKnowledgeMapper;
import com.ginkgocap.ywxt.knowledge.mapper.ColumnKnowledgeValueMapper;
import com.ginkgocap.ywxt.knowledge.service.ColumnKnowledgeService;

@Service("columnknowledgeService")
public class ColumnKnowledgeServiceImpl implements ColumnKnowledgeService {

	@Autowired
	private ColumnKnowledgeDAO columnKnowledgeDAO;

	@Autowired
	private ColumnKnowledgeMapper columnKnowledgeMapper;

	@Autowired
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

	@Override
	public int updateColumn(long knowledge_id, long column_id) {

		ColumnKnowledge columnKnowledge = new ColumnKnowledge();

		columnKnowledge.setColumnId(column_id);

		ColumnKnowledgeExample example = new ColumnKnowledgeExample();
		Criteria criteria = example.createCriteria();
		criteria.andKnowledgeIdEqualTo(knowledge_id);
		return columnKnowledgeMapper.updateByExample(columnKnowledge, example);
	}

	@Override
	public String[] getAllColumnByColumnId(int columnid) {
		
		List<Map<String,Long>> columnIds = columnKnowledgeValueMapper.selectColumnIdTreeById(columnid);
		
		if(null != columnIds && columnIds.size() > 0) {
			
			int size = columnIds.size();
			
			String[] strColumnIds = new String[size];
			
			for (int i = 0; i < size; i++) {
				strColumnIds[i] = columnIds.get(i).get("id")+"";
			}
			
			return strColumnIds;
			
		} else {
			return null;
		}
	}

}
