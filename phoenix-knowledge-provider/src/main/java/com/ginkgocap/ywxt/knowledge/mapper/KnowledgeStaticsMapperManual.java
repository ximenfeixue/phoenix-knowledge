package com.ginkgocap.ywxt.knowledge.mapper;

import org.apache.ibatis.annotations.Param;

public interface KnowledgeStaticsMapperManual {

	int updateStatics(@Param("kid") long kid,
			@Param("commentCount") int commentCount,
			@Param("shareCount") int shareCount,
			@Param("collCount") int collCount,
			@Param("clickCount") int clickCount);

}
