package com.ginkgocap.ywxt.knowledge.service.knowledgetag;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeTag;

/**
 * 知识栏目关系
 * 
 * @author
 * 
 */
public interface KnowledgeTagService {

	/**
	 * 新增知识，把知识ID，栏目ID，存入到知识标签中间表中
	 * 
	 * @param knowledgeRCategory
	 * @return
	 */
	void insertKnowledgeTag(KnowledgeTag knowledgeTag);

	/**
	 * 刪除知识，把知识栏目中间表删除
	 * 
	 * @param knowledgeids
	 * @param
	 * @return
	 */
	int deleteColumnKnowledge(long[] knowledgeids, long columnid);
}
