package com.ginkgocap.ywxt.knowledge.service.knowledgestatics;

import java.util.List;

import com.ginkgocap.ywxt.knowledge.entity.KnowledgeStatics;

/**
 * 知识统计
 * 
 * @author caihe
 * 
 */
public interface KnowledgeStaticsService {

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
