package com.ginkgocap.ywxt.knowledge.mapper;

import com.ginkgocap.ywxt.knowledge.entity.KnowledgeBookBookmark;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeBookBookmarkExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface KnowledgeBookBookmarkMapper {
    int countByExample(KnowledgeBookBookmarkExample example);

    int deleteByExample(KnowledgeBookBookmarkExample example);

    int deleteByPrimaryKey(Long id);

    int insert(KnowledgeBookBookmark record);

    int insertSelective(KnowledgeBookBookmark record);

    List<KnowledgeBookBookmark> selectByExample(KnowledgeBookBookmarkExample example);

    KnowledgeBookBookmark selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") KnowledgeBookBookmark record, @Param("example") KnowledgeBookBookmarkExample example);

    int updateByExample(@Param("record") KnowledgeBookBookmark record, @Param("example") KnowledgeBookBookmarkExample example);

    int updateByPrimaryKeySelective(KnowledgeBookBookmark record);

    int updateByPrimaryKey(KnowledgeBookBookmark record);
}