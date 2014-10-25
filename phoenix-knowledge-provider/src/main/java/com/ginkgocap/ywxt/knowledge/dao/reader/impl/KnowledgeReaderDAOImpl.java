package com.ginkgocap.ywxt.knowledge.dao.reader.impl;


import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.dao.reader.KnowledgeReaderDAO;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeCollectionMapper;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeStaticsMapperManual;
import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.util.MongoUtils;

@Service
public class KnowledgeReaderDAOImpl implements KnowledgeReaderDAO {

	@Resource
	private MongoTemplate mongoTemplate;

	@Resource
	private KnowledgeCollectionMapper knowledgeCollectionMapper;

	@Resource
	private KnowledgeStaticsMapperManual knowledgeStaticsMapperManual;

	@Override
	public Knowledge findHtmlContentFromMongo(long kid, String type)
			throws ClassNotFoundException {
		MongoUtils util = new MongoUtils();
		String c = util.getTableName(type);
		if (StringUtils.isBlank(c))
			return null;

		Knowledge knowledge = (Knowledge) mongoTemplate.findById(kid,
				Class.forName(c), util.getCollectionName(c));
		
		return knowledge;
	}


	@Override
	public int addStaticsCount(long kid, int commentCount, int shareCount,
			int collCount, int clickCount) {

		int count = knowledgeStaticsMapperManual.updateStatics(kid,
				commentCount, shareCount, collCount, clickCount);

		return count;
	}

}
