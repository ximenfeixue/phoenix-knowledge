package com.ginkgocap.ywxt.knowledge.dao.macro;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeAsset;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeMacro;

/**
 * 内容的DAO接口
 * 
 * @author caihe
 * @创建时间：2014-08-27 16:11
 */
public interface KnowledgeMacroDAO {

	/**
	 * 新增知识
	 */

	KnowledgeMacro insertknowledge(KnowledgeMacro knowledge);

	/**
	 * 删除知识
	 */

	void deleteKnowledge(long[] ids);

	/**
	 * 编辑知识(资讯，文章，宏观，资产管理，判例，观点)
	 */

	void updateKnowledge(KnowledgeMacro knowledge);
	
	/**
	 * 查询知识
	 */

	KnowledgeMacro selectKnowledge(long knowledgeid);


}
