package com.ginkgocap.ywxt.knowledge.admin.service;

import java.util.List;
import java.util.Map;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeInvestment;
import com.ginkgocap.ywxt.user.form.DataGridModel;

/**
 * 投融工具后台Service
 * 
 * @author fuliwen
 * 
 */
public interface KnowledgeInvestmentAdminService {

	
	/**
	 * 后台获取所有数据
	 * @return
	 */
	List<KnowledgeInvestment> selectAll();
	
	/**
	 * 获取所有非金桐闹投融工具
	 */
	Map<String,Object> selectByParam(DataGridModel dgm,Map<String,String> map);
	
	/**
	 * 获取单条投融工具
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
	 */
	void checkKnowledgeInvestmentById(long id, int status);
}
