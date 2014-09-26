package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.entity.KnowledgeDraft;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeDraftMapper;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeDraftService;

@Service("knowledgeDraftService")
public class KnowledgeDraftServiceImpl implements KnowledgeDraftService {

	@Resource
	private KnowledgeDraftMapper knowledgeDraftMapper;

	@Override
	public int insertKnowledgeDraft(long knowledgeid, String draftname,
			int type, long userid) {

		KnowledgeDraft draft = new KnowledgeDraft();
		draft.setKnowledgeId(knowledgeid);
		draft.setDraftname(draftname);
		draft.setDrafttype(type);
		draft.setCreatetime(new Date());
		draft.setUserid(userid);
		return knowledgeDraftMapper.insertSelective(draft);
	}

}
