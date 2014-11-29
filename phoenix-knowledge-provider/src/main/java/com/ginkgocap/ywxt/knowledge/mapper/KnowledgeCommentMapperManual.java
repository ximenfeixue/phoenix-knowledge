package com.ginkgocap.ywxt.knowledge.mapper;

import org.apache.ibatis.annotations.Param;


public interface KnowledgeCommentMapperManual {

	int updateCountByPrimaryKey(@Param("commentId") long commentId, @Param("count") int count);
}