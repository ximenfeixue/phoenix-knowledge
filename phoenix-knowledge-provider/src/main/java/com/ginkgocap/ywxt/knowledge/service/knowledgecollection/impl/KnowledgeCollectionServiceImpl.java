package com.ginkgocap.ywxt.knowledge.service.knowledgecollection.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.dao.knowledgecollection.KnowledgeCollectionDAO;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCollection;
import com.ginkgocap.ywxt.knowledge.service.knowledgecollection.KnowledgeCollectionService;

@Service("knowledgeCollectionService")
public class KnowledgeCollectionServiceImpl implements
		KnowledgeCollectionService {

	@Autowired
	private KnowledgeCollectionDAO knowledgeCollectionDAO;

	@Override
	public int insertKnowledgeCollection(KnowledgeCollection knowledgeCollection) {
		return knowledgeCollectionDAO
				.insertKnowledgeCollection(knowledgeCollection);

	}

	@Override
	public int deleteKnowledgeCollection(long[] knowledgeids, long categoryid) {
		
		return knowledgeCollectionDAO.deleteKnowledgeCollection(knowledgeids, categoryid);
	}

}
