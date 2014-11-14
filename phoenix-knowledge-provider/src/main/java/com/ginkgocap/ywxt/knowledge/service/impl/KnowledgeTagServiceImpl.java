package com.ginkgocap.ywxt.knowledge.service.impl;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.util.Constants;
import org.springframework.data.mongodb.core.query.Criteria;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeTagService;

@Service("knowledgeTagService")
public class KnowledgeTagServiceImpl implements KnowledgeTagService {
	
	@Resource
	private MongoTemplate mongoTemplate;

	@Override
	public boolean updateKnowledgeTag(long kid, int type, String tags) {
		String obj = Constants.getTableName(type+"");
		boolean result = false;
		try {
		Criteria c = Criteria.where("_id").is(kid);
		Update update = new Update();
		update.set("tags", tags);
		Query query = new Query(c);
		mongoTemplate.updateFirst(query, update,
				obj.substring(obj.lastIndexOf(".") + 1, obj.length()));
		result = true;
		} catch (Exception e) {
		}
		return result;
	}

//	@Autowired
//	private KnowledgeTagDAO knowledgeTagDAO;
//
//	@Override
//	public int insertKnowledgeTag(KnowledgeTag knowledge) {
//		return knowledgeTagDAO.insertKnowledgeTag(knowledge);
//	}

}
