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
	 * 添加收藏
	 * 
	 * @param kid
	 *            知识id
	 * @param userid
	 *            登陆用户id
	 * @param type
	 *            类型(十一种)
	 * @param source
	 *            来源(可以传在阅读器初始化的关系数据)
	 * @param columnid
	 *            栏目id
	 * @param categoryid
	 *            目录id
	 * @return
	 */
	Map<String, Object> addCollection(long kid, long userid, String type,
			String source, long columnid, long categoryid);
}
