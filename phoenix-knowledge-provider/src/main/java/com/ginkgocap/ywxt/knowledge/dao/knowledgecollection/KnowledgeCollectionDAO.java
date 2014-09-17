package com.ginkgocap.ywxt.knowledge.dao.knowledgecollection;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeCollection;

public interface KnowledgeCollectionDAO {
	/**
	 * 将知识添加到知识收藏表中
	 * 
	 * @param knowledgeRCategory
	 * @return
	 */
	KnowledgeCollection insertKnowledgeCollection(KnowledgeCollection knowledgeCollection);
}
