/**
 * 
 */
package com.ginkgocap.ywxt.knowledge.admin.service;

import java.util.Map;

import com.ginkgocap.ywxt.user.form.DataGridModel;

import net.sf.json.JSONObject;

/**
 * @author liubang
 *
 */
public interface KnowledgeAdminService {
	/**
	 * 获取非金桐脑资讯分页列表
	 * @param start
	 * @param size
	 * @param map 查询条件
	 * @return
	 */
	Map<String,Object> selectByParam(DataGridModel dgm,Map<String,String> map);
	/**
	 * 通过id获取单条资讯
	 * @param id
	 * @return 
	 * @return
	 */
	public Object selectById(long id,String collectionName);
	/**
	 * 删除行业
	 * @param id
	 */
	void update(long id,String title,String cpathid,String content,String desc,String tags,String collectionName);
	
	/**
	 * 审核行业
	 * @param id
	 * @param status
	 */
	void checkStatusById(long id, int status,String collectionNames);
}
