package com.ginkgocap.ywxt.knowledge.dao.knowledgecollection.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.ginkgocap.ywxt.knowledge.dao.knowledgecollection.KnowledgeCollectionDAO;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCollection;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeCollectionMapper;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeCollectionValueMapper;

/**
 * @author caihe
 * 
 */
@Component("knowledgeCollectionDAO")
public class KnowledgeCollectionDAOImpl implements KnowledgeCollectionDAO {

	@Resource
	private KnowledgeCollectionMapper knowledgeCollectionMapper;
	
	@Resource
	private KnowledgeCollectionValueMapper knowledgeCollectionValueMapper;
	
	

	@Override
	public int insertKnowledgeCollection(KnowledgeCollection knowledgeCollection) {
		return knowledgeCollectionMapper.insertSelective(knowledgeCollection);
	}

	@Override
	public int deleteKnowledgeCollection(long[] knowledgeids, long categoryid) {
	
		return knowledgeCollectionValueMapper.deleteKnowledge(knowledgeids, categoryid);
	}

}
