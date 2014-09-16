package com.ginkgocap.ywxt.knowledge.dao.userpermission;

import java.util.List;

public interface UserPermissionDAO {

	// 查询知识ID
	List<Long> selectByreceive_user_id(long receive_user_id);
	
	/**
	 * 新增知识，把知识ID，栏目ID，存入用户权限表
	 * 
	 * @param knowledgeRCategory
	 * @return
	 */
	void  insertUserPermission(long[] receive_uid, long knowledgeid,
			long send_uid, int type, String mento, long column_id);
}
