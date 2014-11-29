package com.ginkgocap.ywxt.knowledge.mapper;

import org.apache.ibatis.annotations.Param;


public interface KnowledgeStaticsMapperManual {

	/**
	 * 修改统计表数据
	 * 
	 * @param kid
	 * @param commentCount
	 *            评论数 Constants.StaticsValue.commentCount.v
	 * @param shareCount
	 *            分享数
	 * @param collCount
	 *            收藏数
	 * @param clickCount
	 *            点击数
	 * @return
	 */
	int updateStatics(@Param("kid") long kid,
			@Param("commentCount") int commentCount,
			@Param("shareCount") int shareCount,
			@Param("collCount") int collCount,
			@Param("clickCount") int clickCount);
}
