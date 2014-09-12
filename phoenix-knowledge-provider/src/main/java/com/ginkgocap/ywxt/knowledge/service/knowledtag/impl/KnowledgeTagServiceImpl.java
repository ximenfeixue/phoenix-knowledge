package com.ginkgocap.ywxt.knowledge.service.knowledtag.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.dao.knowledtag.KnowledgeTagDAO;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeTag;
import com.ginkgocap.ywxt.knowledge.service.knowledgetag.KnowledgeTagService;

@Service("knowledgeTagService")
public class KnowledgeTagServiceImpl implements KnowledgeTagService {

	@Autowired
	private KnowledgeTagDAO knowledgeTagDAO;

	@Override
	public void insertKnowledgeTag(KnowledgeTag knowledge) {
		knowledgeTagDAO.insertKnowledgeTag(knowledge);
	}

	@Override
	public int deleteColumnKnowledge(long[] knowledgeids, long columnid) {
		// TODO Auto-generated method stub
		return 0;
	}

}
