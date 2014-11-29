package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ginkgocap.ywxt.knowledge.dao.statics.KnowledgeStaticsDAO;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeStatics;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeStaticsMapper;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNewsVO;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeStaticsService;

@Service("knowledgeStaticsService")
public class KnowledgeStaticsServiceImpl implements KnowledgeStaticsService {

	@Autowired
	private KnowledgeStaticsDAO knowledgeStaticsDAO;

	@Autowired
	private KnowledgeStaticsMapper knowledgeStaticsMapper;

	@Override
	public int insertKnowledgeStatics(KnowledgeStatics knowledgeStatics) {
		return knowledgeStaticsDAO.insertKnowledgeStatics(knowledgeStatics);
	}

	@Override
	public KnowledgeStatics selectByknowledgeId(long knowledgeid) {
		return knowledgeStaticsMapper.selectByPrimaryKey(knowledgeid);
	}

	@Override
	@Transactional
	public int initKnowledgeStatics(KnowledgeNewsVO vo, Short source) {

		int sV = 0;
		KnowledgeStatics KnowledgeStatics = selectByknowledgeId(vo.getkId());
		if (KnowledgeStatics != null) {
			KnowledgeStatics statics = new KnowledgeStatics();
			statics.setClickcount(0l);
			statics.setCollectioncount(0l);
			statics.setCommentcount(0l);
			statics.setKnowledgeId(vo.getkId());
			statics.setSharecount(0l);
			statics.setTitle(vo.getTitle());
			statics.setSource(source);
			statics.setType(Short.parseShort(vo.getColumnType()));
			sV = knowledgeStaticsMapper.updateByPrimaryKeySelective(statics);
		} else {
			KnowledgeStatics statics = new KnowledgeStatics();
			statics.setClickcount(0l);
			statics.setCollectioncount(0l);
			statics.setCommentcount(0l);
			statics.setKnowledgeId(vo.getkId());
			statics.setSharecount(0l);
			statics.setTitle(vo.getTitle());
			statics.setSource(source);
			statics.setType(Short.parseShort(vo.getColumnType()));
			sV = knowledgeStaticsMapper.insertSelective(statics);
		}
		return sV;
	}

}
