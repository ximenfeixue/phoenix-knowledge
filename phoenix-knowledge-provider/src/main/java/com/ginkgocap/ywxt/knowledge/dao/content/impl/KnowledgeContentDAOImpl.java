package com.ginkgocap.ywxt.knowledge.dao.content.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

import com.ginkgocap.ywxt.knowledge.dao.content.KnowledgeContentDAO;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeContent;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * 内容的DAO接口
 * 
 * @author caihe
 * @创建时间：2014-08-27 16:11
 */

@Component("knowledgeContentDAO")
public class KnowledgeContentDAOImpl extends SqlMapClientDaoSupport implements
		KnowledgeContentDAO {

	@Autowired
	SqlMapClient sqlMapClient;

	@PostConstruct
	public void initSqlMapClient() {
		super.setSqlMapClient(sqlMapClient);
	}

	@Override
	public KnowledgeContent insert(KnowledgeContent knowledgeContent) {

		Long id = (Long) getSqlMapClientTemplate().insert(
				"tb_knowledge_content.insert", knowledgeContent);
		System.out.println(id);
		if (id != null) {
			knowledgeContent.setId(id);
			return knowledgeContent;
		} else {

			return null;
		}
	}

	@Override
	public List<KnowledgeContent> selectByknowledgeId(long knowledgeId) {

		List<KnowledgeContent> list = getSqlMapClientTemplate().queryForList(
				"tb_knowledge_content.insert", knowledgeId);
		if (list != null && list.size() > 0) {
			return list;
		} else {

			return null;
		}
	}

	@Override
	public int update(KnowledgeContent knowledgeContent) {

		int count = getSqlMapClientTemplate().update(
				"tb_knowledge_content.update", knowledgeContent);
		if (count > 0) {

			return 1;
		} else {

			return 0;
		}
	}

}
