package com.ginkgocap.ywxt.knowledge.service.userpermission;

import java.util.Date;
import java.util.List;

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
	int insertUserPermission(long[] receive_uid, long knowledgeid,
			long send_uid, int type, String mento, short column_id);

	/**
	 * 刪除知识，把用户权限记录
	 * 
	 * @param knowledgeids
	 * @param
	 * @return
	 */
	int deleteUserPermission(long[] knowledgeids, long userid);

	// 查询知识ID
	List<Long> selectByreceive_user_id(long receive_user_id, long send_userid);

	/**
	 * 按照条件查询id集合
	 * 
	 * @param receive_user_id
	 *            接受人
	 * @param column_id
	 *            11种类型
	 * @param type -1子页 
	 * @return
	 */
	List<Long> selectByParams(Long receive_user_id, Long column_id, Long type);
}
