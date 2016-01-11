package com.ginkgocap.ywxt.knowledge.dao;

import java.util.Date;
import java.util.List;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeBase;

/**
 * @Title: 知识基础信息表
 * @Description: 存储知识的简要信息
 * @author 周仕奇
 * @date 2016年1月11日 下午2:31:19
 * @version V1.0.0
 */
public interface IKnowledgeBaseDao {
	
	/**
	 * 插入
	 * @author 周仕奇
	 * @date 2016年1月11日 下午6:05:29
	 * @param knowledgeBase
	 * @return
	 * @throws Exception
	 */
	public KnowledgeBase insert(KnowledgeBase knowledgeBase) throws Exception;
	
	/**
	 * 更新
	 * @author 周仕奇
	 * @date 2016年1月11日 下午6:05:40
	 * @param knowledgeBase
	 * @return
	 * @throws Exception
	 */
	public KnowledgeBase update(KnowledgeBase knowledgeBase) throws Exception;
	
	/**
	 * 先删除后插入（删除时根据主键删除）
	 * @author 周仕奇
	 * @date 2016年1月11日 下午6:05:47
	 * @param knowledgeBase
	 * @return
	 * @throws Exception
	 */
	public KnowledgeBase insertAfterDelete(KnowledgeBase knowledgeBase) throws Exception;
	
	/**
	 * 根据主键删除
	 * @author 周仕奇
	 * @date 2016年1月11日 下午6:06:00
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public int deleteById(long id) throws Exception;
	
	/**
	 * 根据创建用户ID删除
	 * @author 周仕奇
	 * @date 2016年1月11日 下午6:09:54
	 * @param createUserId
	 * @return
	 * @throws Exception
	 */
	public int deleteByCreateUserId(long createUserId) throws Exception;
	
	/**
	 * 根据主键提取
	 * @author 周仕奇
	 * @date 2016年1月11日 下午6:06:13
	 * @param knowledgeBase
	 * @return
	 * @throws Exception
	 */
	public KnowledgeBase getById(long id) throws Exception;
	
	/**
	 * 根据用户ID提取
	 * @author 周仕奇
	 * @date 2016年1月11日 下午6:08:37
	 * @param createUserId
	 * @return
	 * @throws Exception
	 */
	public List<KnowledgeBase> getByCreateUserId(long createUserId) throws Exception;
	
	/**
	 * 根据创建用户与知识类型提取
	 * @author 周仕奇
	 * @date 2016年1月11日 下午6:33:07
	 * @param createUserId
	 * @param type 知识类型（0：系统创建，1：用户创建）
	 * @return
	 * @throws Exception
	 */
	public List<KnowledgeBase> getByCreateUserIdAndType(long createUserId,String type) throws Exception;
	
	/**
	 * 根据创建用户与栏目提取
	 * @author 周仕奇
	 * @date 2016年1月11日 下午6:34:35
	 * @param createUserId
	 * @param columnId
	 * @return
	 * @throws Exception
	 */
	public List<KnowledgeBase> getByCreateUserIdAndColumnId(long createUserId,long columnId) throws Exception;
	
	/**
	 * 根据栏目提取
	 * @author 周仕奇
	 * @date 2016年1月11日 下午6:35:03
	 * @param columnId
	 * @return
	 * @throws Exception
	 */
	public List<KnowledgeBase> getByColumnId(long columnId) throws Exception;
	
	/**
	 * 根据类型与栏目提取
	 * @author 周仕奇
	 * @date 2016年1月11日 下午6:35:13
	 * @param type
	 * @param columnId
	 * @return
	 * @throws Exception
	 */
	public List<KnowledgeBase> getByTypeAndColumnId(String type,long columnId) throws Exception;
	
	/**
	 * 根据创建用户、类型、栏目提取
	 * @author 周仕奇
	 * @date 2016年1月11日 下午6:35:29
	 * @param createUserId
	 * @param type
	 * @param columnId
	 * @return
	 * @throws Exception
	 */
	public List<KnowledgeBase> getByCreateUserIdAndTypeAndColumnId(long createUserId,String type,long columnId) throws Exception;
	
	/**
	 * 获取某个时间段之间的知识
	 * @author 周仕奇
	 * @date 2016年1月11日 下午6:35:47
	 * @param beginDate 起始时间，当为null时，系统默认为无限小的一个时间
	 * @param endDate 结束时间，当为null时，系统默认为无限大的一个时间
	 * @return
	 * @throws Exception
	 */
	public List<KnowledgeBase> getByBetweenCreateDate(Date beginDate,Date endDate) throws Exception;
	
	/**
	 * 根据知识类型获取某个时间段之间的知识
	 * @author 周仕奇
	 * @date 2016年1月11日 下午6:49:16
	 * @param type
	 * @param beginDate
	 * @param endDate
	 * @return
	 * @throws Exception
	 */
	public List<KnowledgeBase> getByTypeAndBetweenCreateDate(String type,Date beginDate,Date endDate) throws Exception;
	
	/**
	 * 根据创建用户Id获取某个时间段之间的知识
	 * @author 周仕奇
	 * @date 2016年1月11日 下午6:47:30
	 * @param createUserId
	 * @param beginDate
	 * @param endDate
	 * @return
	 * @throws Exception
	 */
	public List<KnowledgeBase> getByCreateUserIdAndBetweenCreateDate(long createUserId,Date beginDate,Date endDate) throws Exception;
	
	/**
	 * 根据用户Id、栏目获取某个时间段之间的知识
	 * @author 周仕奇
	 * @date 2016年1月11日 下午6:47:34
	 * @param createUserId
	 * @param columnId
	 * @param beginDate
	 * @param endDate
	 * @return
	 * @throws Exception
	 */
	public List<KnowledgeBase> getByCreateUserIdAndColumnIdAndBetweenCreateDate(long createUserId,long columnId,Date beginDate,Date endDate) throws Exception;
	
}