package com.ginkgocap.ywxt.knowledge.service;


import java.util.List;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeNews;

public interface AppKnowledgeNewsService {

	public void updateKnowledge(KnowledgeNews knowledgeNews);
	
	public List<KnowledgeNews> selectKnowledgeByUserId(Long userId);
	
	public KnowledgeNews selectKnowledgeByKnowledgeId(Long knowledgeId);
	
}
