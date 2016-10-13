package com.ginkgocap.ywxt.knowledge.service.impl.common;

import com.ginkgocap.ywxt.knowledge.id.CloudConfig;
import com.ginkgocap.ywxt.knowledge.id.DefaultIdGenerator;
import com.ginkgocap.ywxt.knowledge.id.IdGeneratorFactory;
import com.ginkgocap.ywxt.knowledge.model.common.Constant;
import com.ginkgocap.ywxt.knowledge.service.common.KnowledgeCommonService;
import io.netty.util.internal.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Random;
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
            if (resourceBundle == null) {
                exit("dubbo.properties 必须存在!");
            }

            String sequence = resourceBundle.getString("knowledge.provider.sequence");
            if (sequence == null) {
                exit("knowledge.provider.sequence. 必须不为空.");
            }

            try {
                int sequ = Integer.parseInt(sequence);
                if (sequ <= 0) {
                    exit("knowledge.provider.sequence. 必须大于零 :" + sequence);
                }
            } catch (NumberFormatException ex) {
                exit("knowledge.provider.sequence. 必须是数字 :" + sequence);
            }
            defaultIdGenerator = new DefaultIdGenerator(sequence);
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
    	defaultIdGenerator = IdGeneratorFactory.idGenerator(mongoTemplate);
        logger.info("Unique Id Generator init complete.");
    }
}
