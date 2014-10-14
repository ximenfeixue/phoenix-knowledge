package com.ginkgocap.ywxt.knowledge.service;

import java.util.List;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeAsset;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeMacro;

/**
 * 知识Service
 * 
 * @author caihe
 * 
 */
public interface KnowledgeMacroService {

	/**
	 * 新增文章知识
	 */

	KnowledgeMacro insertknowledge(String title, long userid, String uname,
			long cid, String cname, String cpath, String content, String pic,
			String desc, String essence, String taskid, String tags,
			long knowledgeid, long columnid);

	/**
	 * 删除资讯知识
	 */

	void deleteKnowledge(long[] ids);

	/**
	 * 编辑知识
	 */

	void updateKnowledge(String title, long userid, String uname, long cid,
			String cname, String cpath, String content, String pic,
			String desc, String essence, String taskid, String tags,
			long knowledgeid);

	/**
	 * 查询知识
	 */

	KnowledgeMacro selectKnowledge(long knowledgeid);

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
	List<KnowledgeMacro> selectByParam(Long columnid, long source, Long userid,
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
	void restoreKnowledgeByid(long knowledgeid);

	void deleteforeverKnowledge(long knowledgeid);
}
