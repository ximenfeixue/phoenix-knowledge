package com.ginkgocap.ywxt.knowledge.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCategory;

public interface KnowledgeCollectionValueMapper {

	int batchInsert(List<KnowledgeCategory> list);

	int deleteKnowledge(@Param("knowledgeids") long[] knowledgeids,
			@Param("categoryid") long categoryid);
}