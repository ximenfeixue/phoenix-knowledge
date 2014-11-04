package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.cloud.service.InvestmentAuthenticationService;
import com.ginkgocap.ywxt.knowledge.dao.category.CategoryDao;
import com.ginkgocap.ywxt.knowledge.dao.content.KnowledgeContentDAO;
import com.ginkgocap.ywxt.knowledge.dao.knowledge.KnowledgeDao;
import com.ginkgocap.ywxt.knowledge.dao.knowledgecategory.KnowledgeCategoryDAO;
import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeLaw;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeMainService;
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

	@Resource
	private MongoTemplate mongoTemplate;

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
	public int moveCategoryBatch(long categoryid, long[] knowledgeids,
			long[] categoryids) {

		int count = knowledgeBetweenDAO.deleteKnowledgeRCategory(knowledgeids,
				categoryid);
		if (count > 0) {
			return knowledgeDao.moveCategoryBatch(knowledgeids, categoryids);
		}
		return 0;
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
		if (StringUtils.isNotBlank(knowledgetitle)) {

			Criteria criteria = Criteria.where("title").is(knowledgetitle);
			Query query = new Query(criteria);
			List<KnowledgeLaw> list = mongoTemplate.find(query,
					KnowledgeLaw.class,"KnowledgeLaw");
			if (list != null && list.size() > 0) {
				return 0;
			} else {
				return 1;
			}
		} else {
			return 0;
		}
	}

	@Override
	public int checkInvestmentNameRepeat(String knowledgetitle) {
		return knowledgeDao.checkInvestmentNameRepeat(knowledgetitle);
	}

}
