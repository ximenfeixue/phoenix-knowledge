package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.dao.knowledge.KnowledgeDraftDAO;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeDraft;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeDraftExample;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeDraftExample.Criteria;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeDraftMapper;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeDraftValueMapper;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeDraftService;
import com.ginkgocap.ywxt.knowledge.util.Page;

@Service("knowledgeDraftService")
public class KnowledgeDraftServiceImpl implements KnowledgeDraftService {

	@Resource
	private KnowledgeDraftMapper knowledgeDraftMapper;

	@Resource
	private KnowledgeDraftValueMapper knowledgeDraftValueMapper;

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

		return knowledgeDraftValueMapper.deleteKnowledge(knowledgeids, userid);
	}

	@Override
	public KnowledgeDraft selectByKnowledgeId(long knowledgeid) {

		return knowledgeDraftMapper.selectByPrimaryKey(knowledgeid);
	}

	@Override
	public List<KnowledgeDraft> selectKnowledgeDraft(long userid, String type,
			int pageno, int pagesize) {

		KnowledgeDraftExample example = new KnowledgeDraftExample();
		Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(type)) {
			criteria.andDrafttypeEqualTo(type);
		}
		criteria.andUseridEqualTo(userid);
		example.setOrderByClause("createtime desc");
		example.setLimitStart(pageno);
		example.setLimitEnd(pagesize);
		return knowledgeDraftMapper.selectByExample(example);
	}

	@Override
	public int countKnowledgeDraft(long userid, String type) {

		KnowledgeDraftExample example = new KnowledgeDraftExample();
		Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(type)) {
			criteria.andDrafttypeEqualTo(type);
		}
		criteria.andUseridEqualTo(userid);
		return knowledgeDraftMapper.countByExample(example);
	}

	@Override
	public int deleteKnowledgeDraft(long knowledgeid) {
		
		return 0;
	}

}
