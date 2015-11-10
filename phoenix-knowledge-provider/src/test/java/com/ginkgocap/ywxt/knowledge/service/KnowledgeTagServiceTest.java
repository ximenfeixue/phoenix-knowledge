package com.ginkgocap.ywxt.knowledge.service;

import org.junit.Test; 
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeCategoryService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeTagService;

/**
 * 知识测试类
 * 
 * @author caihe
 * 
 */

public class KnowledgeTagServiceTest extends TestBase {

//	public KnowledgeTagServiceTest() {
//		System.out.println(123456);
//	}
//
	@Autowired
	private KnowledgeTagService knowledgeTagService;
//
//	@Autowired
//	private KnowledgeCategoryService knowledgeBetweenService;
//
//	@Test
//	public void testinsertKnowledgeTag() {
//
//		KnowledgeTag knowledge = new KnowledgeTag();
//
//		knowledgeTagService.insertKnowledgeTag(knowledge);
//	}
//
	@Test
	public void testdeleteKnowledgeR() {

		knowledgeTagService.saveOrUpdateUserTag(9999L,  "121dd", 36L, 1L, -1L);
	}
}
