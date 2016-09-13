package com.ginkgocap.ywxt.knowledge.service;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeBase;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeMongo;
import com.ginkgocap.ywxt.knowledge.utils.PackingDataUtil;
import com.gintong.rocketmq.api.DefaultMessageService;
import com.gintong.rocketmq.api.enums.TopicType;
import com.gintong.rocketmq.api.model.RocketSendResult;
import com.gintong.rocketmq.api.utils.FlagTypeUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("bigDataService")
public class BigDataService
{
	/**知识MQ插入*/
	public final static String KNOWLEDGE_INSERT = FlagTypeUtils.createKnowledgeFlag();

	/**知识MQ更新*/
	public final static String KNOWLEDGE_UPDATE = FlagTypeUtils.createKnowledgeFlag();

	/**知识MQ删除*/
	public final static String KNOWLEDGE_DELETE = FlagTypeUtils.createKnowledgeFlag();

	private static final Logger logger = LoggerFactory.getLogger(BigDataService.class);

	@Autowired(required = true)
	private DefaultMessageService defaultMessageService;

	public void sendMessage(String optionType, KnowledgeBase base, long userId) {
        if (base == null) {
            logger.error("Knowledge base is null, so skip to send..");
            return;
        }
		logger.info("notify bigdata， userId: " + userId);
		RocketSendResult result = null;
		try {
			if (StringUtils.isNotBlank(optionType)) {
				result = defaultMessageService.sendMessage(TopicType.KNOWLEDGE_TOPIC, optionType, PackingDataUtil.packingSendBigData(base,userId));
				logger.info("response: " + result.getSendResult());
			} else {
				defaultMessageService.sendMessage(TopicType.KNOWLEDGE_TOPIC, PackingDataUtil.packingSendBigData(base,userId));
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("send failed.  response: " + result.getSendResult());
		}
	}

	public void sendMessage(String optionType, List<KnowledgeBase> knowledgeBaseList, long userId) {
		if(knowledgeBaseList != null && !knowledgeBaseList.isEmpty()) {
			for (KnowledgeBase base : knowledgeBaseList) {
				if (base != null) {
					this.sendMessage(optionType, base, userId);
				}
			}
			
		}
	}

	public void deleteMessage(long knowledgeId, int columnId, long userId)
			throws Exception {
		
		KnowledgeBase base = new KnowledgeBase();
		base.setId(knowledgeId);
		base.setColumnId(columnId);
        base.setType((short)columnId);
		
		this.sendMessage(KNOWLEDGE_DELETE, base, userId);
	}

}
