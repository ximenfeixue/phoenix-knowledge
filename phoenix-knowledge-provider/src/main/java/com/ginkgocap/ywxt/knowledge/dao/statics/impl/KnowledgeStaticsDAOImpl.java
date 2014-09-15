package com.ginkgocap.ywxt.knowledge.dao.statics.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

import com.ginkgocap.ywxt.knowledge.dao.category.CategoryDao;
import com.ginkgocap.ywxt.knowledge.dao.content.KnowledgeContentDAO;
import com.ginkgocap.ywxt.knowledge.dao.statics.KnowledgeStaticsDAO;
import com.ginkgocap.ywxt.knowledge.model.Category;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeRCategory;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeStatics;
import com.ginkgocap.ywxt.knowledge.service.category.impl.CategoryHelper;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * @author caihe
 * 
 */
@Component("knowledgeStaticsDAO")
public class KnowledgeStaticsDAOImpl extends SqlMapClientDaoSupport implements
		KnowledgeStaticsDAO {

	@Autowired
	SqlMapClient sqlMapClient;

	@PostConstruct
	public void initSqlMapClient() {
		super.setSqlMapClient(sqlMapClient);
	}

	@Override
	public void insertKnowledgeStatics(KnowledgeStatics knowledgeStatics) {
		
		getSqlMapClientTemplate().insert("tb_knowledge_statics.insert",
				knowledgeStatics);

	}

}
