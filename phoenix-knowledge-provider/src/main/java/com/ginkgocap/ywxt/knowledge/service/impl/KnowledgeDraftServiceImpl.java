package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.entity.KnowledgeDraft;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeDraftExample;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeDraftExample.Criteria;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeDraftMapper;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeDraftService;

@Service("knowledgeDraftService")
public class KnowledgeDraftServiceImpl implements KnowledgeDraftService {

	@Resource
	private KnowledgeDraftMapper knowledgeDraftMapper;

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

}
