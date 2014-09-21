package com.ginkgocap.ywxt.knowledge.dao.reader.impl;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.dao.reader.KnowledgeReaderDAO;
import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.util.MongoUtils;

@Service
public class KnowledgeReaderDAOImpl implements KnowledgeReaderDAO {

	@Resource
	private MongoTemplate mongoTemplate;

	@Override
	public String findHtmlContentFromMongo(long kid, String type)
			throws ClassNotFoundException {

		MongoUtils util = new MongoUtils();
		String c = util.getTableName(type);
		if (StringUtils.isBlank(c))
			return null;

		Knowledge knowledge = (Knowledge) mongoTemplate.findById(kid,
				Class.forName(c), util.getCollectionName(c));
		if (knowledge == null)
			return null;

		return StringUtils.isBlank(knowledge.getHcontent()) ? knowledge
				.getContent() : knowledge.getHcontent();
	}

}
