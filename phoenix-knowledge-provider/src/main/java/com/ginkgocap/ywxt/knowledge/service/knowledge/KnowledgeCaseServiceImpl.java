package com.ginkgocap.ywxt.knowledge.service.knowledge;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeCase;

@Service("knowledgeCaseService")
public class KnowledgeCaseServiceImpl implements KnowledgeCaseService {
	
	@Resource
    private MongoTemplate mongoTemplate;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public Long addKnowledgeCase(KnowledgeCase k) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long updateKnowledgeCase(KnowledgeCase k) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getKnowledgeCaseDetail(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean delKnowledgeCase(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

}
