package com.ginkgocap.ywxt.knowledge.dao.knowledgecategory.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

import com.ginkgocap.ywxt.knowledge.dao.content.KnowledgeContentDAO;
import com.ginkgocap.ywxt.knowledge.dao.knowledgecategory.KnowledgeCategoryDAO;
import com.ginkgocap.ywxt.knowledge.dao.usercategory.UserCategoryDao;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCategory;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeCategoryValueMapper;
import com.ginkgocap.ywxt.knowledge.mapper.UserCategoryTestMapper;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeRCategory;
import com.ginkgocap.ywxt.knowledge.model.UserCategory;
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
	private UserCategoryDao userCategoryDao;

	@Autowired
	private KnowledgeContentDAO knowledgeContentDAO;

	@Resource
	private UserCategoryTestMapper userCategoryTestMapper;

	@Resource
	private KnowledgeCategoryValueMapper knowledgeCategoryValueMapper;

	private CategoryHelper helper = new CategoryHelper();

	@Override
	public int insertKnowledgeRCategory(long knowledgeid, long categoryid[],
			long userid, String title, String author, int path,
			String share_author, Date createtime, String tag, String know_desc,
			long column_id, String pic_path) {
		List<KnowledgeCategory> list = new ArrayList<KnowledgeCategory>();
		KnowledgeCategory knowledgeRCategory = null;
		for (int k = 0; k < categoryid.length; k++) {
			UserCategory category = userCategoryDao
					.selectByPrimaryKey(categoryid[k]);
			if (category != null) {
				knowledgeRCategory = new KnowledgeCategory();
				knowledgeRCategory.setKnowledgeId((long) knowledgeid);
				knowledgeRCategory.setCategoryId(categoryid[k]);
				knowledgeRCategory.setUserId((long) userid);
				knowledgeRCategory.setTitle(title);
				knowledgeRCategory.setAuthor(author);
				knowledgeRCategory.setPath((short) path);
				knowledgeRCategory.setShareAuthor(share_author);
				knowledgeRCategory.setCreatetime(createtime);
				knowledgeRCategory.setTag(tag);
				knowledgeRCategory.setcDesc(know_desc);
				knowledgeRCategory.setColumnId((long) column_id);
				knowledgeRCategory.setPicPath(pic_path);
				try {
					// 得到要添加的分类的父类parentId
					long parentId = category.getParentId();
					// 得到要添加的分类的父类sortId
					String parentSortId = parentId > 0 ? userCategoryDao
							.selectByPrimaryKey(parentId).getSortId() : "";
					// 通过parentSortId得到子类最大已添加的sortId
					String childMaxSortId = userCategoryDao.selectMaxSortId(
							category.getUserId(), parentSortId);
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
		return knowledgeCategoryValueMapper.batchInsert(list);

	}

	@Override
	public int deleteKnowledgeRCategory(long[] knowledgeids, long categoryid) {

		return knowledgeCategoryValueMapper.deleteKnowledge(knowledgeids,
				categoryid);

	}

	@Override
	public long countByKnowledgeCategoryId(long id) {
		return (Long) getSqlMapClientTemplate().queryForObject(
				"tb_knowledge_category.countByKnowledgeCategoryId", id);
	}

}
