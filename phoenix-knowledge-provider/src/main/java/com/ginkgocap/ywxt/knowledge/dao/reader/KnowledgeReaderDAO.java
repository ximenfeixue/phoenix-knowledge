package com.ginkgocap.ywxt.knowledge.dao.reader;

import com.ginkgocap.ywxt.knowledge.model.Knowledge;

public interface KnowledgeReaderDAO {

	Knowledge findHtmlContentFromMongo(long kid, String type)
			throws ClassNotFoundException;


	int addStaticsCount(long kid, int commentCount, int shareCount,
			int collCount, int clickCount);

}
