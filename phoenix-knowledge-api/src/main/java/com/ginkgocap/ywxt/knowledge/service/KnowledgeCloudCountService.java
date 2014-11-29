package com.ginkgocap.ywxt.knowledge.service;

import java.util.Map;

import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.user.model.User;


/**
 * 知识Service
 * 
 * @author zhangwei
 * 
 */
public interface KnowledgeCloudCountService {
	
	public void countReader(Long kid);
	
	public Map<String,Object> isHavePremisson(Knowledge knowledge,User user);
	
}
