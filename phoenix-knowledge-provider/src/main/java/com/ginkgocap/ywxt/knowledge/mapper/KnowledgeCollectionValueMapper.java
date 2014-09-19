package com.ginkgocap.ywxt.knowledge.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCategory;

public interface KnowledgeCollectionValueMapper {

	int batchInsert(List<KnowledgeCategory> list);

	int deleteKnowledge(@Param("knowledgeids") long[] knowledgeids,
			@Param("categoryid") long categoryid);

	List<Map<String, Object>> selectKnowledgeCollection(
			@Param("column_id") long column_id,
			@Param("knowledgeType") String knowledgeType,
			@Param("category_id") long category_id,
			@Param("pageno") int pageno, @Param("pagesize") int pagesize);
}