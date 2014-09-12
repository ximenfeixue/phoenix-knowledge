package com.ginkgocap.ywxt.knowledge.dao.columnknowledge.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

import com.ginkgocap.ywxt.knowledge.dao.category.CategoryDao;
import com.ginkgocap.ywxt.knowledge.dao.columnknowledge.ColumnKnowledgeDAO;
import com.ginkgocap.ywxt.knowledge.dao.content.KnowledgeContentDAO;
import com.ginkgocap.ywxt.knowledge.model.Category;
import com.ginkgocap.ywxt.knowledge.model.ColumnKnowledge;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeRCategory;
import com.ginkgocap.ywxt.knowledge.service.category.impl.CategoryHelper;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * @author zhangwei
 * 
 */
@Component("columnknowledgeDAO")
public class ColumnKnowledgeDAOImpl extends SqlMapClientDaoSupport implements
		ColumnKnowledgeDAO {

	@Autowired
	SqlMapClient sqlMapClient;

	@PostConstruct
	public void initSqlMapClient() {
		super.setSqlMapClient(sqlMapClient);
	}

	@Override
	public void insertColumnKnowledge(ColumnKnowledge columnKnowledge) {

		getSqlMapClientTemplate().insert("tb_column_knowledge.insert",
				columnKnowledge);

	}

	@Override
	public int deleteColumnKnowledge(long[] knowledgeids, long columnid) {

		Map<String, Object> map = new HashMap<String, Object>();
		if (knowledgeids.length > 0) {
			map.put("knowledgeids", knowledgeids);
		}
		if (columnid > 0) {
			map.put("column_id", columnid);
		}
		int count = getSqlMapClientTemplate().delete(
				"tb_column_knowledge.delete", map);
		return count;
	}

}
