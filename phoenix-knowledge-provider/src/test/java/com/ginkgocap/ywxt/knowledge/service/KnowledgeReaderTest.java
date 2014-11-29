package com.ginkgocap.ywxt.knowledge.service;

import javax.annotation.Resource;

import org.junit.Test;

import com.ginkgocap.ywxt.knowledge.base.TestBase;

public class KnowledgeReaderTest extends TestBase {

	@Resource
	private KnowledgeReaderService knowledgeReaderService;

	@Resource
	private KnowledgeCommentService knowledgeCommentService;

	@Resource
	private KnowledgeReportService knowledgeReportService;

	@Test
	public void testCount() {

//		knowledgeReaderService.getKnowledgeContent(1, "1");
	}

	@Test
	public void testAddComment() {
//		System.out.println(knowledgeCommentService.addComment(1001, 10470309,
//				0, "这文章不错呀"));
	}

	@Test
	public void testFindComment() {
//		System.out.println(knowledgeCommentService.findCommentList(332715, 47, 1, 5, user));
	}

	@Test
	public void testAddReport() {
//		System.out.println(knowledgeReportService.addReport(1001, "1",
//				"太垃圾了太垃圾了太垃圾了太垃圾了太垃圾了太垃圾了太垃圾了太垃圾了", 10470309));
	}
}
