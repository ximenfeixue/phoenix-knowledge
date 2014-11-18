package com.ginkgocap.ywxt.knowledge.dao.knowledge;

import java.util.List;

import com.ginkgocap.ywxt.knowledge.entity.KnowledgeDraft;
import com.ginkgocap.ywxt.knowledge.model.Knowledge;

public interface KnowledgeDraftDAO {

	List<KnowledgeDraft> selectKnowledgeDraft(long userid, String type,
			int pageno, int pagesize);

	int countKnowledgeDraft(long userid, String type);

	/**
	 * 删除草稿箱知识
	 */

	int deleteKnowledgeDraft(long[] knowledgeids, long userid);

	/**
	 * 删除草稿箱知识
	 */

	int deleteKnowledgeDraft(long knowledgeid);
	
	int insertKnowledge(long knowledgeid, String draftname, String drafttype,
			String type, long userid);
	
}