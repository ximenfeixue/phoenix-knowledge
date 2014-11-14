package com.ginkgocap.ywxt.knowledge.service;

//import com.ginkgocap.ywxt.knowledge.entity.KnowledgeTag;

/**
 * 知识标签关系
 * 
 * @author
 * 
 */
public interface KnowledgeTagService {

	/**
	 * 新增知识，把知识ID，栏目ID，存入到知识标签中间表中
	 * 
	 * @param knowledgeRCategory
	 * @return
	 */
	//int insertKnowledgeTag(KnowledgeTag knowledgeTag);

	
	/** 知识标签更改方法 */
	boolean updateKnowledgeTag(long kid,int type,String tags);
}
