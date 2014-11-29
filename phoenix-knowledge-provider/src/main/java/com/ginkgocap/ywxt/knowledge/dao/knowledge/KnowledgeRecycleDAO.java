package com.ginkgocap.ywxt.knowledge.dao.knowledge;

import java.util.List;

import com.ginkgocap.ywxt.knowledge.entity.KnowledgeRecycle;

public interface KnowledgeRecycleDAO {

	List<KnowledgeRecycle> selectKnowledgeRecycle(long userid, String type,
			int pageno, int pagesize);

	int countKnowledgeRecycle(long userid, String type);

	/**
	 * 删除草稿箱知识
	 */

	int deleteKnowledgeRecycle(long[] knowledgeids, long userid);

	/**
	 * 删除草稿箱知识
	 */

	int deleteKnowledgeRecycle(long knowledgeid);
}