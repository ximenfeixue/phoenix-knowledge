package com.ginkgocap.ywxt.knowledge.service.userpermission;

import java.util.List;

import com.ginkgocap.ywxt.knowledge.model.UserPermission;

/**
 * 知识栏目关系
 * 
 * @author
 * 
 */
public interface UserPermissionService {

	/**
	 * 新增知识，把知识ID，栏目ID，存入用户权限表
	 * 
	 * @param knowledgeRCategory
	 * @return
	 */
	void insertUserPermission(long[] receive_uid, long knowledgeid,
			long send_uid, int type, String mento, long column_id);

	/**
	 * 刪除知识，把用户权限记录
	 * 
	 * @param knowledgeids
	 * @param
	 * @return
	 */
	int deleteUserPermission(long[] knowledgeids, long userid);

	// 查询知识ID
	List<Long> selectByreceive_user_id(long receive_user_id);
}
