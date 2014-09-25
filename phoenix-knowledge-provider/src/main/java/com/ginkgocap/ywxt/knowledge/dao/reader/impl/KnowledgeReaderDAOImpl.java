package com.ginkgocap.ywxt.knowledge.dao.reader.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.dao.reader.KnowledgeReaderDAO;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCollection;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeStatics;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeStaticsExample;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeStaticsExample.Criteria;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeCollectionMapper;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeStaticsMapper;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeStaticsMapperManual;
import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.util.DateUtil;
import com.ginkgocap.ywxt.knowledge.util.MongoUtils;
import com.ginkgocap.ywxt.knowledge.util.TypeUtils;

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
	public int addCollection(long kid, long userid, String type, String source,
			long columnid, long categoryid) {
		KnowledgeCollection coll = new KnowledgeCollection();
		coll.setKnowledgeId(kid);
		coll.setCategoryId(categoryid);
		coll.setKnowledgetype(type);
		coll.setColumnId(columnid);
		coll.setSource(new TypeUtils().getRelationNameByType(source));
		coll.setTimestamp(new Date());

		return knowledgeCollectionMapper.insertSelective(coll);
	}

	@Override
	public int addStaticsCount(long kid, int commentCount, int shareCount,
			int collCount, int clickCount) {

		int count = knowledgeStaticsMapperManual.updateStatics(kid,
				commentCount, shareCount, collCount, clickCount);

		return count;
	}

}
