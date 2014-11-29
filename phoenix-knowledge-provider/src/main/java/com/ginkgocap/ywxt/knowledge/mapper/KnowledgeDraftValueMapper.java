package com.ginkgocap.ywxt.knowledge.mapper;

import org.apache.ibatis.annotations.Param;

public interface KnowledgeDraftValueMapper {

	int deleteKnowledge(@Param("knowledgeids") long[] knowledgeids,
			@Param("userid") long userid);
}