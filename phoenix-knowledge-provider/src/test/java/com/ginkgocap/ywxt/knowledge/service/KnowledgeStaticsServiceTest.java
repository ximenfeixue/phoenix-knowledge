package com.ginkgocap.ywxt.knowledge.service;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeStatics;
import com.ginkgocap.ywxt.knowledge.service.knowledge.KnowledgeCategoryService;
import com.ginkgocap.ywxt.knowledge.service.knowledge.KnowledgeStaticsService;

/**
 * 知识测试类
 * 
 * @author caihe
 * 
 */

public class KnowledgeStaticsServiceTest extends TestBase {

	public KnowledgeStaticsServiceTest() {
		System.out.println(123456);
	}

	@Autowired
	private KnowledgeStaticsService knowledgeStaticsService;

	@Autowired
	private KnowledgeCategoryService knowledgeCategoryService;

	@Test
	public void testinsertKnowledgeR() {

		KnowledgeStatics knowledgeStatics = new KnowledgeStatics();
		knowledgeStatics.setKnowledgeId((long) 111);
		// knowledgeStatics.setCollectionCount(0);
		// knowledgeStatics.setCommentCount(0);
		// knowledgeStatics.setShareCount(0);
		// knowledgeStatics.setClickCount(0);
		knowledgeStaticsService.insertKnowledgeStatics(knowledgeStatics);
	}

	// 测试获取评论排行列表
	@Test
	public void testSelectRankList() {
		KnowledgeStatics knowledgeStatics = knowledgeStaticsService
				.selectByknowledgeId(111);
		System.out.println(knowledgeStatics.getKnowledgeId());

	}

}
