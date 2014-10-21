package com.ginkgocap.ywxt.knowledge.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCategory;

public interface KnowledgeCollectionValueMapper {

	int batchInsert(List<KnowledgeCategory> list);

	int deleteKnowledge(@Param("knowledgeids") long[] knowledgeids,
			@Param("categoryid") long categoryid);

	List<Map<String, Object>> selectKnowledgeCollection(
			@Param("column_id") long column_id,
			@Param("knowledgeType") String knowledgeType,
			@Param("category_id") long category_id,
			@Param("pageno") int pageno, @Param("pagesize") int pagesize);
	
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
    List selectKnowledgeAll(@Param("source") String source, @Param("knowledgeType") String knowledgeType,
            @Param("collectionUserId") long collectionUserId, @Param("sortId") String sortId,
            @Param("pageno") int pageno, @Param("pagesize") int pagesize);

    /**
     * 统计所有收藏的知识
     * @param source 6种来源
     * @param knowledgeType 11种类型
     * @param collectionUserId 当前知识收藏的用户
     * @return
     */
    long countKnowledgeAll(@Param("source") String source, @Param("knowledgeType") String knowledgeType,
            @Param("collectionUserId") long collectionUserId, @Param("sortId") String sortId);
}