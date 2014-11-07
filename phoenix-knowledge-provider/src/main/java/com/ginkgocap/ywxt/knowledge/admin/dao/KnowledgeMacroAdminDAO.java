package com.ginkgocap.ywxt.knowledge.admin.dao;

import java.util.List;
import java.util.Map;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeMacro;

/**
 * 知识后台的DAO接口
 * 
 * @author fuliwen
 * @创建时间：2014-11-05 16:11
 */
public interface KnowledgeMacroAdminDAO {
	
	List<KnowledgeMacro> findAll();
	
	/**
	 * 获取非金桐脑宏观分页列表
	 * @param start
	 * @param size
	 * @param map 查询条件
	 * @return
	 */
	Map<String,Object> selectKnowledgeMacroList(Integer start, Integer size,Map<String,String> map);
	
	/**
	 * 获取非金桐脑宏观数量
	 * @return
	 */
	long selectKnowledgeMacroListCount();
	
	/**
	 * 通过id获取单条宏观
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
	 * @param status
	 */
	void checkStatusById(long id, int status);
}
