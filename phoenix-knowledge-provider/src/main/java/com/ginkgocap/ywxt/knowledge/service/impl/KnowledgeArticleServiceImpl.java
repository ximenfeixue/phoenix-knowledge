package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.dao.knowledge.KnowledgeDao;
import com.ginkgocap.ywxt.knowledge.dao.knowledgearticle.KnowledgeArticleDAO;
import com.ginkgocap.ywxt.knowledge.dao.knowledgecategory.KnowledgeCategoryDAO;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeArticle;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNews;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeArticleService;
import com.ibatis.sqlmap.client.SqlMapClient;

@Service("knowledgeArticleService")
public class KnowledgeArticleServiceImpl implements KnowledgeArticleService {

	@Autowired
	private KnowledgeDao knowledgeDao;

	@Autowired
	private KnowledgeArticleDAO knowledgeArticleDAO;

	@Autowired
	private KnowledgeCategoryDAO knowledgeBetweenDAO;

	@Override
	public void deleteKnowledge(long[] ids) {

		knowledgeArticleDAO.deleteKnowledge(ids);
	}

	@Override
	public void updateKnowledge(String title, long userid, String uname,
			long cid, String cname, String cpath, String content, String pic,
			String desc, String essence, String taskid, String tags,
			long knowledgeid) {

		knowledgeArticleDAO.updateKnowledge(title, userid, uname, cid, cname,
				cpath, content, pic, desc, essence, taskid, tags, knowledgeid);
	}

	@Override
	public KnowledgeArticle selectKnowledge(long knowledgeid) {

		return knowledgeArticleDAO.selectKnowledge(knowledgeid);
	}

	@Override
	public List<KnowledgeArticle> selectByParam(Long columnid, long source,
			Long userid, List<Long> ids, int page, int size) {
		return knowledgeArticleDAO.selectByParam(columnid, source, userid, ids,
				page, size);
	}

	@Override
	public void deleteKnowledgeByid(long knowledgeid) {
		knowledgeArticleDAO.deleteKnowledgeByid(knowledgeid);
	}

	@Override
	public KnowledgeArticle insertknowledge(String title, long userid,
			String uname, long cid, String cname, String cpath, String content,
			String pic, String desc, String essence, String taskid,
			String tags, long knowledgeid, long columnid) {

		return knowledgeArticleDAO.insertknowledge(title, userid, uname, cid,
				cname, cpath, content, pic, desc, essence, taskid, tags,
				knowledgeid, columnid);
	}

	@Override
	public void restoreKnowledgeByid(long knowledgeid) {
		knowledgeArticleDAO.restoreKnowledgeByid(knowledgeid);

	}
}
