package com.ginkgocap.ywxt.knowledge.dao.knowledtag;

import com.ginkgocap.ywxt.knowledge.entity.KnowledgeTag;

public interface KnowledgeTagDAO {
	/**
	 * 新增知识，把知识ID，目录ID，存入到知识标签中间表中
	 * 
	 * @param knowledgeRCategory
	 * @return
	 */
	int insertKnowledgeTag(KnowledgeTag knowledge);

}
