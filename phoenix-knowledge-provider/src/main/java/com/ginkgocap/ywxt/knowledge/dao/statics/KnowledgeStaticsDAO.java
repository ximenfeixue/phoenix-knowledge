package com.ginkgocap.ywxt.knowledge.dao.statics;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeStatics;

public interface KnowledgeStaticsDAO {

	/**
	 * 新增知识，把知识相关数据添加到知识统计表内
	 * 
	 * @param knowledgeRCategory
	 * @return
	 */
	void insertKnowledgeStatics(KnowledgeStatics knowledgeStatics);

}
