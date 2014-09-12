package com.ginkgocap.ywxt.knowledge.service.news;

import java.util.List;

import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNews;

/**
 * 知识Service
 * 
 * @author zhangwei
 * 
 */
public interface KnowledgeNewsService {

	/**
	 * 新增资讯知识
	 */

	KnowledgeNews insertknowledge(KnowledgeNews knowledge);

	/**
	 * 删除资讯知识
	 */

	void deleteKnowledge(long[] ids);

	/**
	 * 编辑知识
	 */

	void updateKnowledge(KnowledgeNews knowledge);

	/**
	 * 查询知识
	 */

	KnowledgeNews selectKnowledge(long knowledgeid);

}
