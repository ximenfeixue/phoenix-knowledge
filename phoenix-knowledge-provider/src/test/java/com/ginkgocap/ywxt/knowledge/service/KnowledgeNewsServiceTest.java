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
import com.ginkgocap.ywxt.knowledge.service.idUtil.KnowledgeMongoIncService;
import com.ginkgocap.ywxt.knowledge.service.knowledge.KnowledgeMainService;
import com.ginkgocap.ywxt.knowledge.service.knowledgecategory.KnowledgeCategoryService;
import com.ginkgocap.ywxt.knowledge.service.news.KnowledgeNewsService;

/**
 * 知识测试类
 * 
 * @author zhangwei
 * @创建时间：2013-03-29 10:24:22
 */

public class KnowledgeNewsServiceTest extends TestBase {

	public KnowledgeNewsServiceTest() {
		System.out.println(123456);
	}

	@Autowired
	private KnowledgeMainService knowledgeMainService;
	@Autowired
	private KnowledgeNewsService knowledgeNewsService;
	@Autowired
	private InvestmentCommonService investmentCommonService;
	@Autowired
	private InvestmentAuthenticationService investmentAuthenticationService;

	@Autowired
	private KnowledgeCategoryService knowledgeBetweenService;

	@Autowired
	private KnowledgeContentService knowledgeContentService;

	@Autowired
	private KnowledgeMongoIncService knowledgeMongoIncService;

	@Test
	public void testinsertKnowledge() {
		long id = knowledgeMongoIncService.getKnowledgeIncreaseId();
		KnowledgeNews knowledge = new KnowledgeNews();
		knowledge.setId(id);
		knowledge.setUid(111);
		knowledge.setStatus(1);
		knowledge.setReport_status(5);
		knowledge.setTitle("测试标题");
		knowledge.setCname("不知道");
		knowledge.setContent("这是测试内容");
		long[] categoryid = { 1, 2 };

		KnowledgeNews knowresult = knowledgeNewsService
				.insertknowledge(knowledge);
		if (knowresult != null && knowresult.getId() > 0) {

			// knowledgeBetweenService.insertKnowledgeRCategory(knowledge,
			// categoryid);
			System.out.println("ID : " + knowresult.getId());
		}
	}

	@Test
	public void testdeleteKnowledge() {
		long[] ids = { 5, 6 };
//		long categoryid = 3;
		knowledgeNewsService.deleteKnowledge(ids);
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
		knowledgeNewsService.updateKnowledge(knowledge);

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

		KnowledgeNews knowledgeNews = knowledgeNewsService.selectKnowledge(1);
		System.out.println(knowledgeNews.getContent());

	}
}
