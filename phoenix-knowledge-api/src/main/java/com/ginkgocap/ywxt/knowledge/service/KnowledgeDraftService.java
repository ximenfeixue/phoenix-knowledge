package com.ginkgocap.ywxt.knowledge.service;

import java.util.List;

import com.ginkgocap.ywxt.knowledge.entity.KnowledgeDraft;

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

	List<KnowledgeDraft> selectKnowledgeDraft(long userid, String type,
			int pageno, int pagesize);

}
