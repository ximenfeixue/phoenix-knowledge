package com.ginkgocap.ywxt.knowledge.mapper;

import com.ginkgocap.ywxt.knowledge.entity.KnowledgeTag;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeTagExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface KnowledgeTagMapper {
    int countByExample(KnowledgeTagExample example);

    int deleteByExample(KnowledgeTagExample example);

    int deleteByPrimaryKey(Long id);

    int insert(KnowledgeTag record);

    int insertSelective(KnowledgeTag record);

    List<KnowledgeTag> selectByExample(KnowledgeTagExample example);

    KnowledgeTag selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") KnowledgeTag record, @Param("example") KnowledgeTagExample example);

    int updateByExample(@Param("record") KnowledgeTag record, @Param("example") KnowledgeTagExample example);

    int updateByPrimaryKeySelective(KnowledgeTag record);

    int updateByPrimaryKey(KnowledgeTag record);
}