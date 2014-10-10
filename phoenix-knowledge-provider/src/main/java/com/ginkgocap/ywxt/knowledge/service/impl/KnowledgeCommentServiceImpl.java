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
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeStaticsMapperManual;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeCommentService;
import com.ginkgocap.ywxt.knowledge.util.Constants;
import com.ginkgocap.ywxt.user.model.User;
import com.ginkgocap.ywxt.user.service.UserService;

@Service("knowledgeCommentService")
public class KnowledgeCommentServiceImpl implements KnowledgeCommentService {

	@Resource
	private KnowledgeCommentMapper knowledgeCommentMapper;

	@Resource
	private KnowledgeStaticsMapperManual knowledgeStaticsMapperManual;

	@Resource
	private UserService userService;

	@Override
	public Map<String, Object> addComment(long kid, long userid, long pid,
			String content) {
		Map<String, Object> result = new HashMap<String, Object>();
		User user = userService.selectByPrimaryKey(userid);
		if (user == null) {
			result.put(Constants.status, Constants.ResultType.fail.v());
			result.put(Constants.errormessage,
					Constants.ErrorMessage.artUserNotExsit.c());
			return result;
		}
		KnowledgeComment comment = new KnowledgeComment();
		comment.setKnowledgeId(kid);
		comment.setCreatetime(new Date());
		comment.setParentid(pid);
		comment.setStatus(true);
		comment.setUserId(userid);
		comment.setStatus(Constants.CommentStatus.common.c());
		comment.setCount(0l);
		comment.setContent(content);
		comment.setUsername(user.getUserName());
		comment.setPic(user.getPicPath());

		int v = knowledgeCommentMapper.insertSelective(comment);
		if (v == 0) {

			result.put(Constants.status, Constants.ResultType.fail.v());
			result.put(Constants.errormessage,
					Constants.ErrorMessage.addCommentFail.c());
		} else {
			knowledgeStaticsMapperManual.updateStatics(kid,
					Constants.StaticsValue.commentCount.v(), 0, 0, 0);
			result.put(Constants.status, Constants.ResultType.success.v());
		}
		return result;
	}

	@Override
	public Map<String, Object> findCommentList(long kid, long pid, Integer pno,
			Integer psize) {
		Map<String, Object> result = new HashMap<String, Object>();
		KnowledgeCommentExample example = new KnowledgeCommentExample();
		Criteria criteria = example.createCriteria();
		criteria.andKnowledgeIdEqualTo(kid);
		criteria.andParentidEqualTo(pid);
		example.setLimitStart((pno - 1) * psize);
		example.setLimitEnd(psize);

		example.setOrderByClause("createtime desc");
		List<KnowledgeComment> list = knowledgeCommentMapper
				.selectByExample(example);
		result.put("list", list);
		result.put("totalcount", knowledgeCommentMapper.countByExample(example));

		result.put(Constants.status, Constants.ResultType.success.v());
		return result;
	}

	@Override
	public Map<String, Object> delComment(long id, long userid) {
		Map<String, Object> result = new HashMap<String, Object>();
		KnowledgeComment kc = knowledgeCommentMapper.selectByPrimaryKey(id);
		if (kc == null) {
			result.put(Constants.status, Constants.ResultType.fail.v());
			result.put(Constants.errormessage,
					Constants.ErrorMessage.commentNotExsit.c());
			return result;
		}
		if (userid != kc.getUserId()) {
			result.put(Constants.status, Constants.ResultType.fail.v());
			result.put(Constants.errormessage,
					Constants.ErrorMessage.delCommentNotPermission.c());
			return result;
		}

		int v = knowledgeCommentMapper.deleteByPrimaryKey(id);
		if (v == 0) {
			result.put(Constants.status, Constants.ResultType.fail.v());
			result.put(Constants.errormessage,
					Constants.ErrorMessage.delCommentFail.c());
			return result;
		}
		
		result.put(Constants.status, Constants.ResultType.success.v());

		return result;
	}

}
