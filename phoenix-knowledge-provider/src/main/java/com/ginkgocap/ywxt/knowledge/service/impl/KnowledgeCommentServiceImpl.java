package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ginkgocap.ywxt.knowledge.entity.KnowledgeComment;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCommentExample;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCommentExample.Criteria;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeCommentMapper;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeCommentMapperManual;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeStaticsMapperManual;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeCommentVO;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeCommentService;
import com.ginkgocap.ywxt.knowledge.util.Constants;
import com.ginkgocap.ywxt.user.model.User;
import com.ginkgocap.ywxt.user.service.UserService;

@Service("knowledgeCommentService")
public class KnowledgeCommentServiceImpl implements KnowledgeCommentService {

	@Resource
	private KnowledgeCommentMapper knowledgeCommentMapper;
	@Resource
	private KnowledgeCommentMapperManual knowledgeCommentMapperManual;

	@Resource
	private KnowledgeStaticsMapperManual knowledgeStaticsMapperManual;

	@Resource
	private UserService userService;

	private static final int pno = 1;

	private static final int psize = 10;

	@Override
	@Transactional
	public Map<String, Object> addComment(long kid, User user, long pid,
			String content) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (user == null) {
			result.put(Constants.status, Constants.ResultType.fail.v());
			result.put(Constants.errormessage,
					Constants.ErrorMessage.userNotLogin.c());
			return result;
		}
		User currUser = userService.selectByPrimaryKey(user.getId());
		if (currUser == null) {
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
		comment.setUserId(user.getId());
		comment.setStatus(Constants.CommentStatus.common.c());
		comment.setCount(0l);
		comment.setContent(content);
		comment.setUsername(currUser.getName());
		comment.setPic(currUser.getPicPath());

		int v = knowledgeCommentMapper.insertSelective(comment);

		if (v == 0) {

			result.put(Constants.status, Constants.ResultType.fail.v());
			result.put(Constants.errormessage,
					Constants.ErrorMessage.addCommentFail.c());
		} else {
			// 修改子评论数
			if (pid != 0) {
				knowledgeCommentMapperManual.updateCountByPrimaryKey(pid, 1);
			}
			// 修改统计信息表值
			knowledgeStaticsMapperManual.updateStatics(kid,
					Constants.StaticsValue.commentCount.v(), 0, 0, 0);

			result.putAll(findCommentList(kid, pid, 1, 10, user));
			result.put("pno", 1);
		}

		return result;
	}

	@Override
	public Map<String, Object> findCommentList(long kid, long pid, Integer pno,
			Integer psize, User user) {
		Map<String, Object> result = new HashMap<String, Object>();
		KnowledgeCommentExample example = new KnowledgeCommentExample();
		Criteria criteria = example.createCriteria();
		criteria.andKnowledgeIdEqualTo(kid);
		result.put("total", knowledgeCommentMapper.countByExample(example));

		criteria.andParentidEqualTo(pid);
		example.setLimitStart((pno - 1) * psize);
		example.setLimitEnd(psize);
		example.setOrderByClause("createtime desc");
		List<KnowledgeComment> kcList = knowledgeCommentMapper
				.selectByExample(example);

		List<KnowledgeCommentVO> kcVOList = null;
		if (kcList != null && kcList.size() > 0) {
			kcVOList = new ArrayList<KnowledgeCommentVO>();
			for (KnowledgeComment kc : kcList) {
				try {
					KnowledgeCommentVO kcVO = new KnowledgeCommentVO();
					PropertyUtils.copyProperties(kcVO, kc);
					kcVO.setIsSelf(user == null ? false : user.getId() == kcVO
							.getUserId() ? true : false);
					kcVOList.add(kcVO);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		result.put("list", kcVOList);
		result.put("totalcount", knowledgeCommentMapper.countByExample(example));
		result.put(Constants.status, Constants.ResultType.success.v());
		return result;
	}

	// private String text2Html(String content) {
	// if (StringUtils.isBlank(content)) {
	// return "";
	// }
	//
	// return content.replaceAll("\n", "<br/>").replaceAll(" ", "&nbsp;");
	// }

	@Override
	@Transactional
	public Map<String, Object> delComment(long id, long kId, User user) {
		Map<String, Object> result = new HashMap<String, Object>();
		KnowledgeComment kc = knowledgeCommentMapper.selectByPrimaryKey(id);
		if (kc == null) {
			result.put(Constants.status, Constants.ResultType.fail.v());
			result.put(Constants.errormessage,
					Constants.ErrorMessage.commentNotExsit.c());
			return result;
		}
		if (user.getId() != kc.getUserId()) {
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
		} else {
			int delV = -1;
			// 修改子评论数
			if (kc.getParentid() != 0) {
				knowledgeCommentMapperManual.updateCountByPrimaryKey(
						kc.getParentid(), -1);
			} else {
				KnowledgeCommentExample example = new KnowledgeCommentExample();
				Criteria criteria = example.createCriteria();
				criteria.andParentidEqualTo(id);
				delV = delV - knowledgeCommentMapper.deleteByExample(example);
			}
			// 删除总评论数
			knowledgeStaticsMapperManual.updateStatics(kId, delV, 0, 0, 0);
		}

		result.putAll(findCommentList(kId, kc.getParentid(), pno, psize, user));
		result.put("pno", 1);
		result.put(Constants.status, Constants.ResultType.success.v());

		return result;
	}

	@Override
	public int deleteCommentByknowledgeId(long knowledgeid, long userId) {
		KnowledgeCommentExample example = new KnowledgeCommentExample();
		Criteria criteria = example.createCriteria();
		criteria.andKnowledgeIdEqualTo(knowledgeid);
		criteria.andUserIdEqualTo(userId);
		return knowledgeCommentMapper.deleteByExample(example);

	}

}