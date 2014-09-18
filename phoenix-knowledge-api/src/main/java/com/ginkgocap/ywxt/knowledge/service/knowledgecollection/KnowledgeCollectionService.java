package com.ginkgocap.ywxt.knowledge.service.knowledgecollection;

import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCollection;

/**
 * 知识收藏
 * 
 * @author caihe
 * 
 */
public interface KnowledgeCollectionService {

	/**
	 * 将知识添加到知识收藏表中
	 * 
	 * @param knowledgeRCategory
	 * @return
	 */
	int insertKnowledgeCollection(KnowledgeCollection knowledgeCollection);

}
