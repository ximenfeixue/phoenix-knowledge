package com.ginkgocap.ywxt.knowledge.dao.knowledge.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
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
public class KnowledgeDaoImpl extends SqlMapClientDaoSupport implements
		KnowledgeDao {
	
	@Autowired
	SqlMapClient sqlMapClient;

	@PostConstruct
	public void initSqlMapClient() {
		super.setSqlMapClient(sqlMapClient);
	}
	
	@Override
	public int insert(Knowledge record) {
		long r=record.getId();
		Long i=0l;
		if(r>0){
			return updateByPrimaryKey(record);
		}else{
			i = (Long) getSqlMapClientTemplate().insert("tb_knowledge.insert", record);
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
	public int deleteByPrimaryKey(Long id) {
		// TODO Auto-generated method stub
		return 0;
	}



	@Override
	public int updateByPrimaryKey(Knowledge record) {
		int i =  getSqlMapClientTemplate().update("knowledge.updateByPrimaryKey",record);
		return i;
	}

	@Override
	public int checkNameRepeat(int knowledgetype, String knowledgetitle) {

		Map<String, Object> map = new HashMap<String, Object>();
		if (knowledgetype != 0) {
			map.put("knowledgetype", knowledgetype);
		}
		if (StringUtils.isNotBlank(knowledgetitle)) {
			map.put("knowledgetitle", knowledgetitle);
		}
		List<Knowledge> list = (List<Knowledge>) getSqlMapClientTemplate()
				.queryForList("tb_knowledge.selectByTitle", map);
		if (list != null && list.size() > 0) {

			return 0;// 名称已存在
		} else {
			return 1;// 名称可以使用
		}
	}
}
