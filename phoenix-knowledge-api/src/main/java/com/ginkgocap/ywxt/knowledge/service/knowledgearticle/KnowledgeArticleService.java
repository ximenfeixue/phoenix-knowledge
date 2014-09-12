package com.ginkgocap.ywxt.knowledge.service.knowledgearticle;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeArticle;

/**
 * 知识Service
 * 
 * @author caihe
 * 
 */
public interface KnowledgeArticleService {

	/**
	 * 新增知识
	 */

	KnowledgeArticle insertknowledge(KnowledgeArticle knowledge);

	/**
	 * 删除知识
	 */

	void deleteKnowledge(long[] ids);

	/**
	 * 编辑知识
	 */

	void updateKnowledge(KnowledgeArticle knowledge);

	/**
	 * 查询知识
	 */

	KnowledgeArticle selectKnowledge(long knowledgeid);

}
