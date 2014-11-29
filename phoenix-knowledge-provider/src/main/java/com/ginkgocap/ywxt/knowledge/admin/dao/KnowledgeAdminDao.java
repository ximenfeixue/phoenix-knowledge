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
	 * 审核行业
	 * @param id
	 * @param status
	 */
	void checkStatusById(long id, int status,String collectionNames);
	/**
	 * 修改知识
	 * @param id 知识id
	 * @param title 标题
	 * @param cpathid 栏目
	 * @param content 内容
	 * @param tags 标签
	 * @param collectionName 知识类型
	 * liubang
	 * @param collectionName2 
	 */
	void update(long id, String title, String cpathid, String content, String desc,
			String tags, String collectionName);
}
