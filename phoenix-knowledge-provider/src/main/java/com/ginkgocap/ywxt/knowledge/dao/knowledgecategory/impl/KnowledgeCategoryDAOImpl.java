package com.ginkgocap.ywxt.knowledge.dao.knowledgecategory.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

import com.ginkgocap.ywxt.knowledge.dao.category.CategoryDao;
import com.ginkgocap.ywxt.knowledge.dao.content.KnowledgeContentDAO;
import com.ginkgocap.ywxt.knowledge.dao.knowledgecategory.KnowledgeCategoryDAO;
import com.ginkgocap.ywxt.knowledge.model.Category;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNews;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeRCategory;
import com.ginkgocap.ywxt.knowledge.service.category.impl.CategoryHelper;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * @author zhangwei
 * 
 */
@Component("knowledgeCategoryDAO")
public class KnowledgeCategoryDAOImpl extends SqlMapClientDaoSupport implements
		KnowledgeCategoryDAO {

	@Autowired
	SqlMapClient sqlMapClient;

	@PostConstruct
	public void initSqlMapClient() {
		super.setSqlMapClient(sqlMapClient);
	}

	@Autowired
	private CategoryDao categoryDao;

	@Autowired
	private KnowledgeContentDAO knowledgeContentDAO;

	private CategoryHelper helper = new CategoryHelper();

	@Override
	public void insertKnowledgeRCategory(long knowledgeid, long categoryid[],
			long userid, String title, String author, int path,
			String share_author, Date createtime, String tag, String know_desc,
			long column_id, String pic_path) {
		List<KnowledgeRCategory> list = new ArrayList<KnowledgeRCategory>();
		KnowledgeRCategory knowledgeRCategory = null;
		for (int k = 0; k < categoryid.length; k++) {
			Category category = categoryDao.selectByPrimaryKey(categoryid[k]);
			if (category != null) {
				knowledgeRCategory = new KnowledgeRCategory();
				knowledgeRCategory.setKnowledgeid(knowledgeid);
				knowledgeRCategory.setCategoryid(categoryid[k]);
				knowledgeRCategory.setUserid(userid);
				knowledgeRCategory.setTitle(title);
				knowledgeRCategory.setAuthor(author);
				knowledgeRCategory.setPath(path);
				knowledgeRCategory.setShare_author(share_author);
				knowledgeRCategory.setCreatetime(createtime);
				knowledgeRCategory.setTag(tag);
				knowledgeRCategory.setKnow_desc(know_desc);
				knowledgeRCategory.setColumn_id(column_id);
				knowledgeRCategory.setPic_path(pic_path);
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
							newSortId = helper.generateSortId(childMaxSortId);
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
			} else {
				continue;
			}

		}
		getSqlMapClientTemplate().insert("tb_knowledge_category.batchInsert",
				list);

	}

	@Override
	public int deleteKnowledgeRCategory(long[] knowledgeids, long categoryid) {

		Map<String, Object> map = new HashMap<String, Object>();
		if (knowledgeids.length > 0) {
			map.put("knowledgeids", knowledgeids);
		}
		if (categoryid > 0) {
			map.put("categoryid", categoryid);
		}
		int count = getSqlMapClientTemplate().delete(
				"tb_knowledge_category.delete", map);
		return count;
	}

    @Override
    public long countByKnowledgeCategoryId(long id) {
        return (Long)getSqlMapClientTemplate().queryForObject(
                "tb_knowledge_category.countByKnowledgeCategoryId", id);
    }

}
