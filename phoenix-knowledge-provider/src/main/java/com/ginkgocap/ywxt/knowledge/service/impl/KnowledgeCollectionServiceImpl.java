package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.dao.knowledgecollection.KnowledgeCollectionDAO;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCollection;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeCollectionService;

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

		return knowledgeCollectionDAO.deleteKnowledgeCollection(knowledgeids,
				categoryid);
	}

	@Override
	public List<Long> selectKnowledgeCollection(long column_id,
			String knowledgeType, long category_id, int pageno, int pagesize) {

		return knowledgeCollectionDAO.selectKnowledgeCollection(column_id,
				knowledgeType, category_id, pageno, pagesize);
	}

}
