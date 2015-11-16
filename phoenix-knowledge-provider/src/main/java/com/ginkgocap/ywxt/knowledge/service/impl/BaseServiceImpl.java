package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.ywxt.knowledge.thread.NoticeThreadPool;
import com.ginkgocap.ywxt.knowledge.util.Constants;

public class BaseServiceImpl{

	@Autowired(required =true)
	private NoticeThreadPool noticeThreadPool;
	/**
	 * 大数据通知接口
	 * 
	 * @param vo
	 * @param noticeType
	 */
	public void noticeDataCenter(String knowledgeType,Long knowledgeId, String noticeType) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("oper", noticeType);
		params.put("type", knowledgeType);
		params.put("kId", knowledgeId);
		noticeThreadPool.noticeDataCenter(Constants.noticeType.knowledge.v(), params);
	}
	
	/**
	 * 
	 */
}
