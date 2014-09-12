package com.ginkgocap.ywxt.knowledge.service.userpermission;

import java.util.List;

import com.ginkgocap.ywxt.knowledge.model.ColumnKnowledge;

/**
 * 知识栏目关系
 * 
 * @author
 * 
 */
public interface UserPermissionService {

	/**
	 * 新增知识，把知识ID，栏目ID，存入到知识栏目中间表中
	 * 
	 * @param knowledgeRCategory
	 * @return
	 */
	void insertColumnKnowledge(ColumnKnowledge columnKnowledge);

	/**
	 * 刪除知识，把知识栏目中间表删除
	 * 
	 * @param knowledgeids
	 * @param
	 * @return
	 */
	int deleteColumnKnowledge(long[] knowledgeids, long columnid);

	
	//查询知识ID
	List<Long> selectByreceive_user_id(long receive_user_id);
}
