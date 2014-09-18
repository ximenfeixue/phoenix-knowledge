package com.ginkgocap.ywxt.knowledge.reader;

import com.ginkgocap.ywxt.knowledge.entity.KnowledgeStatics;
import com.ginkgocap.ywxt.user.model.User;

public interface KnowledgeReader {

	User getUserInfo(long userid);
	
	KnowledgeStatics getKnowledgeStatusCount(long kid);
}
