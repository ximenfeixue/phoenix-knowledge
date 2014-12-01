package com.ginkgocap.ywxt.knowledge.service;

import java.util.List;

import com.ginkgocap.ywxt.knowledge.entity.KnowledgeStatics;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNewsVO;

/**
 * 知识统计
 * 
 * @author caihe
 * 
 */
public interface KnowledgeStaticsService {

	/**
	 * 新增知识，把知识相关数据添加到知识统计表内
	 * 
	 * @param knowledgeRCategory
	 * @return
	 */
	int insertKnowledgeStatics(KnowledgeStatics knowledgeStatics);

	/**
	 * 根据知识ID查询数据
	 * 
	 * @param type
	 *            栏目类型
	 * @return
	 */
	KnowledgeStatics selectByknowledgeId(long knowledgeid);

	/**
	 * 初始化知识统计信息
	 * 
	 * @param vo
	 * @param source
	 * @return
	 */
	int initKnowledgeStatics(KnowledgeNewsVO vo, Short source);

	// 删除统计表信息
	int deleteKnowledgeStatics(long knowledgeid);

	/**
	 * 初始化知识统计信息
	 * 
	 * @param vo
	 * @param source
	 * @return
	 */
	int initKnowledgeStatics(long knowledgeid, String title, Short type);

}
