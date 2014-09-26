package com.ginkgocap.ywxt.knowledge.service;

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
	int insertKnowledgeDraft(long knowledgeid, String draftname, int type,
			long userid);

}
