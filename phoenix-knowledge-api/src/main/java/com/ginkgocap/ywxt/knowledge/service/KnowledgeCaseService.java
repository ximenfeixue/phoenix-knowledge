package com.ginkgocap.ywxt.knowledge.service;

import java.util.List;

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
	
	/**
	 * 删除知识
	 */

	void deleteKnowledge(long[] ids);
	
	/**
	 * 通过用户id查询用户所有的案例
	 * size 和  limit暂时未实现 未来有新需求再用，预留出接口
	 * @param userId
	 * @param id 案例的id 如果id>0 则返回除此之外 该用户创建的其他案例
 	 * @param size
	 * @param limit
	 * @return
	 */
	public List<KnowledgeCase> getUserCase(Long userId,Long id,int size,int limit);
	
	public boolean updateCaseAfterTransCase(Long id,String content,int status);
}
