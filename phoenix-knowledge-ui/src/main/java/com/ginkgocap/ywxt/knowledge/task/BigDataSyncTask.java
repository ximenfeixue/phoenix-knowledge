package com.ginkgocap.ywxt.knowledge.task;

import com.ginkgocap.parasol.tags.model.Tag;
import com.ginkgocap.ywxt.knowledge.model.BigData;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeBase;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeUtil;
import com.ginkgocap.ywxt.knowledge.service.TagServiceLocal;
import com.ginkgocap.ywxt.knowledge.utils.PackingDataUtil;
import com.gintong.rocketmq.api.DefaultMessageService;
import com.gintong.rocketmq.api.enums.TopicType;
import com.gintong.rocketmq.api.model.RocketSendResult;
import com.gintong.rocketmq.api.utils.FlagTypeUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Repository("bigDataSyncTask")
public class BigDataSyncTask implements Runnable, InitializingBean
{
	private final Logger logger = LoggerFactory.getLogger(BigDataSyncTask.class);

	/**知识MQ插入*/
	public final static String KNOWLEDGE_INSERT = FlagTypeUtils.createKnowledgeFlag();

	/**知识MQ更新*/
	public final static String KNOWLEDGE_UPDATE = FlagTypeUtils.updateKnowledgeFlag();

	/**知识MQ删除*/
	public final static String KNOWLEDGE_DELETE = FlagTypeUtils.deleteKnowledgeFlag();

    @Resource
    private TagServiceLocal tagServiceLocal;

	@Autowired
	private DefaultMessageService defaultMessageService;

    private final int maxQeueSize = 2000;

	private BlockingQueue<SyncBigData> knowQueue = new ArrayBlockingQueue<SyncBigData>(maxQeueSize);

	public void addToMessageQueue(String optionType, BigData base) {
		if (base == null) {
			logger.error("bigData is null, so skip to add.");
			return;
		}
		try {
			knowQueue.put(new SyncBigData(optionType, base));
		} catch (Exception ex) {
			logger.error("add syncBigData to queue failed.");
		}
	}

	private void sendMessage(String optionType, BigData bigData) {
		if (bigData == null) {
			logger.error("Knowledge base is null, so skip to send..");
			return;
		}
		logger.info("push knowledge to bigdata， userId: " + bigData.getCid() + " knowledgeId: " + bigData.getKid());
		RocketSendResult result = null;
		try {
			if (StringUtils.isNotBlank(optionType)) {
				result = defaultMessageService.sendMessage(TopicType.KNOWLEDGE_TOPIC, optionType, PackingDataUtil.packingSendBigData(bigData));
				logger.info("response: " + result.getSendResult());
			} else {
				defaultMessageService.sendMessage(TopicType.KNOWLEDGE_TOPIC, PackingDataUtil.packingSendBigData(bigData));
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("send failed.  response: " + (result != null ? result.getSendResult() : ""));
		}
	}

	public void pushKnowledge(SyncBigData syncBigData)
    {
        if (syncBigData != null && syncBigData.base != null) {
            BigData base = syncBigData.base;
            long userId = base.getCid();
            try {
                List<String> tagNames = new ArrayList<String>();
                List<Tag> tagList = tagServiceLocal.getTagList(userId);
                if (CollectionUtils.isNotEmpty(tagList)) {
                    Map<Long, Tag> tagMap = new HashMap<Long, Tag>(tagList.size());
                    for (Tag tag : tagList) {
                        if (tag != null) {
                            tagMap.put(tag.getId(), tag);
                        }
                    }
                    List<Long> idList = KnowledgeUtil.convertStringToLongList(base.getTags());
                    for (long id : idList) {
                        Tag tag = tagMap.get(id);
                        if (tag != null) {
                            tagNames.add(tag.getTagName());
                        } else {
                            logger.error("Can't find tag by id: " + id);
                        }
                    }
                    if (CollectionUtils.isNotEmpty(tagNames)) {
                        String tagNameString = KnowledgeUtil.writeObjectToJson(tagNames);
                        logger.info("will send to bigdata service. tag names: " + tagNameString);
                        base.setTags(tagNameString);
                    }
                }
            } catch (Exception ex) {
                logger.error("get tag list failed. userId : " + userId);
            }
            //base.setTags();
            this.sendMessage(syncBigData.optionType, base);
        }
	}

	public void deleteMessage(long knowledgeId, int columnType, long userId) {
		
		BigData base = new BigData();
		base.setKid(knowledgeId);
		base.setCid(userId);
		base.setColumnid(columnType);
        base.setColumnType((short)columnType);
		
		//this.sendMessage(KNOWLEDGE_DELETE, base, userId);
		addToMessageQueue(KNOWLEDGE_DELETE, base);
	}

	private static class SyncBigData
	{
		private String optionType;
		private BigData base;

		public SyncBigData(String optionType,BigData base)
		{
			this.optionType = optionType;
			this.base = base;
		}
	}

	public void run() {
		try {
			while (true) {
				SyncBigData bigData = knowQueue.take();
				if (bigData != null) {
					pushKnowledge(bigData);
				} else {
					logger.error("bigData is null, so skip to push");
				}
			}
		} catch (InterruptedException ex) {
			logger.error("Exist thread, as it was interrupted.");
		}
	}

	public void afterPropertiesSet() throws Exception {
		logger.info("Knowledge message queue starting.");
		new Thread(this).start();
		logger.info("Knowledge message queue started.");
	}
}
