package com.ginkgocap.ywxt.knowledge.service;

import java.util.List;
import java.util.Map;

import com.ginkgocap.ywxt.knowledge.entity.KnowledgeDraft;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNewsVO;
import com.ginkgocap.ywxt.knowledge.util.Page;
import com.ginkgocap.ywxt.user.model.User;

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
	Map<String, Object> insertKnowledgeDraft(KnowledgeNewsVO vo, User user);

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

	List<KnowledgeDraft> selectKnowledgeDraft(long userid, String type,
			String keyword, int pageno, int pagesize);

	int countKnowledgeDraft(long userid, String type, String keyword);

	/**
	 * 删除草稿箱知识
	 */

	int deleteKnowledgeDraft(long knowledgeid);

	int updateKnowledgeDaraft(long knowledgeid, String draftname,
			String drafttype, long userid, String type);

}
