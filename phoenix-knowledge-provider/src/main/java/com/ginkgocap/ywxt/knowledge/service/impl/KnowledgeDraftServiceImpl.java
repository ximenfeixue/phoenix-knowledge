package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.dao.knowledge.KnowledgeDraftDAO;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeDraft;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeDraftMapper;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeDraftService;
import com.ginkgocap.ywxt.knowledge.util.Page;

@Service("knowledgeDraftService")
public class KnowledgeDraftServiceImpl implements KnowledgeDraftService {

	@Resource
	private KnowledgeDraftMapper knowledgeDraftMapper;

	@Autowired
	private KnowledgeDraftDAO knowledgeDraftDAO;

	@Override
	public int insertKnowledgeDraft(long knowledgeid, String draftname,
			String type, long userid) {

		KnowledgeDraft draft = new KnowledgeDraft();
		draft.setKnowledgeId(knowledgeid);
		draft.setDraftname(draftname);
		draft.setDrafttype(type);
		draft.setCreatetime(new Date());
		draft.setUserid(userid);
		return knowledgeDraftMapper.insertSelective(draft);
	}

	@Override
	public Page<KnowledgeDraft> selectKnowledgeDraft(Page<KnowledgeDraft> page,
			long userid, String type) {
		List<KnowledgeDraft> list = knowledgeDraftDAO.selectKnowledgeDraft(
				userid, type, (page.getPageNo() - 1) * page.getPageSize(),
				page.getPageSize());

		int count = knowledgeDraftDAO.countKnowledgeDraft(userid, type);

		page.setTotalItems(count);
		page.setResult(list);

		return page;
	}

	@Override
	public int deleteKnowledgeDraft(long[] knowledgeids, long userid) {

		return knowledgeDraftDAO.deleteKnowledgeDraft(knowledgeids, userid);
	}

	@Override
	public KnowledgeDraft selectByKnowledgeId(long knowledgeid) {

		return knowledgeDraftMapper.selectByPrimaryKey(knowledgeid);
	}

	@Override
	public List<KnowledgeDraft> selectKnowledgeDraft(long userid, String type,
			int pageno, int pagesize) {

		return knowledgeDraftDAO.selectKnowledgeDraft(userid, type, pageno,
				pagesize);
	}

	@Override
	public int countKnowledgeDraft(long userid, String type) {

		return knowledgeDraftDAO.countKnowledgeDraft(userid, type);
	}

	@Override
	public int deleteKnowledgeDraft(long knowledgeid) {
		
		return 0;
	}

}
