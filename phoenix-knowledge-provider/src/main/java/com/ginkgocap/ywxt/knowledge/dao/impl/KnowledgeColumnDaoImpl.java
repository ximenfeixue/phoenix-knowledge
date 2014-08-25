package com.ginkgocap.ywxt.knowledge.dao.impl;


import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

import com.ginkgocap.ywxt.knowledge.dao.KnowledgeColumnDao;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeColumn;
import com.ibatis.sqlmap.client.SqlMapClient;

@Component("knowledgeColumnDao")
public class KnowledgeColumnDaoImpl implements KnowledgeColumnDao {

	@Override
	public KnowledgeColumn insert(KnowledgeColumn kc) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public KnowledgeColumn update(KnowledgeColumn kc) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public KnowledgeColumn queryById(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long countByPidAndName(int parentColumnId, String columnName) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void delById(long id) {
		// TODO Auto-generated method stub
		
	}

}
