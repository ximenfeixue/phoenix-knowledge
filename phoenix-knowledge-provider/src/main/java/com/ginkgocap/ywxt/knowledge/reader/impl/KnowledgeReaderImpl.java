package com.ginkgocap.ywxt.knowledge.reader.impl;

import javax.annotation.Resource;

import com.ginkgocap.ywxt.knowledge.entity.KnowledgeStatics;
import com.ginkgocap.ywxt.knowledge.reader.KnowledgeReader;
import com.ginkgocap.ywxt.knowledge.service.knowledgestatics.KnowledgeStaticsService;
import com.ginkgocap.ywxt.user.model.User;
import com.ginkgocap.ywxt.user.service.UserService;

public class KnowledgeReaderImpl implements KnowledgeReader {

	@Resource
	private UserService userService;

	@Resource
	private KnowledgeStaticsService knowledgeStaticsService;

	@Override
	public User getUserInfo(long userid) {
		return userService.selectByPrimaryKey(userid);
	}

	@Override
	public KnowledgeStatics getKnowledgeStatusCount(long kid) {
		return knowledgeStaticsService.selectByknowledgeId(kid);
	}

}
