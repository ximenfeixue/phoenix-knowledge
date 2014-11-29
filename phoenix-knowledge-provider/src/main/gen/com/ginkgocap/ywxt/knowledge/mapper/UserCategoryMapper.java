package com.ginkgocap.ywxt.knowledge.mapper;

import com.ginkgocap.ywxt.knowledge.entity.UserCategory;
import com.ginkgocap.ywxt.knowledge.entity.UserCategoryExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserCategoryMapper {
    int countByExample(UserCategoryExample example);

    int deleteByExample(UserCategoryExample example);

    int deleteByPrimaryKey(Long id);

    int insert(UserCategory record);

    int insertSelective(UserCategory record);

    List<UserCategory> selectByExample(UserCategoryExample example);

    UserCategory selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") UserCategory record, @Param("example") UserCategoryExample example);

    int updateByExample(@Param("record") UserCategory record, @Param("example") UserCategoryExample example);

    int updateByPrimaryKeySelective(UserCategory record);

    int updateByPrimaryKey(UserCategory record);
}