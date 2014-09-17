package com.ginkgocap.ywxt.knowledge.dao.knowledgecategory;

import java.util.Date;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeNews;

public interface KnowledgeCategoryDAO {
	/**
	 * 新增知识，把知识ID，目录ID，存入到知识目录中间表中
	 * 
	 * @param knowledgeRCategory
	 * @return
	 */
	void insertKnowledgeRCategory(long knowledgeid, long categoryid[],long userid,
			String title, String author, int path, String share_author,
			Date createtime, String tag, String know_desc, long column_id,
			String pic_path);

	int deleteKnowledgeRCategory(long[] knowledgeids, long categoryid);
	/**
	 * 查询栏目下知识个数
	 * @param id
	 * @return
	 */
	long countByKnowledgeCategoryId(long id);
}
