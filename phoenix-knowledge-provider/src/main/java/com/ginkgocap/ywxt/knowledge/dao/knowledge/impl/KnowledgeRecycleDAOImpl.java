package com.ginkgocap.ywxt.knowledge.dao.knowledge.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.ginkgocap.ywxt.knowledge.dao.knowledge.KnowledgeRecycleDAO;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeDraftExample;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeRecycle;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeRecycleExample;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeRecycleExample.Criteria;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeDraftValueMapper;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeRecycleMapper;

/**
 * @author caihe
 * 
 */
@Component("knowledgeRecycle")
public class KnowledgeRecycleDAOImpl implements KnowledgeRecycleDAO {

	@Resource
	private KnowledgeRecycleMapper knowledgeRecycleMapper;

	@Resource
	private KnowledgeDraftValueMapper knowledgeDraftValueMapper;

	@Override
	public List<KnowledgeRecycle> selectKnowledgeRecycle(long userid,
			String type, int pageno, int pagesize) {

		KnowledgeRecycleExample example = new KnowledgeRecycleExample();
		Criteria criteria = example.createCriteria();
		criteria.andUseridEqualTo(userid);
		example.setOrderByClause("createtime desc");
		example.setLimitStart(pageno);
		example.setLimitEnd(pagesize);
		return knowledgeRecycleMapper.selectByExample(example);
	}

	@Override
	public int countKnowledgeRecycle(long userid, String type) {

		KnowledgeRecycleExample example = new KnowledgeRecycleExample();
		Criteria criteria = example.createCriteria();
		criteria.andUseridEqualTo(userid);
		return knowledgeRecycleMapper.countByExample(example);
	}

	@Override
	public int deleteKnowledgeRecycle(long[] knowledgeids, long userid) {

		return knowledgeDraftValueMapper.deleteKnowledge(knowledgeids, userid);
	}

	@Override
	public int deleteKnowledgeRecycle(long knowledgeid) {

		return knowledgeRecycleMapper.deleteByPrimaryKey(knowledgeid);
	}

}
