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

public class KnowledgeBetweenServiceTest extends TestBase {

	public KnowledgeBetweenServiceTest() {
		System.out.println(123456);
	}

	@Autowired
	private KnowledgeContentService knowledgeContentService;

	@Autowired
	private KnowledgeCategoryService knowledgeBetweenService;

	@Test
	public void testinsertKnowledgeR() {

		KnowledgeNews knowledge = new KnowledgeNews();
//		knowledge.setKnowledgetitle("测试名");
//		knowledge.setKnowledgesource("不知道");
//		knowledge.setPictureTaskId("111");
//		knowledge.setModifytime(new Date());
//		knowledge.setCreate_user_id(123);
		long[] categoryid = { 1, 2 };

		knowledgeBetweenService.insertKnowledgeRCategory(knowledge, categoryid);
	}

	@Test
	public void testdeleteKnowledgeR() {

		long[] knowledgeids = { 1, 2 };
		long categoryid = 3;
		System.out.println(knowledgeBetweenService.deleteKnowledgeRCategory(knowledgeids,
				categoryid));
	}
}
