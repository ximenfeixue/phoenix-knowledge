package com.ginkgocap.ywxt.knowledge.service;

import java.util.List;
import java.util.Map;

import com.ginkgocap.ywxt.knowledge.entity.KnowledgeDraft;
import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNewsVO;
import com.ginkgocap.ywxt.knowledge.util.Page;
import com.ginkgocap.ywxt.user.model.User;

/**
 * 知识相关的关系表
 * 
 * @author caihe
 * 
 */
@Deprecated
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
	
	
	/**
	 * 知识存入草稿箱(待测版本)
	 * @author zhangwei
	 * @return
	 */
	Map<String, Object> insertKnowledgeDraftNew(KnowledgeNewsVO vo, User user);
	
	/**
	 * 知识存入草稿箱(待测版本) 专门为编辑投融工具所建，编辑投融工具不需要更新目录权限等
	 * @author zhangwei
	 * @return
	 */
	Map<String, Object> insertKnowledgeDraftNew(KnowledgeNewsVO vo, User user,boolean isUpdate);
	
	/**
	 * 删除单个知识
	 * @param knowledgeId 知识对应的KnowledgeMainId (别传错了)
	 * @param type 知识类型
	 * @return
	 */
	int deleteKnowledgeSingalDraft(Long knowledgeMainId, String type);
	/**
	 * 删除单个知识
	 * @param knowledgeId
	 * @param type
	 * @param userId
	 * @return
	 */
	int deleteKnowledgeSingalDraft(Long knowledgeMainId, String type, Long userId);
	
	
	/**
	 * 删除单个草稿箱知识
	 */
	
	public Knowledge getDraftByMainId(Long knowledgeId,String type);
	
    public Knowledge getDraftByMainIdAndUser(Long knowledgeId,String type,Long userId);
	
}
