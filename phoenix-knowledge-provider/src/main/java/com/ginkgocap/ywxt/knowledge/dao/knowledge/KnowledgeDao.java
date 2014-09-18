package com.ginkgocap.ywxt.knowledge.dao.knowledge;

import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNews;

public interface KnowledgeDao {
	int deleteByPrimaryKey(Long id);

	int insert(Knowledge record);

	int insertSelective(Knowledge record);

	Knowledge selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(Knowledge record);

	int updateByPrimaryKey(Knowledge record);
	
	/**
	 * 检查投融工具新建名称是否重复
	 * 
	 * @param knowledgeid
	 * @param knowledgeTitle
	 * @return
	 */
	int checkInvestmentNameRepeat(String knowledgetitle);
	
	/**
	 * 检查行业新建名称是否重复
	 * 
	 * @param knowledgeid
	 * @param knowledgeTitle
	 * @return
	 */
	int checkIndustryNameRepeat(String knowledgetitle);
	
	/**
	 * 检查法律法规新建名称是否重复
	 * 
	 * @param knowledgeid
	 * @param knowledgeTitle
	 * @return
	 */
	int checkLayNameRepeat(String knowledgetitle);

	/**
	 * 批量知识移动到其它多个目录下
	 * 
	 * @param knowledgeid
	 * @return
	 */
	int moveCategoryBatch(long knowledgeids[], long categoryids[]);

	/**
	 * 新增知识(资讯，文章，宏观，资产管理，判例，观点)
	 */

	Knowledge insertknowledge(Knowledge knowledge);

	/**
	 * 删除知识(资讯，文章，宏观，资产管理，判例，观点)
	 */

	int deleteKnowledge(long[] ids, long categoryid);

	/**
	 * 编辑知识(资讯，文章，宏观，资产管理，判例，观点)
	 */

	int updateKnowledge(Knowledge knowledge,long categoryid[]);

	/**
	 * 编辑知识，如果目录更改，相应的知识目录中间表也更改
	 * 
	 * @param knowledgeids
	 * @param categoryid
	 * @return
	 */
	int updateKnowledgeRCategory(long knowledgeid, long categoryid,long[] categoryids);
	
	/**
	 * 編輯知识，把知识目录中间表删除
	 * 
	 * @param knowledgeids
	 * @param categoryid
	 * @return
	 */
	int deleteKnowledgeRCategory(long knowledgeid, long categoryid);
}
