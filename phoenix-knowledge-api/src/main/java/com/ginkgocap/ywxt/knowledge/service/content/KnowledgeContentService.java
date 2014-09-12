package com.ginkgocap.ywxt.knowledge.service.content;

import java.util.List;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeContent;

/**
 * 內容的service接口
 * 
 * @author caihe
 * @创建时间：2014-08-27 16:11
 */
public interface KnowledgeContentService {

	/**
	 * 新增内容
	 * 
	 * @param knowledge
	 * @return
	 */
	KnowledgeContent insert(KnowledgeContent knowledgeContent);

	/**
	 * 当編輯知识同时要修改内容
	 * 
	 * @param knowledge
	 * @return
	 */
	int update(KnowledgeContent knowledgeContent);

	/**
	 * 查询内容
	 * 
	 * @param knowledge
	 * @return
	 */
	List<KnowledgeContent> selectByknowledgeId(long knowledgeId);

	/**
	 * 删除内容
	 * 
	 * @param knowledge
	 * @return
	 */
	int deleteByknowledgeId(long[] knowledgeId);

}
