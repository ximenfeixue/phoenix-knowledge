package com.ginkgocap.ywxt.knowledge.dao.statics.impl;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

import com.ginkgocap.ywxt.knowledge.dao.statics.KnowledgeStaticsDAO;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeStatics;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeStaticsMapper;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * @author caihe
 * 
 */
@Component("knowledgeStaticsDAO")
public class KnowledgeStaticsDAOImpl implements KnowledgeStaticsDAO {

	@Resource
	private KnowledgeStaticsMapper knowledgeStaticsMapper;

	@Override
	public int insertKnowledgeStatics(KnowledgeStatics knowledgeStatics) {

		return knowledgeStaticsMapper.insertSelective(knowledgeStatics);

	}

	@Override
	public KnowledgeStatics selectByknowledgeId(long knowledgeid) {

		return knowledgeStaticsMapper.selectByPrimaryKey(knowledgeid);
	}

}
