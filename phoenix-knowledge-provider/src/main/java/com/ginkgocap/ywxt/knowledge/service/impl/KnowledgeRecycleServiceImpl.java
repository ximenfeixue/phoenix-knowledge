package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.dao.knowledge.KnowledgeRecycleDAO;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeRecycle;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeRecycleExample;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeRecycleMapper;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeRecycleValueMapper;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeRecycleService;

@Service("knowledgeRecycleService")
public class KnowledgeRecycleServiceImpl implements KnowledgeRecycleService {

	@Resource
	private KnowledgeRecycleMapper knowledgeRecycleMapper;

	@Resource
	private KnowledgeRecycleValueMapper knowledgeRecycleValueMapper;

	@Autowired
	private KnowledgeRecycleDAO knowledgeRecycleDAO;

	@Override
	public int deleteKnowledgeDraft(long[] knowledgeids, long userid) {

		return knowledgeRecycleDAO.deleteKnowledgeRecycle(knowledgeids, userid);
	}

	@Override
	public KnowledgeRecycle selectByKnowledgeId(long knowledgeid) {

		return knowledgeRecycleMapper.selectByPrimaryKey(knowledgeid);
	}

	@Override
	public List<KnowledgeRecycle> selectKnowledgeRecycle(long userid,
			String type, int pageno, int pagesize) {

		return knowledgeRecycleDAO.selectKnowledgeRecycle(userid, type, pageno,
				pagesize);
	}

	@Override
	public int countKnowledgeRecycle(long userid, String type) {

		return knowledgeRecycleDAO.countKnowledgeRecycle(userid, type);
	}

	@Override
	public int deleteKnowledgeRecycle(long knowledgeid) {

		return knowledgeRecycleMapper.deleteByPrimaryKey(knowledgeid);
	}

	@Override
	public int insertKnowledgeRecycle(long knowledgeid, String recyclename,
			String type, long userid) {

		KnowledgeRecycle recycle = new KnowledgeRecycle();
		recycle.setKnowledgeId(knowledgeid);
		recycle.setTitle(recyclename);
		recycle.setUserid(userid);
		recycle.setCreatetime(new Date());
		recycle.setType(type);
		return knowledgeRecycleMapper.insertSelective(recycle);
	}

	@Override
	public int emptyKnowledgeRecycle() {

		return knowledgeRecycleValueMapper.emptyKnowledgeRecycle();
	}
}
