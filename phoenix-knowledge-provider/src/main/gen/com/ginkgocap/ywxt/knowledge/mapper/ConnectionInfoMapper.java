package com.ginkgocap.ywxt.knowledge.mapper;

import com.ginkgocap.ywxt.knowledge.entity.ConnectionInfo;
import com.ginkgocap.ywxt.knowledge.entity.ConnectionInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ConnectionInfoMapper {
    int countByExample(ConnectionInfoExample example);

    int deleteByExample(ConnectionInfoExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ConnectionInfo record);

    int insertSelective(ConnectionInfo record);

    List<ConnectionInfo> selectByExample(ConnectionInfoExample example);

    ConnectionInfo selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ConnectionInfo record, @Param("example") ConnectionInfoExample example);

    int updateByExample(@Param("record") ConnectionInfo record, @Param("example") ConnectionInfoExample example);

    int updateByPrimaryKeySelective(ConnectionInfo record);

    int updateByPrimaryKey(ConnectionInfo record);
}