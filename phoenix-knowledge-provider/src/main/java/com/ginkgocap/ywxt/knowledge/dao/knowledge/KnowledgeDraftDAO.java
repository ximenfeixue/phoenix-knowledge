package com.ginkgocap.ywxt.knowledge.dao.knowledge;

import java.util.List;

import com.ginkgocap.ywxt.knowledge.entity.KnowledgeDraft;

public interface KnowledgeDraftDAO {

	List<KnowledgeDraft> selectKnowledgeDraft(long userid, String type,
			int pageno, int pagesize);

	int countKnowledgeDraft(long userid, String type);

	/**
	 * 删除草稿箱知识
	 */

	int deleteKnowledgeDraft(long[] knowledgeids, long userid);
}