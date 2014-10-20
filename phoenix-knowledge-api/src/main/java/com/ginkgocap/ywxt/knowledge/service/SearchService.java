package com.ginkgocap.ywxt.knowledge.service;

import java.util.Map;

public interface SearchService {

	/**
	 * 标签查询
	 * 
	 * @param userid
	 *            用户ID
	 * @param type
	 *            　（1-首页热门标签）
	 * @return
	 */
	public Map<String, Object> getUserTag(long userid, String type);

	/**
	 * 关键词搜索
	 * 
	 * @param userid
	 *            用户ID
	 * @param keywords
	 *            关键词
	 * @param scope
	 *            范围
	 * @param pno
	 *            当前页号
	 * @param pszie
	 *            每页请求条数
	 * @return
	 */
	public Map<String, Object> searchByKeywords(long userid, String keywords,
			String scope, String pno, String pszie);

	/**
	 * 标签搜索
	 * 
	 * @param userid
	 *            用户ID
	 * @param keywords
	 *            关键词
	 * @param scope
	 *            范围
	 * @param pno
	 *            页号
	 * @param pszie
	 *            每页请求条数
	 * @return
	 */
	public Map<String, Object> searchTags(long userid, String keywords,
			String scope, String pno, String pszie);

	/**
	 * 分享到金桐脑
	 * 
	 * @param userid
	 *            用户ID
	 * @param kid
	 *            知识ID
	 * @param type
	 *            知识类型
	 * @return
	 */
	public Map<String, Object> shareToJinTN(long userid, long kid, String type);

}
