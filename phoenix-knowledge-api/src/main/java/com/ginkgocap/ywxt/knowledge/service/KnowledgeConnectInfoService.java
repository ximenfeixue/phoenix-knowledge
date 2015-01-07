package com.ginkgocap.ywxt.knowledge.service;

import java.util.List;
import java.util.Map;

import com.ginkgocap.ywxt.knowledge.entity.ConnectionInfo;

/**
 * 知识关联信息Service
 * 
 * @author caihe
 * 
 */
public interface KnowledgeConnectInfoService {

	Map<String, Object> insertKnowledgeConnectInfo(String knowledgeasso,
			Long knowledgeId);

	void deleteKnowledgeConnectInfo(Long knowledgeid);

}
