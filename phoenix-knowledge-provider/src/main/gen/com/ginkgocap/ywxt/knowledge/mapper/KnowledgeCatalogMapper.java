package com.ginkgocap.ywxt.knowledge.mapper;

import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCatalog;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCatalogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface KnowledgeCatalogMapper {
    int countByExample(KnowledgeCatalogExample example);

    int deleteByExample(KnowledgeCatalogExample example);

    int deleteByPrimaryKey(Long id);

    int insert(KnowledgeCatalog record);

    int insertSelective(KnowledgeCatalog record);

    List<KnowledgeCatalog> selectByExample(KnowledgeCatalogExample example);

    KnowledgeCatalog selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") KnowledgeCatalog record, @Param("example") KnowledgeCatalogExample example);

    int updateByExample(@Param("record") KnowledgeCatalog record, @Param("example") KnowledgeCatalogExample example);

    int updateByPrimaryKeySelective(KnowledgeCatalog record);

    int updateByPrimaryKey(KnowledgeCatalog record);
}