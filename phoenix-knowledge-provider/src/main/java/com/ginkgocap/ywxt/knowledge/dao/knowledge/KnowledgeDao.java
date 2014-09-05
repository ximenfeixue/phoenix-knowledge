package com.ginkgocap.ywxt.knowledge.dao.knowledge;

import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeRCategory;

public interface KnowledgeDao {
	int deleteByPrimaryKey(Long id);

	int insert(Knowledge record);

	int insertSelective(Knowledge record);

	Knowledge selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(Knowledge record);

	int updateByPrimaryKey(Knowledge record);

	/**
	 * 检查知识名称是否重复
	 * 
	 * @param knowledgetype
	 * @param knowledgetitle
	 * @return
	 */
	int checkNameRepeat(int knowledgetype, String knowledgetitle);

	int deleteKnowledgeRCategory(long[] knowledgeids, long categoryid);

	/**
	 * 批量知识移动到其它多个目录下
	 * 
	 * @param knowledgeid
	 * @return
	 */
	void moveCategoryBatch(long knowledgeids[], long categoryids[]);

	/**
	 * 新增知识(资讯，文章，宏观，资产管理，判例，观点)
	 */

	Knowledge insertknowledge(Knowledge knowledge);

	/**
	 * 删除知识(资讯，文章，宏观，资产管理，判例，观点)
	 */

	int deleteKnowledge(String[] ids);

	/**
	 * 编辑知识(资讯，文章，宏观，资产管理，判例，观点)
	 */

	int updateKnowledge(Knowledge knowledge);

	/**
	 * 新增知识，把知识ID，目录ID，存入到知识目录中间表中
	 * 
	 * @param knowledgeRCategory
	 * @return
	 */
	void insertKnowledgeRCategory(Knowledge knowledge, long categoryid[]);

}
