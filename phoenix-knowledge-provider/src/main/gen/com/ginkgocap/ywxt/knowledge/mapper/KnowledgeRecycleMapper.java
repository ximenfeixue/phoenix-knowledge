package com.ginkgocap.ywxt.knowledge.mapper;

import com.ginkgocap.ywxt.knowledge.entity.KnowledgeRecycle;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeRecycleExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface KnowledgeRecycleMapper {
    int countByExample(KnowledgeRecycleExample example);

    int deleteByExample(KnowledgeRecycleExample example);

    int deleteByPrimaryKey(Long knowledgeId);

    int insert(KnowledgeRecycle record);

    int insertSelective(KnowledgeRecycle record);

    List<KnowledgeRecycle> selectByExample(KnowledgeRecycleExample example);

    KnowledgeRecycle selectByPrimaryKey(Long knowledgeId);

    int updateByExampleSelective(@Param("record") KnowledgeRecycle record, @Param("example") KnowledgeRecycleExample example);

    int updateByExample(@Param("record") KnowledgeRecycle record, @Param("example") KnowledgeRecycleExample example);

    int updateByPrimaryKeySelective(KnowledgeRecycle record);

    int updateByPrimaryKey(KnowledgeRecycle record);
}