package com.ginkgocap.ywxt.knowledge.dao.knowledgetag.impl;

import java.util.HashMap; 
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

import com.ginkgocap.ywxt.knowledge.dao.category.CategoryDao;
import com.ginkgocap.ywxt.knowledge.dao.content.KnowledgeContentDAO;
import com.ginkgocap.ywxt.knowledge.dao.knowledtag.KnowledgeTagDAO;

import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * @author caihe
 * 
 */
@Component("knowledgeTagDAO")
public class KnowledgeTagDAOImpl extends SqlMapClientDaoSupport implements
		KnowledgeTagDAO {

	@Autowired
	SqlMapClient sqlMapClient;

	@PostConstruct
	public void initSqlMapClient() {
		super.setSqlMapClient(sqlMapClient);
	}
//
//	@Autowired
//	private CategoryDao categoryDao;
//
//	@Autowired
//	private KnowledgeContentDAO knowledgeContentDAO;
//
//	@Resource
//	private KnowledgeTagMapper knowledgeTagMapper;
//
//	@Override
//	public int insertKnowledgeTag(KnowledgeTag knowledge) {
//
//		return knowledgeTagMapper.insertSelective(knowledge);
//
//	}

}
