package com.ginkgocap.ywxt.knowledge.mapper;

import com.ginkgocap.ywxt.knowledge.entity.KnowledgeDraft;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeDraftExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface KnowledgeDraftMapper {
    int countByExample(KnowledgeDraftExample example);

    int deleteByExample(KnowledgeDraftExample example);

    int deleteByPrimaryKey(Long knowledgeId);
    
    int deleteByPrimaryKeyAndUserId(KnowledgeDraft knowledgeDraft);

    int insert(KnowledgeDraft record);

    int insertSelective(KnowledgeDraft record);

    List<KnowledgeDraft> selectByExample(KnowledgeDraftExample example);

    KnowledgeDraft selectByPrimaryKey(Long knowledgeId);

    int updateByExampleSelective(@Param("record") KnowledgeDraft record, @Param("example") KnowledgeDraftExample example);

    int updateByExample(@Param("record") KnowledgeDraft record, @Param("example") KnowledgeDraftExample example);

    int updateByPrimaryKeySelective(KnowledgeDraft record);

    int updateByPrimaryKey(KnowledgeDraft record);
}