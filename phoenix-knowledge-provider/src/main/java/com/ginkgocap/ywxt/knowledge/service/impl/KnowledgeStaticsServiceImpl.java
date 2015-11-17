package com.ginkgocap.ywxt.knowledge.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeStatics;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeStaticsMapper;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNewsVO;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeStaticsService;
import com.ginkgocap.ywxt.knowledge.util.Constants;

@Service("knowledgeStaticsService")
public class KnowledgeStaticsServiceImpl implements KnowledgeStaticsService {
	@Autowired
	private KnowledgeStaticsMapper knowledgeStaticsMapper;

	@Override
	public int insertKnowledgeStatics(KnowledgeStatics knowledgeStatics) {
		return knowledgeStaticsMapper.insertSelective(knowledgeStatics);
	}

	@Override
	public KnowledgeStatics selectByknowledgeId(long knowledgeid) {
		return knowledgeStaticsMapper.selectByPrimaryKey(knowledgeid);
	}

	@Override
	@Transactional
	public int initKnowledgeStatics(KnowledgeNewsVO vo, Short source) {
		int sV = 0;
		KnowledgeStatics knowledgeStatics = selectByknowledgeId(vo.getkId());
		KnowledgeStatics statics = new KnowledgeStatics();
		statics.setClickcount(0l);
		statics.setCollectioncount(0l);
		statics.setCommentcount(0l);
		statics.setKnowledgeId(vo.getkId());
		statics.setSharecount(0l);
		statics.setSource(source);
		statics.setType(Short.parseShort(vo.getColumnType()));
		if (knowledgeStatics != null) {
			sV = knowledgeStaticsMapper.updateByPrimaryKeySelective(statics);
		} else {
			sV = knowledgeStaticsMapper.insertSelective(statics);
		}
		return sV;
	
	}

	@Override
	public int deleteKnowledgeStatics(long knowledgeid) {

		KnowledgeStatics KnowledgeStatics = selectByknowledgeId(knowledgeid);
		if (KnowledgeStatics != null) {
			int count = knowledgeStaticsMapper.deleteByPrimaryKey(knowledgeid);
			if (count > 0) {
				return 1;
			} else {
				return 0;
			}

		} else {

			return 0;
		}
	}

	@Override
	public int initKnowledgeStatics(long knowledgeid, String title, Short type) {

		KnowledgeStatics KnowledgeStatics = selectByknowledgeId(knowledgeid);
		if (KnowledgeStatics != null) {
			knowledgeStaticsMapper.deleteByPrimaryKey(knowledgeid);
		}
		KnowledgeStatics statics = new KnowledgeStatics();
		statics.setClickcount(0l);
		statics.setCollectioncount(0l);
		statics.setCommentcount(0l);
		statics.setKnowledgeId(knowledgeid);
		statics.setSharecount(0l);
		statics.setSource((short) Constants.KnowledgeSource.user.v());
		statics.setType(type);
		int sV = knowledgeStaticsMapper.insertSelective(statics);
		if (sV > 0) {
			return 1;
		}
		return 0;
	}
}
