package com.ginkgocap.ywxt.knowledge.mapper;

import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCollection;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCollectionExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface KnowledgeCollectionMapper {
    int countByExample(KnowledgeCollectionExample example);

    int deleteByExample(KnowledgeCollectionExample example);

    int deleteByPrimaryKey(Long id);

    int insert(KnowledgeCollection record);

    int insertSelective(KnowledgeCollection record);

    List<KnowledgeCollection> selectByExample(KnowledgeCollectionExample example);

    KnowledgeCollection selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") KnowledgeCollection record, @Param("example") KnowledgeCollectionExample example);

    int updateByExample(@Param("record") KnowledgeCollection record, @Param("example") KnowledgeCollectionExample example);

    int updateByPrimaryKeySelective(KnowledgeCollection record);

    int updateByPrimaryKey(KnowledgeCollection record);
}