package com.ginkgocap.ywxt.knowledge.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.service.KnowledgeInvestmentService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeMongoIncService;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeInvestment;

@Service("knowledgeInvestmentService")
public class KnowledgeInvestmentServiceImpl implements
		KnowledgeInvestmentService {
	
	@Resource
    private MongoTemplate mongoTemplate;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource
	private KnowledgeMongoIncService KnowledgeMongoIncService;
	
	@Override
	public Long addKnowledgeInvestment(KnowledgeInvestment k) {
		Long id=KnowledgeMongoIncService.getKnowledgeIncreaseId();
		k.setId(id);
		mongoTemplate.save(k);
		return id;
	}

	@Override
	public Long updateKnowledgeInvestment(KnowledgeInvestment k) {
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
		mongoTemplate.findAndModify(query, update, KnowledgeInvestment.class);
		return id;
	}

	@Override
	public KnowledgeInvestment getKnowledgeInvestmentDetail(Long id) {
		Criteria c = Criteria.where("id").is(id);
		Query query = new Query(c);
		return (KnowledgeInvestment) mongoTemplate.findOne(query, KnowledgeInvestment.class);
	}

}
