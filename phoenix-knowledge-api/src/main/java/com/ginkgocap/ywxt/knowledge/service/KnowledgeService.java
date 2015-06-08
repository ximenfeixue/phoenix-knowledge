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
	 * 删除资讯知识
	 */

	Map<String, Object> deleteKnowledge(String knowledgeids, long catetoryid,
			String types, String titles, User user);

	/**
	 * 编辑知识
	 */

	Map<String, Object> updateKnowledge(KnowledgeNewsVO vo, User user);

	/**
	 * 查询知识
	 */
 
	Knowledge selectKnowledge(long knowledgeid, String type);

	/**
	 * 根据条件查询资讯
	 * 
	 * @param columnid
	 *            栏目类型
	 * @param source
	 *            1首页 2其他
	 * @param userid
	 *            用户id
	 * @param ids
	 *            资讯ids
	 * @return
	 */
	List<KnowledgeNews> selectByParam(Long columnid, long source, Long userid,
			List<Long> ids, int page, int size);

	/**
	 * 删除草稿箱中的知识同时，将MongoDB中的知识删除
	 */
	void deleteKnowledgeByid(long knowledgeid);

	/**
	 * 恢复回收站中的知识
	 * 
	 * @param knowledgeid
	 */
	void restoreKnowledgeByid(long knowledgeid, long userid);

	void deleteforeverKnowledge(long knowledgeid);

	Map<String, Object> deleteKnowledgeNew(String knowledgeids, long groupid,
			long userid);

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

	/**
	 * 编辑知识(投融工具)
	 */

	public void updateKnowledgeForInvestment(Long id, String pic,
			String refrenceData, String imageBookData, String content,
			String desc, Long userId);

	public void updateByPrimaryKey(KnowledgeBase kb);

	Map<String, Object> saveKnowledge(KnowledgeNewsVO vo, User user);

	/**
	 *  添加知识到知识目录表
	 * @param vo
	 * @param user
	 * @return
	 */
	Map<String, Object> insertCatalogueIds(KnowledgeNewsVO vo, User user);

}
