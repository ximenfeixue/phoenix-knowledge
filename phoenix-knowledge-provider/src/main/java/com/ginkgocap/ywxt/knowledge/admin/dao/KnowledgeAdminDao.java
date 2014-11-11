/**
 * 
 */
package com.ginkgocap.ywxt.knowledge.admin.dao;

import java.util.Map;

import net.sf.json.JSONObject;

/**
 * @author liubang
 *
 */
public interface KnowledgeAdminDao {
	/**
	 * 获取非金桐脑资讯分页列表
	 * @param start
	 * @param size
	 * @param map 查询条件
	 * @return
	 */
	Map<String,Object> selectKnowledgeNewsList(Integer start, Integer size,Map<String,String> map);
	/**
	 * 通过id获取单条资讯
	 * @param id
	 * @return 
	 * @return
	 */
	public Object selectKnowledgeNewsById(long id,String collectionName);
	/**
	 * 删除行业
	 * @param id
	 */
	void deleteKnowledgeIndustryById(long id,String collectionName);
	
	/**
	 * 审核行业
	 * @param id
	 * @param status
	 */
	void checkStatusById(long id, int status,String collectionNames);
}
