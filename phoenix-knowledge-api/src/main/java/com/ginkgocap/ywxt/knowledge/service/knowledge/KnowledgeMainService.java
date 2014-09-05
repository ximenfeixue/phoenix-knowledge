package com.ginkgocap.ywxt.knowledge.service.knowledge;

import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeRCategory;

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
	 * 检查新建名称是否重复
	 * 
	 * @param knowledgeid
	 * @param knowledgeTitle
	 * @return
	 */
	int checkNameRepeat(int knowledgetype, String knowledgetitle);

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
	void moveCategoryBatch(long categoryid, long knowledgeids[],
			long categoryids[]);

	/**
	 * 刪除知识，把知识目录中间表删除
	 * 
	 * @param knowledgeids
	 * @param categoryid
	 * @return
	 */
	int deleteKnowledgeRCategory(long[] knowledgeids, long categoryid);

	/**
	 * 新增知识，把知识ID，目录ID，存入到知识目录中间表中
	 * 
	 * @param knowledgeRCategory
	 * @return
	 */
	void insertKnowledgeRCategory(Knowledge knowledge, long categoryid[]);

	/**
	 * 新增知识(资讯，文章，宏观，资产管理，判例，观点)
	 */

	Knowledge insertknowledge(Knowledge knowledge, long categoryid[]);

	/**
	 * 删除知识(资讯，文章，宏观，资产管理，判例，观点)
	 */

	int deleteKnowledge(long[] ids,long categoryid);

	/**
	 * 编辑知识(资讯，文章，宏观，资产管理，判例，观点)
	 */

	int updateKnowledge(Knowledge knowledge);
}
