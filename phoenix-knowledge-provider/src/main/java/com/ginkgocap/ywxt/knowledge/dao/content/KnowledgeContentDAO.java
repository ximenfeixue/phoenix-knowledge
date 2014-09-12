package com.ginkgocap.ywxt.knowledge.dao.content;

import java.util.List;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeContent;

/**
 * 内容的DAO接口
 * 
 * @author caihe
 * @创建时间：2014-08-27 16:11
 */
public interface KnowledgeContentDAO {

	/**
	 * 新增内容
	 * 
	 * @param knowledge
	 * @return
	 */
	KnowledgeContent insert(KnowledgeContent knowledgeContent);

	/**
	 * 查询内容
	 * 
	 * @param knowledge
	 * @return
	 */
	List<KnowledgeContent> selectByknowledgeId(long knowledgeId);

	/**
	 * 編輯内容
	 * 
	 * @param knowledge
	 * @return
	 */
	int update(KnowledgeContent knowledgeContent);
	
	/**
	 * 删除内容
	 * 
	 * @param knowledge
	 * @return
	 */
	int deleteByknowledgeId(long[] knowledgeId);

}
