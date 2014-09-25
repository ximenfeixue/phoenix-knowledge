package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.entity.KnowledgeComment;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCommentExample;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCommentExample.Criteria;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeCommentMapper;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeCommentService;
import com.ginkgocap.ywxt.knowledge.util.Constants;

@Service("knowledgeCommentService")
public class KnowledgeCommentServiceImpl implements KnowledgeCommentService {

	@Resource
	private KnowledgeCommentMapper knowledgeCommentMapper;

	@Override
	public Map<String, Object> addComment(long kid, long userid, long pid,
			String content) {
		Map<String, Object> result = new HashMap<String, Object>();
		KnowledgeComment comment = new KnowledgeComment();
		comment.setKnowledgeId(kid);
		comment.setCreatetime(new Date());
		comment.setParentid(pid);
		comment.setStatus(true);
		comment.setUserId(userid);
		comment.setStatus(Constants.CommentStatus.common.c());
		comment.setCount(0l);
		comment.setContent(content);
		int v = knowledgeCommentMapper.insertSelective(comment);
		if (v == 0) {
			result.put(Constants.status, Constants.ResultType.fail.v());
			result.put(Constants.errormessage,
					Constants.ErrorMessage.addCommentFail.c());
		} else {
			result.put(Constants.status, Constants.ResultType.success.v());
		}
		return result;
	}

	@Override
	public Map<String, Object> findCommentList(long kid, long pid) {
		Map<String, Object> result = new HashMap<String, Object>();
		KnowledgeCommentExample example = new KnowledgeCommentExample();
		Criteria criteria = example.createCriteria();
		criteria.andKnowledgeIdEqualTo(kid);
		criteria.andParentidEqualTo(pid);
		List<KnowledgeComment> list = knowledgeCommentMapper
				.selectByExample(example);
		result.put("list", list);
		result.put(Constants.status, Constants.ResultType.success.v());

		return result;
	}

}
