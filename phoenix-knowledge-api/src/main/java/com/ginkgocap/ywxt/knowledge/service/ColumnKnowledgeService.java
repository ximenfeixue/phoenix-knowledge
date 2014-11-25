package com.ginkgocap.ywxt.knowledge.service;

import com.ginkgocap.ywxt.knowledge.entity.ColumnKnowledge;

/**
 * 知识栏目关系
 * 
 * @author
 * 
 */
public interface ColumnKnowledgeService {

	/**
	 * 新增知识，把知识ID，栏目ID，存入到知识栏目中间表中
	 * 
	 * @param knowledgeRCategory
	 * @return
	 */
	int insertColumnKnowledge(long column_id, long knowledge_id,long user_id,int type);

	/**
	 * 刪除知识，把知识栏目中间表删除
	 * 
	 * @param knowledgeids
	 * @param
	 * @return
	 */
	int deleteColumnKnowledge(long[] knowledgeids, long columnid);
	
	int updateColumnKnowledge(long column_id, long knowledge_id,long user_id,int type);
	
	int updateColumn(long knowledge_id,long column_id);
	
	/**
	 * zhangzhen 加入
	 * 查询指定栏目下(包括自己)所有栏目的id
	 * 2014-11-25
	 * */
	String[] getAllColumnByColumnId(int columnid);
}
