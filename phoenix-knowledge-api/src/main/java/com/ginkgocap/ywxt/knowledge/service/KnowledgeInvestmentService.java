package com.ginkgocap.ywxt.knowledge.service;

import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeInvestment;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNews;

/**
 * 知识Service
 * 
 * @author zhangwei
 * 
 */
public interface KnowledgeInvestmentService {
	
	/**
	 * 新增（投融工具知识）
	 * @param k
	 * @return
	 */
	public Long addKnowledgeInvestment(KnowledgeInvestment k);
	
	/**
	 * 修改知识 （投融工具）
	 * @param k
	 * @return
	 */
	public Long updateKnowledgeInvestment(KnowledgeInvestment k);
	
	/**
	 * 获取知识详情 (投融工具的详情可能和别的略有区别，需要取出knowledgeId后再调用其他service才可)
	 * @param id
	 * @return
	 */
	public KnowledgeInvestment getKnowledgeInvestmentDetail(Long id);
	
	/**
	 * 恢复回收站中的知识
	 * 
	 * @param knowledgeid
	 */
	void restoreKnowledgeByid(long knowledgeid);
	
	/**
	 * 删除知识
	 */

	void deleteKnowledge(long[] ids);
	
	void deleteforeverKnowledge(long knowledgeid);
	
	public KnowledgeInvestment getKnowledgeInvestmentDetailByOid(Long oid);
}
