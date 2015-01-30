package com.ginkgocap.ywxt.knowledge.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ginkgocap.ywxt.knowledge.entity.ConnectionInfo;

public interface ConnectionInfoValueMapper {

    int insertConnectionInfo(List<ConnectionInfo> list);
    
    List<String> selectTags(@Param("kid") long kid, @Param("connType") int connType);
    
}
