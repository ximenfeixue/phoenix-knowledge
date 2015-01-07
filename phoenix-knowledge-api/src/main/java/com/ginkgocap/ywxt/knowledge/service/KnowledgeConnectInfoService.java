package com.ginkgocap.ywxt.knowledge.service;

import java.util.Map;


/**
 * 知识关联信息Service
 * 
 * @author caihe
 * 
 */
public interface KnowledgeConnectInfoService {

	Map<String, Object> insertKnowledgeConnectInfo(String knowledgeasso,Long knowledgeId);
	
	void deleteKnowledgeConnectInfo(Long knowledgeid);
	
}
