package com.ginkgocap.ywxt.knowledge.dao.knowledgecollection.impl;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

import com.ginkgocap.ywxt.knowledge.dao.knowledgecollection.KnowledgeCollectionDAO;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCollection;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeCollectionMapper;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * @author caihe
 * 
 */
@Component("knowledgeCollectionDAO")
public class KnowledgeCollectionDAOImpl extends SqlMapClientDaoSupport
		implements KnowledgeCollectionDAO {

	@Autowired
	SqlMapClient sqlMapClient;

	@PostConstruct
	public void initSqlMapClient() {
		super.setSqlMapClient(sqlMapClient);
	}

	@Resource
	private KnowledgeCollectionMapper knowledgeCollectionMapper;

	@Override
	public int insertKnowledgeCollection(KnowledgeCollection knowledgeCollection) {
		return knowledgeCollectionMapper.insertSelective(knowledgeCollection);
	}

}
