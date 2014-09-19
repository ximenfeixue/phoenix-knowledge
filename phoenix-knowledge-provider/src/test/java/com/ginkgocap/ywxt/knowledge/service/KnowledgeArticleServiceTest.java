package com.ginkgocap.ywxt.knowledge.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.ywxt.cloud.service.InvestmentAuthenticationService;
import com.ginkgocap.ywxt.cloud.service.InvestmentCommonService;
import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeArticle;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNews;
import com.ginkgocap.ywxt.knowledge.service.knowledge.KnowledgeArticleService;
import com.ginkgocap.ywxt.knowledge.service.knowledge.KnowledgeCategoryService;
import com.ginkgocap.ywxt.knowledge.service.knowledge.KnowledgeContentService;
import com.ginkgocap.ywxt.knowledge.service.knowledge.KnowledgeMainService;
import com.ginkgocap.ywxt.knowledge.service.knowledge.KnowledgeNewsService;

/**
 * 知识测试类
 * 
 * @author zhangwei
 * @创建时间：2013-03-29 10:24:22
 */

public class KnowledgeArticleServiceTest extends TestBase {

	public KnowledgeArticleServiceTest() {
		System.out.println(123456);
	}

	@Autowired
	private KnowledgeArticleService knowledgeArticleService;
	
	@Test
	public void testinsertKnowledge() {
		KnowledgeArticle knowledge = new KnowledgeArticle();
		knowledge.setId(2);
		knowledge.setUid(111);
		knowledge.setStatus(1);
		knowledge.setReport_status(5);
		knowledge.setCname("不知道");
		knowledge.setContent("这是测试内容");
		long[] categoryid = { 1, 2 };

		KnowledgeArticle knowresult = knowledgeArticleService
				.insertknowledge(knowledge);
		if (knowresult != null && knowresult.getId() > 0) {

			// knowledgeBetweenService.insertKnowledgeRCategory(knowledge,
			// categoryid);

		}
	}

	@Test
	public void testdeleteKnowledge() {
		long[] ids = { 1, 2 };
		long categoryid = 3;
		knowledgeArticleService.deleteKnowledge(ids);
//		knowledgeBetweenService.deleteKnowledgeRCategory(ids, categoryid);

	}

	@Test
	public void testupdateKnowledge() {
		KnowledgeNews knowledge = new KnowledgeNews();
		knowledge.setId(2);
		knowledge.setUid(123);
		knowledge.setStatus(1);
		knowledge.setReport_status(4);
		knowledge.setCname("修改后的不知道");
		knowledge.setContent("修改后的这是测试内容");
//		knowledgeNewsService.updateKnowledge(knowledge);

	}

	@Test
	public void testselectKnowledgenews() {
		KnowledgeNews knowledge = new KnowledgeNews();
		knowledge.setId(2);
		knowledge.setUid(123);
		knowledge.setStatus(1);
		knowledge.setReport_status(4);
		knowledge.setCname("修改后的不知道");
		knowledge.setContent("修改后的这是测试内容");

//		KnowledgeNews knowledgeNews = knowledgeNewsService.selectKnowledge(1);
//		System.out.println(knowledgeNews.getContent());

	}
}
