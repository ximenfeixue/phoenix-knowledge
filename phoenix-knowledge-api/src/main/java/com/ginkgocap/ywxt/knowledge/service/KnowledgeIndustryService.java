package com.ginkgocap.ywxt.knowledge.service;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeIndustry;

/**
 * 知识Service
 * 
 * @author zhangwei
 * 
 */
public interface KnowledgeIndustryService {
	
	/**
	 * 新增（行业知识）
	 * @param k
	 * @return
	 */
	public Long addKnowledgeIndustry(KnowledgeIndustry k);
	
	/**
	 * 修改知识 （行业）
	 * @param k
	 * @return
	 */
	public Long updateKnowledgeIndustry(KnowledgeIndustry k);
	
	/**
	 * 获取知识详情 (行业的详情可能和别的略有区别，需要取出knowledgeId后再调用其他service才可)
	 * @param id
	 * @return
	 */
	public KnowledgeIndustry getKnowledgeIndustryDetail(Long id);
	
}
