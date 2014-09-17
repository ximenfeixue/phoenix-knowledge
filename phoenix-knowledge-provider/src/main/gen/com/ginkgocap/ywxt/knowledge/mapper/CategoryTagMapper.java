package com.ginkgocap.ywxt.knowledge.mapper;

import com.ginkgocap.ywxt.knowledge.entity.CategoryTag;
import com.ginkgocap.ywxt.knowledge.entity.CategoryTagExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CategoryTagMapper {
    int countByExample(CategoryTagExample example);

    int deleteByExample(CategoryTagExample example);

    int deleteByPrimaryKey(Long id);

    int insert(CategoryTag record);

    int insertSelective(CategoryTag record);

    List<CategoryTag> selectByExample(CategoryTagExample example);

    CategoryTag selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CategoryTag record, @Param("example") CategoryTagExample example);

    int updateByExample(@Param("record") CategoryTag record, @Param("example") CategoryTagExample example);

    int updateByPrimaryKeySelective(CategoryTag record);

    int updateByPrimaryKey(CategoryTag record);
}