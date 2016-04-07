package com.ginkgocap.ywxt.knowledge.service.impl.common;

import com.ginkgocap.ywxt.knowledge.model.common.Ids;
import com.ginkgocap.ywxt.knowledge.service.common.IKnowledgeCommonService;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("knowledgeCommonService")
public class KnowledgeCommonServiceImpl implements IKnowledgeCommonService {
	
	@Resource
    private MongoTemplate mongoTemplate;
	
	@Override
	public Long getKnowledgeSeqenceId() {
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
	}

}
