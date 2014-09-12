package com.ginkgocap.ywxt.knowledge.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.ywxt.cloud.service.InvestmentAuthenticationService;
import com.ginkgocap.ywxt.cloud.service.InvestmentCommonService;
import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeAsset;
import com.ginkgocap.ywxt.knowledge.service.asset.KnowledgeAssetService;
import com.ginkgocap.ywxt.knowledge.service.content.KnowledgeContentService;
import com.ginkgocap.ywxt.knowledge.service.knowledge.KnowledgeMainService;
import com.ginkgocap.ywxt.knowledge.service.knowledgecategory.KnowledgeCategoryService;
import com.ginkgocap.ywxt.knowledge.service.news.KnowledgeNewsService;

/**
 * 知识测试类
 * 
 * @author zhangwei
 * @创建时间：2013-03-29 10:24:22
 */

public class KnowledgeAssetServiceTest extends TestBase {

	public KnowledgeAssetServiceTest() {
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
	private KnowledgeContentService knowledgeContentService;

	@Autowired
	private KnowledgeAssetService knowledgeAssetService;

	@Autowired
	private KnowledgeCategoryService knowledgeBetweenService;
	
//	@Autowired
//	private KnowledgeMongoIncService knowledgeMongoIncService;
	

	@Test
	public void testinsertKnowledge() {
		KnowledgeAsset knowledge = new KnowledgeAsset();
//		knowledge.setKnowledgetitle("测试名");
//		knowledge.setKnowledgesource("不知道");
//		knowledge.setPictureTaskId("111");
//		knowledge.setModifytime(new Date());
//		knowledge.setCreate_user_id(123);
		long[] categoryid = { 1, 2 };

		KnowledgeAsset knowresult = knowledgeAssetService.insertknowledge(
				knowledge);
		if (knowresult.getId() > 0) {
//			knowledgeBetweenService.insertKnowledgeRCategory(knowledge,
//					categoryid);
			
			System.out.println("knowledge" + knowresult.getId());
		}
	}

}
