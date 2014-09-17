package com.ginkgocap.ywxt.knowledge.mapper;

import com.ginkgocap.ywxt.knowledge.entity.KnowledgeStatics;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeStaticsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface KnowledgeStaticsMapper {
    int countByExample(KnowledgeStaticsExample example);

    int deleteByExample(KnowledgeStaticsExample example);

    int deleteByPrimaryKey(Long knowledgeId);

    int insert(KnowledgeStatics record);

    int insertSelective(KnowledgeStatics record);

    List<KnowledgeStatics> selectByExample(KnowledgeStaticsExample example);

    KnowledgeStatics selectByPrimaryKey(Long knowledgeId);

    int updateByExampleSelective(@Param("record") KnowledgeStatics record, @Param("example") KnowledgeStaticsExample example);

    int updateByExample(@Param("record") KnowledgeStatics record, @Param("example") KnowledgeStaticsExample example);

    int updateByPrimaryKeySelective(KnowledgeStatics record);

    int updateByPrimaryKey(KnowledgeStatics record);
}