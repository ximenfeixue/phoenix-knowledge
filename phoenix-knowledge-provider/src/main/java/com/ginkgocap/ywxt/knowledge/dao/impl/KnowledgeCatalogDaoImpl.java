package com.ginkgocap.ywxt.knowledge.dao.impl;


import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

import com.ginkgocap.ywxt.knowledge.dao.KnowledgeCatalogDao;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeCatalog;
import com.ibatis.sqlmap.client.SqlMapClient;

@Component("knowledgeCatalogDao")
public class KnowledgeCatalogDaoImpl implements KnowledgeCatalogDao {

	@Override
	public KnowledgeCatalog insert(KnowledgeCatalog kc) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public KnowledgeCatalog update(KnowledgeCatalog kc) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public KnowledgeCatalog queryById(long id) {
		// TODO Auto-generated method stub
		return null;
	}
//    @Autowired
//    SqlMapClient sqlMapClient;
//
//    @PostConstruct
//    public void initSqlMapClient() {
//        super.setSqlMapClient(sqlMapClient);
//    }
//    
//    @Override
//    public KnowledgeCatalog insert(KnowledgeCatalog kc) {
//        Long id = (Long) getSqlMapClientTemplate().insert("tb_knowledge_catalog.insert", kc);
//        if (id != null) {
//            kc.setId(id);
//            return kc;
//        } else {
//            return null;
//        }
//    }
//
//    @Override
//    public KnowledgeCatalog update(KnowledgeCatalog kc) {
//        getSqlMapClientTemplate().update("tb_knowledge_catalog.update", kc);
//        return kc;
//    }
//
//    @Override
//    public KnowledgeCatalog queryById(long id) {
//        KnowledgeCatalog kl = (KnowledgeCatalog) getSqlMapClientTemplate().queryForObject("tb_knowledge_catalog.selectById", id);
//        return kl;
//    }

}
