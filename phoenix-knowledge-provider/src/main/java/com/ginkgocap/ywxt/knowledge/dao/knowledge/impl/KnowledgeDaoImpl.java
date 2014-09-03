package com.ginkgocap.ywxt.knowledge.dao.knowledge.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

import com.ginkgocap.ywxt.knowledge.dao.category.CategoryDao;
import com.ginkgocap.ywxt.knowledge.dao.knowledge.KnowledgeDao;
import com.ginkgocap.ywxt.knowledge.model.Category;
import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeRCategory;
import com.ginkgocap.ywxt.knowledge.service.category.impl.CategoryHelper;
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

	private CategoryHelper helper = new CategoryHelper();

	@PostConstruct
	public void initSqlMapClient() {
		super.setSqlMapClient(sqlMapClient);
	}

	
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
		 int i =  getSqlMapClientTemplate().update("knowledge.updateByPrimaryKey",record);
			return i;
	 }

	@Override
	public int checkNameRepeat(int knowledgetype, String knowledgetitle) {

		Map<String, Object> map = new HashMap<String, Object>();
		if (knowledgetype != 0) {
			map.put("knowledgetype", knowledgetype);
		}
		if (StringUtils.isNotBlank(knowledgetitle)) {
			map.put("knowledgetitle", knowledgetitle);
		}
		List<Knowledge> list = (List<Knowledge>) getSqlMapClientTemplate()
				.queryForList("tb_knowledge.selectByTitle", map);
		if (list != null && list.size() > 0) {

			return 0;// 名称已存在
		} else {
			return 1;// 名称可以使用
		}
	}

	@Override
	public void moveCategory(long knowledgeid, long categoryid, String sortId) {

		Map<String, Object> map = new HashMap<String, Object>();
		if (knowledgeid > 0) {
			map.put("knowledgeid", knowledgeid);
		}

		if (knowledgeid > 0) {
			map.put("categoryid", categoryid);
		}

		if (StringUtils.isNotBlank(sortId)) {
			map.put("sortId", sortId);
		}
		getSqlMapClientTemplate().update("tb_knowledge_category.update", map);
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
				"tb_knowledge_category.delete", map);
		return count;
	}

	@Override
	public void moveCategoryBatch(long[] knowledgeids, long[] categoryids) {
		List<KnowledgeRCategory> list = new ArrayList<KnowledgeRCategory>();
		KnowledgeRCategory knowledgeRCategory = null;
		for (int i = 0; i < knowledgeids.length; i++) {
			for (int k = 0; k < categoryids.length; k++) {
				knowledgeRCategory = new KnowledgeRCategory();
				knowledgeRCategory.setKnowledgeid(knowledgeids[i]);
				knowledgeRCategory.setCategoryid(categoryids[k]);
				Category category = categoryDao
						.selectByPrimaryKey(categoryids[k]);
				if (category != null) {
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
							knowledgeRCategory.setSortId(newSortId);
						} else {
							knowledgeRCategory.setSortId(category.getSortId());
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					list.add(knowledgeRCategory);
				}else{
					continue;
				}

			}
		}
		getSqlMapClientTemplate().insert("tb_knowledge_category.batchInsert",
				list);
	}

	@Override
	public int deleteByPrimaryKey(Long id) {
		// TODO Auto-generated method stub
		return 0;
	}

}
