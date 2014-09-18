package com.ginkgocap.ywxt.knowledge.reader.impl;

import javax.annotation.Resource;

import com.ginkgocap.ywxt.knowledge.entity.KnowledgeStatics;
import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.reader.KnowledgeReader;
import com.ginkgocap.ywxt.knowledge.service.knowledgestatics.KnowledgeStaticsService;
import com.ginkgocap.ywxt.user.model.User;
import com.ginkgocap.ywxt.user.service.FriendsRelationService;
import com.ginkgocap.ywxt.user.service.UserService;

public abstract class KnowledgeReaderImpl implements KnowledgeReader {

	@Resource
	private UserService userService;

	@Resource
	private KnowledgeStaticsService knowledgeStaticsService;
	
	@Resource
	private FriendsRelationService friendsRelationService;

	@Override
	public User getUserInfo(long userid) {
		return userService.selectByPrimaryKey(userid);
	}

	@Override
	public KnowledgeStatics getKnowledgeStatusCount(long kid) {
		return knowledgeStaticsService.selectByknowledgeId(kid);
	}

	@Override
	public boolean isExistFriends(long loginuserid, long kuid) {
		return friendsRelationService.isExistFriends(loginuserid, kuid);
	}

	@Override
	public boolean showHeadTag(long kid,short type) {
//		Knowledge 
		return false;
	}
	
	
}
