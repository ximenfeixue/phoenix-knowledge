package com.ginkgocap.ywxt.knowledge.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCollection;

/**
 * 知识收藏
 * 
 * @author caihe
 * 
 */
public interface KnowledgeCollectionService {

	/**
	 * 添加收藏
	 * @param kid 知识Id
	 * @param columnid 栏目id
	 * @param type 知识类型（默认0：其他,1：资讯，2：投融工具，3：行业，4：经典案例，5：图书报告，6：资产管理，7：宏观，8：观点，9：判例，10，法律法规，11：文章）
	 * @param source 知识来源(1：自己，2：好友，3：金桐脑，4：全平台，5：组织)
	 * @param categoryid 目录id
	 * @return
	 */
	Map<String, Object> insertKnowledgeCollection(long kid ,long columnid ,String type,String source,long categoryid);

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
     * @param source 6种来源
     * @param knowledgeType 11种类型
     * @param collectionUserId 当前知识收藏的用户
     * @param pageno 当前页
     * @param pagesize 每页大小
     * @return
     */
    Map<String,Object> queryKnowledgeAll(String source, String knowledgeType, long collectionUserId,String sortId, String keyword,int pageno, int pagesize);
    
    /**
     * 分页按条件查询收藏的知识
     * @param source 6种来源
     * @param knowledgeType 11种类型
     * @param collectionUserId 当前知识收藏的用户
     * @param pageno 当前页
     * @param pagesize 每页大小
     * @return
     */
    @SuppressWarnings("rawtypes")
    List selectKnowledgeAll(String source, String knowledgeType, long collectionUserId, int pageno, int pagesize);

    /**
     * 统计所有收藏的知识
     * @param source 6种来源
     * @param knowledgeType 11种类型
     * @param collectionUserId 当前知识收藏的用户
     * @return
     */
    long countKnowledgeAll(String source, String knowledgeType, long collectionUserId);
}
