package com.ginkgocap.ywxt.knowledge.service;

import java.util.Map;

import com.ginkgocap.ywxt.knowledge.entity.KnowledgeStatics;
import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.user.model.User;
/**
 * 
 * @author haiyan
 *
 */

public interface KnowledgeReaderService {

	/**
	 * 获取用户信息
	 * 
	 * @param userid
	 *            用户Id
	 * @return
	 */
	User getUserInfo(long userid);

	/**
	 * 获取知识各种关系数据量
	 * 
	 * @param kid
	 *            知识id
	 * @return
	 */
	KnowledgeStatics getKnowledgeStatusCount(long kid);

	/**
	 * 查询登陆用户与作者关系 self(1, "自己"), friends(2, "好友"), jinTN(3, "金桐脑"), platform(4,
	 * "全平台"), org(5, "组织"),notFriends(6,"不是好友");
	 * 
	 * @param loginuserid
	 *            登陆用户Id
	 * @param kuid
	 *            知识所属用户Id
	 * @return
	 */
	Map<String, Integer> authorAndLoginUserRelation(long loginuserid, long kuid);

	/**
	 * 返回文章头部标签显示
	 * 
	 * @param kid
	 *            知识Id
	 * @param type
	 *            文章类型(十一种类型之一)
	 * @return
	 */
	Map<String, Boolean> showHeadMenu(long kid, String type);

	/**
	 * 返回文章头部标签显示
	 * 
	 * @param kid
	 *            知识Id
	 * @param type
	 *            知识类型
	 * @param userId
	 *            用户ID
	 * @param authorId
	 *            知识作者ID
	 * @return
	 */
	Map<String, Boolean> showHeadMenu(long kid, String type, long userId,
			long authorId);

	/**
	 * 返回文章内容，包括title content
	 * 
	 * @param kid
	 *            知识id
	 * @param type
	 *            知识类型(十一种类型之一)
	 * @return
	 * @throws ClassNotFoundException
	 */
	Map<String, Object> getKnowledgeContent(Knowledge knowledge, String type);

	/**
	 * 获取知识用户ID
	 * 
	 * @param kid
	 *            知识ID
	 * @return
	 */
	Knowledge getKnowledgeById(long kid, String type);

	/**
	 * 获取知识阅读器头部信息
	 * 
	 * @param kid
	 *            知识Id
	 * @param kUId
	 *            知识作者ID
	 * @param userId
	 *            登陆用户ID
	 * @param type
	 *            知识类型
	 * @return
	 */
	Map<String, Object> getReaderHeadMsg(long kid, long kUId, long userId,
			String type);

	/**
	 * 获取知识详细信息
	 * 
	 * @param kid
	 *            知识Id
	 * @param sessUser
	 *            登陆用户对象
	 * @param type
	 *            知识类型
	 * @return
	 */
	Map<String, Object> getKnowledgeDetail(long kid, User sessUser, String type);

}
