package com.ginkgocap.ywxt.knowledge.service.knowledge;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.cloud.model.InvestmentWord;
import com.ginkgocap.ywxt.cloud.service.InvestmentAuthenticationService;
import com.ginkgocap.ywxt.knowledge.dao.category.CategoryDao;
import com.ginkgocap.ywxt.knowledge.dao.knowledge.KnowledgeDao;
import com.ginkgocap.ywxt.knowledge.model.Category;
import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeRCategory;
import com.ginkgocap.ywxt.knowledge.service.category.impl.CategoryHelper;
import com.ibatis.sqlmap.client.SqlMapClient;

@Service("knowledgeMainService")
public class KnowledgeMainServiceImpl implements KnowledgeMainService {

	@Autowired
	private InvestmentAuthenticationService investmentAuthenticationService;
	@Autowired
	private KnowledgeDao knowledgeDao;

	@Autowired
	private CategoryDao categoryDao;

	@Autowired
	SqlMapClient sqlMapClient;
	private CategoryHelper helper = new CategoryHelper();

	@Override
	public Long saveKnowledge(Knowledge knowledge, Object knowledgeDetail) {

		int type = knowledge.getKnowledgetype();
		Long id = 0l;
		switch (type) {
		case 1:
			break;
		case 2:
			InvestmentWord word = (InvestmentWord) knowledgeDetail;
			id = investmentAuthenticationService.createInvestmentWord(word);
			break;

		default:
			break;
		}
		knowledge.setKnowledgeid(id);
		int i = knowledgeDao.insert(knowledge);
		return (long) i;
	}

	@Override
	public <T> T getKnowLedgeDetail(int id, int type) {
		return null;
	}

	@Override
	public int checkNameRepeat(int knowledgeType, String knowledgeTitle) {

		return knowledgeDao.checkNameRepeat(knowledgeType, knowledgeTitle);
	}

	@Override
	public int deleteKnowledgeRCategory(long[] knowledgeids, long categoryid) {

		return knowledgeDao.deleteKnowledgeRCategory(knowledgeids, categoryid);
	}

	@Override
	public void moveCategoryBatch(long categoryid, long[] knowledgeids,
			long[] categoryids) {

		int count = knowledgeDao.deleteKnowledgeRCategory(knowledgeids,
				categoryid);
		if (count > 0) {
			knowledgeDao.moveCategoryBatch(knowledgeids, categoryids);
		}
	}

	@Override
	public boolean updateKnowledge(Knowledge knowledge, Object knowledgeDetail) {
		int type = knowledge.getKnowledgetype();
		Long id = 0l;
		switch (type) {
		case 1:
			break;
		case 2:
			InvestmentWord word = (InvestmentWord) knowledgeDetail;
			id = investmentAuthenticationService.createInvestmentWord(word);
			break;

		default:
			break;
		}
		knowledge.setKnowledgeid(id);
		int i = knowledgeDao.insert(knowledge);
		return true;

	}

	@Override
	public Long saveKnowledgeTitle(Knowledge knowledge, Object knowledgeDetail) {
		int type = knowledge.getKnowledgetype();
		Long id = 0l;
		switch (type) {
		case 1:
			break;
		case 2:
			InvestmentWord word = (InvestmentWord) knowledgeDetail;
			id = investmentAuthenticationService.createInvestmentWord(word);
			break;
		default:
			break;
		}
		knowledge.setKnowledgeid(id);
		int i = knowledgeDao.insert(knowledge);
		return (long) i;
	}

	@Override
	public Knowledge insertknowledge(Knowledge knowledge, long[] categoryids) {

		return knowledgeDao.insertknowledge(knowledge);
	}

	@Override
	public int deleteKnowledge(long ids[], long categoryid) {

		int count = 0;
		count = knowledgeDao.deleteKnowledge(ids, categoryid);
		if (count > 0) {
			count = knowledgeDao.deleteKnowledgeRCategory(ids, categoryid);
			if (count > 0) {
				return 1;
			} else {
				return 0;
			}
		} else {
			return 0;
		}
	}

	@Override
	public int updateKnowledge(Knowledge knowledge) {

		return knowledgeDao.updateKnowledge(knowledge);
	}

	@Override
	public void insertKnowledgeRCategory(Knowledge knowledge, long categoryid[]) {

		knowledgeDao.insertKnowledgeRCategory(knowledge, categoryid);
	}

}
