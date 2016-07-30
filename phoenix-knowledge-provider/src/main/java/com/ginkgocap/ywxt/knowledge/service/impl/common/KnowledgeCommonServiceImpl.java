package com.ginkgocap.ywxt.knowledge.service.impl.common;

import com.ginkgocap.ywxt.knowledge.id.CloudConfig;
import com.ginkgocap.ywxt.knowledge.id.DefaultIdGenerator;
import com.ginkgocap.ywxt.knowledge.model.common.Constant;
import com.ginkgocap.ywxt.knowledge.service.common.KnowledgeCommonService;
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
import java.util.concurrent.atomic.AtomicInteger;

@Service("knowledgeCommonService")
public class KnowledgeCommonServiceImpl implements KnowledgeCommonService, InitializingBean {
    private final static Logger logger = LoggerFactory.getLogger(KnowledgeCommonServiceImpl.class);
	@Resource
    private MongoTemplate mongoTemplate;

    private static DefaultIdGenerator defaultIdGenerator = null;

    private AtomicInteger autoIncrease = new AtomicInteger(1);
    /*
	@Override
	public Long getKnowledgeSequenceId() {

		Criteria c = Criteria.where("name").is("kid");
		Update update =new Update();
		Query query = new Query(c);
        Ids ids = new Ids();
        synchronized(mongoTemplate) {
            Ids hasIds = mongoTemplate.findOne(query, Ids.class);
            update.inc("cid", 1);
            if (hasIds != null && hasIds.getCid() > 0) {
                ids = mongoTemplate.findAndModify(query, update, Ids.class);
            } else {
                ids.setCid(1l);
                ids.setName("kid");
                mongoTemplate.insert(ids);
                ids = mongoTemplate.findAndModify(query, update, Ids.class);
            }
        }
		return ids.getCid();
	}*/


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
        if (defaultIdGenerator != null) {
            try {
                long sequenceId = Long.parseLong(defaultIdGenerator.next());
                logger.info("generated  sequenceId： {}", sequenceId);
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

    private static void exit(String message)
    {
        logger.error(message);
        System.exit(0);;
    }

    private long tempId()
    {
        logger.error("唯一I的生成器出问题，请赶快排查");
        return System.currentTimeMillis() + autoIncrease.getAndIncrement();
    }

    private static int getNextNum()
    {
        int max=1500000;
        int min=1;
        Random random = new Random();
        return random.nextInt(max)%(max-min+1) + min;
    }

    private void initUniqueIdGenerator()
    {
        String ipAddress = getHostIp();
        if (ipAddress == null) {
            logger.error("Can't get host Ip address, please check the host configure..");
            //Dummy a address
            ipAddress = "127.0.0.101";
        }
        int sequence = 1;
        String collectionName = CloudConfig.class.getSimpleName();
        logger.info("ipAddress: {} collectionName: {}",ipAddress, collectionName);
        if (!mongoTemplate.collectionExists(collectionName)) {
            logger.info("collectionName not exist, so create it");
            saveCloudConfig(sequence, ipAddress, collectionName);
            defaultIdGenerator = new DefaultIdGenerator(String.valueOf(sequence));
        } else {
            Query query = ipQuery(ipAddress);
            CloudConfig cloud = mongoTemplate.findOne(query, CloudConfig.class, collectionName);
            if (cloud != null) {
                logger.info("find ip: {} in collectionName id: {}",ipAddress, cloud.getId());
                defaultIdGenerator = new DefaultIdGenerator(String.valueOf(cloud.getId()));
            }
            else {
                query = idQuery();
                cloud = mongoTemplate.findOne(query, CloudConfig.class, collectionName);
                if (cloud != null) {
                    logger.info("find the max ip: {} in collectionName id: {}",ipAddress, cloud.getId());
                    cloud = saveCloudConfig(cloud.getId()+1, ipAddress, collectionName);
                    defaultIdGenerator = new DefaultIdGenerator(String.valueOf(cloud.getId()));
                } else {
                    logger.error("Can't get exist cloud configure, please check..");
                    cloud = saveCloudConfig(getNextNum(), ipAddress, collectionName);
                    defaultIdGenerator = new DefaultIdGenerator(String.valueOf(cloud.getId()));
                }
            }
        }
    }

    private Query ipQuery(String ipAddress){
        Query query = new Query();
        query.addCriteria(Criteria.where("ip").is(ipAddress));
        return query;
    }

    private Query idQuery()
    {
        Query query = new Query();
        query.with(new Sort(Sort.Direction.DESC, Constant._ID));
        query.limit(1);
        return query;
    }

    private CloudConfig saveCloudConfig(int id,String ipAddress,String collectionName)
    {
        CloudConfig cloud = new CloudConfig(id, ipAddress);
        mongoTemplate.insert(cloud, collectionName);
        return cloud;
    }

    private String getHostIp()
    {
        String ip = null;
        try {
            InetAddress addr = InetAddress.getLocalHost();
            if (addr != null) {
                ip = addr.getHostAddress().toString();
                logger.info("Ip: {}", ip);
            }
            else {
                logger.error("get localhost failed...");
            }
        } catch (IOException e) {// TODO Auto-generated catch
            logger.error("get localhost failed... error: {}", e.getMessage());
        }
        return ip;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initUniqueIdGenerator();
        logger.info("Unique Id Generator init complete...");
    }
}
