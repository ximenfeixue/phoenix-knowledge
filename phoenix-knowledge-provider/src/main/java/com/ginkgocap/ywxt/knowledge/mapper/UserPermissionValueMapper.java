package com.ginkgocap.ywxt.knowledge.mapper;

import java.util.List;

import com.ginkgocap.ywxt.knowledge.entity.UserPermission;

public interface UserPermissionValueMapper {

    int batchInsert(List<UserPermission> list);

    List<Long> selectByParams(Long receive_user_id, Long column_id, Long type);
    
    List<Long> selectByParamsSingle(Long receive_user_id, Long column_id);
}