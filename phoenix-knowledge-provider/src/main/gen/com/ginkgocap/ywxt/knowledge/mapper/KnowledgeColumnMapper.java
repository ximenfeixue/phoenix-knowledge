package com.ginkgocap.ywxt.knowledge.mapper;

import com.ginkgocap.ywxt.knowledge.entity.KnowledgeColumn;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeColumnExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface KnowledgeColumnMapper {
    int countByExample(KnowledgeColumnExample example);

    int deleteByExample(KnowledgeColumnExample example);

    int deleteByPrimaryKey(Long id);

    int insert(KnowledgeColumn record);

    int insertSelective(KnowledgeColumn record);

    List<KnowledgeColumn> selectByExample(KnowledgeColumnExample example);

    KnowledgeColumn selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") KnowledgeColumn record, @Param("example") KnowledgeColumnExample example);

    int updateByExample(@Param("record") KnowledgeColumn record, @Param("example") KnowledgeColumnExample example);

    int updateByPrimaryKeySelective(KnowledgeColumn record);

    int updateByPrimaryKey(KnowledgeColumn record);
}