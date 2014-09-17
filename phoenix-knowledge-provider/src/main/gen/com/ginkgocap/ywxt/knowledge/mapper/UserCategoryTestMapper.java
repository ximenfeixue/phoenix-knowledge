package com.ginkgocap.ywxt.knowledge.mapper;

import com.ginkgocap.ywxt.knowledge.entity.UserCategoryTest;
import com.ginkgocap.ywxt.knowledge.entity.UserCategoryTestExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserCategoryTestMapper {
    int countByExample(UserCategoryTestExample example);

    int deleteByExample(UserCategoryTestExample example);

    int deleteByPrimaryKey(Long id);

    int insert(UserCategoryTest record);

    int insertSelective(UserCategoryTest record);

    List<UserCategoryTest> selectByExample(UserCategoryTestExample example);

    UserCategoryTest selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") UserCategoryTest record, @Param("example") UserCategoryTestExample example);

    int updateByExample(@Param("record") UserCategoryTest record, @Param("example") UserCategoryTestExample example);

    int updateByPrimaryKeySelective(UserCategoryTest record);

    int updateByPrimaryKey(UserCategoryTest record);
}