package com.ginkgocap.ywxt.knowledge.service;

import java.util.Map;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeShare;

/**知识分享
 * @author liuyang
 *
 */
public interface KnowledgeShareService {

	/**
	 * 添加分享信息
	 * @param knowledgeShare 
	 * @return KnowledgeShare
	 */
	KnowledgeShare save(long userId, long knowledgeId, String receiverId, String receiverName);
	/**
	 * 查询我分享的
	 * @param userId 当前用户id
	 * @param currentPage 起始页
	 * @param pageSize 页大小
	 * @return Map<String,Object> map get "list" and "page"
	 */
	Map<String,Object> findMyShare(long userId, int currentPage, int pageSize, String title);
	/**
	 * 查询分享给我的
	 * @param userId 当前用户id
	 * @param currentPage 起始页
	 * @param pageSize 页大小
	 * @return Map<String,Object> map get "list" and "page"
	 */
	Map<String,Object> findShareMe(long userId, int currentPage, int pageSize, String title);
	/**
	 * 删除分享信息
	 * @param knowledgeId
	 */
	void deleteShareInfoByKnowledgeId(long knowledgeId);
	/**
	 * 查询我分享的一条数据
	 * @param userId 当前用户
	 * @param knowledgeShareId 知识id
	 * @return KnowledgeShare
	 */
	KnowledgeShare findMyShareOne(long userId , long knowledgeId);
	/**
	 * 查询分享给我的一条数据
	 * @param userId 接收人
	 * @param knowledgeShareId 知识id
	 * @return KnowledgeShare
	 */
	KnowledgeShare findShareMeOne(long userId , long knowledgeId);
	/**
	 * 更新标题
	 * @param userId 用户，
	 * @param knowledgeId 知识id
	 * @param title 标题
	 */
	void updateTitle(long userId , long knowledgeId, String title);
	/**
	 * 删除我分享的
	 * @param userId 当前用户Id
	 * @param knowledgeId 知识id
	 */
	void deleteMyShare(long userId , long knowledgeId);
	/**
	 * 删除分享给我的
	 * @param userId 接收人id 
	 * @param knowledgeId 知识id
	 */
	void deleteShareMe(long userId , long knowledgeId);
}
