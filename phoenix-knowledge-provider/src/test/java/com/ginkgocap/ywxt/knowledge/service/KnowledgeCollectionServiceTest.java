package com.ginkgocap.ywxt.knowledge.service;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeCollection;
import com.ginkgocap.ywxt.knowledge.service.knowledgecollection.KnowledgeCollectionService;

/**
 * 知识测试类
 * 
 * @author caihe
 * 
 */

public class KnowledgeCollectionServiceTest extends TestBase {

	public KnowledgeCollectionServiceTest() {
		System.out.println(123456);
	}

	@Autowired
	private KnowledgeCollectionService knowledgeCollectionService;

	@Test
	public void testinsertKnowledgeR() {

		KnowledgeCollection knowledgeCollection = new KnowledgeCollection();

		knowledgeCollection.setKnowledge_id(12);
		knowledgeCollection.setColumn_id(21);
		knowledgeCollection.setTimestamp(new Date());
		knowledgeCollection.setCategory_id(22);
		knowledgeCollection.setKnowledgeType("1");
		knowledgeCollection.setSource("不知道");
		KnowledgeCollection result = knowledgeCollectionService
				.insertKnowledgeCollection(knowledgeCollection);
		System.out.println(result.getId());
	}
}
