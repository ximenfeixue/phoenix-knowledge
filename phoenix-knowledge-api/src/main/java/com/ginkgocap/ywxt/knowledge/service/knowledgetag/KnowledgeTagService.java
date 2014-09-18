package com.ginkgocap.ywxt.knowledge.service.knowledgetag;

import com.ginkgocap.ywxt.knowledge.entity.KnowledgeTag;

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
	int  insertKnowledgeTag(KnowledgeTag knowledgeTag);

	/**
	 * 刪除知识，把知识标签中间表删除
	 * 
	 * @param knowledgeids
	 * @param
	 * @return
	 */
	int deleteColumnKnowledge(long[] knowledgeids, long userid);
}
