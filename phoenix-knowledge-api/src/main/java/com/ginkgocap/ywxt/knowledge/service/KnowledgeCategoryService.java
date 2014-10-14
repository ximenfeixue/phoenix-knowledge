package com.ginkgocap.ywxt.knowledge.service;

import java.util.Date;
import java.util.List;

import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCategory;

/**
 * 知识相关的关系表
 * 
 * @author caihe
 * 
 */
public interface KnowledgeCategoryService {

	/**
	 * 新增资讯知识，把知识ID，目录ID，存入到知识目录中间表中
	 * 
	 * @param knowledgeRCategory
	 * @return
	 */
	int insertKnowledgeRCategory(long knowledgeid, long categoryid[],
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
	 * 查询目录下知识个数
	 * 
	 * @param id
	 * @return
	 */
	int countByKnowledgeCategoryId(long categoryid);

	int deleteKnowledgeCategory(long knowledgeid);

	int updateKnowledgeCategory(long knowledgeid, long categoryid);
	
	List<KnowledgeCategory> selectKnowledgeCategory(long knowledgeid, long categoryid);

}
