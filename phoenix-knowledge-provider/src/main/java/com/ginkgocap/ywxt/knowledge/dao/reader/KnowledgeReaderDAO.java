package com.ginkgocap.ywxt.knowledge.dao.reader;

public interface KnowledgeReaderDAO {
	
	String findHtmlContentFromMongo(long kid, String type)
			throws ClassNotFoundException;
	
}
