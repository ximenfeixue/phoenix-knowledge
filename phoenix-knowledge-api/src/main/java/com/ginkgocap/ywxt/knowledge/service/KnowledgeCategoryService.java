package com.ginkgocap.ywxt.knowledge.service;

import java.util.Date;
import java.util.List;

import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCategory;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNewsVO;

/**
 * 知识相关的关系表
 * 
 * @author caihe
 * 
 */
public interface KnowledgeCategoryService {

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

	List<KnowledgeCategory> selectKnowledgeCategory(long knowledgeid,
			long categoryid);

	int updateKnowledgeCategorystatus(long knowledgeid, long categoryid);

	int insertCategory(long knowledgeid, long categoryid, long userid,
			String title, String author, String path, String share_author,
			Date createtime, String tag, String know_desc, long column_id,
			String pic_path);

	/**
	 * 添加知识到知识目录关系表
	 * 
	 * @param id
	 *            知识ID
	 * @param cIds
	 *            目录ID集合
	 * @param userId
	 *            用户ID
	 * @param username
	 *            用户名
	 * @param columnPath
	 *            栏目路径
	 * @param vo
	 *            知识对象
	 * @return
	 */
	int insertKnowledgeRCategory(long id, long[] cIds, long userId,
			String username, String columnPath, KnowledgeNewsVO vo);

	/**
	 * 获取格式化后的目录
	 * 
	 * @param ids
	 *            目录id集合(1,2,3,4)
	 * @param type
	 *            类型(0 正常目录 1 收藏夹目录)
	 * @param userId
	 *            用户ID
	 * @return
	 * @author haiyan
	 */
	long[] getCurrentCategoryArray(String ids, int type, long userId);
}
