package com.ginkgocap.ywxt.knowledge.mapper;

import com.ginkgocap.ywxt.knowledge.entity.ColumnVisible;
import com.ginkgocap.ywxt.knowledge.entity.ColumnVisibleExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ColumnVisibleMapper {
    int countByExample(ColumnVisibleExample example);

    int deleteByExample(ColumnVisibleExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ColumnVisible record);

    int insertSelective(ColumnVisible record);

    List<ColumnVisible> selectByExample(ColumnVisibleExample example);

    ColumnVisible selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ColumnVisible record, @Param("example") ColumnVisibleExample example);

    int updateByExample(@Param("record") ColumnVisible record, @Param("example") ColumnVisibleExample example);

    int updateByPrimaryKeySelective(ColumnVisible record);

    int updateByPrimaryKey(ColumnVisible record);
}