package com.ginkgocap.ywxt.knowledge.service;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeAsset;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNews;

/**
 * 知识Service
 * 
 * @author caihe
 * 
 */
public interface KnowledgeAssetService {

	/**
	 * 新增资讯知识
	 */

	KnowledgeAsset insertknowledge(KnowledgeAsset knowledge);

	/**
	 * 删除资讯知识
	 */

	void deleteKnowledge(long[] ids);

	/**
	 * 编辑知识
	 */

	void updateKnowledge(KnowledgeAsset knowledge);

	/**
	 * 查询知识
	 */

	KnowledgeAsset selectKnowledge(long knowledgeid);

}
