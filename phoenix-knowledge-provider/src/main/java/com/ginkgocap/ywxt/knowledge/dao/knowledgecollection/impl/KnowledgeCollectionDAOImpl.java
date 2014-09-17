package com.ginkgocap.ywxt.knowledge.dao.knowledgecollection.impl;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

import com.ginkgocap.ywxt.knowledge.dao.knowledgecollection.KnowledgeCollectionDAO;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeCollection;
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

	@Override
	public KnowledgeCollection insertKnowledgeCollection(
			KnowledgeCollection knowledgeCollection) {
		try {
			Long id = (Long) getSqlMapClientTemplate().insert(
					"tb_knowledge_collection.insert", knowledgeCollection);
			if (id != null) {
				knowledgeCollection.setId(id);
				return knowledgeCollection;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
