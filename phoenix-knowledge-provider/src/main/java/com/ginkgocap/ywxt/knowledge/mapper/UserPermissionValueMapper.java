package com.ginkgocap.ywxt.knowledge.mapper;

import java.util.List;

import com.ginkgocap.ywxt.knowledge.entity.UserPermission;

public interface UserPermissionValueMapper {

	int batchInsert(List<UserPermission> list);
}