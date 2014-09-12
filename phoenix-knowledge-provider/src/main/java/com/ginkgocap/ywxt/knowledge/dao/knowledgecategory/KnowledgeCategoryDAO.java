package com.ginkgocap.ywxt.knowledge.dao.knowledgecategory;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeNews;

public interface KnowledgeCategoryDAO {
	/**
	 * 新增知识，把知识ID，目录ID，存入到知识目录中间表中
	 * 
	 * @param knowledgeRCategory
	 * @return
	 */
	void insertKnowledgeRCategory(KnowledgeNews knowledge, long categoryid[]);
	
	int deleteKnowledgeRCategory(long[] knowledgeids, long categoryid);
}
