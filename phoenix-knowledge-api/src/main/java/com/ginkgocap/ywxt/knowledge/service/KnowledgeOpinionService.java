package com.ginkgocap.ywxt.knowledge.service.knowledge;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeMacro;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeOpinion;

/**
 * 知识Service
 * 
 * @author caihe
 * 
 */
public interface KnowledgeOpinionService {

	/**
	 * 新增资讯知识
	 */

	KnowledgeOpinion insertknowledge(KnowledgeOpinion knowledge);

	/**
	 * 删除资讯知识
	 */

	void deleteKnowledge(long[] ids);

	/**
	 * 编辑知识
	 */

	void updateKnowledge(KnowledgeOpinion knowledge);

	/**
	 * 查询知识
	 */

	KnowledgeOpinion selectKnowledge(long knowledgeid);

}
