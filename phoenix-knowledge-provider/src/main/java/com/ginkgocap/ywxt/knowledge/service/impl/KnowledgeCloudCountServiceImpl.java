package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.entity.UserPermissionExample;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeCollectionMapper;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeStaticsMapperManual;
import com.ginkgocap.ywxt.knowledge.mapper.UserPermissionMapper;
import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.service.AttachmentService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeCloudCountService;
import com.ginkgocap.ywxt.knowledge.util.Constants;
import com.ginkgocap.ywxt.user.model.User;

@Service("knowledgeCloudCountService")
public class KnowledgeCloudCountServiceImpl implements KnowledgeCloudCountService {
	
	@Resource
	private KnowledgeStaticsMapperManual knowledgeStaticsMapperManual;
	

	@Resource
	private MongoTemplate mongoTemplate;

	@Resource
	private UserPermissionMapper userPermissionMapper;
	
	@Override
	public void countReader(Long kid) {
		// 添加点击数
				knowledgeStaticsMapperManual.updateStatics(kid, 0, 0, 0,
						Constants.StaticsValue.clickCount.v());
	}

	@Override
	public Map<String, Object> isHavePremisson(Knowledge knowledge,User user) {
		Map<String, Object> result=new HashMap<String, Object>();
		// 判断用户对此文章权限
		UserPermissionExample example = new UserPermissionExample();
		com.ginkgocap.ywxt.knowledge.entity.UserPermissionExample.Criteria criteria = example
				.createCriteria();
		if (user == null) {
			criteria.andReceiveUserIdEqualTo((long) Constants.Ids.platform.v());
			criteria.andKnowledgeIdEqualTo(knowledge.getId());

		} else {
			if (user.getId() == knowledge.getUid()) {
				result.put(Constants.status, Constants.ResultType.success.v());
				return result;
			}
			List<Long> idList = new ArrayList<Long>();
			idList.add((long) Constants.Ids.platform.v());
			idList.add(user.getId());
			criteria.andKnowledgeIdEqualTo(knowledge.getId());
			criteria.andReceiveUserIdIn(idList);
			criteria.andTypeNotEqualTo(Constants.PermissionType.xiaoles.v());
		}
		int v = userPermissionMapper.countByExample(example);
		if (v > 0) {
			result.put(Constants.status, Constants.ResultType.success.v());
		} else {
			result.put(Constants.status, Constants.ResultType.fail.v());
			result.put(Constants.errormessage,
					Constants.ErrorMessage.artPermissionNotFound.c());
		}
		return result;
	}



}
