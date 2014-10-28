package com.ginkgocap.ywxt.knowledge.service;

import java.util.Map;

public interface MobileSearchService {

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
	public Map<String, Object> searchByKeywords(Long userid, String keywords,
			String scope, String pno, String pszie);
	
	public Map<String, Object> selectKnowledgeByTagsAndkeywords(Long userid,
			String keywords, String scope, String tag, int page, int size);
}
