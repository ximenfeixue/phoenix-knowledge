package com.ginkgocap.ywxt.knowledge.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ginkgocap.ywxt.knowledge.model.UserPermissionMongo;

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
			long send_uid, int type, String mento,  short column_type,long column_id);
	/**
	 * 新增知识，把知识ID，栏目ID，存入用户权限表
	 * 
	 * @param knowledgeRCategory
	 * @return
	 */
	int insertUserPermission(long[] receive_uid, long knowledgeid,
			long send_uid, int type, String mento,  short column_type,long column_id,String title,String desc,String picPath,String tags);

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
	 * @param type
	 *            -1子页
	 * @return
	 */
	List<Long> selectByParams(Long receive_user_id, Long column_id, Long type);

	int deleteUserPermission(long knowledgeid, long userid);
	
	/**
	 * 获取我的分享
	 * @param userId 用户id
	 * @param start start
 	 * @param pageSize step  (为0时查询全部数据)
	 * @return
	 */
	public Map<String,Object> getMyShare(Long userId,int start,int pageSize);
	
	public Map<String,Object> getShareme(Long userId,int start,int pageSize);
}
