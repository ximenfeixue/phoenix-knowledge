package com.ginkgocap.ywxt.knowledge.mapper;

import com.ginkgocap.ywxt.knowledge.entity.KnowledgeColumnSubscribe;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeColumnSubscribeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface KnowledgeColumnSubscribeMapper {
    int countByExample(KnowledgeColumnSubscribeExample example);

    int deleteByExample(KnowledgeColumnSubscribeExample example);

    int insert(KnowledgeColumnSubscribe record);

    int insertSelective(KnowledgeColumnSubscribe record);

    List<KnowledgeColumnSubscribe> selectByExample(KnowledgeColumnSubscribeExample example);

    int updateByExampleSelective(@Param("record") KnowledgeColumnSubscribe record, @Param("example") KnowledgeColumnSubscribeExample example);

    int updateByExample(@Param("record") KnowledgeColumnSubscribe record, @Param("example") KnowledgeColumnSubscribeExample example);
}