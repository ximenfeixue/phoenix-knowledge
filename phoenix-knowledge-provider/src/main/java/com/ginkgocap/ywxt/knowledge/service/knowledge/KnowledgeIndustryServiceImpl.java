package com.ginkgocap.ywxt.knowledge.service.knowledge;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeIndustry;

public class KnowledgeIndustryServiceImpl implements KnowledgeIndustryService {
	
	@Resource
    private MongoTemplate mongoTemplate;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public Long addKnowledgeIndustry(KnowledgeIndustry k) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long updateKnowledgeIndustry(KnowledgeIndustry k) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getKnowledgeIndustryDetail(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
