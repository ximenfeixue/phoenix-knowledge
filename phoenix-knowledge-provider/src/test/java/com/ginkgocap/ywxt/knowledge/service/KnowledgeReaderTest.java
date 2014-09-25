package com.ginkgocap.ywxt.knowledge.service;

import javax.annotation.Resource;

import org.junit.Test;

import com.ginkgocap.ywxt.knowledge.base.TestBase;

public class KnowledgeReaderTest extends TestBase{

	@Resource
	private KnowledgeReaderService knowledgeReaderService;
	
	@Resource
	private KnowledgeCommentService knowledgeCommentService;
	@Test
	public void testCount(){
	
		knowledgeReaderService.getKnowledgeContent(1, "1");
	}
	
	@Test
	public void testAddComment(){
		System.out.println(knowledgeCommentService.addComment(1001, 10470309, 0, "这文章不错呀"));
	}
	
	
	@Test
	public void testFindComment(){
		System.out.println(knowledgeCommentService.findCommentList(1001, 0));
	}
}
