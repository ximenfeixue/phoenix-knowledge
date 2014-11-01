package com.ginkgocap.ywxt.knowledge.service;

import javax.annotation.Resource;

import org.junit.Test;

import com.ginkgocap.ywxt.knowledge.base.TestBase;

public class KnowledgeCommentTest extends TestBase{
	
	@Resource
	private KnowledgeCommentService knowledgeCommentService;
	
	@Test
	public void testKnowledgeComment(){
		knowledgeCommentService.addComment(222, 1, 166, "zzzz");
	}
}
