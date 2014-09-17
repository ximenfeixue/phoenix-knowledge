package com.ginkgocap.ywxt.knowledge.service.knowledgecategory;

import java.util.Date;

import com.ginkgocap.ywxt.knowledge.entity.UserCategoryTest;

/**
 * 知识相关的关系表
 * 
 * @author liuyang
 * 
 */
public interface KnowledgeCategoryService {

	/**
	 * 新增资讯知识，把知识ID，目录ID，存入到知识目录中间表中
	 * 
	 * @param knowledgeRCategory
	 * @return
	 */
	void insertKnowledgeRCategory(long knowledgeid, long categoryid[],
			long userid, String title, String author, int path,
			String share_author, Date createtime, String tag, String know_desc,
			long column_id, String pic_path);

	/**
	 * 刪除知识，把知识目录中间表删除
	 * 
	 * @param knowledgeids
	 * @param categoryid
	 * @return
	 */
	int deleteKnowledgeRCategory(long[] knowledgeids, long categoryid);
	   /**
     * 查询栏目下知识个数
     * @param id
     * @return
     */
    long countByKnowledgeCategoryId(long id);

}
