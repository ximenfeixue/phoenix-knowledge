package com.ginkgocap.ywxt.knowledge.dao.knowledgearticle;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeArticle;

/**
 * 内容的DAO接口
 * 
 * @author caihe
 * @创建时间：2014-08-27 16:11
 */
public interface KnowledgeArticleDAO {

	/**
	 * 新增知识
	 */

	KnowledgeArticle insertknowledge(KnowledgeArticle knowledge);

	/**
	 * 删除知识
	 */

	void deleteKnowledge(long[] ids);

	/**
	 * 编辑知识(资讯，文章，宏观，资产管理，判例，观点)
	 */

	void updateKnowledge(KnowledgeArticle knowledge);

	/**
	 * 查询知识
	 */

	KnowledgeArticle selectKnowledge(long knowledgeid);

}
