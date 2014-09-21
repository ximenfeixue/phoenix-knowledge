package com.ginkgocap.ywxt.knowledge.service.knowledge;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeAsset;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeMacro;

/**
 * 知识Service
 * 
 * @author caihe
 * 
 */
public interface KnowledgeMacroService {

	/**
	 * 新增资讯知识
	 */

	KnowledgeMacro insertknowledge(KnowledgeMacro knowledge);

	/**
	 * 删除资讯知识
	 */

	void deleteKnowledge(long[] ids);

	/**
	 * 编辑知识
	 */

	void updateKnowledge(KnowledgeMacro knowledge);

	/**
	 * 查询知识
	 */

	KnowledgeMacro selectKnowledge(long knowledgeid);

}
