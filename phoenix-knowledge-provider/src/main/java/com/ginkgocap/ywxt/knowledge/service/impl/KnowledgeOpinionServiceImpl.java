package com.ginkgocap.ywxt.knowledge.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.dao.knowledge.KnowledgeDao;
import com.ginkgocap.ywxt.knowledge.dao.knowledgecategory.KnowledgeCategoryDAO;
import com.ginkgocap.ywxt.knowledge.dao.opinion.KnowledgeOpinionDAO;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeOpinion;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeOpinionService;
import com.ibatis.sqlmap.client.SqlMapClient;

@Service("knowledgeOpinionService")
public class KnowledgeOpinionServiceImpl implements KnowledgeOpinionService {

	@Autowired
	private KnowledgeDao knowledgeDao;

	@Autowired
	private KnowledgeOpinionDAO knowledgeOpinionDAO;

	@Autowired
	private KnowledgeCategoryDAO knowledgeBetweenDAO;

	@Autowired
	SqlMapClient sqlMapClient;

	@Override
	public KnowledgeOpinion insertknowledge(KnowledgeOpinion knowledge) {

		KnowledgeOpinion knowledgeNews = knowledgeOpinionDAO
				.insertknowledge(knowledge);
		if (knowledgeNews == null) {

			return null;
		} else {
			return knowledgeNews;
		}

	}

	@Override
	public void deleteKnowledge(long[] ids) {

		knowledgeOpinionDAO.deleteKnowledge(ids);
	}

	@Override
	public void updateKnowledge(KnowledgeOpinion knowledge) {

		int count = 0;
		knowledgeOpinionDAO.updateKnowledge(knowledge);
		if (count > 0) {
			// if (categoryids != null && categoryids.length > 0) {
			//
			// count = knowledgeDao.deleteKnowledgeRCategory(
			// knowledge.getId(), categoryid);
			// if (count > 0) {
			// knowledgeBetweenDAO.insertKnowledgeRCategory(knowledge,
			// categoryids);
			// return 1;
			// }
			// }
			// return 1;
		}
	}

	@Override
	public KnowledgeOpinion selectKnowledge(long knowledgeid) {

		return knowledgeOpinionDAO.selectKnowledge(knowledgeid);
	}

}
