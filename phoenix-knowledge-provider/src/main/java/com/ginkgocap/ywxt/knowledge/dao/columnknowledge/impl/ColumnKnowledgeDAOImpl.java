package com.ginkgocap.ywxt.knowledge.dao.columnknowledge.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

import com.ginkgocap.ywxt.knowledge.dao.columnknowledge.ColumnKnowledgeDAO;
import com.ginkgocap.ywxt.knowledge.entity.ColumnKnowledge;
import com.ginkgocap.ywxt.knowledge.mapper.ColumnKnowledgeMapper;
import com.ginkgocap.ywxt.knowledge.mapper.ColumnKnowledgeValueMapper;
import com.ibatis.sqlmap.client.SqlMapClient;

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
	public int insertColumnKnowledge(ColumnKnowledge columnKnowledge) {

		return columnKnowledgeMapper.insertSelective(columnKnowledge);

	}

	@Override
	public int deleteColumnKnowledge(long[] knowledgeids, long columnid) {

		return columnKnowledgeValueMapper.deleteKnowledge(knowledgeids,
				columnid);
	}

}
