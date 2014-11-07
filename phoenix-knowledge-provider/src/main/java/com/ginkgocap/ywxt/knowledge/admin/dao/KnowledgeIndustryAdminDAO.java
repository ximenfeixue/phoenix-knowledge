package com.ginkgocap.ywxt.knowledge.admin.dao;

import java.util.List;
import java.util.Map;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeIndustry;

/**
 * 知识后台的DAO接口
 * 
 * @author fuliwen
 * @创建时间：2014-11-05 16:11
 */
public interface KnowledgeIndustryAdminDAO {
	
	List<KnowledgeIndustry> findAll();
	
	/**
	 * 获取非金桐脑行业分页列表
	 * @param start
	 * @param size
	 * @param map 查询条件
	 * @return
	 */
	Map<String,Object> selectKnowledgeIndustryList(Integer start, Integer size,Map<String,String> map);
	
	/**
	 * 获取非金桐脑行业数量
	 * @return
	 */
	long selectKnowledgeIndustryListCount();
	
	/**
	 * 通过id获取单条行业
	 * @param id
	 * @return
	 */
	KnowledgeIndustry selectKnowledgeIndustryById(long id);
	
	/**
	 * 删除行业
	 * @param id
	 */
	void deleteKnowledgeIndustryById(long id);
	
	/**
	 * 审核行业
	 * @param id
	 * @param status
	 */
	void checkStatusById(long id, int status);
}
