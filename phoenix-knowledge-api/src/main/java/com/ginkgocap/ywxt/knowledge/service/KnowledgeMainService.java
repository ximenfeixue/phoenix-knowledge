package com.ginkgocap.ywxt.knowledge.service;

import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNews;

/**
 * 知识Service
 * 
 * @author zhangwei
 * 
 */
public interface KnowledgeMainService {

	/**
	 * 存储知识
	 * 
	 * @return
	 */
	public Long saveKnowledge(Knowledge knowledge, Object knowledgeDetail);

	/**
	 * 更新知识
	 * 
	 * @return
	 */
	public boolean updateKnowledge(Knowledge knowledge, Object knowledgeDetail);

	/**
	 * 获取知识详情
	 * 
	 * @param id
	 *            知识对应“详情”中的id
	 * @param type
	 *            知识类型
	 * @return
	 */
	public <T> T getKnowLedgeDetail(int id, int type);

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
	int checkLawNameRepeat(String knowledgetitle);

	/**
	 * 存储知识基本信息（试用透融工具、行业）
	 * 
	 * @return
	 */
	public Long saveKnowledgeTitle(Knowledge knowledge, Object knowledgeDetail);

	//

	/**
	 * 批量知识移动到其它多个目录下
	 * 
	 * @param knowledgeid
	 * 
	 */
	int moveCategoryBatch(long categoryid, long knowledgeids[],
			long categoryids[]);

	/**
	 * 将多个目录下多个知识移动到其它多个目录下
	 * 
	 * @param knowledgeid
	 * 
	 */
	int moveCategorysBatch(long knowledgeids[], long categoryids[]);

	/**
	 * 編輯知识，把知识目录中间表删除
	 * 
	 * @param knowledgeids
	 * @param categoryid
	 * @return
	 */
	int deleteKnowledgeRCategory(long knowledgeid, long categoryid);

	/**
	 * 编辑知识，如果目录更改，相应的知识目录中间表也更改
	 * 
	 * @param knowledgeids
	 * @param categoryid
	 * @return
	 */
	int updateKnowledgeRCategory(long knowledgeid, long categoryid,
			long[] categoryids);

	/**
	 * 新增知识(资讯，文章，宏观，资产管理，判例，观点)
	 */

	Knowledge insertknowledge(Knowledge knowledge, long categoryid[]);

	/**
	 * 删除知识(资讯，文章，宏观，资产管理，判例，观点)
	 */

	int deleteKnowledge(long[] ids, long categoryid);

	/**
	 * 编辑知识(资讯，文章，宏观，资产管理，判例，观点)
	 */

	int updateKnowledge(Knowledge knowledge, long categoryid,
			long categoryids[]);

	/**
	 * 查询知识(资讯，文章，宏观，资产管理，判例，观点)
	 */

	// List<Knowledge> selectKnowledge(Knowledge knowledge);

}
