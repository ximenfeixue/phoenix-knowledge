package com.ginkgocap.ywxt.knowledge.dao.asset;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeAsset;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNews;

/**
 * 内容的DAO接口
 * 
 * @author caihe
 * @创建时间：2014-08-27 16:11
 */
public interface KnowledgeAssetDAO {

	/**
	 * 新增知识
	 */

	KnowledgeAsset insertknowledge(KnowledgeAsset knowledge);

	/**
	 * 删除知识
	 */

	void deleteKnowledge(long[] ids);

	/**
	 * 编辑知识(资讯，文章，宏观，资产管理，判例，观点)
	 */

	void updateKnowledge(KnowledgeAsset knowledge);
	
	/**
	 * 查询知识
	 */

	KnowledgeAsset selectKnowledge(long knowledgeid);


}
