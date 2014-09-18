package com.ginkgocap.ywxt.knowledge.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ginkgocap.ywxt.knowledge.entity.UserPermission;

public interface UserPermissionValueMapper {

	int batchInsert(List<UserPermission> list);

	int delete(@Param("knowledgeids") long[] knowledgeids,
			@Param("userid") long userid);
}