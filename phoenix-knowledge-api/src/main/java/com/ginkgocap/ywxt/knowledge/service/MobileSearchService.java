package com.ginkgocap.ywxt.knowledge.service;

import java.util.Map;

public interface MobileSearchService {

	/** 关键词搜索*/
	public Map<String, Object> searchByKeywords(Long userid, String keywords,
			String scope, String pno, String pszie);

	/** 根据标签与关键字搜索 */
	public Map<String, Object> selectKnowledgeByTagsAndkeywords(Long userid,
			String keywords, String scope, String tag, int page, int size);

	/** 根据我的收藏与关键字搜索 */
	public Map<String, Object> selectKnowledgeByMyCollectionAndkeywords(
			Long userid, String keywords, String scope, int page, int size);
}
