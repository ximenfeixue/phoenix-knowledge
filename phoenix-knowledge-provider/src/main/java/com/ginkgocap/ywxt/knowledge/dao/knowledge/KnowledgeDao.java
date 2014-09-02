package com.ginkgocap.ywxt.knowledge.dao.knowledge;

import com.ginkgocap.ywxt.knowledge.model.Knowledge;

public interface KnowledgeDao {
	// int deleteByPrimaryKey(Long id);
	//
	// int insert(Knowledge record);
	//
	// int insertSelective(Knowledge record);
	//
	// Knowledge selectByPrimaryKey(Long id);
	//
	// int updateByPrimaryKeySelective(Knowledge record);
	//
	// int updateByPrimaryKey(Knowledge record);
	/**
	 * 检查知识名称是否重复
	 * 
	 * @param knowledgetype
	 * @param knowledgetitle
	 * @return
	 */
	int checkNameRepeat(int knowledgetype, String knowledgetitle);

	/**
	 * 将知识移动到其它目录
	 * 
	 * @param knowledgeid
	 * @param userid
	 * @param sortId
	 * @return
	 */
	void moveCategory(long knowledgeid, long categoryid, String sortId);

	int deleteKnowledgeRCategory(long knowledgeid, long categoryid);

	/**
	 * 批量知识移动到其它多个目录下
	 * 
	 * @param knowledgeid
	 * @return
	 */
	void moveCategoryBatch(long knowledgeids[], long categoryids[]);
}