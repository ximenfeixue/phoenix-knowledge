package com.ginkgocap.ywxt.knowledge.admin.dao;

import java.util.List;
import java.util.Map;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeInvestment;

/**
 * 知识后台的DAO接口
 * 
 * @author fuliwen
 * @创建时间：2014-11-05 16:11
 */
public interface KnowledgeInvestmentAdminDAO {
	
	List<KnowledgeInvestment> findAll();
	
	/**
	 * 获取非金桐脑投融工具分页列表
	 * @param start
	 * @param size
	 * @param map 查询条件
	 * @return
	 */
	Map<String,Object> selectKnowledgeInvestmentList(Integer start, Integer size,Map<String,String> map);
	
	/**
	 * 获取非金桐脑投融工具数量
	 * @return
	 */
	long selectKnowledgeInvestmentListCount();
	
	/**
	 * 通过id获取单条投融工具
	 * @param id
	 * @return
	 */
	KnowledgeInvestment selectKnowledgeInvestmentById(long id);
	
	/**
	 * 删除投融工具
	 * @param id
	 */
	void deleteKnowledgeInvestmentById(long id);
	
	/**
	 * 审核投融工具
	 * @param id
	 * @param status
	 */
	void checkStatusById(long id, int status);
}
