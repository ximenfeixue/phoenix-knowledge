package com.ginkgocap.ywxt.knowledge.dao.knowledgecategory.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ginkgocap.ywxt.knowledge.dao.content.KnowledgeContentDAO;
import com.ginkgocap.ywxt.knowledge.dao.knowledgecategory.KnowledgeCategoryDAO;
import com.ginkgocap.ywxt.knowledge.dao.usercategory.UserCategoryDao;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCategory;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCategoryExample;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCategoryExample.Criteria;
import com.ginkgocap.ywxt.knowledge.entity.UserCategory;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeCategoryMapper;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeCategoryValueMapper;
import com.ginkgocap.ywxt.knowledge.mapper.UserCategoryMapper;
import com.ginkgocap.ywxt.knowledge.mapper.UserCategoryValueMapper;
import com.ginkgocap.ywxt.knowledge.service.UserCategoryService;
import com.ginkgocap.ywxt.knowledge.service.impl.CategoryHelper;

/**
 * @author caihe
 * 
 */
@Component("knowledgeCategoryDAO")
public class KnowledgeCategoryDAOImpl implements KnowledgeCategoryDAO {

	@Autowired
	private UserCategoryDao userCategoryDao;

	@Autowired
	private KnowledgeContentDAO knowledgeContentDAO;

	@Resource
	private KnowledgeCategoryValueMapper knowledgeCategoryValueMapper;

	@Resource
	private KnowledgeCategoryMapper knowledgeCategoryMapper;

	private CategoryHelper helper = new CategoryHelper();

	@Resource
	private UserCategoryService userCategoryService;

	@Resource
	private UserCategoryMapper userCategoryMapper;

	@Autowired
	private UserCategoryValueMapper userCategoryValueMapper;

	@Override
	public int insertKnowledgeRCategory(long knowledgeid, long categoryid[],
			long userid, String title, String author, int path,
			String share_author, Date createtime, String tag, String know_desc,
			long column_id, String pic_path) {
		List<KnowledgeCategory> list = new ArrayList<KnowledgeCategory>();
		KnowledgeCategory knowledgeRCategory = null;
		for (int k = 0; k < categoryid.length; k++) {
			UserCategory category = userCategoryService
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
					String parentSortId = parentId > 0 ? userCategoryMapper.selectByPrimaryKey(parentId).getSortid() : "";
					// 通过parentSortId得到子类最大已添加的sortId
					String childMaxSortId = userCategoryValueMapper.selectMaxSortId(category.getUserId(),parentSortId, category.getType());
					if (StringUtils.isBlank(category.getSortid())) {
						// 如果用户第一次添加，将childMaxSortId赋值
						String newSortId = new String("");
						if (childMaxSortId == null	|| "null".equals(childMaxSortId)|| "".equals(childMaxSortId)) {
							newSortId = parentSortId + "000000001";
						} else {
							newSortId = helper.generateSortId(childMaxSortId);
						}
						// 通过已添加的最大的SortId生成新的SortId
						// 设置最新的sortId
						knowledgeRCategory.setSortid(newSortId);
					} else {
						knowledgeRCategory.setSortid(category.getSortid());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				list.add(knowledgeRCategory);
			} else {
				continue;
			}

		}
		if (list != null && list.size() > 0) {

			return knowledgeCategoryValueMapper.batchInsert(list);
		} else {

			return 0;
		}

	}

	@Override
	public int deleteKnowledgeRCategory(long[] knowledgeids, long categoryid) {

		return knowledgeCategoryValueMapper.deleteKnowledge(knowledgeids,
				categoryid);

	}

	@Override
	public int countByKnowledgeCategoryId(long categoryid) {
		KnowledgeCategoryExample example = new KnowledgeCategoryExample();
		Criteria criteria = example.createCriteria();
		if (categoryid > 0) {
			criteria.andCategoryIdEqualTo(categoryid);
		}
		return knowledgeCategoryMapper.countByExample(example);
	}

	@Override
	public int deleteKnowledgeCategory(long knowledgeid) {
		KnowledgeCategoryExample example = new KnowledgeCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andKnowledgeIdEqualTo(knowledgeid);
		return knowledgeCategoryMapper.deleteByExample(example);
	}

}
