package com.ginkgocap.ywxt.knowledge.mapper;

import java.util.List;

import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCategory;

public interface KnowledgeCategoryValueMapper {

	int batchInsert(List<KnowledgeCategory> list);
}