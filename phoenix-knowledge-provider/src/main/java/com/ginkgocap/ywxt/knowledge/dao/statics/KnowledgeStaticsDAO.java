package com.ginkgocap.ywxt.knowledge.dao.statics;

import java.util.List;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeStatics;

public interface KnowledgeStaticsDAO {

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
