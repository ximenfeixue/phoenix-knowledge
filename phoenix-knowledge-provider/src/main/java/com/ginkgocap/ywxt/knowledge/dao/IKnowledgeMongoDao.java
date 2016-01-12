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
	public final static String KNOWLEDGE_COLLECTION_NAME = "knowledge";
	public KnowledgeMongo insert(KnowledgeMongo KnowledgeMongo,long knowledgeId,User user) throws Exception;
	public KnowledgeMongo update(KnowledgeMongo KnowledgeMongo,User user) throws Exception;
	public KnowledgeMongo insertAfterDelete(KnowledgeMongo KnowledgeMongo,long knowledgeId,User user) throws Exception;
	public int deleteById(long id) throws Exception;
	public int deleteByIds(List<Long> ids) throws Exception;
	public int deleteByCreateUserId(long createUserId) throws Exception;
	public KnowledgeMongo getById(long id) throws Exception;
}