package com.ginkgocap.ywxt.knowledge.service;

import java.util.Map;

import com.ginkgocap.ywxt.user.model.User;

public interface KnowledgeCommentService {

	/**
	 * 添加评论
	 * 
	 * @param kid
	 *            知识Id
	 * @param userid
	 *            登陆用户id
	 * @param pid
	 *            评论父类id（一级传0）
	 * @param content
	 *            评论内容
	 * @return
	 */
	Map<String, Object> addComment(long kid, User user, long pid, String content);

	/**
	 * 查询评论
	 * 
	 * @param kid
	 *            知识Id
	 * @param pid
	 *            父级Id (0查一级，非0查2级)
	 * @param psize
	 * @param pno
	 * @param user
	 * @return
	 */
	Map<String, Object> findCommentList(long kid, long pid, Integer pno,
			Integer psize, User user);

	/**
	 * 删除评论
	 * 
	 * @param id
	 *            评论id
	 * @param kId
	 *            知识Id
	 * @param userid
	 *            登陆用户Id
	 * @return
	 */
	Map<String, Object> delComment(long id, long kId, User user);

	int deleteCommentByknowledgeId(long knowledgeid, long userid);

	/**
	 * @param id
	 * @param kId
	 * @param user
	 * @param pno
	 * @return
	 */
	Map<String, Object> delComment(long id, long kId,
			User user, Integer pno); 
}
