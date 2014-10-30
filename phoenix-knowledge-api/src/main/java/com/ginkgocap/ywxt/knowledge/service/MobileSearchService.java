package com.ginkgocap.ywxt.knowledge.service;

import java.util.Map;

public interface MobileSearchService {

	/** 关键词搜索 */
	public Map<String, Object> searchByKeywords(Long userid, String keywords,
			String scope, String pno, String pszie);

	/** 根据标签与关键字搜索 */
	public Map<String, Object> selectKnowledgeByTagsAndkeywords(Long userid,
			String keywords, String scope, String tag, int page, int size);

	/** 根据我的收藏与关键字搜索 */
	public Map<String, Object> selectKnowledgeByMyCollectionAndkeywords(
			Long userid, String keywords, String scope, int page, int size);

	/** 分享给我的并按关键字搜索 */
	public Map<String, Object> selectShareMeByKeywords(Long userid,
			String keywords, String scope, int page, int size);

	/** 范围性搜索数据,来源&栏目版的知识数据 */
	public Map<String, Object> selectKnowledgeBySourceAndColumn(Long userid,
			long columnId, String scope, int page, int size);
}
