package com.ginkgocap.ywxt.knowledge.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCategory;

public interface KnowledgeCategoryValueMapper {

	int batchInsert(List<KnowledgeCategory> list);
	
	List<KnowledgeCategory> selectKnowledgeIds(@Param("userId")long userId, @Param("type")int type, @Param("sortId")String sortId,@Param("gtnid")Long gtnid);

	int deleteKnowledge(@Param("knowledgeids") long[] knowledgeids,
			@Param("categoryid") long categoryid);
}