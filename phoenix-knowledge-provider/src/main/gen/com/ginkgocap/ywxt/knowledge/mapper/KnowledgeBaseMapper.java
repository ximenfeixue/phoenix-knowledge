package com.ginkgocap.ywxt.knowledge.mapper;

import com.ginkgocap.ywxt.knowledge.entity.KnowledgeBase;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeBaseExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface KnowledgeBaseMapper {
    int countByExample(KnowledgeBaseExample example);

    int deleteByExample(KnowledgeBaseExample example);

    int deleteByPrimaryKey(Long knowledgeId);

    int insert(KnowledgeBase record);

    int insertSelective(KnowledgeBase record);

    List<KnowledgeBase> selectByExample(KnowledgeBaseExample example);

    KnowledgeBase selectByPrimaryKey(Long knowledgeId);

    int updateByExampleSelective(@Param("record") KnowledgeBase record, @Param("example") KnowledgeBaseExample example);

    int updateByExample(@Param("record") KnowledgeBase record, @Param("example") KnowledgeBaseExample example);

    int updateByPrimaryKeySelective(KnowledgeBase record);

    int updateByPrimaryKey(KnowledgeBase record);
}