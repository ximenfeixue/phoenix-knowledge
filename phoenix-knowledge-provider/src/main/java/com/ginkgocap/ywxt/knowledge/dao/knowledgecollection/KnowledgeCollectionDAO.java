package com.ginkgocap.ywxt.knowledge.dao.knowledgecollection;

import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCollection;

public interface KnowledgeCollectionDAO {
	/**
	 * 将知识添加到知识收藏表中
	 * 
	 * @param knowledgeRCategory
	 * @return
	 */
	int insertKnowledgeCollection(KnowledgeCollection knowledgeCollection);

	/**
	 * 将知识从收藏夹中删除
	 * 
	 * @param knowledgeRCategory
	 * @return
	 */
	int deleteKnowledgeCollection(long[] knowledgeids, long categoryid);
}
