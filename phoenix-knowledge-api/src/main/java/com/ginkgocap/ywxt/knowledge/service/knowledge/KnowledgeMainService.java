package com.ginkgocap.ywxt.knowledge.service.knowledge;

import java.util.Map;

import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeShare;

/**
 * 知识Service
 * 
 * @author zhangwei
 * 
 */
public interface KnowledgeMainService {

//	/**
//	 * 存储知识
//	 * 
//	 * @return
//	 */
//	public Long saveKnowledge(Knowledge knowledge, Object knowledgeDetail);
//
//	/**
//	 * 更新知识
//	 * 
//	 * @return
//	 */
//	public boolean updateKnowledge(Knowledge knowledge, Object knowledgeDetail);
//
//	/**
//	 * 获取知识详情
//	 * 
//	 * @param id
//	 *            知识对应“详情”中的id
//	 * @param type
//	 *            知识类型
//	 * @return
//	 */
//	public <T> T getKnowLedgeDetail(int id, int type);

	/**
	 * 检查新建名称是否重复
	 * 
	 * @param knowledgeid
	 * @param knowledgeTitle
	 * @return
	 */
	int checkNameRepeat(int knowledgetype, String knowledgetitle);
}
