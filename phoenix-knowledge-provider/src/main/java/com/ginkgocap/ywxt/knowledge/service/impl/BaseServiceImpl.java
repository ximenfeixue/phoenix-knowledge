package com.ginkgocap.ywxt.knowledge.service.impl;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gintong.rocketmq.api.DefaultMessageService;
import com.gintong.rocketmq.common.FlagTypeUtils;
import com.gintong.rocketmq.common.RocketSendResult;
import com.gintong.rocketmq.common.TopicType;

@Service
public class BaseServiceImpl {

	@Autowired(required = true)
	private DefaultMessageService defaultMessageService ;

	/**
	 * 大数据通知接口
	 * 
	 */
	public void noticeDataCenter(String noticeType, Object bean) {

		String flagType = "";
		if (StringUtils.equals("upd", noticeType)) {
			flagType = FlagTypeUtils.updateKnowledgeFlag();
		} else if (StringUtils.equals("add", noticeType)) {
			flagType = FlagTypeUtils.createKnowledgeFlag();
		} else if (StringUtils.equals("del", noticeType)) {
			flagType = FlagTypeUtils.deleteKnowledgeFlag();
		}
		defaultMessageService.sendMessage(TopicType.KNOWLEDGE_TOPIC, flagType, beanToJson(bean));
	}

	public static String beanToJson(Object bean) {
		JSONObject json = JSONObject.fromObject(bean);
		return json.toString();
	}
}
