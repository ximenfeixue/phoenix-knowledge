package com.ginkgocap.ywxt.knowledge.service.knowledgearticle.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.dao.knowledge.KnowledgeDao;
import com.ginkgocap.ywxt.knowledge.dao.knowledgearticle.KnowledgeArticleDAO;
import com.ginkgocap.ywxt.knowledge.dao.knowledgecategory.KnowledgeCategoryDAO;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeArticle;
import com.ginkgocap.ywxt.knowledge.service.knowledgearticle.KnowledgeArticleService;
import com.ibatis.sqlmap.client.SqlMapClient;

@Service("knowledgeArticleService")
public class KnowledgeArticleServiceImpl implements KnowledgeArticleService {

	@Autowired
	private KnowledgeDao knowledgeDao;

	@Autowired
	private KnowledgeArticleDAO knowledgeArticleDAO;

	@Autowired
	private KnowledgeCategoryDAO knowledgeBetweenDAO;

	@Autowired
	SqlMapClient sqlMapClient;

	@Override
	public KnowledgeArticle insertknowledge(KnowledgeArticle knowledge) {

		KnowledgeArticle knowledgeNews = knowledgeArticleDAO
				.insertknowledge(knowledge);
		if (knowledgeNews == null) {

			return null;
		} else {
			return knowledgeNews;
		}

	}

	@Override
	public void deleteKnowledge(long[] ids) {

		knowledgeArticleDAO.deleteKnowledge(ids);
	}

	@Override
	public void updateKnowledge(KnowledgeArticle knowledge) {

		knowledgeArticleDAO.updateKnowledge(knowledge);
	}

	@Override
	public KnowledgeArticle selectKnowledge(long knowledgeid) {

		return knowledgeArticleDAO.selectKnowledge(knowledgeid);
	}

}
