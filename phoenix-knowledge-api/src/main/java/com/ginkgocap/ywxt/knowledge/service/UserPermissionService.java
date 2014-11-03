package com.ginkgocap.ywxt.knowledge.service;

import java.util.List;
import java.util.Map;

import com.ginkgocap.ywxt.knowledge.entity.UserPermission;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNewsVO;
import com.ginkgocap.ywxt.user.model.User;

/**
 * 知识栏目关系
 * 
 * @author
 * 
 */
public interface UserPermissionService {

	/**
	 * 添加知识权限信息
	 * 
	 * @param permList
	 *            接收者ID集合
	 * @param knowledgeid
	 *            知识ID
	 * @param send_uid
	 *            发起者ID
	 * @param shareMessage
	 * @param column_type
	 *            知识类型
	 * @param column_id
	 *            栏目ID
	 * @return
	 */
	int insertUserPermission(List<String> permList, long knowledgeid,
			long send_uid, String shareMessage, short column_type,
			long column_id);

	/**
	 * 如果是全平台，走分享接口
	 * 
	 * @param permList
	 * @return
	 */
	void insertUserShare(List<String> permList, long kId, KnowledgeNewsVO vo,
			User user);

	/**
	 * 新增知识，把知识ID，栏目ID，存入用户权限表
	 * 
	 * @param knowledgeRCategory
	 * @return
	 */
	int insertUserPermission(List<String> permList, long knowledgeid,
			long send_uid, int type, String mento, short column_type,
			long column_id, String title, String desc, String picPath,
			String tags);

	/**
	 * 刪除知识，把用户权限记录
	 * 
	 * @param knowledgeids
	 * @param
	 * @return
	 */
	int deleteUserPermission(long[] knowledgeids, long userid);

	int deleteUserPermission(long knowledgeid);

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
	 * 
	 * @param userId
	 *            用户id
	 * @param start
	 *            start
	 * @param pageSize
	 *            step (为0时查询全部数据)
	 * @return
	 */
	public Map<String, Object> getMyShare(Long userId, int start, int pageSize);

	public Map<String, Object> getShareme(Long userId, int start, int pageSize);

	List<UserPermission> selectUserPermission(long knowledgeid, long userid);

	/**
	 * 
	 * @@param permList 接收者ID集合
	 * @param title
	 *            标题
	 * @param desc
	 *            描述
	 * @param picPath
	 *            图片路径
	 * @param tags
	 *            标签
	 * @param send_uid
	 *            发送人ID
	 * @param type
	 *            类型
	 * @param mento
	 *            分享信息
	 * @param column_type
	 *            栏目类型
	 * @param column_id
	 *            栏目ID
	 * @param knowledgeid
	 *            知识ID
	 * @return
	 */
	public boolean insertUserPermissionMongo(List<Long> receiveList,
			String title, String desc, String picPath, String tags,
			long send_uid, String mento, short column_type, long column_id,
			long knowledgeid);
}
