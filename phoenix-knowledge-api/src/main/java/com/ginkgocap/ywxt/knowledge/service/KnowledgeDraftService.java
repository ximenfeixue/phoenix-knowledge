package com.ginkgocap.ywxt.knowledge.service;

import java.util.List;

import com.ginkgocap.ywxt.knowledge.entity.KnowledgeDraft;
import com.ginkgocap.ywxt.knowledge.util.Page;

/**
 * 知识相关的关系表
 * 
 * @author caihe
 * 
 */
public interface KnowledgeDraftService {

	/**
	 * 知识存入草稿箱
	 * 
	 * @author caihe
	 * @return
	 */
	int insertKnowledgeDraft(long knowledgeid, String draftname, String type,
			long userid);

	/**
	 * 查询草稿箱列表
	 * 
	 * @param page
	 * @param userid
	 * @param type
	 * @return
	 */
	Page<KnowledgeDraft> selectKnowledgeDraft(Page<KnowledgeDraft> page,
			long userid, String type);

	/**
	 * 删除草稿箱知识
	 */

	int deleteKnowledgeDraft(long[] knowledgeids, long userid);

	/**
	 * 根据知识ID查询草稿箱列表
	 */
	KnowledgeDraft selectByKnowledgeId(long knowledgeid);
}
