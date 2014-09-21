package com.ginkgocap.ywxt.knowledge.service.knowledge.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.dao.content.KnowledgeContentDAO;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeContent;
import com.ginkgocap.ywxt.knowledge.service.knowledge.KnowledgeContentService;

@Service("knowledgeContentService")
public class KnowledgeContentServiceImpl implements KnowledgeContentService {

	@Autowired
	private KnowledgeContentDAO knowledgeContentDAO;

	public KnowledgeContentDAO getKnowledgeContentDAO() {
		return knowledgeContentDAO;
	}

	public void setKnowledgeContentDAO(KnowledgeContentDAO knowledgeContentDAO) {
		this.knowledgeContentDAO = knowledgeContentDAO;
	}

	@Override
	public KnowledgeContent insert(KnowledgeContent knowledgeContent) {

		return knowledgeContentDAO.insert(knowledgeContent);
	}

	@Override
	public List<KnowledgeContent> selectByknowledgeId(long knowledgeId) {

		return null;
	}

	@Override
	public int update(KnowledgeContent knowledgeContent) {

		return knowledgeContentDAO.update(knowledgeContent);
	}

	@Override
	public int deleteByknowledgeId(long[] knowledgeId) {
		return knowledgeContentDAO.deleteByknowledgeId(knowledgeId);
	}

}
