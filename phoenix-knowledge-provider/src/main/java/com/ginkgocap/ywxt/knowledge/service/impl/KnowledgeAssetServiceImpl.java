package com.ginkgocap.ywxt.knowledge.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.dao.asset.KnowledgeAssetDAO;
import com.ginkgocap.ywxt.knowledge.dao.knowledge.KnowledgeDao;
import com.ginkgocap.ywxt.knowledge.dao.knowledgecategory.KnowledgeCategoryDAO;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeAsset;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeAssetService;
import com.ibatis.sqlmap.client.SqlMapClient;

@Service("knowledgeAssetService")
public class KnowledgeAssetServiceImpl implements KnowledgeAssetService {

	@Autowired
	private KnowledgeDao knowledgeDao;

	@Autowired
	private KnowledgeAssetDAO knowledgeAssetDAO;

	@Autowired
	private KnowledgeCategoryDAO knowledgeBetweenDAO;

	@Autowired
	SqlMapClient sqlMapClient;

	@Override
	public KnowledgeAsset insertknowledge(KnowledgeAsset knowledge) {

		KnowledgeAsset knowledgeNews = knowledgeAssetDAO
				.insertknowledge(knowledge);
		if (knowledgeNews == null) {

			return null;
		} else {
			return knowledgeNews;
		}

	}

	@Override
	public void deleteKnowledge(long[] ids) {

		knowledgeAssetDAO.deleteKnowledge(ids);
	}

	@Override
	public void updateKnowledge(KnowledgeAsset knowledge) {

		knowledgeAssetDAO.updateKnowledge(knowledge);
	}

	@Override
	public KnowledgeAsset selectKnowledge(long knowledgeid) {

		return knowledgeAssetDAO.selectKnowledge(knowledgeid);
	}

}
