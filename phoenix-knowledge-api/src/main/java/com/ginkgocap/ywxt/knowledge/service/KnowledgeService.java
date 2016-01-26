package com.ginkgocap.ywxt.knowledge.service;

import java.util.List;
import java.util.Map;

import com.ginkgocap.ywxt.knowledge.entity.KnowledgeBase;
import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNews;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNewsVO;
import com.ginkgocap.ywxt.user.model.User;

/**
 * 知识Service
 * 
 * @author zhangwei
 * 
 */
public interface KnowledgeService {

	/**
	 * 
	 * @param kId
	 *            知识ID
	 * @param vo
	 *            知识对象
	 * @param path
	 *            栏目路径
	 * @param id
	 *            用户ID
	 * @param username
	 *            用户名
	 * @return
	 */

	Map<String, Object> insertknowledge(KnowledgeNewsVO vo, User user);

	/**
	 * 编辑知识
	 */

	Map<String, Object> updateKnowledge(KnowledgeNewsVO vo, User user);

	/**
	 * 查询知识
	 */

	Knowledge selectKnowledge(long knowledgeid, String type);

	/**
	 * 删除知识
	 * @param knowledgeids
	 * @param groupid
	 * @param userid
	 * @return
	 */
	Map<String, Object> deleteKnowledgeNew(String knowledgeids, long groupid, long userid);

	/**
	 * 添加用户分享
	 * 
	 * @param vo
	 *            知识对象
	 * @param user
	 *            登陆用户对象
	 */
	List<String> insertUserShare(KnowledgeNewsVO vo, User user);

	/**
	 * 用户权限分享
	 * 
	 * @param vo
	 *            知识对象
	 * @param user
	 *            登陆用户对象
	 */
	Map<String, Object> insertUserPermissions(KnowledgeNewsVO vo, User user);

	public void updateByPrimaryKey(KnowledgeBase kb);

	/**
	 * 保存知识
	 * @param vo
	 * @param user
	 * @return
	 */
	Map<String, Object> saveKnowledge(KnowledgeNewsVO vo, User user);

	/**
	 * 添加知识到知识目录表
	 * 
	 * @param vo
	 * @param user
	 * @return
	 */
	Map<String, Object> insertCatalogueIds(KnowledgeNewsVO vo, User user);

	/**
	 * Method: initOldSerach <br>
	 * Description: 初始化知识老数据到MQ <br>
	 * Creator: xutianlong@gingtong.com <br>
	 * parrom: type 知识的栏目类型
	 * parome: start 开始页，end 结束页
	 * Date: 2016/1/21 9:57
	 */
	public String initOldSerach(int type,int start,int end);

}
