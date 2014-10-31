package com.ginkgocap.ywxt.knowledge.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeAsset;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeIndustry;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeInvestment;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNews;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeIndustryService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeMongoIncService;
import com.ginkgocap.ywxt.knowledge.util.Constants;

@Service("knowledgeIndustryService")
public class KnowledgeIndustryServiceImpl implements KnowledgeIndustryService {

	@Resource
	private MongoTemplate mongoTemplate;

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Resource
	private KnowledgeMongoIncService KnowledgeMongoIncService;

	@Override
	public Long addKnowledgeIndustry(KnowledgeIndustry k) {
		Long id = KnowledgeMongoIncService.getKnowledgeIncreaseId();
		k.setId(id);
		mongoTemplate.save(k,"KnowledgeIndustry");
		return id;
	}

	@Override
	public Long updateKnowledgeIndustry(KnowledgeIndustry k) {
		long id = k.getId();
		Criteria c = Criteria.where("id").is(id);
		Update update = new Update();
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
		mongoTemplate.findAndModify(query, update, KnowledgeIndustry.class, "KnowledgeIndustry");
		return id;
	}

	@Override
	public KnowledgeIndustry getKnowledgeIndustryDetail(Long id) {
		Criteria c = Criteria.where("id").is(id);
		Query query = new Query(c);
		return (KnowledgeIndustry) mongoTemplate.findOne(query,
				KnowledgeIndustry.class);
	}

	@Override
	public void restoreKnowledgeByid(long knowledgeid) {

		Criteria criteria = Criteria.where("_id").is(knowledgeid);
		Query query = new Query(criteria);
		KnowledgeIndustry kdnews = mongoTemplate.findOne(query,
				KnowledgeIndustry.class, "KnowledgeIndustry");
		if (kdnews != null) {
			Update update = new Update();
			update.set("status", Constants.Status.checked.v());
			mongoTemplate.updateFirst(query, update, "KnowledgeIndustry");
		}
	}

	@Override
	public void deleteKnowledge(long[] ids) {
		for (int i = 0; i < ids.length; i++) {

			Criteria criteria = Criteria.where("_id").in(ids);
			Query query = new Query(criteria);
			Update update = new Update();
			update.set("status", Constants.Status.recycle.v());
			mongoTemplate.updateFirst(query, update, "KnowledgeIndustry");
		}

	}

	@Override
	public void deleteforeverKnowledge(long knowledgeid) {

		Criteria criteria = Criteria.where("_id").is(knowledgeid);
		Query query = new Query(criteria);
		KnowledgeIndustry kdnews = mongoTemplate.findOne(query,
				KnowledgeIndustry.class, "KnowledgeIndustry");
		if (kdnews != null) {
			Update update = new Update();
			update.set("status", Constants.Status.foreverdelete.v());
			mongoTemplate.updateFirst(query, update, "KnowledgeIndustry");
		}

	}

}
