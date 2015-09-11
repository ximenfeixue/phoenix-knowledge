package com.ginkgocap.ywxt.knowledge.service;

import java.util.List;
import java.util.Map;

import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCommentQuery;
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
	/**
	 * 分页查询评论
	 * @param param
	 * @return
	 */
	public List<KnowledgeCommentQuery> getCommentByPage(Map<String, Object> param);
	/**
	 * 查询评论数量
	 * @param param
	 * @return
	 */
	public int getCommentCount(Map<String, Object> param);
	/**
	 * 删除评论 设置标志位
	 * @param idList
	 * @param userId 删除操作人ID
	 * @param userName 删除操作人姓名
	 */
	public void deleteComment(List<Long> idList, Long userId, String userName);
	/**
	 * 彻底删除评论 物理删除
	 * @param idList
	 */
	public void deleteCommentCompletely(List<Long> idList);
	/**
	 * 恢复已删除的评论
	 * @param idList
	 */
	public void recoverDeletedComment(List<Long> idList);
	/**
	 * 屏蔽评论内容
	 * @param idList
	 */
	public void invisibleComment(List<Long> idList);
	/**
	 * 恢复屏蔽内容
	 * @param idList
	 */
	public void recoverInvisibleComment(List<Long> idList);
}
