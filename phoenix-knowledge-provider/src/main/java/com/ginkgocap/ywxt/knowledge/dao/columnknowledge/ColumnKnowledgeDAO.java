package com.ginkgocap.ywxt.knowledge.dao.columnknowledge;

import com.ginkgocap.ywxt.knowledge.entity.ColumnKnowledge;

public interface ColumnKnowledgeDAO {
	/**
	 * 新增知识，把知识ID，目录ID，存入到知识栏目中间表中
	 * 
	 * @param knowledgeRCategory
	 * @return
	 */
	int insertColumnKnowledge(ColumnKnowledge columnKnowledge);

	int deleteColumnKnowledge(long[] knowledgeids, long columnid);
}
