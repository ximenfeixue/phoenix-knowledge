package com.ginkgocap.ywxt.knowledge.service;

import java.util.List;

import com.ginkgocap.ywxt.knowledge.entity.KnowledgeDraft;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeRecycle;
import com.ginkgocap.ywxt.knowledge.util.Page;

/**
 * 知识相关的关系表
 * 
 * @author caihe
 * 
 */
public interface KnowledgeRecycleService {

	/**
	 * 回收站
	 * 
	 * @author caihe
	 * @return
	 */
	int insertKnowledgeRecycle(long knowledgeid, String recyclename,
			String type, long userid,long categoryid);

	/**
	 * 删除草稿箱知识
	 */

	int deleteKnowledgeDraft(long[] knowledgeids, long userid);

	/**
	 * 根据知识ID查询草稿箱列表
	 */
	KnowledgeRecycle selectByKnowledgeId(long knowledgeid);

	List<KnowledgeRecycle> selectKnowledgeRecycle(long userid, String type,
			int pageno, int pagesize);

	int countKnowledgeRecycle(long userid, String type);

	/**
	 * 删除回收站知识
	 */

	int deleteKnowledgeRecycle(long knowledgeid);
	
	/**
	 * 清空回收站
	 */

	int emptyKnowledgeRecycle(long userid);
	
	

}
