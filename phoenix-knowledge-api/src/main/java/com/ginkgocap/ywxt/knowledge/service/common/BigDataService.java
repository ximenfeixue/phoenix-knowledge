package com.ginkgocap.ywxt.knowledge.service.common;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeMongo;
import com.gintong.rocketmq.api.utils.FlagTypeUtils;

import java.util.List;

/**
 * @Title: 大数据MQ服务
 * @Description: 向MQ推送数据服务接口
 * @author 周仕奇
 * @date 2016年1月14日 下午3:19:31
 * @version V1.0.0
 */
public interface BigDataService {
	
	/**知识MQ插入*/
	public final static String KNOWLEDGE_INSERT = FlagTypeUtils.createKnowledgeFlag();
	
	/**知识MQ更新*/
	public final static String KNOWLEDGE_UPDATE = FlagTypeUtils.createKnowledgeFlag();
	
	/**知识MQ删除*/
	public final static String KNOWLEDGE_DELETE = FlagTypeUtils.createKnowledgeFlag();
	
	/**
	 * MQ数据发送
	 * @author 周仕奇
	 * @date 2016年1月14日 下午3:19:04
	 * @param optionType 操作类型，插入（KNOWLEDGE_INSERT）、更新（KNOWLEDGE_UPDATE）、删除（KNOWLEDGE_DELETE）
	 * @param knowledgeMongo 发送的数据
	 * @param userId
	 * @throws Exception
	 */
	public void sendMessage(String optionType, KnowledgeMongo knowledgeMongo,long userId) throws Exception;

	/**
	 * MQ数据发送（批量发送）
	 * @author 周仕奇
	 * @date 2016年1月14日 下午6:16:07
	 * @param optionType 操作类型，插入（KNOWLEDGE_INSERT）、更新（KNOWLEDGE_UPDATE）、删除（KNOWLEDGE_DELETE）
	 * @param knowledgeMongoList 发送的数据
	 * @param userId
	 * @throws Exception
	 */
	public void sendMessage(String optionType,List<KnowledgeMongo> knowledgeMongoList, long userId) throws Exception;
	
	/**
	 * MQ数据删除
	 * @author 周仕奇
	 * @date 2016年1月14日 下午6:44:34
	 * @param knowledgeId
	 * @param columnId
	 * @param userId
	 * @throws Exception
	 */
	public void deleteMessage(long knowledgeId,short columnId, long userId) throws Exception;
	
	
}