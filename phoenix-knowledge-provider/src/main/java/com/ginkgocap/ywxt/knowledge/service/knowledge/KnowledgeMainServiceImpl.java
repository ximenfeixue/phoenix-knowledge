package com.ginkgocap.ywxt.knowledge.service.knowledge;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.cloud.model.InvestmentWord;
import com.ginkgocap.ywxt.cloud.service.InvestmentAuthenticationService;
import com.ginkgocap.ywxt.knowledge.dao.knowledge.KnowledgeDao;
import com.ginkgocap.ywxt.knowledge.model.Knowledge;

@Service("knowledgeMainService")
public class KnowledgeMainServiceImpl implements KnowledgeMainService {

	@Autowired
	private InvestmentAuthenticationService investmentAuthenticationService;
	@Autowired
	private KnowledgeDao knowledgeDao;

//	@Override
//	public Long saveKnowledge(Knowledge knowledge, Object knowledgeDetail) {
//
//		int type = knowledge.getKnowledgetype();
//		Long id = 0l;
//		switch (type) {
//		case 1:
//			break;
//		case 2:
//			InvestmentWord word = (InvestmentWord) knowledgeDetail;
//			id = investmentAuthenticationService.createInvestmentWord(word);
//			break;
//
//		default:
//			break;
//		}
//		knowledge.setKnowledgeid(id);
//		int i = knowledgeDao.insert(knowledge);
//		return (long) i;
//	}
//
//	@Override
//	public boolean updateKnowledge(Knowledge knowledge, Object knowledgeDetail) {
//
//		return false;
//	}
//
//	@Override
//	public <T> T getKnowLedgeDetail(int id, int type) {
//		return null;
//	}

	@Override
	public int checkNameRepeat(int knowledgeType, String knowledgeTitle) {

		return knowledgeDao.checkNameRepeat(knowledgeType, knowledgeTitle);
	}

}
