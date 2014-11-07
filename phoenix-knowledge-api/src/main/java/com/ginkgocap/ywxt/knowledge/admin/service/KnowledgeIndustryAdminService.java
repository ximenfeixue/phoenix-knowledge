package com.ginkgocap.ywxt.knowledge.admin.service;

import java.util.List;
import java.util.Map;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeIndustry;
import com.ginkgocap.ywxt.user.form.DataGridModel;

/**
 * 行业后台Service
 * 
 * @author fuliwen
 * 
 */
public interface KnowledgeIndustryAdminService {

	
	/**
	 * 后台获取所有数据
	 * @return
	 */
	List<KnowledgeIndustry> selectAll();
	
	/**
	 * 获取所有非金桐闹行业
	 */
	Map<String,Object> selectByParam(DataGridModel dgm,Map<String,String> map);
	
	/**
	 * 获取单条行业
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
	 */
	void checkKnowledgeIndustryById(long id, int status);
}
