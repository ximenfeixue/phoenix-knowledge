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
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

import com.ginkgocap.ywxt.knowledge.dao.category.CategoryDao;
import com.ginkgocap.ywxt.knowledge.dao.content.KnowledgeContentDAO;
import com.ginkgocap.ywxt.knowledge.dao.knowledge.KnowledgeDao;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCategory;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeCategoryValueMapper;
import com.ginkgocap.ywxt.knowledge.model.Category;
import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeIndustry;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeInvestment;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeLaw;
import com.ginkgocap.ywxt.knowledge.service.category.impl.CategoryHelper;
import com.ginkgocap.ywxt.knowledge.util.Constants;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * @author zhangwei
 * 
 */
@Component("knowledgeDao")
public class KnowledgeDaoImpl extends SqlMapClientDaoSupport implements
		KnowledgeDao {

	@Autowired
	SqlMapClient sqlMapClient;

	@Autowired
	private CategoryDao categoryDao;

	@Autowired
	private KnowledgeContentDAO knowledgeContentDAO;

	private CategoryHelper helper = new CategoryHelper();

	@Resource
	private MongoTemplate mongoTemplate;

	@PostConstruct
	public void initSqlMapClient() {
		super.setSqlMapClient(sqlMapClient);
	}

	@Resource
	private KnowledgeCategoryValueMapper knowledgeCategoryValueMapper;

	@Override
	public int insert(Knowledge record) {
		long r = record.getId();
		Long i = 0l;
		if (r > 0) {
			return updateByPrimaryKey(record);
		} else {
			i = (Long) getSqlMapClientTemplate().insert("knowledge.insert",
					record);
		}
		return i.intValue();
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
		int i = getSqlMapClientTemplate().update(
				"knowledge.updateByPrimaryKey", record);
		return i;
	}

	@Override
	public int moveCategoryBatch(long[] knowledgeids, long[] categoryids) {
		List<KnowledgeCategory> list = new ArrayList<KnowledgeCategory>();
		KnowledgeCategory knowledgeRCategory = null;
		for (int i = 0; i < knowledgeids.length; i++) {
			for (int k = 0; k < categoryids.length; k++) {
				Category category = categoryDao
						.selectByPrimaryKey(categoryids[k]);
				if (category != null) {
					knowledgeRCategory = new KnowledgeCategory();
					knowledgeRCategory.setKnowledgeId(knowledgeids[i]);
					knowledgeRCategory.setCategoryId(categoryids[k]);
					try {
						// 得到要添加的分类的父类parentId
						long parentId = category.getParentId();
						// 得到要添加的分类的父类sortId
						String parentSortId = parentId > 0 ? categoryDao
								.selectByPrimaryKey(parentId).getSortId() : "";
						// 通过parentSortId得到子类最大已添加的sortId
						String childMaxSortId = categoryDao.selectMaxSortId(
								category.getUid(), parentSortId);
						if (StringUtils.isBlank(category.getSortId())) {
							// 如果用户第一次添加，将childMaxSortId赋值
							String newSortId = new String("");
							if (childMaxSortId == null
									|| "null".equals(childMaxSortId)
									|| "".equals(childMaxSortId)) {
								newSortId = parentSortId + "000000001";
							} else {
								newSortId = helper
										.generateSortId(childMaxSortId);
							}
							// 通过已添加的最大的SortId生成新的SortId
							// 设置最新的sortId
							knowledgeRCategory.setSortid(newSortId);
						} else {
							knowledgeRCategory.setSortid(category.getSortId());
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					list.add(knowledgeRCategory);
				} else {
					continue;
				}

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
	public Knowledge insertknowledge(Knowledge knowledge) {

		Long id = (Long) getSqlMapClientTemplate().insert(
				"tb_knowledge.insertknowledge", knowledge);
		if (id != null) {
			knowledge.setId(id);
			return knowledge;
		} else {

			return null;
		}
	}

	@Override
	public int deleteKnowledge(long[] ids, long categoryid) {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ids", ids);
		int count = getSqlMapClientTemplate().delete(
				"tb_knowledge.deleteBatch", map);
		if (count > 0) {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public int updateKnowledge(Knowledge knowledge, long categoryid[]) {

		int count = getSqlMapClientTemplate().update(
				"tb_knowledge.updateknowledge", knowledge);
		if (count > 0) {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public int updateKnowledgeRCategory(long knowledgeid, long categoryid,
			long[] categoryids) {

		return 0;
	}

	@Override
	public int deleteKnowledgeRCategory(long knowledgeid, long categoryid) {

		Map<String, Object> map = new HashMap<String, Object>();
		if (knowledgeid > 0) {
			map.put("knowledgeid", knowledgeid);
		}
		if (categoryid > 0) {
			map.put("categoryid", categoryid);
		}
		int count = getSqlMapClientTemplate().delete(
				"tb_knowledge_category.deleteknowledgeid", map);
		return count;
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
}
