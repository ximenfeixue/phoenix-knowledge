package com.ginkgocap.ywxt.knowledge.dao.reader;

import java.util.Map;

public interface KnowledgeReaderDAO {

	Map<String, Object> findHtmlContentFromMongo(long kid, String type)
			throws ClassNotFoundException;

	int addCollection(long kid, long userid, String type, String source,
			long columnid, long categoryid);

}
