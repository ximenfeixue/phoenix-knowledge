package com.ginkgocap.ywxt.knowledge.dao;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeBase;

import java.util.Date;
import java.util.List;

/**
 * @Title: 知识基础信息表
 * @date 2016年1月11日 下午2:31:19
 * @version V1.0.0
 */
public interface KnowledgeMysqlDao {
	
	/**
	 * 插入
	 * @param knowledgeBase
	 * @return
	 * @throws Exception
	 */
	KnowledgeBase insert(KnowledgeBase knowledgeBase) throws Exception;
	
	/**
	 * 批量插入
	 * @param knowledgeBaseList
	 * @return
	 * @throws Exception
	 */
	List<KnowledgeBase> insertList(List<KnowledgeBase> knowledgeBaseList) throws Exception;
	
	/**
	 * 更新
	 * @param knowledgeBase
	 * @return
	 * @throws Exception
	 */
	boolean update(KnowledgeBase knowledgeBase) throws Exception;

	/**
	 * 更新
	 * @param knowledgeBase
	 * @return
	 * @throws Exception
	 */
	public boolean updateOrigin(KnowledgeBase knowledgeBase) throws Exception;

	/**
	 * 根据主键删除
	 * @param id
	 * @return
	 * @throws Exception
	 */
	int deleteByKnowledgeId(long id) throws Exception;
	
	/**
	 * 根据主键list批量删除
	 * @date 2016年1月12日 上午9:51:21
	 * @return
	 */
	int batchDeleteByIds(List<Long> ids) throws Exception;

	/**
	 * 根据主键list批量删除
	 * @date 2016年1月12日 上午9:51:21
	 * @return
	 */
	boolean logicDeleteByIds(List<Long> ids) throws Exception;

	/**
	 * 根据主键list批量删除
	 * @date 2016年1月12日 上午9:51:21
	 * @return
	 */
	boolean logicRecoveryByIds(List<Long> ids) throws Exception;

	/**
	 * 根据创建用户ID删除
	 * @date 2016年1月11日 下午6:09:54
	 * @param createUserId
	 * @return
	 * @throws Exception
	 */
	int deleteByCreateUserId(long createUserId) throws Exception;
	
	/**
	 * 根据主键提取
	 * @date 2016年1月11日 下午6:06:13
	 * @param knowledgeId
	 * @return
	 * @throws Exception
	 */
	KnowledgeBase getByKnowledgeId(long knowledgeId) throws Exception;

    /**
     * 根据知识Id, 关键字提取
     * @param knowledgeIds
     * @return
     * @throws Exception
     */
    List<KnowledgeBase> getByKnowledgeIdKeyWord(List<Long> knowledgeIds,String keyword) throws Exception;
	/**
	 * 根据主键list批量提取
	 * @date 2016年1月12日 上午9:54:24
	 * @param knowledgeIds
	 * @return
	 * @throws Exception
	 */
	List<KnowledgeBase> getByKnowledgeIds(List<Long> knowledgeIds) throws Exception;
	
	/**
	 * 提取所有数据
	 * @param start
	 * @param size
	 * @return
	 * @throws Exception
	 */
	List<KnowledgeBase> getAllBase(int start,int size) throws Exception;

	/**
	 * 提取所有数据数量
	 * @return
	 * @throws Exception
	 */
	long getAllPublicCount(short permission) throws Exception;
	/**
	 * 提取所有数据
	 * @param start
	 * @param size
	 * @return
	 * @throws Exception
	 */
	List<KnowledgeBase> getAllPublic(int start,int size,short permission) throws Exception;

	/**
	 * 根据用户ID提取
	 * @param createUserId
	 * @param start 分页起始行数
	 * @param size 分页大小
	 * @return
	 * @throws Exception
	 */
	List<KnowledgeBase> getByCreateUserId(long createUserId,int start,int size) throws Exception;

	/**
	 * 根据用户 Name 提取
	 * @param userName
	 * @return int
	 * @throws Exception
	 */
	int countByCreateUserName(String userName) throws Exception;

	/**
	 * 根据用户 Name 提取
	 * @param userName
	 * @param start 分页起始行数
	 * @param size 分页大小
	 * @return int
	 * @throws Exception
	 */
	List<KnowledgeBase> getByCreateUserName(String userName, int start, int size) throws Exception;


	/**
	 * 根据用户 Name 提取
	 * @param title
	 * @return int
	 * @throws Exception
	 */
	public int countByTitle(String title) throws Exception;

	/**
	 * 根据用户 Name 提取
	 * @param title
	 * @param start 分页起始行数
	 * @param size 分页大小
	 * @return int
	 * @throws Exception
	 */
	public List<KnowledgeBase> getByTitle(String title, int start, int size) throws Exception;

	/**
	 * 根据创建用户与知识类型提取
	 * @param UserId
	 * @param type 知识类型（0：系统创建，1：用户创建）
	 * @param start 分页起始行数
	 * @param size 分页大小
	 * @return
	 * @throws Exception
	 */
	List<KnowledgeBase> getByCreateUserIdAndType(long UserId,short type,int start,int size) throws Exception;

	/**
	 * 根据创建用户与栏目提取
	 * @param createUserId
	 * @param columnId
	 * @param start 分页起始行数
	 * @param size 分页大小
	 * @return
	 * @throws Exception
	 */
	List<KnowledgeBase> getByCreateUserIdAndColumnId(long createUserId,int columnId,int start,int size) throws Exception;
	
	/**
	 * 根据栏目提取
	 * @param columnId
	 * @param start 分页起始行数
	 * @param size 分页大小
	 * @return
	 * @throws Exception
	 */
	List<KnowledgeBase> getByColumnId(int columnId,int start,int size) throws Exception;

	/**
	 * 根据栏目提取
	 * @param columnId
	 * @return
	 * @throws Exception
	 */
	long getPublicCountByColumnId(int columnId,short permission) throws Exception;

	/**
	 * 根据栏目提取
	 * @param columnId
	 * @param start 分页起始行数
	 * @param size 分页大小
	 * @return
	 * @throws Exception
	 */
	List<KnowledgeBase> getPublicByColumnId(int columnId,short permission,int start,int size) throws Exception;

	/**
	 * 根据关键字提取
	 * @param keyWord
	 * @return
	 * @throws Exception
	 */
	int countByUserIdKeyWord(long userId,String keyWord) throws Exception;
    /**
     * 根据关键字提取
     * @param keyWord
     * @param start 分页起始行数
     * @param size 分页大小
     * @return
     * @throws Exception
     */
    List<KnowledgeBase> getByUserIdKeyWord(long userId,String keyWord,int start,int size) throws Exception;

    /**
     * 根据栏目提取
     * @param columnId
     * @param keyWord
     * @param start 分页起始行数
     * @param size 分页大小
     * @return
     * @throws Exception
     */
    List<KnowledgeBase> getByColumnIdAndKeyWord(String keyWord,int columnId,int start,int size) throws Exception;

	/**
	 * 根据类型与栏目提取
	 * @param type
	 * @param columnId
	 * @param start 分页起始行数
	 * @param size 分页大小
	 * @return
	 * @throws Exception
	 */
	List<KnowledgeBase> getByTypeAndColumnId(short type,int columnId,int start,int size) throws Exception;
	
	/**
	 * 根据类型提取
	 * @param type
	 * @param start 分页起始行数
	 * @param size 分页大小
	 * @return
	 * @throws Exception
	 */
	List<KnowledgeBase> getByType(short type,int start,int size) throws Exception;
	
	/**
	 * 根据创建用户、类型、栏目提取
	 * @param createUserId
	 * @param type
	 * @param columnId
	 * @param start 分页起始行数
	 * @param size 分页大小
	 * @return
	 * @throws Exception
	 */
	List<KnowledgeBase> getByCreateUserIdAndTypeAndColumnId(long createUserId,short type,int columnId,int start,int size) throws Exception;
	
	/**
	 * 获取某个时间段之间的知识
	 * @param beginDate 起始时间，当为null时，系统默认为无限小的一个时间
	 * @param endDate 结束时间，当为null时，系统默认为无限大的一个时间
	 * @param start 分页起始行数
	 * @param size 分页大小
	 * @return
	 * @throws Exception
	 */
	List<KnowledgeBase> getByBetweenCreateDate(Date beginDate,Date endDate,int start,int size) throws Exception;
	
	/**
	 * 根据知识类型获取某个时间段之间的知识
	 * @param type
	 * @param beginDate
	 * @param endDate
	 * @param start 分页起始行数
	 * @param size 分页大小
	 * @return
	 * @throws Exception
	 */
	List<KnowledgeBase> getByTypeAndBetweenCreateDate(short type,Date beginDate,Date endDate,int start,int size) throws Exception;
	
	/**
	 * 根据创建用户Id获取某个时间段之间的知识
	 * @param createUserId
	 * @param beginDate
	 * @param endDate
	 * @param start 分页起始行数
	 * @param size 分页大小
	 * @return
	 * @throws Exception
	 */
	List<KnowledgeBase> getByCreateUserIdAndBetweenCreateDate(long createUserId,Date beginDate,Date endDate,int start,int size) throws Exception;
	
	/**
	 * 根据用户Id、栏目获取某个时间段之间的知识
	 * @param createUserId
	 * @param columnId
	 * @param beginDate
	 * @param endDate
	 * @param start 分页起始行数
	 * @param size 分页大小
	 * @return
	 * @throws Exception
	 */
	List<KnowledgeBase> getByCreateUserIdAndColumnIdAndBetweenCreateDate(long createUserId,int columnId,Date beginDate,Date endDate,int start,int size) throws Exception;
	
	/**
	 * 根据状态提取
	 * @param status 状态（0为无效/删除，1为有效，2为草稿，3,：回收站）
	 * @param start 分页起始行数
	 * @param size 分页大小
	 * @return
	 * @throws Exception
	 */
	List<KnowledgeBase> getByStatus(short status,int start,int size) throws Exception;
	
	/**
	 * 根据审核状态提取
	 * @param auditStatus 审核状态（0：未通过，1：审核中，2：审核通过）
	 * @param start 分页起始行数
	 * @param size 分页大小
	 * @return
	 * @throws Exception
	 */
	List<KnowledgeBase> getByAuditStatus(short auditStatus,int start,int size) throws Exception;

	/**
	 * 根据举报状态提取
	 * @param reportStatus 举报状态（3：举报审核未通过，即无非法现象，2：举报审核通过，1:未被举报，0：已被举报）
	 * @param start 分页起始行数
	 * @param size 分页大小
	 * @return
	 * @throws Exception
	 */
	List<KnowledgeBase> getByReportStatus(short reportStatus,int start,int size) throws Exception;
	
	/**
	 * 根据用户Id、状态提取
	 * @param createUserId
	 * @param status 状态（0为无效/删除，1为有效，2为草稿，3,：回收站）
	 * @param start 分页起始行数
	 * @param size 分页大小
	 * @return
	 * @throws Exception
	 */
	List<KnowledgeBase> getByCreateUserIdAndStatus(long createUserId,short status,int start,int size) throws Exception;
	
	/**
	 * 根据用户Id、审核状态提取
	 * @param createUserId
	 * @param auditStatus 审核状态（0：未通过，1：审核中，2：审核通过）
	 * @param start 分页起始行数
	 * @param size 分页大小
	 * @return
	 * @throws Exception
	 */
	List<KnowledgeBase> getByCreateUserIdAndAuditStatus(long createUserId,short auditStatus,int start,int size) throws Exception;

	/**
	 * 根据用户Id、举报状态提取
	 * @param createUserId
	 * @param reportStatus 举报状态（3：举报审核未通过，即无非法现象，2：举报审核通过，1:未被举报，0：已被举报）
	 * @param start 分页起始行数
	 * @param size 分页大小
	 * @return
	 * @throws Exception
	 */
	List<KnowledgeBase> getByCreateUserIdAndReportStatus(long createUserId,short reportStatus,int start,int size) throws Exception;
	
	/**
	 * 根据栏目Id、状态提取
	 * @param columnId
	 * @param status 状态（0为无效/删除，1为有效，2为草稿，3,：回收站）
	 * @param start 分页起始行数
	 * @param size 分页大小
	 * @return
	 * @throws Exception
	 */
	List<KnowledgeBase> getByColumnIdAndStatus(int columnId,short status,int start,int size) throws Exception;
	
	/**
	 * 根据栏目Id、审核状态提取
	 * @param auditStatus 审核状态（0：未通过，1：审核中，2：审核通过）
	 * @param start 分页起始行数
	 * @param size 分页大小
	 * @return
	 * @throws Exception
	 */
	List<KnowledgeBase> getByColumnIdAndAuditStatus(int columnId,short auditStatus,int start,int size) throws Exception;

	/**
	 * 根据栏目Id、举报状态提取
	 * @param reportStatus 举报状态（3：举报审核未通过，即无非法现象，2：举报审核通过，1:未被举报，0：已被举报）
	 * @param start 分页起始行数
	 * @param size 分页大小
	 * @return
	 * @throws Exception
	 */
	List<KnowledgeBase> getByColumnIdAndReportStatus(int columnId,short reportStatus,int start,int size) throws Exception;

	/**
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	int getKnowledgeCount(long userId) throws Exception;

	/**
	 * @param type
	 * @param size
	 * @return
	 * @throws Exception
	 */
	List<Long> getKnowledgeIdsByType(short type, int size)  throws Exception;

	/**
	 * @param userId
	 * @param start 分页起始行数
	 * @param size 分页大小
	 * @return
	 * @throws Exception
	 */
	List<KnowledgeBase> getKnowledgeNoDirectory(long userId,int start,int size) throws Exception;

	List getCreatedKnowledgeCountGroupByDay(long userId, long startDate, long endDate);

	int countAllNotModified()  throws Exception;

	List<KnowledgeBase> getAllKnowledgeNotModified(int start,int size) throws Exception;
}