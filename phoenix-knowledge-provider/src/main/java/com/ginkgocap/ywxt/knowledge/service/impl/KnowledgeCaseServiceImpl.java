package com.ginkgocap.ywxt.knowledge.service.knowledge.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeCase;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeIndustry;
import com.ginkgocap.ywxt.knowledge.service.knowledge.KnowledgeCaseService;
import com.ginkgocap.ywxt.knowledge.service.knowledge.KnowledgeMongoIncService;

@Service("knowledgeCaseService")
public class KnowledgeCaseServiceImpl implements KnowledgeCaseService {
	
	@Resource
    private MongoTemplate mongoTemplate;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource
	private KnowledgeMongoIncService KnowledgeMongoIncService;
	
	@Override
	public Long addKnowledgeCase(KnowledgeCase k) {
		Long id=KnowledgeMongoIncService.getKnowledgeIncreaseId();
		k.setId(id);
		mongoTemplate.save(k);
		return id;
	}

	@Override
	public Long updateKnowledgeCase(KnowledgeCase k) {
		long id=k.getId();
		Criteria c = Criteria.where("id").is(id);
		Update update =new Update();
		Query query = new Query(c);
		update.set("cid", k.getCid());
		update.set("cname", k.getCname());
		update.set("content", k.getContent());
		update.set("cpathid", k.getCpathid());
		update.set("createtime", k.getCreatetime());
		update.set("desc", k.getDesc());
		update.set("essence", k.getEssence());
		update.set("ish", k.getIsh());
		update.set("modifytime", k.getModifytime());
		update.set("oid", k.getOid());
		update.set("pic", k.getPic());
		update.set("report_status", k.getReport_status());
		update.set("s_addr", k.getS_addr());
		update.set("source", k.getSource());
		update.set("status", k.getStatus());
		update.set("taskid", k.getTaskid());
		update.set("title", k.getTitle());
		update.set("uid", k.getUid());
		update.set("uname", k.getUname());
		mongoTemplate.findAndModify(query, update, KnowledgeCase.class);
		return id;
	}

	@Override
	public KnowledgeCase getKnowledgeCaseDetail(Long id) {
		Criteria c = Criteria.where("id").is(id);
		Query query = new Query(c);
		return (KnowledgeCase) mongoTemplate.find(query, KnowledgeCase.class);
	}

}
