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
	
	void updateKnowledge(KnowledgeNewsVO vo,User user);
	
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
	 * @return 
	 */
	Map<String,Object> addNews(KnowledgeNewsVO vo,User user);
	
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
	
	/**
	 * 编辑目录
	 * @param category
	 */
	public void update(AdminUserCategory category);
	
	
	/**
	 * 主键查询
	 */
	
	public AdminUserCategory selectByPrimaryKey(long id);
	
	/**
	 * 修改标识位
	 * @param id
	 * @param userType
	 */
	public void updateUseType(long id,int userType);
	
	/**
	 * 批量删除知识
	 * @param ids
	 * @param types
	 * @return
	 */
	public Map<String,Object> batchDeleteKnowledge(String knowledgeids);
	
	/**
	 * 查询目录树
	 * @param userid
	 * @param pid
	 * @return
	 */
	public List<AdminUserCategory> selectUserCategory(long userid,Long pid,int  type);
	
}
