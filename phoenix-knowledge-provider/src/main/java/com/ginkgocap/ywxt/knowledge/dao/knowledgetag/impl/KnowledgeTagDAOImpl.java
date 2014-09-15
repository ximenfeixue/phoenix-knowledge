package com.ginkgocap.ywxt.knowledge.dao.knowledgetag.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

import com.ginkgocap.ywxt.knowledge.dao.category.CategoryDao;
import com.ginkgocap.ywxt.knowledge.dao.content.KnowledgeContentDAO;
import com.ginkgocap.ywxt.knowledge.dao.knowledtag.KnowledgeTagDAO;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeTag;
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

	@Autowired
	private CategoryDao categoryDao;

	@Autowired
	private KnowledgeContentDAO knowledgeContentDAO;

	@Override
	public void insertKnowledgeTag(KnowledgeTag knowledge) {

		getSqlMapClientTemplate().insert("tb_knowledge_tag.insert",
				knowledge);

	}

	@Override
	public int deleteKnowledgeRCategory(long[] knowledgeids, long categoryid) {

		Map<String, Object> map = new HashMap<String, Object>();
		if (knowledgeids.length > 0) {
			map.put("knowledgeids", knowledgeids);
		}
		if (categoryid > 0) {
			map.put("categoryid", categoryid);
		}
		int count = getSqlMapClientTemplate().delete(
				"tb_knowledge_category.delete", map);
		return count;
	}

}
