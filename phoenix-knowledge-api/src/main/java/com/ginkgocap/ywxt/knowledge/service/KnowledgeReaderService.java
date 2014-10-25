package com.ginkgocap.ywxt.knowledge.service;

import java.util.Map;

import com.ginkgocap.ywxt.knowledge.entity.KnowledgeStatics;
import com.ginkgocap.ywxt.user.model.User;

public interface KnowledgeReaderService {

	/**
	 * 获取用户信息
	 * 
	 * @param userid
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
	 * 查询登陆用户与作者关系 self(0, "自己"), friends(1, "好友"), jinTN(3, "金桐脑"),none(4,
	 * "无关系");
	 * 
	 * @param loginuserid
	 * @param kuid
	 * @return
	 */
	Map<String, Integer> authorAndLoginUserRelation(long loginuserid, long kuid);

	/**
	 * 返回文章头部标签显示
	 * 
	 * @param kid
	 * @param type
	 *            文章类型(十一种类型之一)
	 * @return
	 */
	Map<String, Boolean> showHeadTag(long kid, String type);

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
	Map<String, Object> getKnowledgeContent(long kid, String type);

	/**
	 * 获取知识用户ID
	 * @param kid 知识ID
	 * @return
	 */
	long getKUIdByKId(long kid,String type);
	
	
}
