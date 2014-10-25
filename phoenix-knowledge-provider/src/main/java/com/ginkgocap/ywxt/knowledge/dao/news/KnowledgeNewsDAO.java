package com.ginkgocap.ywxt.knowledge.dao.news;

import java.util.List;

import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNews;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNewsVO;
import com.ginkgocap.ywxt.user.model.User;

/**
 * 内容的DAO接口
 * 
 * @author caihe
 * @创建时间：2014-08-27 16:11
 */
public interface KnowledgeNewsDAO {

	/**
	 * 
	 * @param kId 知识ID
	 * @param vo 知识对象
	 * @param columnPath 栏目路径
	 * @param userId 用户ID
	 * @param username 用户名
	 * @return
	 */

	Knowledge insertknowledge(KnowledgeNewsVO vo, User user);
	
	/**
	 * 草稿箱存值
	 * @return
	 */

	Knowledge insertknowledgeDraft(KnowledgeNewsVO vo, User user);

	/**
	 * 删除知识
	 */

	void deleteKnowledge(long[] ids);

	/**
	 * 编辑知识(资讯，文章，宏观，资产管理，判例，观点)
	 */

	void updateKnowledge(String title, long userid, String uname, long cid,
			String cname, String cpath, String content, String pic,
			String desc, String essence, String taskid, String tags,
			long knowledgeid);

	/**
	 * 查询知识
	 */

	KnowledgeNews selectKnowledge(long knowledgeid);

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

	void restoreKnowledgeByid(long knowledgeid);

}
