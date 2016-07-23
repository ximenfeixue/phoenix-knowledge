package com.ginkgocap.ywxt.knowledge.v1;

import com.ginkgocap.ywxt.knowledge.model.common.KnowledgeDetail;

import java.util.List;

/**
 * @Title: 知识详细表
 * @Description: 存储知识详细，使用mongoDB进行存储
 * @date 2016年1月11日 下午2:31:19
 * @version V1.0.0
 */
public interface KnowledgeMongoDaoV1 {
	
	/**
	 * 默认数据库表名，一般用作数据表的前缀
	 */
	public final static String KNOWLEDGE_COLLECTION_NAME = "knowledge";
	
	/**
	 * 用户自行录入的数据，表名后缀
	 */
	public final static String KNOWLEDGE_COLLECTION_USERSELF_NAME = "UserSelf";
	
	/**
	 * 插入
	 * @date 2016年1月13日 上午10:54:20
	 * @param knowledgeDetail
	 * @return
	 * @throws Exception
	 */
	public KnowledgeDetail insert(KnowledgeDetail knowledgeDetail) throws Exception;
	
	/**
	 * 批量插入
	 * @date 2016年1月13日 下午4:24:56
	 * @param knowledgeDetailList
	 * @return
	 * @throws Exception
	 */
	public List<KnowledgeDetail> insertList(List<KnowledgeDetail> knowledgeDetailList) throws Exception;
	
	/**
	 * 更新
	 * @date 2016年1月13日 上午10:54:29
	 * @param knowledgeDetail
	 * @return
	 * @throws Exception
	 */
	public KnowledgeDetail update(KnowledgeDetail knowledgeDetail);
	
	/**
	 * 先删除后插入
	 * @date 2016年1月13日 上午10:54:44
	 * @param knowledgeDetail
	 * @return
	 * @throws Exception
	 */
	public KnowledgeDetail insertAfterDelete(KnowledgeDetail knowledgeDetail) throws Exception;
	
	/**
	 * 根据主键及栏目删除数据
	 * @date 2016年1月13日 上午10:54:47
	 * @param id
	 * @param columnId
	 * @return
	 * @throws Exception
	 */
	public int deleteByIdAndColumnId(long id,int columnId) throws Exception;
	
	/**
	 * 根据主键list以及栏目批量删除数据
	 * @date 2016年1月13日 上午10:54:50
	 * @param ids
	 * @param columnId
	 * @return
	 * @throws Exception
	 */
	public int deleteByIdsAndColumnId(List<Long> ids,int columnId) throws Exception;
	
	/**
	 * 根据用户Id以及栏目删除数据
	 * @date 2016年1月13日 上午10:54:53
	 * @param createUserId
	 * @param columnId
	 * @return
	 * @throws Exception
	 */
	public int deleteByCreateUserIdAndColumnId(long createUserId,int columnId) throws Exception;

	/**
	 *
	 * @date 2016年1月13日 上午10:54:53
	 * @param knowledgeId
	 * @param directoryId
	 * @return
	 * @throws Exception
	 */
	public boolean deleteKnowledgeDirectory(long knowledgeId,int columnId,long directoryId);
	
	/**
	 * 根据主键以及栏目提取数据
	 * @date 2016年1月13日 上午10:54:56
	 * @param id
	 * @param columnId
	 * @return
	 * @throws Exception
	 */
	public KnowledgeDetail getByIdAndColumnId(long id,int columnId);
	
	/**
	 * 根据主键list以及栏目提取数据
	 * @date 2016年1月13日 上午10:54:58
	 * @param ids
	 * @param columnId
	 * @return
	 * @throws Exception
	 */
	public List<KnowledgeDetail> getByIdsAndColumnId(List<Long> ids,int columnId) throws Exception;

	/**
	 *
	 * @date 2016年1月13日 上午10:54:58
	 * @param userId
	 * @param start
	 * @param size
	 * @throws Exception
	 */
	public List<KnowledgeDetail> getNoDirectory(long userId,int start,int size);
}