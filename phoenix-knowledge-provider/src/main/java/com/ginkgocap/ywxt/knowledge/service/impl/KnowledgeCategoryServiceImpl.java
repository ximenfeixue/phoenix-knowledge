package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.dao.knowledgecategory.KnowledgeCategoryDAO;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeBase;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCategory;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCategoryExample;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCategoryExample.Criteria;
import com.ginkgocap.ywxt.knowledge.entity.UserCategory;
import com.ginkgocap.ywxt.knowledge.entity.UserCategoryExample;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeBaseMapper;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeCategoryMapper;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeCategoryValueMapper;
import com.ginkgocap.ywxt.knowledge.mapper.UserCategoryMapper;
import com.ginkgocap.ywxt.knowledge.mapper.UserCategoryValueMapper;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNewsVO;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeCategoryService;
import com.ginkgocap.ywxt.knowledge.service.UserCategoryService;
import com.ginkgocap.ywxt.knowledge.util.Constants;
import com.ginkgocap.ywxt.knowledge.util.HtmlToText;
import com.ginkgocap.ywxt.knowledge.util.KnowledgeUtil;
import com.ginkgocap.ywxt.knowledge.util.tree.ConvertUtil;

@Service("knowledgeCategoryService")
public class KnowledgeCategoryServiceImpl implements KnowledgeCategoryService {

	@Autowired
	private KnowledgeCategoryDAO knowledgeBetweenDAO;

	@Resource
	private KnowledgeCategoryMapper knowledgeCategoryMapper;
	@Resource
	private UserCategoryService userCategoryService;

	@Autowired
	private UserCategoryValueMapper userCategoryValueMapper;

	@Resource
	private UserCategoryMapper userCategoryMapper;

	@Resource
	private KnowledgeCategoryValueMapper knowledgeCategoryValueMapper;

	@Resource
	private KnowledgeBaseMapper knowledgeBaseMapper;

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

	@Override
	public int updateKnowledgeCategory(long knowledgeid, long categoryid) {

		KnowledgeCategory knowledgecategory = new KnowledgeCategory();
		knowledgecategory.setStatus(Constants.ReportStatus.unreport.v() + "");
		KnowledgeCategoryExample example = new KnowledgeCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andKnowledgeIdEqualTo(knowledgeid);
		criteria.andCategoryIdEqualTo(categoryid);
		return knowledgeCategoryMapper.updateByExampleSelective(
				knowledgecategory, example);
	}

	@Override
	public List<KnowledgeCategory> selectKnowledgeCategory(long knowledgeid,
			long categoryid) {
		KnowledgeCategoryExample example = new KnowledgeCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andKnowledgeIdEqualTo(knowledgeid);
		criteria.andCategoryIdEqualTo(categoryid);
		return knowledgeCategoryMapper.selectByExample(example);
	}

	@Override
	public int updateKnowledgeCategorystatus(long knowledgeid, long categoryid) {

		KnowledgeCategory knowledgecategory = new KnowledgeCategory();
		knowledgecategory.setStatus(Constants.ReportStatus.report.v() + "");
		KnowledgeCategoryExample example = new KnowledgeCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andKnowledgeIdEqualTo(knowledgeid);
		criteria.andCategoryIdEqualTo(categoryid);
		return knowledgeCategoryMapper.updateByExampleSelective(
				knowledgecategory, example);
	}

	@Override
	public int insertCategory(long knowledgeid, long categoryid, long userid,
			String title, String author, String path, String share_author,
			Date createtime, String tag, String know_desc, long column_id,
			String pic_path) {

		return knowledgeBetweenDAO.insertCategory(knowledgeid, categoryid,
				userid, title, author, path, share_author, createtime, tag,
				know_desc, column_id, pic_path);
	}

	@Override
	public int insertKnowledgeRCategory(long kId, long[] cIds, long userId,
			String username, String columnPath, KnowledgeNewsVO vo) {
		// 批量插入知识目录表
		List<KnowledgeCategory> list = new ArrayList<KnowledgeCategory>();
		KnowledgeCategory knowledgeRCategory = null;
		for (int k = 0; k < cIds.length; k++) {
			UserCategory category = userCategoryService
					.selectByPrimaryKey(cIds[k]);
			if (category != null) {
				knowledgeRCategory = new KnowledgeCategory();
				knowledgeRCategory.setKnowledgeId(kId);
				knowledgeRCategory.setCategoryId(cIds[k]);
				// 1生效 0不生效
				knowledgeRCategory.setStatus(vo.getStatus());

				list.add(knowledgeRCategory);
			}
		}
		int v = knowledgeCategoryValueMapper.batchInsert(list);

		if (v != 0) {
			KnowledgeBase b = knowledgeBaseMapper.selectByPrimaryKey(kId);
			if (b == null) {
				// 插入知识基类
				KnowledgeBase base = new KnowledgeBase();
				base.setKnowledgeId(kId);
				base.setTitle(vo.getTitle());
				int columnType = Integer.parseInt(vo.getColumnType());
				String desc = vo.getDesc();
				if (columnType == Constants.Type.Investment.v()
						|| columnType == Constants.Type.Industry.v()
						|| columnType == Constants.Type.Case.v()) {
					if (desc != null && desc.length() > 0) {
						base.setcDesc(HtmlToText.htmlTotest(desc));
					} else {
						base.setcDesc(HtmlToText.htmlTotest(vo.getContent()));
					}
				} else {
					base.setcDesc(HtmlToText.htmlTotest(vo.getContent()));
				}
				base.setColumnId(Long.parseLong(vo.getColumnid()));
				base.setColumnType(Short.parseShort(vo.getColumnType()));
				base.setCreatetime(new Date());
				base.setTag(StringUtils.isNotBlank(vo.getTags()) ? ConvertUtil
						.ToEnglishSymbol(vo.getTags()) : "");
				base.setAuthor(username);
				base.setPath(columnPath);
				base.setEssence(Short.parseShort(vo.getEssence()));
				base.setPicPath(vo.getPic());
				base.setUserId(userId);
				base.setTaskid(vo.getTaskId());

				int returnV = knowledgeBaseMapper.insertSelective(base);
			}

		}

		return v;
	}

	@Override
	public long[] getCurrentCategoryArray(String ids, int type, long userId) {
		long[] cIds = null;
		if (StringUtils.isBlank(ids)) {
			UserCategoryExample example = new UserCategoryExample();
			com.ginkgocap.ywxt.knowledge.entity.UserCategoryExample.Criteria criteria = example
					.createCriteria();
			criteria.andSortidEqualTo(Constants.unGroupSortId);
			criteria.andUserIdEqualTo(userId);
			criteria.andCategoryTypeEqualTo((short) type);
			List<UserCategory> list = userCategoryMapper
					.selectByExample(example);
			cIds = new long[1];
			cIds[0] = list.get(0).getId();
		} else {
			cIds = KnowledgeUtil.formatString(ids);
		}

		return cIds;
	}

	@Override
	public List<KnowledgeCategory> selectKnowledgeCategory(long knowledgeid) {

		KnowledgeCategoryExample example = new KnowledgeCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andKnowledgeIdEqualTo(knowledgeid);
		return knowledgeCategoryMapper.selectByExample(example);
	}

	@Override
	public int insertKnowledgeCategoryNogroup(long knowledgeid, long categoryid) {

		KnowledgeCategory knowledgecategory = new KnowledgeCategory();
		knowledgecategory.setStatus(Constants.ReportStatus.report.v() + "");
		knowledgecategory.setKnowledgeId(knowledgeid);
		knowledgecategory.setCategoryId(categoryid);
		return knowledgeCategoryMapper.insertSelective(knowledgecategory);
	}

	@Override
	public List<KnowledgeCategory> selectKnowledgeCategory(long knowledgeid,
			long categoryid, String status) {
		KnowledgeCategoryExample example = new KnowledgeCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andKnowledgeIdEqualTo(knowledgeid);
		criteria.andCategoryIdEqualTo(categoryid);
		criteria.andStatusEqualTo(status);
		return knowledgeCategoryMapper.selectByExample(example);
	}

	@Override
	public List<KnowledgeCategory> selectKnowledgeCategory(long knowledgeid,
			String status) {
		KnowledgeCategoryExample example = new KnowledgeCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andKnowledgeIdEqualTo(knowledgeid);
		criteria.andStatusEqualTo(Constants.KnowledgeCategoryStatus.effect.v()
				+ "");
		return knowledgeCategoryMapper.selectByExample(example);
	}

}
