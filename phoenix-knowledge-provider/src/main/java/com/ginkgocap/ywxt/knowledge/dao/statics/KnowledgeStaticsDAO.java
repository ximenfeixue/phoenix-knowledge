package com.ginkgocap.ywxt.knowledge.dao.statics;

import java.util.List;

import com.ginkgocap.ywxt.knowledge.entity.KnowledgeStatics;

public interface KnowledgeStaticsDAO {

	/**
	 * 新增知识，把知识相关数据添加到知识统计表内
	 * 
	 * @param knowledgeRCategory
	 * @return
	 */
	int insertKnowledgeStatics(KnowledgeStatics knowledgeStatics);

	/**
	 * 根据知识ID查询数据
	 * 
	 * @param type
	 *            栏目类型
	 * @return
	 */
	KnowledgeStatics selectByknowledgeId(long knowledgeid);

}
