package com.ginkgocap.ywxt.knowledge.dao.knowledge.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.ginkgocap.ywxt.knowledge.dao.knowledge.KnowledgeDraftDAO;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeDraft;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeDraftExample;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeDraftExample.Criteria;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeDraftMapper;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeDraftValueMapper;

/**
 * @author caihe
 * 
 */
@Component("knowledgeDraft")
public class KnowledgeDraftDAOImpl implements KnowledgeDraftDAO {

	@Resource
	private KnowledgeDraftMapper knowledgeDraftMapper;

	@Resource
	private KnowledgeDraftValueMapper knowledgeDraftValueMapper;

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
	public int deleteKnowledgeDraft(long[] knowledgeids, long userid) {

		return knowledgeDraftValueMapper.deleteKnowledge(knowledgeids, userid);
	}

	@Override
	public int deleteKnowledgeDraft(long knowledgeid) {

		return knowledgeDraftMapper.deleteByPrimaryKey(knowledgeid);
	}

	@Override
	public int insertKnowledge(long knowledgeid, String draftname,
			String drafttype, String type, long userid) {

		KnowledgeDraft knowledgedraft = new KnowledgeDraft();
		knowledgedraft.setKnowledgeId(knowledgeid);
		knowledgedraft.setDrafttype(drafttype);
		knowledgedraft.setDraftname(draftname);
		knowledgedraft.setUserid(userid);
		knowledgedraft.setType(type);
		knowledgedraft.setCreatetime(new Date());
		return knowledgeDraftMapper.insert(knowledgedraft);
	}

}
