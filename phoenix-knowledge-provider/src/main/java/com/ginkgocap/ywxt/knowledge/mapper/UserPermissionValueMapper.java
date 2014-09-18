package com.ginkgocap.ywxt.knowledge.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ginkgocap.ywxt.knowledge.entity.UserPermission;

public interface UserPermissionValueMapper {

    int batchInsert(List<UserPermission> list);

    List<Long> selectByParamsSingle(Long receive_user_id, Long column_id);

	int delete(@Param("knowledgeids") long[] knowledgeids,
			@Param("userid") long userid);

	List<Map<String, Object>> selectByreceive_user_id(
			@Param("receive_user_id") long receive_user_id,
			@Param("send_userid") long send_userid);

	List<Map<String, Object>> selectByParams(
			@Param("receive_user_id") long receive_user_id,
			@Param("column_id") long column_id, @Param("type") long type);
}
