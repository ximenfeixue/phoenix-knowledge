package com.ginkgocap.ywxt.knowledge.service.impl.common;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeMongo;
import com.ginkgocap.ywxt.knowledge.service.common.IBigDataService;
import com.ginkgocap.ywxt.knowledge.utils.PackingDataUtil;
import com.ginkgocap.ywxt.user.model.User;
import com.gintong.rocketmq.api.DefaultMessageService;
import com.gintong.rocketmq.api.enums.TopicType;
import com.gintong.rocketmq.api.model.RocketSendResult;

@Service
public class BigDataService implements IBigDataService{

	private static final Logger logger = LoggerFactory.getLogger(BigDataService.class);

	@Autowired(required = true)
	private DefaultMessageService defaultMessageService;

	@Override
	public void sendMessage(String optionType, KnowledgeMongo knowledgeMongo, User user) {
		logger.info("通知大数据，发送请求 请求用户{}", user.getId());
		RocketSendResult result = null;
		try {
			if (StringUtils.isNotBlank(optionType)) {
				result = defaultMessageService.sendMessage(TopicType.KNOWLEDGE_TOPIC, optionType, PackingDataUtil.packingSendBigData(knowledgeMongo,user));
				logger.info("返回参数{}", result.getSendResult());
			} else {
				defaultMessageService.sendMessage(TopicType.KNOWLEDGE_TOPIC, PackingDataUtil.packingSendBigData(knowledgeMongo,user));
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("发送失败  返回参数{}", result.getSendResult());
		}
	}
	
	@Override
	public void sendMessage(String optionType, List<KnowledgeMongo> knowledgeMongoList, User user) {
		if(knowledgeMongoList != null && !knowledgeMongoList.isEmpty()) {
			
			for (KnowledgeMongo data : knowledgeMongoList) {
				
				this.sendMessage(optionType, data, user);
				
			}
			
		}
	}

	@Override
	public void deleteMessage(long knowledgeId, long columnId, User user)
			throws Exception {
		
		KnowledgeMongo data = new KnowledgeMongo();
		
		data.setId(knowledgeId);
		data.setColumnId(columnId);
		
		this.sendMessage(KNOWLEDGE_UPDATE, data, user);
		
	}

}
