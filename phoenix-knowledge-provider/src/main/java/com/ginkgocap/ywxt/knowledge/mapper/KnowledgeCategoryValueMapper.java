package com.ginkgocap.ywxt.knowledge.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCategory;

public interface KnowledgeCategoryValueMapper {

    int batchInsert(List<KnowledgeCategory> list);

    @SuppressWarnings("rawtypes")
    List selectKnowledgeIds(@Param("userId") long userId, @Param("type") int type,
            @Param("sortId") String sortId, @Param("gtnid") Long gtnid, @Param("tid") String tid,
            @Param("lid") String lid,@Param("start") int start, @Param("size")int size);

    int countKnowledgeIds(@Param("userId") long userId, @Param("type") int type, @Param("sortId") String sortId,
            @Param("gtnid") Long gtnid, @Param("tid") String tid, @Param("lid") String lid);

    int deleteKnowledge(@Param("knowledgeids") long[] knowledgeids, @Param("categoryid") long categoryid);
}