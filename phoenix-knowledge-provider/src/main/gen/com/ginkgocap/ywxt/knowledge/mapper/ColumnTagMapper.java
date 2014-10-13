package com.ginkgocap.ywxt.knowledge.mapper;

import com.ginkgocap.ywxt.knowledge.entity.ColumnTag;
import com.ginkgocap.ywxt.knowledge.entity.ColumnTagExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ColumnTagMapper {
    int countByExample(ColumnTagExample example);

    int deleteByExample(ColumnTagExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ColumnTag record);

    int insertSelective(ColumnTag record);

    List<ColumnTag> selectByExample(ColumnTagExample example);

    ColumnTag selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ColumnTag record, @Param("example") ColumnTagExample example);

    int updateByExample(@Param("record") ColumnTag record, @Param("example") ColumnTagExample example);

    int updateByPrimaryKeySelective(ColumnTag record);

    int updateByPrimaryKey(ColumnTag record);
}