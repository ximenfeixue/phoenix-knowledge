package com.ginkgocap.ywxt.knowledge.admin.dao;

import java.util.List;
import java.util.Map;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeNews;

/**
 * 知识后台的DAO接口
 * 
 * @author fuliwen
 * @创建时间：2014-11-05 16:11
 */
public interface KnowledgeNewsAdminDAO {
	
	List<KnowledgeNews> findAll();
	
	/**
	 * 获取非金桐脑资讯分页列表
	 * @param start
	 * @param size
	 * @param map 查询条件
	 * @return
	 */
	Map<String,Object> selectKnowledgeNewsList(Integer start, Integer size,Map<String,String> map);
	
	/**
	 * 获取非金桐脑资讯数量
	 * @return
	 */
	long selectKnowledgeNewsListCount();
	
	/**
	 * 通过id获取单条资讯
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
	 * @param status
	 */
	void checkStatusById(long id, int status);
}
