package com.ginkgocap.ywxt.knowledge.service;

import java.util.List;
import java.util.Map;

import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCollection;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeBaseVO;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeCollectionVO;
import com.ginkgocap.ywxt.user.model.User;

/**
 * 知识收藏
 * 
 * @author caihe
 * 
 */
public interface KnowledgeCollectionService {

	/**
	 * 将知识从收藏夹中删除
	 * 
	 * @param knowledgeRCategory
	 * @return
	 */
	int deleteKnowledgeCollection(long[] knowledgeids, long categoryid);

	/**
	 * 查询收藏夹中的知识
	 * 
	 * @param column_id
	 * @param knowledgeType
	 * @param category_id
	 * @return
	 */
	List<Long> selectKnowledgeCollection(long column_id, String knowledgeType,
			long category_id, int pageno, int pagesize);

	/**
	 * 返回收藏夹下知识的分页信息
	 * 
	 * @param source
	 *            6种来源
	 * @param knowledgeType
	 *            11种类型
	 * @param collectionUserId
	 *            当前知识收藏的用户
	 * @param pageno
	 *            当前页
	 * @param pagesize
	 *            每页大小
	 * @return
	 */
	Map<String, Object> queryKnowledgeAll(String source, String knowledgeType,
			long collectionUserId, String sortId, String keyword, int pageno,
			int pagesize);

	/**
	 * 分页按条件查询收藏的知识
	 * 
	 * @param source
	 *            6种来源
	 * @param knowledgeType
	 *            11种类型
	 * @param collectionUserId
	 *            当前知识收藏的用户
	 * @param pageno
	 *            当前页
	 * @param pagesize
	 *            每页大小
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	List selectKnowledgeAll(String source, String knowledgeType,
			long collectionUserId, int pageno, int pagesize);

	/**
	 * 统计所有收藏的知识
	 * 
	 * @param source
	 *            6种来源
	 * @param knowledgeType
	 *            11种类型
	 * @param collectionUserId
	 *            当前知识收藏的用户
	 * @return
	 */
	long countKnowledgeAll(String source, String knowledgeType,
			long collectionUserId);

	/**
	 * 添加收藏
	 * 
	 * @param vo
	 *            收藏对象
	 * @param user
	 *            用户对象
	 * @return
	 * @author haiyan
	 */
	Map<String, Object> insertKnowledgeCollection(KnowledgeCollectionVO vo,
			User user);

	/**
	 * 是否已收藏
	 * 
	 * @param userId 用户Id
	 * @param kId 知识Id
	 * @return
	 * @author haiyan
	 */
	boolean isCollection(long userId, long kId);

	/**
	 * 删除收藏知识
	 * 
	 * @param userId 用户Id
	 * @param kId 知识Id
	 * @return
	 * @author haiyan
	 */
	boolean delCollection(long userId, long kId);

	/**
	 * 添加基础信息
	 * 
	 * @param vo 
	 * @param user 知识Id
	 * @return
	 * @author haiyan
	 */
	boolean addBaseInfo(KnowledgeBaseVO vo, User user);
	
	/**
	 * 查询已收藏目录
	 * @param userId 用户ID
	 * @param kId 知识ID
	 * @return
	 * @author haiyan
	 */
	List<KnowledgeCollection> queryCollCategoryIds(long userId,long kId);
	
	/**
	 * 批量删除收藏知识
	 * @param userId  用户ID
	 * @param kIds 知识ID集合
	 * @return
	 */
	
	 Map<String, Object> delCollection(long userId, String kIds); 
	 
	 /**
	  * 收藏知识移动
	  * move()： <p>实现知识在本收藏目录树中进行移动移动</p>
	  * @param userId 用户id
	  * @param knowledgeids 知识id(多个以英文逗号分隔)
	  * @param categoryids 目录id(多个以英文逗号分隔)
	  * @return map<String,String> 返回成功失败信息
	  * @author bianzhiwei
	  * @since 2014-11-06
	  */
	 void move(long id, List<Long> knowledgeids, List<Long> categoryids,long cid); 
	
}
