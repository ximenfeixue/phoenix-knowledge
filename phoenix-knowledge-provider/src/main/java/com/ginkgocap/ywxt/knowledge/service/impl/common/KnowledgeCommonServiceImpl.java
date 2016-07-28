package com.ginkgocap.ywxt.knowledge.service.impl.common;

import com.ginkgocap.ywxt.knowledge.id.DefaultIdGenerator;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeUtil;
import com.ginkgocap.ywxt.knowledge.model.common.Ids;
import com.ginkgocap.ywxt.knowledge.service.common.KnowledgeCommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Random;
import java.util.ResourceBundle;

@Service("knowledgeCommonService")
public class KnowledgeCommonServiceImpl implements KnowledgeCommonService {
    private final static Logger logger = LoggerFactory.getLogger(KnowledgeCommonServiceImpl.class);
	@Resource
    private MongoTemplate mongoTemplate;

    private static ResourceBundle resourceBundle =  ResourceBundle.getBundle("conf/dubbo");
    private static DefaultIdGenerator defaultIdGenerator = null;

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
        }

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

    private static void exit(String message)
    {
        logger.error(message);
        System.exit(0);;
    }

    private long tempId()
    {
        logger.error("唯一I的生成器出问题，请赶快排查");
        return System.currentTimeMillis() + getNextNum();
    }

    private static int getNextNum()
    {
        int max=1500000;
        int min=1;
        Random random = new Random();
        return random.nextInt(max)%(max-min+1) + min;
    }

}
