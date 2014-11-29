package com.ginkgocap.ywxt.knowledge.service;

import com.ginkgocap.ywxt.knowledge.entity.ColumnKnowledge;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCategory;

public interface KnowledgeCommonService {

	/**
	 * 获取文章所在栏目
	 * @param kid
	 * @return
	 */
	ColumnKnowledge getKnowledgeColumn(long kid);

	/**
	 * 获取文章所在目录
	 * @param kid
	 * @return
	 */
	
	KnowledgeCategory getKnowledgeCategory(long kid);
	
	
}
