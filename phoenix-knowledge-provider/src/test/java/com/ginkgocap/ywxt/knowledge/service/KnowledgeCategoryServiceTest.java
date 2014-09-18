package com.ginkgocap.ywxt.knowledge.service;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.ywxt.cloud.service.InvestmentAuthenticationService;
import com.ginkgocap.ywxt.cloud.service.InvestmentCommonService;
import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNews;
import com.ginkgocap.ywxt.knowledge.service.content.KnowledgeContentService;
import com.ginkgocap.ywxt.knowledge.service.knowledge.KnowledgeMainService;
import com.ginkgocap.ywxt.knowledge.service.knowledgecategory.KnowledgeCategoryService;
import com.ginkgocap.ywxt.knowledge.service.news.KnowledgeNewsService;

/**
 * 知识测试类
 * 
 * @author caihe
 * 
 */

public class KnowledgeCategoryServiceTest extends TestBase {

	public KnowledgeCategoryServiceTest() {
		System.out.println(123456);
	}

	@Autowired
	private KnowledgeContentService knowledgeContentService;

	@Autowired
	private KnowledgeCategoryService knowledgeCategoryService;

	@Test
	public void testinsertKnowledgeR() {

		long[] categoryid = { 1, 2 };
		knowledgeCategoryService.insertKnowledgeRCategory(11, categoryid, 111,
				"测试标题", "author", 6, "share_author", new Date(), "标签",
				"know_desc", 11, "D:\\pic.png");
	}
}
