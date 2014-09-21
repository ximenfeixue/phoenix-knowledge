package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.dao.knowledge.KnowledgeDao;
import com.ginkgocap.ywxt.knowledge.dao.knowledgecategory.KnowledgeCategoryDAO;
import com.ginkgocap.ywxt.knowledge.dao.news.KnowledgeNewsDAO;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNews;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeNewsService;
import com.ibatis.sqlmap.client.SqlMapClient;

@Service("knowledgeNewsService")
public class KnowledgeNewsServiceImpl implements KnowledgeNewsService {

	@Autowired
	private KnowledgeDao knowledgeDao;

	@Autowired
	private KnowledgeNewsDAO knowledgeNewsDAO;

	@Autowired
	private KnowledgeCategoryDAO knowledgeBetweenDAO;

	@Autowired
	SqlMapClient sqlMapClient;

	@Override
	public KnowledgeNews insertknowledge(KnowledgeNews knowledge) {

		KnowledgeNews knowledgeNews = knowledgeNewsDAO
				.insertknowledge(knowledge);
		if (knowledgeNews == null) {

			return null;
		} else {
			return knowledgeNews;
		}

	}

	@Override
	public void deleteKnowledge(long[] ids) {

		knowledgeNewsDAO.deleteKnowledge(ids);
	}

	@Override
	public void updateKnowledge(KnowledgeNews knowledge) {

		knowledgeNewsDAO.updateKnowledge(knowledge);
	}

	@Override
	public KnowledgeNews selectKnowledge(long knowledgeid) {

		return knowledgeNewsDAO.selectKnowledge(knowledgeid);
	}

	@Override
	public List<KnowledgeNews> selectByParam(Long columnid, long source,
			Long userid, List<Long> ids, int page, int size) {
		return knowledgeNewsDAO.selectByParam(columnid, source, userid, ids,
				page, size);
	}

}
