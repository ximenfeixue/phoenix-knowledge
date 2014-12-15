package com.ginkgocap.ywxt.knowledge.dao.knowledge.impl;

import java.util.ArrayList; 
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;
import com.ginkgocap.ywxt.knowledge.dao.knowledge.KnowledgeDao;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCategory;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeCategoryValueMapper; 
import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeIndustry;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeInvestment;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeLaw;
import com.ginkgocap.ywxt.knowledge.service.impl.CategoryHelper;
import com.ginkgocap.ywxt.knowledge.util.Constants;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * @author zhangwei
 * 
 */
@Component("knowledgeDao")
public class KnowledgeDaoImpl  implements
		KnowledgeDao {
 
	private CategoryHelper helper = new CategoryHelper();

	@Resource
	private MongoTemplate mongoTemplate;

 

	@Resource
	private KnowledgeCategoryValueMapper knowledgeCategoryValueMapper;

	@Override
	public int insert(Knowledge record) {
        return 0;
		 
	}

 

	@Override
	public int moveCategoryBatch(long[] knowledgeids, long[] categoryids) {
		List<KnowledgeCategory> list = new ArrayList<KnowledgeCategory>();
		KnowledgeCategory knowledgeRCategory = null;
		for (int i = 0; i < knowledgeids.length; i++) {
			for (int k = 0; k < categoryids.length; k++) {
				knowledgeRCategory = new KnowledgeCategory();
				knowledgeRCategory.setKnowledgeId(knowledgeids[i]);
				knowledgeRCategory.setCategoryId(categoryids[k]);
				knowledgeRCategory
						.setStatus(Constants.KnowledgeCategoryStatus.effect.v()
								+ "");
				list.add(knowledgeRCategory);
			}
		}
		return knowledgeCategoryValueMapper.batchInsert(list);
	}

	@Override
	public int deleteByPrimaryKey(Long id) {
		// TODO Auto-generated method stub
		return 0;
	}
 
 

	@Override
	public int checkIndustryNameRepeat(String knowledgetitle) {

		if (StringUtils.isNotBlank(knowledgetitle)) {

			Criteria criteria = Criteria.where("title").is(knowledgetitle);
			Query query = new Query(criteria);
			List<KnowledgeIndustry> list = mongoTemplate.find(query,
					KnowledgeIndustry.class);
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
	public int checkLayNameRepeat(String knowledgetitle) {
		if (StringUtils.isNotBlank(knowledgetitle)) {

			Criteria criteria = Criteria.where("title").is(knowledgetitle);
			Query query = new Query(criteria);
			List<KnowledgeLaw> list = mongoTemplate.find(query,
					KnowledgeLaw.class);
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

		if (StringUtils.isNotBlank(knowledgetitle)) {
			Criteria criteria = Criteria.where("title").is(knowledgetitle)
					.and("status").is(4);
			Query query = new Query(criteria);
			List<KnowledgeInvestment> list = mongoTemplate.find(query,
					KnowledgeInvestment.class);
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
	public void updateInvestment(Long id,String pic,String refrenceData,String imageBookData, String content, String desc) {
		Criteria c = Criteria.where("_id").is(id);
		Update update = new Update();
		update.set("desc", desc);
		update.set("pic", pic);
		update.set("content", content);
		update.set("refrenceData", refrenceData);
		update.set("imageBookData", imageBookData);
		Query query = new Query(c);
		mongoTemplate.updateFirst(query, update, "KnowledgeInvestment");
	}



    @Override
    public int insertSelective(Knowledge record) {
        // TODO Auto-generated method stub
        return 0;
    }



    @Override
    public Knowledge selectByPrimaryKey(Long id) {
        // TODO Auto-generated method stub
        return null;
    }



    @Override
    public int updateByPrimaryKeySelective(Knowledge record) {
        // TODO Auto-generated method stub
        return 0;
    }



    @Override
    public int updateByPrimaryKey(Knowledge record) {
        // TODO Auto-generated method stub
        return 0;
    }



    @Override
    public Knowledge insertknowledge(Knowledge knowledge) {
        // TODO Auto-generated method stub
        return null;
    }



    @Override
    public int deleteKnowledge(long[] ids, long categoryid) {
        // TODO Auto-generated method stub
        return 0;
    }



    @Override
    public int updateKnowledge(Knowledge knowledge, long[] categoryid) {
        // TODO Auto-generated method stub
        return 0;
    }



    @Override
    public int updateKnowledgeRCategory(long knowledgeid, long categoryid, long[] categoryids) {
        // TODO Auto-generated method stub
        return 0;
    }



    @Override
    public int deleteKnowledgeRCategory(long knowledgeid, long categoryid) {
        // TODO Auto-generated method stub
        return 0;
    }
}
