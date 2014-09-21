package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.Date; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.dao.knowledgecategory.KnowledgeCategoryDAO; 
import com.ginkgocap.ywxt.knowledge.service.KnowledgeCategoryService;

@Service("knowledgeCategoryService")
public class KnowledgeCategoryServiceImpl implements KnowledgeCategoryService {

	@Autowired
	private KnowledgeCategoryDAO knowledgeBetweenDAO;

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

}
