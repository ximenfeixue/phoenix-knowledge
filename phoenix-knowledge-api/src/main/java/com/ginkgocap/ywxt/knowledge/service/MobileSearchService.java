package com.ginkgocap.ywxt.knowledge.service;

import java.util.Map;

/**
 * @Description: 移动端官方搜索接口
 * @Author: zhangzhen
 * @CreateDate: 2014-10-20
 * @Version: [v1.0]
 */

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

	/** 我的好友的知识并按关键字过滤分页显示 */
	public Map<String, Object> selectMyFriendKnowledgeByKeywords(
			String friends, long columnId, String scope, int page, int size);
	
	/**
	 *  根据我的栏目和来源查询知识列表
	 *  根据栏目和来源获取知识列表 ( 0-全部;1-金桐脑;2-全平台;4-自己 )
	 *  */
	public Map<String, Object> selectknowledgeByColumnIdAndSource(
			long columnId, long source, String scope, int page, int size);
	
	/** 查询我的所有好友的指定栏目下，所有知识
	 * 根据栏目和来源获取知识列表 ( 3-好友 )
	 *  */
	public Map<String, Object> selectMyFriendknowledgeByColumnId(
			long columnId,long userId, String scope, int page, int size);
}
