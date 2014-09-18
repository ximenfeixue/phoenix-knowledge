package com.ginkgocap.ywxt.knowledge.mapper;

import org.apache.ibatis.annotations.Param;

public interface ColumnKnowledgeValueMapper {

	int deleteKnowledge(@Param("knowledgeids") long[] knowledgeids,
			@Param("columnid") long columnid);
}