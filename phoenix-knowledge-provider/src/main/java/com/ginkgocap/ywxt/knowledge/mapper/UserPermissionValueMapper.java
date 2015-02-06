package com.ginkgocap.ywxt.knowledge.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ginkgocap.ywxt.knowledge.entity.UserPermission;

public interface UserPermissionValueMapper {

    int batchInsert(List<UserPermission> list);

    List<Long> selectByParamsSingle(@Param("receive_user_id")Long receive_user_id, @Param("column_type")Long column_type);
    List<Long> selectPlatform(@Param("receive_user_id")Long receive_user_id, @Param("column_type")Long column_type);
    List<String> selectVisble(@Param("receive_user_id")Long receive_user_id, @Param("column_type")Long column_type);
    List<Long> selectVisbleColumnid(@Param("receive_user_id")Long receive_user_id, @Param("column_type")Long column_type);

	int delete(@Param("knowledgeids") long[] knowledgeids,
			@Param("userid") long userid);

	List<Map<String, Object>> selectByreceive_user_id(
			@Param("receive_user_id") long receive_user_id,
			@Param("send_userid") long send_userid);

//	List<Map<String, Object>> selectByParams(
//	@Param("receive_user_id") long receive_user_id,
//	@Param("column_id") long column_id, @Param("type") long type);
	
	/**
	 * 获取分享给全平台的知识id列表，大乐和中乐
	 * @param receive_user_id
	 * @param columnIds
	 * @return
	 */
	List<Long> selectKnowledgeIdsByParams(@Param("receive_user_id") Long receive_user_id,@Param("send_user_id") Long send_user_id, @Param("columnIds") List<String> columnIds);
	
	/**
	 * 通过发送者id和栏目id获取相应权限
	 * @param send_user_id 分享者ID
	 * @param columnid	知识栏目ID
	 * @param type	分享类型
	 * @return
	 */
	List<UserPermission> selectPermissionsByParams(@Param("send_user_id") long send_user_id, @Param("column_id") long column_id, @Param("type") Integer type);
	
	/**
	 * 更新权限表中的栏目，用于栏目删除时，将相应栏目归类到未分组栏目下
	 * @param new_column_id
	 * @param send_user_id
	 * @param old_column_id
	 * @param column_type
	 * @return
	 */
	long batchUpdateColumn(@Param("new_column_id") long new_column_id, 
							@Param("send_user_id") long send_user_id, 
							@Param("old_column_id") long old_column_id,
							@Param("column_type") long column_type );
}
