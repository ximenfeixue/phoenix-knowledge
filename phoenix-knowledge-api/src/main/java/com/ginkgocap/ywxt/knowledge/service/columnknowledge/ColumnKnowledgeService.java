package com.ginkgocap.ywxt.knowledge.service.columnknowledge;

import com.ginkgocap.ywxt.knowledge.model.ColumnKnowledge;

/**
 * 知识栏目关系
 * 
 * @author
 * 
 */
public interface ColumnKnowledgeService {

	/**
	 * 新增知识，把知识ID，栏目ID，存入到知识栏目中间表中
	 * 
	 * @param knowledgeRCategory
	 * @return
	 */
	void insertColumnKnowledge(ColumnKnowledge columnKnowledge);

	/**
	 * 刪除知识，把知识栏目中间表删除
	 * 
	 * @param knowledgeids
	 * @param
	 * @return
	 */
	int deleteColumnKnowledge(long[] knowledgeids, long columnid);
}
