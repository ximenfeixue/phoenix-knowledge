package com.ginkgocap.ywxt.knowledge.service.knowledge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.cloud.service.InvestmentAuthenticationService;
import com.ginkgocap.ywxt.knowledge.dao.category.CategoryDao;
import com.ginkgocap.ywxt.knowledge.dao.content.KnowledgeContentDAO;
import com.ginkgocap.ywxt.knowledge.dao.knowledge.KnowledgeDao;
import com.ginkgocap.ywxt.knowledge.dao.knowledgecategory.KnowledgeCategoryDAO;
import com.ginkgocap.ywxt.knowledge.model.Knowledge;
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
	private KnowledgeContentDAO knowledgeContentDAO;

	@Autowired
	private KnowledgeCategoryDAO knowledgeBetweenDAO;

	@Autowired
	SqlMapClient sqlMapClient;

	@Override
	public Long saveKnowledge(Knowledge knowledge, Object knowledgeDetail) {

		// int type = knowledge.getKnowledgetype();
		// Long id = 0l;
		// switch (type) {
		// case 1:
		// break;
		// case 2:
		// InvestmentWord word = (InvestmentWord) knowledgeDetail;
		// id = investmentAuthenticationService.createInvestmentWord(word);
		// break;
		//
		// default:
		// break;
		// }
		// knowledge.setKnowledgeid(id);
		// int i = knowledgeDao.insert(knowledge);
		// return (long) i;
		return (long) 0;
	}

	@Override
	public <T> T getKnowLedgeDetail(int id, int type) {
		return null;
	}

	@Override
	public void moveCategoryBatch(long categoryid, long[] knowledgeids,
			long[] categoryids) {

		int count = knowledgeBetweenDAO.deleteKnowledgeRCategory(knowledgeids,
				categoryid);
		if (count > 0) {
			knowledgeDao.moveCategoryBatch(knowledgeids, categoryids);
		}
	}

	@Override
	public boolean updateKnowledge(Knowledge knowledge, Object knowledgeDetail) {
		// int type = knowledge.getKnowledgetype();
		// Long id = 0l;
		// switch (type) {
		// case 1:
		// break;
		// case 2:
		// InvestmentWord word = (InvestmentWord) knowledgeDetail;
		// id = investmentAuthenticationService.createInvestmentWord(word);
		// break;
		//
		// default:
		// break;
		// }
		// knowledge.setKnowledgeid(id);
		// int i = knowledgeDao.insert(knowledge);
		return true;

	}

	@Override
	public Long saveKnowledgeTitle(Knowledge knowledge, Object knowledgeDetail) {
		// int type = knowledge.getKnowledgetype();
		// Long id = 0l;
		// switch (type) {
		// case 1:
		// break;
		// case 2:
		// InvestmentWord word = (InvestmentWord) knowledgeDetail;
		// id = investmentAuthenticationService.createInvestmentWord(word);
		// break;
		// default:
		// break;
		// }
		// knowledge.setKnowledgeid(id);
		// int i = knowledgeDao.insert(knowledge);
		return (long) 0;
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
			count = knowledgeContentDAO.deleteByknowledgeId(ids);
			if (count > 0) {
				count = knowledgeBetweenDAO.deleteKnowledgeRCategory(ids,
						categoryid);
				if (count > 0) {
					return 1;

				} else {
					return 0;
				}
			}
		}
		return 0;
	}

	@Override
	public int updateKnowledge(Knowledge knowledge, long categoryid,
			long categoryids[]) {
		int count = 0;
		count = knowledgeDao.updateKnowledge(knowledge, categoryids);
		if (count > 0) {
			if (categoryids != null && categoryids.length > 0) {

				count = deleteKnowledgeRCategory(knowledge.getId(), categoryid);
				if (count > 0) {
					// insertKnowledgeRCategory(knowledge, categoryids);
					return 1;
				}
			}
			return 1;
		}
		return 0;
	}

	@Override
	public int updateKnowledgeRCategory(long knowledgeid, long categoryid,
			long[] categoryids) {

		return 0;
	}

	@Override
	public int deleteKnowledgeRCategory(long knowledgeid, long categoryid) {

		return knowledgeDao.deleteKnowledgeRCategory(knowledgeid, categoryid);
	}

	@Override
	public int checkIndustryNameRepeat(String knowledgetitle) {

		return knowledgeDao.checkIndustryNameRepeat(knowledgetitle);
	}

	@Override
	public int checkLawNameRepeat(String knowledgetitle) {
		return knowledgeDao.checkLayNameRepeat(knowledgetitle);
	}

	@Override
	public int checkInvestmentNameRepeat(String knowledgetitle) {
		return knowledgeDao.checkInvestmentNameRepeat(knowledgetitle);
	}

}
