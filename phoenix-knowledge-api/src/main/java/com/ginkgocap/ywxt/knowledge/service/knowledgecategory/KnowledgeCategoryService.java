package com.ginkgocap.ywxt.knowledge.service.knowledgecategory;

import java.util.Map;

import com.ginkgocap.ywxt.knowledge.model.Favorites;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNews;

/**
 * 知识相关的关系表
 * 
 * @author liuyang
 * 
 */
public interface KnowledgeCategoryService {

	/**
	 * 新增资讯知识，把知识ID，目录ID，存入到知识目录中间表中
	 * 
	 * @param knowledgeRCategory
	 * @return
	 */
	void insertKnowledgeRCategory(KnowledgeNews knowledge, long categoryid[]);
	
	/**
	 * 刪除知识，把知识目录中间表删除
	 * 
	 * @param knowledgeids
	 * @param categoryid
	 * @return
	 */
	int deleteKnowledgeRCategory(long[] knowledgeids, long categoryid);
}
