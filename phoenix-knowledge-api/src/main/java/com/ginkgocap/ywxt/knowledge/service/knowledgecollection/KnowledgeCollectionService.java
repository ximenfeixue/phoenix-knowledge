package com.ginkgocap.ywxt.knowledge.service.knowledgecollection;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeCollection;

/**
 * 知识相关的关系表
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
	KnowledgeCollection insertKnowledgeCollection(KnowledgeCollection knowledgeCollection);

	/**
	 * 刪除知识，把知识目录中间表删除
	 * 
	 * @param knowledgeids
	 * @param categoryid
	 * @return
	 */
	int deleteKnowledgeRCategory(long[] knowledgeids, long categoryid);
}
