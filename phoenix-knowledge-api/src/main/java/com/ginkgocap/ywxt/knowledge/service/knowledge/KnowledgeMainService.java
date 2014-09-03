package com.ginkgocap.ywxt.knowledge.service.knowledge;

import com.ginkgocap.ywxt.knowledge.model.Knowledge;

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

	/**
	 * 知识移动到其它目录下
	 * 
	 * @param knowledgeid
	 * @return
	 */
	void moveCategory(long knowledgeid, long categoryid, String sortId);

	//

	/**
	 * 批量知识移动到其它多个目录下
	 * 
	 * @param knowledgeid
	 *            检查新建名称是否重复
	 * 
	 * @param knowledgeid
	 * @param knowledgeTitle
	 * @return
	 */
	void moveCategoryBatch(long knowledgeid, long categoryid,
			long knowledgeids[], long categoryids[]);

	int deleteKnowledgeRCategory(long knowledgeid, long categoryid);

}
