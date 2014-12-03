package com.ginkgocap.ywxt.knowledge.mapper;

import com.ginkgocap.ywxt.knowledge.entity.ColumnKnowledge;
import com.ginkgocap.ywxt.knowledge.entity.ColumnKnowledgeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ColumnKnowledgeMapper {
    int countByExample(ColumnKnowledgeExample example);

    int deleteByExample(ColumnKnowledgeExample example);

    int deleteByPrimaryKey(Long knowledgeId);

    int insert(ColumnKnowledge record);

    int insertSelective(ColumnKnowledge record);

    List<ColumnKnowledge> selectByExample(ColumnKnowledgeExample example);

    ColumnKnowledge selectByPrimaryKey(Long knowledgeId);

    int updateByExampleSelective(@Param("record") ColumnKnowledge record, @Param("example") ColumnKnowledgeExample example);

    int updateByExample(@Param("record") ColumnKnowledge record, @Param("example") ColumnKnowledgeExample example);

    int updateByPrimaryKeySelective(ColumnKnowledge record);

    int updateByPrimaryKey(ColumnKnowledge record);
}