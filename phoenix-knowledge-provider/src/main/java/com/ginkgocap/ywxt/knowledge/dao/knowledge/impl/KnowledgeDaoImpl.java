package com.ginkgocap.ywxt.knowledge.dao.knowledge.impl;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

import com.ginkgocap.ywxt.knowledge.dao.knowledge.KnowledgeDao;
import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * @author zhangwei
 *
 */
@Component("knowledgeDao")
public class KnowledgeDaoImpl extends SqlMapClientDaoSupport implements KnowledgeDao {
	
	@Autowired
	SqlMapClient sqlMapClient;

	@PostConstruct
	public void initSqlMapClient() {
		super.setSqlMapClient(sqlMapClient);
	}
	
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(Knowledge record) {
		long r=record.getId();
		Long i=0l;
		if(r>0){
			return updateByPrimaryKey(record);
		}else{
			i = (Long) getSqlMapClientTemplate().insert("knowledge.insert", record);
		}
		return i.intValue();
	}

	@Override
	public int insertSelective(Knowledge record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Knowledge selectByPrimaryKey(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateByPrimaryKeySelective(Knowledge record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateByPrimaryKey(Knowledge record) {
		// TODO Auto-generated method stub
		return 0;
	}

	

}
