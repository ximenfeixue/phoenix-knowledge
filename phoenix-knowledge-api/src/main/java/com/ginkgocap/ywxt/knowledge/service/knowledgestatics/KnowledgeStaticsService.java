package com.ginkgocap.ywxt.knowledge.service.knowledgestatics;

import java.util.List;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeStatics;

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
	void insertKnowledgeStatics(KnowledgeStatics knowledgeStatics);
	
	/**
	 * 根据类型查询评论排行
	 * @param type 栏目类型
	 * @return
	 */
	List<KnowledgeStatics>selectRankList(int type);

}
