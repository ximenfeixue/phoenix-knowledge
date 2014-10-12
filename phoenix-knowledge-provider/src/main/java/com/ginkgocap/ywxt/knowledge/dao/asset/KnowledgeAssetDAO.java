package com.ginkgocap.ywxt.knowledge.dao.asset;

import java.util.List;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeAsset;

/**
 * 内容的DAO接口
 * 
 * @author caihe
 * @创建时间：2014-08-27 16:11
 */
public interface KnowledgeAssetDAO {

	/**
	 * 新增资讯知识
	 */

	KnowledgeAsset insertknowledge(String title, long userid, String uname,
			long cid, String cname, String cpath, String content, String pic,
			String desc, String essence, String taskid, String tags,
			long knowledgeid, long columnid);

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

	KnowledgeAsset selectKnowledge(long knowledgeid);

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
	List<KnowledgeAsset> selectByParam(Long columnid, long source, Long userid,
			List<Long> ids, int page, int size);

	/**
	 * 删除草稿箱中的知识同时，将MongoDB中的知识删除
	 */
	void deleteKnowledgeByid(long knowledgeid);

	void restoreKnowledgeByid(long knowledgeid);

}
