package com.ginkgocap.ywxt.knowledge.service;

import java.util.Map;

import com.ginkgocap.ywxt.knowledge.entity.KnowledgeStatics;
import com.ginkgocap.ywxt.user.model.User;

public interface KnowledgeReader {

	User getUserInfo(long userid);

	KnowledgeStatics getKnowledgeStatusCount(long kid);

	Map<String, Integer> isExistFriends(long loginuserid, long kuid);

	Map<String, Boolean> showHeadTag(long kid, int type);

	String getKnowledgeContent(long kid,String type);
}
