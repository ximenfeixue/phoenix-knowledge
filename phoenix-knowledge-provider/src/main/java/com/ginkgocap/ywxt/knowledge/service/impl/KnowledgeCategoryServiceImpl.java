package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.dao.knowledgecategory.KnowledgeCategoryDAO;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCategory;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCategoryExample;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCategoryExample.Criteria;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeCategoryMapper;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeCategoryService;
import com.ginkgocap.ywxt.knowledge.util.Constants;

@Service("knowledgeCategoryService")
public class KnowledgeCategoryServiceImpl implements KnowledgeCategoryService {

	@Autowired
	private KnowledgeCategoryDAO knowledgeBetweenDAO;

	@Resource
	private KnowledgeCategoryMapper knowledgeCategoryMapper;

	@Override
	public int insertKnowledgeRCategory(long knowledgeid, long categoryid[],
			long userid, String title, String author, int path,
			String share_author, Date createtime, String tag, String know_desc,
			long column_id, String pic_path) {
		return knowledgeBetweenDAO.insertKnowledgeRCategory(knowledgeid,
				categoryid, userid, title, author, path, share_author,
				createtime, tag, know_desc, column_id, pic_path);
	}

	@Override
	public int deleteKnowledgeRCategory(long[] knowledgeids, long categoryid) {

		return knowledgeBetweenDAO.deleteKnowledgeRCategory(knowledgeids,
				categoryid);
	}

	@Override
	public int countByKnowledgeCategoryId(long categoryid) {
		return knowledgeBetweenDAO.countByKnowledgeCategoryId(categoryid);
	}

	@Override
	public int deleteKnowledgeCategory(long knowledgeid) {

		return 0;
	}

	@Override
	public int updateKnowledgeCategory(long knowledgeid, long categoryid) {

		KnowledgeCategory knowledgecategory = new KnowledgeCategory();
		knowledgecategory.setStatus(Constants.ReportStatus.unreport.v() + "");
		KnowledgeCategoryExample example = new KnowledgeCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andKnowledgeIdEqualTo(knowledgeid);
		criteria.andCategoryIdEqualTo(categoryid);
		return knowledgeCategoryMapper.updateByExample(knowledgecategory,
				example);
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
		return knowledgeCategoryMapper.updateByExample(knowledgecategory,
				example);
	}

}
