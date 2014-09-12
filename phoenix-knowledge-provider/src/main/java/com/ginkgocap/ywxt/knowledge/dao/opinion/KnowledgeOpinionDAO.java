package com.ginkgocap.ywxt.knowledge.dao.opinion;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeOpinion;

/**
 * 内容的DAO接口
 * 
 * @author caihe
 * @创建时间：2014-08-27 16:11
 */
public interface KnowledgeOpinionDAO {

	/**
	 * 新增知识
	 */

	KnowledgeOpinion insertknowledge(KnowledgeOpinion knowledge);

	/**
	 * 删除知识
	 */

	void deleteKnowledge(long[] ids);

	/**
	 * 编辑知识(资讯，文章，宏观，资产管理，判例，观点)
	 */

	void updateKnowledge(KnowledgeOpinion knowledge);
	
	/**
	 * 查询知识
	 */

	KnowledgeOpinion selectKnowledge(long knowledgeid);


}
