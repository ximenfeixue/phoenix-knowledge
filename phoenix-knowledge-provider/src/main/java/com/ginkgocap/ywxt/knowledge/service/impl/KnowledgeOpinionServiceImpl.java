package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.dao.knowledge.KnowledgeDao;
import com.ginkgocap.ywxt.knowledge.dao.knowledgecategory.KnowledgeCategoryDAO;
import com.ginkgocap.ywxt.knowledge.dao.opinion.KnowledgeOpinionDAO;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNews;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeOpinion;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeOpinionService;
import com.ginkgocap.ywxt.knowledge.util.Constants;
import com.ibatis.sqlmap.client.SqlMapClient;

@Service("knowledgeOpinionService")
public class KnowledgeOpinionServiceImpl implements KnowledgeOpinionService {

	@Autowired
	private KnowledgeDao knowledgeDao;

	@Autowired
	private KnowledgeOpinionDAO knowledgeOpinionDAO;

	@Autowired
	private KnowledgeCategoryDAO knowledgeBetweenDAO;

	@Resource
	private MongoTemplate mongoTemplate;

	@Override
	public void deleteKnowledge(long[] ids) {

		knowledgeOpinionDAO.deleteKnowledge(ids);
	}

	@Override
	public void updateKnowledge(String title, long userid, String uname,
			long cid, String cname, String cpath, String content, String pic,
			String desc, String essence, String taskid, String tags,
			long knowledgeid) {

		knowledgeOpinionDAO.updateKnowledge(title, userid, uname, cid, cname,
				cpath, content, pic, desc, essence, taskid, tags, knowledgeid);
	}

	@Override
	public KnowledgeOpinion selectKnowledge(long knowledgeid) {

		return knowledgeOpinionDAO.selectKnowledge(knowledgeid);
	}

	@Override
	public List<KnowledgeOpinion> selectByParam(Long columnid, long source,
			Long userid, List<Long> ids, int page, int size) {
		return knowledgeOpinionDAO.selectByParam(columnid, source, userid, ids,
				page, size);
	}

	@Override
	public void deleteKnowledgeByid(long knowledgeid) {
		knowledgeOpinionDAO.deleteKnowledgeByid(knowledgeid);
	}

	@Override
	public KnowledgeOpinion insertknowledge(String title, long userid,
			String uname, long cid, String cname, String cpath, String content,
			String pic, String desc, String essence, String taskid,
			String tags, long knowledgeid, long columnid) {

		return knowledgeOpinionDAO.insertknowledge(title, userid, uname, cid,
				cname, cpath, content, pic, desc, essence, taskid, tags,
				knowledgeid, columnid);
	}

	@Override
	public void restoreKnowledgeByid(long knowledgeid) {
		knowledgeOpinionDAO.restoreKnowledgeByid(knowledgeid);

	}

	@Override
	public void deleteforeverKnowledge(long knowledgeid) {

		Criteria criteria = Criteria.where("_id").is(knowledgeid);
		Query query = new Query(criteria);
		KnowledgeOpinion kdnews = mongoTemplate.findOne(query,
				KnowledgeOpinion.class, "KnowledgeOpinion");
		if (kdnews != null) {
			Update update = new Update();
			update.set("status", Constants.Status.foreverdelete.v());
			mongoTemplate.updateFirst(query, update, "KnowledgeOpinion");
		}

	}

}
