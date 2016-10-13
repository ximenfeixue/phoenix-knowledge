package com.ginkgocap.ywxt.knowledge.service.impl.common;

import com.ginkgocap.ywxt.knowledge.id.DefaultIdGenerator;
import com.ginkgocap.ywxt.knowledge.id.DistributedLock;
import com.ginkgocap.ywxt.knowledge.id.IdGeneratorFactory;
import com.ginkgocap.ywxt.knowledge.service.common.KnowledgeCommonService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

@Service("knowledgeCommonService")
public class KnowledgeCommonServiceImpl implements KnowledgeCommonService, InitializingBean {
    private final static Logger logger = LoggerFactory.getLogger(KnowledgeCommonServiceImpl.class);
	@Resource
    private MongoTemplate mongoTemplate;

    private static DefaultIdGenerator defaultIdGenerator = null;

    private AtomicInteger autoIncrease = new AtomicInteger(1);

    private DistributedLock lock = null;

    @Override
    public Long getKnowledgeSequenceId()
    {
        /*
        if (defaultIdGenerator == null) {
             try {
                long sequenceId = Long.parseLong(defaultIdGenerator.next());
                logger.debug("generated  sequenceId： " + sequenceId);
                return sequenceId;
            } catch (NumberFormatException ex) {
                logger.error("生成唯一Id不是数字 ： " + ex.getMessage());
                return tempId();
            } catch (Throwable ex) {
                return tempId();
            }
        }*/
        if (lock == null) {
            ResourceBundle resource = ResourceBundle.getBundle("dubbo");
            String zookeeperHost = resource.getString("dubbo.registry.address");
            if (zookeeperHost != null) {
                int start = zookeeperHost.indexOf("//") + 1;
                int end = zookeeperHost.indexOf("?") - 1;
                if (start > 0 && end > 0 && end < zookeeperHost.length()) {
                    zookeeperHost = zookeeperHost.substring(start, end);
                }
                if (StringUtils.isNotBlank(zookeeperHost)) {
                    lock = new DistributedLock(zookeeperHost, "id_locknode_");
                    logger.info("create Distributed Lock success...");
                }
            }
        }

        if (defaultIdGenerator != null) {
            try {
                long sequenceId = 0;
                if (lock != null) {
                    lock.acquire();
                    sequenceId = Long.parseLong(defaultIdGenerator.next());
                    lock.release();
                } else {
                    sequenceId = Long.parseLong(defaultIdGenerator.next());
                }
                logger.debug("generated  sequenceId： " + sequenceId);
                return sequenceId;
            } catch (NumberFormatException ex) {
                logger.error("生成唯一Id不是数字 ： " + ex.getMessage());
                return tempId();
            } catch (Throwable ex) {
                return tempId();
            }
        }
        return tempId();
    }

    private long tempId()
    {
        logger.error("唯一I的生成器出问题，请赶快排查");
        return System.currentTimeMillis() + autoIncrease.getAndIncrement();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
    	//defaultIdGenerator = IdGeneratorFactory.idGenerator(mongoTemplate);
        defaultIdGenerator = IdGeneratorFactory.idGenerator("1");
        logger.info("Unique Id Generator init complete.");
    }
}
