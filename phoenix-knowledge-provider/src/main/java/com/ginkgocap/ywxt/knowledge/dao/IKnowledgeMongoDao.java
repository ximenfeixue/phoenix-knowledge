package com.ginkgocap.ywxt.knowledge.dao;

import java.util.List;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeMongo;
import com.ginkgocap.ywxt.user.model.User;

/**
 * @Title: 知识详细表
 * @Description: 存储知识详细，使用mongoDB进行存储
 * @author 周仕奇
 * @date 2016年1月11日 下午2:31:19
 * @version V1.0.0
 */
public interface IKnowledgeMongoDao {
	
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
	 * @author 周仕奇
	 * @date 2016年1月13日 上午10:54:20
	 * @param KnowledgeMongo
	 * @param knowledgeId 在MySQL插入知识简表之后返回的知识主键
	 * @param user
	 * @param collectionName 数据表名称（当前参数可传可不传），不传此参数时，将根据columnId构造出表名称
	 * @return
	 * @throws Exception
	 */
	public KnowledgeMongo insert(KnowledgeMongo KnowledgeMongo,long knowledgeId,User user,String... collectionName) throws Exception;
	
	/**
	 * 更新
	 * @author 周仕奇
	 * @date 2016年1月13日 上午10:54:29
	 * @param KnowledgeMongo
	 * @param user
	 * @param collectionName 数据表名称（当前参数可传可不传），不传此参数时，将根据columnId构造出表名称
	 * @return
	 * @throws Exception
	 */
	public KnowledgeMongo update(KnowledgeMongo KnowledgeMongo,User user,String... collectionName) throws Exception;
	
	/**
	 * 先删除后插入
	 * @author 周仕奇
	 * @date 2016年1月13日 上午10:54:44
	 * @param KnowledgeMongo
	 * @param knowledgeId
	 * @param user
	 * @param collectionName 数据表名称（当前参数可传可不传），不传此参数时，将根据columnId构造出表名称
	 * @return
	 * @throws Exception
	 */
	public KnowledgeMongo insertAfterDelete(KnowledgeMongo KnowledgeMongo,long knowledgeId,User user,String... collectionName) throws Exception;
	
	/**
	 * 根据主键及栏目删除数据
	 * @author 周仕奇
	 * @date 2016年1月13日 上午10:54:47
	 * @param id
	 * @param columnId
	 * @param collectionName 数据表名称（当前参数可传可不传），不传此参数时，将根据columnId构造出表名称
	 * @return
	 * @throws Exception
	 */
	public int deleteByIdAndColumnId(long id,long columnId,String... collectionName) throws Exception;
	
	/**
	 * 根据主键list以及栏目批量删除数据
	 * @author 周仕奇
	 * @date 2016年1月13日 上午10:54:50
	 * @param ids
	 * @param columnId
	 * @param collectionName 数据表名称（当前参数可传可不传），不传此参数时，将根据columnId构造出表名称
	 * @return
	 * @throws Exception
	 */
	public int deleteByIdsAndColumnId(List<Long> ids,long columnId,String... collectionName) throws Exception;
	
	/**
	 * 根据用户Id以及栏目删除数据
	 * @author 周仕奇
	 * @date 2016年1月13日 上午10:54:53
	 * @param createUserId
	 * @param columnId
	 * @param collectionName 数据表名称（当前参数可传可不传），不传此参数时，将根据columnId构造出表名称
	 * @return
	 * @throws Exception
	 */
	public int deleteByCreateUserIdAndColumnId(long createUserId,long columnId,String... collectionName) throws Exception;
	
	/**
	 * 根据主键以及栏目提取数据
	 * @author 周仕奇
	 * @date 2016年1月13日 上午10:54:56
	 * @param id
	 * @param columnId
	 * @param collectionName 数据表名称（当前参数可传可不传），不传此参数时，将根据columnId构造出表名称
	 * @return
	 * @throws Exception
	 */
	public KnowledgeMongo getByIdAndColumnId(long id,long columnId,String... collectionName) throws Exception;
	
	/**
	 * 根据主键list以及栏目提取数据
	 * @author 周仕奇
	 * @date 2016年1月13日 上午10:54:58
	 * @param ids
	 * @param columnId
	 * @param collectionName 数据表名称（当前参数可传可不传），不传此参数时，将根据columnId构造出表名称
	 * @return
	 * @throws Exception
	 */
	public List<KnowledgeMongo> getByIdsAndColumnId(List<Long> ids,long columnId,String... collectionName) throws Exception;
}