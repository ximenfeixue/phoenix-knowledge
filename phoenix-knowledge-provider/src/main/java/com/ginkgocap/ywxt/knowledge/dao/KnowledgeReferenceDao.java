package com.ginkgocap.ywxt.knowledge.dao;

import com.ginkgocap.ywxt.knowledge.model.common.KnowledgeReference;

import java.util.List;

/**
 * @Title: 知识来源表
 * @Description: 存储知识来源，例如来源url、来源网站名、来源作者等信息
 * @version V1.0.0
 */
public interface KnowledgeReferenceDao {
	
	/**
	 * 插入
	 * @param knowledgeReference
	 * @return
	 * @throws Exception
	 */
	public KnowledgeReference insert(KnowledgeReference knowledgeReference) throws Exception;
	
	/**
	 * 批量插入
	 * @param knowledgeReference
	 * @return
	 * @throws Exception
	 */
	public List<KnowledgeReference> insertList(List<KnowledgeReference> knowledgeReference) throws Exception;
	
	/**
	 * 更新
	 * @param knowledgeReference
	 * @return
	 * @throws Exception
	 */
	public KnowledgeReference update(KnowledgeReference knowledgeReference) throws Exception;

	/**
	 * 根据主键删除
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public int deleteById(long id) throws Exception;
	
	/**
	 * 根据主键list删除
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	public int deleteByIds(List<Long> ids) throws Exception;
	
	/**
	 * 根据知识主键删除
	 * @param knowledgeId
	 * @return
	 * @throws Exception
	 */
	public int deleteByKnowledgeId(long knowledgeId) throws Exception;
	
	/**
	 * 根据知识主键list删除
	 * @param knowledgeIds
	 * @return
	 * @throws Exception
	 */
	public int batchDeleteByKnowledgeIds(List<Long> knowledgeIds) throws Exception;
	
	/**
	 * 根据主键提取
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public KnowledgeReference getById(long id) throws Exception;
	
	/**
	 * 根据主键list提取
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	public List<KnowledgeReference> getByIds(List<Long> ids) throws Exception;
	
	/**
	 * 根据主键、状态提取
	 * @param id
	 * @param status
	 * @return
	 * @throws Exception
	 */
	public KnowledgeReference getByIdAndStatus(long id,String status)  throws Exception;
	
	/**
	 * 根据知识主键提取
	 * @param knowledgeId
	 * @return
	 * @throws Exception
	 */
	public KnowledgeReference getByKnowledgeId(long knowledgeId)  throws Exception;
	
	/**
	 * 根据知识主键list提取
	 * @param knowledgeIds
	 * @return
	 * @throws Exception
	 */
	public List<KnowledgeReference> getByKnowledgeIds(List<Long> knowledgeIds)  throws Exception;
	
	/**
	 * 根据知识主键、状态提取
	 * @param knowledgeId
	 * @param status
	 * @return
	 * @throws Exception
	 */
	public KnowledgeReference getByKnowledgeIdAndStatus(long knowledgeId,String status)  throws Exception;
}