package com.ginkgocap.ywxt.knowledge.mapper;

import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCategory;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCategoryExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface KnowledgeCategoryMapper {
    int countByExample(KnowledgeCategoryExample example);

    int deleteByExample(KnowledgeCategoryExample example);

    int deleteByPrimaryKey(Long id);

    int insert(KnowledgeCategory record);

    int insertSelective(KnowledgeCategory record);

    List<KnowledgeCategory> selectByExample(KnowledgeCategoryExample example);

    KnowledgeCategory selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") KnowledgeCategory record, @Param("example") KnowledgeCategoryExample example);

    int updateByExample(@Param("record") KnowledgeCategory record, @Param("example") KnowledgeCategoryExample example);

    int updateByPrimaryKeySelective(KnowledgeCategory record);

    int updateByPrimaryKey(KnowledgeCategory record);
}