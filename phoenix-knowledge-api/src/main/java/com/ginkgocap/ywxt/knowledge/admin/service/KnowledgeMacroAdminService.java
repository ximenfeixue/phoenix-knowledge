package com.ginkgocap.ywxt.knowledge.admin.service;

import java.util.List;
import java.util.Map;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeMacro;
import com.ginkgocap.ywxt.user.form.DataGridModel;

/**
 * 宏观后台Service
 * 
 * @author fuliwen
 * 
 */
public interface KnowledgeMacroAdminService {

	
	/**
	 * 后台获取所有数据
	 * @return
	 */
	List<KnowledgeMacro> selectAll();
	
	/**
	 * 获取所有非金桐闹宏观
	 */
	Map<String,Object> selectByParam(DataGridModel dgm,Map<String,String> map);
	
	/**
	 * 获取单条宏观
	 * @param id
	 * @return 
	 */
	KnowledgeMacro selectKnowledgeMacroById(long id);
	
	/**
	 * 删除宏观
	 * @param id
	 */
	void deleteKnowledgeMacroById(long id);
	
	/**
	 * 审核宏观
	 * @param id
	 */
	void checkKnowledgeMacroById(long id, int status);
}
