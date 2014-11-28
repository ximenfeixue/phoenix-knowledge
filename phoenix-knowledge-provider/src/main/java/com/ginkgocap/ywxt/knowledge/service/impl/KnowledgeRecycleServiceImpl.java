package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ginkgocap.ywxt.knowledge.dao.knowledge.KnowledgeRecycleDAO;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeRecycle;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeRecycleExample;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeRecycleExample.Criteria;
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
	public KnowledgeRecycle selectByKnowledgeId(long knowledgeid) {

		return knowledgeRecycleMapper.selectByPrimaryKey(knowledgeid);
	}

	@Override
	public List<KnowledgeRecycle> selectKnowledgeRecycle(long userid,
			String type, String keyword, int pageno, int pagesize) {

		KnowledgeRecycleExample example = new KnowledgeRecycleExample();
		Criteria criteria = example.createCriteria();
		criteria.andUseridEqualTo(userid);
		if (StringUtils.isNotBlank(keyword)) {
			criteria.andTitleLike("%" + keyword + "%");
		}
		example.setOrderByClause("createtime desc");
		example.setLimitStart(pageno);
		example.setLimitEnd(pagesize);
		return knowledgeRecycleMapper.selectByExample(example);
	}

	@Override
	public int countKnowledgeRecycle(long userid, String type, String keyword) {

		KnowledgeRecycleExample example = new KnowledgeRecycleExample();
		Criteria criteria = example.createCriteria();
		criteria.andUseridEqualTo(userid);
		if (StringUtils.isNotBlank(keyword)) {
			criteria.andTitleLike("%" + keyword + "%");
		}
		return knowledgeRecycleMapper.countByExample(example);
	}

	@Override
	@Transactional
	public int deleteKnowledgeRecycle(long knowledgeid) {

		return knowledgeRecycleMapper.deleteByPrimaryKey(knowledgeid);
	}

	@Override
	@Transactional
	public int insertKnowledgeRecycle(long knowledgeid, String recyclename,
			String type, long userid, long categoryid) {

		KnowledgeRecycle recycle = new KnowledgeRecycle();
		recycle.setKnowledgeId(knowledgeid);
		recycle.setTitle(recyclename);
		recycle.setUserid(userid);
		recycle.setCreatetime(new Date());
		recycle.setType(type);
		recycle.setCatetoryid(categoryid);
		return knowledgeRecycleMapper.insertSelective(recycle);
	}

	@Override
	public int emptyKnowledgeRecycle(long userid) {
		KnowledgeRecycleExample example = new KnowledgeRecycleExample();
		Criteria criteria = example.createCriteria();
		criteria.andUseridEqualTo(userid);
		return knowledgeRecycleMapper.deleteByExample(example);
	}
}
