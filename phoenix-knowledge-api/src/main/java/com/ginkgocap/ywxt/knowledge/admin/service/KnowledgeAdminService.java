/**
 * 
 */
package com.ginkgocap.ywxt.knowledge.admin.service;

import java.util.List;
import java.util.Map;

import com.ginkgocap.ywxt.knowledge.entity.UserCategory;
import com.ginkgocap.ywxt.knowledge.model.AdminUserCategory;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNewsVO;
import com.ginkgocap.ywxt.user.form.DataGridModel;
import com.ginkgocap.ywxt.user.model.User;

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
	
	/**
	 * 添加资讯
	 * @param id
	 * @param status
	 */
	void addNews(KnowledgeNewsVO vo,User user);
	
	/**
	 * 查询后台运维系统目录
	 * @param userId
	 * @param sortId
	 * @param type
	 * @return
	 */
	public String listUserCategory(long userId, String sortId,Byte type);
	
	/**
	 * 添加后台运维系统目录
	 * @param category
	 * @return
	 */
	public String insert(AdminUserCategory category);
	
}
