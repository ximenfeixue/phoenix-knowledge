package com.ginkgocap.ywxt.knowledge.mapper;

import com.ginkgocap.ywxt.knowledge.entity.KnowledgeComment;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCommentExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface KnowledgeCommentMapper {
    int countByExample(KnowledgeCommentExample example);

    int deleteByExample(KnowledgeCommentExample example);

    int deleteByPrimaryKey(Long id);

    int insert(KnowledgeComment record);

    int insertSelective(KnowledgeComment record);

    List<KnowledgeComment> selectByExample(KnowledgeCommentExample example);

    KnowledgeComment selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") KnowledgeComment record, @Param("example") KnowledgeCommentExample example);

    int updateByExample(@Param("record") KnowledgeComment record, @Param("example") KnowledgeCommentExample example);

    int updateByPrimaryKeySelective(KnowledgeComment record);

    int updateByPrimaryKey(KnowledgeComment record);
}