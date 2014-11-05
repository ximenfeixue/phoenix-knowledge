package com.ginkgocap.ywxt.knowledge.admin.service;

import java.util.List;
import java.util.Map;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeNews;
import com.ginkgocap.ywxt.user.form.DataGridModel;

/**
 * 知识后台Service
 * 
 * @author fuliwen
 * 
 */
public interface KnowledgeNewsAdminService {

	
	/**
	 * 后台获取所有数据
	 * @return
	 */
	List<KnowledgeNews> selectAll();
	
	/**
	 * 获取所有非金桐闹资讯
	 */
	Map<String,Object> selectAll(DataGridModel dgm,Map<String,Object> map);
	
	/**
	 * 获取单条资讯
	 * @param id
	 * @return 
	 */
	KnowledgeNews selectKnowledgeNewsById(long id);
	
	/**
	 * 删除资讯
	 * @param id
	 */
	void deleteKnowledgeNewsById(long id);
	
	/**
	 * 审核资讯
	 * @param id
	 */
	void checkKnowledgeNewsById(long id, int status);
}
