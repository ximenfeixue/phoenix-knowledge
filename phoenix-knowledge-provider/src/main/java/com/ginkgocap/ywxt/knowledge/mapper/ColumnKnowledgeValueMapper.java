package com.ginkgocap.ywxt.knowledge.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface ColumnKnowledgeValueMapper {

	int deleteKnowledge(@Param("knowledgeids") long[] knowledgeids,
			@Param("columnid") long columnid);
	List<Long> selectKnowledgeIds(@Param("userId")long userId, @Param("sortId")String sortId);
	
	List<Map<String,Long>> selectColumnIdTreeById(@Param("columnid") long columnid);
}