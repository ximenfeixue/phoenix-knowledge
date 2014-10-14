package com.ginkgocap.ywxt.knowledge.service;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeCase;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeIndustry;

/**
 * 知识Service
 * 
 * @author zhangwei
 * 
 */
public interface KnowledgeCaseService {
	
	/**
	 * 新增（经典案例知识）
	 * @param k
	 * @return
	 */
	public Long addKnowledgeCase(KnowledgeCase k);
	
	/**
	 * 修改知识 （经典案例）
	 * @param k
	 * @return
	 */
	public Long updateKnowledgeCase(KnowledgeCase k);
	
	/**
	 * 获取知识详情 (经典案例的详情可能和别的略有区别，需要取出knowledgeId后再调用其他service才可)
	 * @param id
	 * @return
	 */
	public KnowledgeCase getKnowledgeCaseDetail(Long id);
	
	/**
	 * 恢复回收站中的知识
	 * 
	 * @param knowledgeid
	 */
	void restoreKnowledgeByid(long knowledgeid);
	
	void deleteforeverKnowledge(long knowledgeid);
	
}
