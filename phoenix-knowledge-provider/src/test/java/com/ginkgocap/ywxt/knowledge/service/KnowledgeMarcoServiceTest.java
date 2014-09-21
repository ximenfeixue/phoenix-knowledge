package com.ginkgocap.ywxt.knowledge.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.ywxt.cloud.service.InvestmentAuthenticationService;
import com.ginkgocap.ywxt.cloud.service.InvestmentCommonService;
import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeMacro;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeAssetService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeCategoryService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeContentService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeMacroService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeMainService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeMongoIncService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeNewsService;

/**
 * 知识测试类
 * 
 * @author zhangwei
 * @创建时间：2013-03-29 10:24:22
 */

public class KnowledgeMarcoServiceTest extends TestBase {

	public KnowledgeMarcoServiceTest() {
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

	@Autowired
	private KnowledgeMacroService KnowledgeMacroService;

	@Autowired
	private KnowledgeMongoIncService knowledgeMongoIncService;

	@Test
	public void testinsertKnowledge() {
		long id = knowledgeMongoIncService.getKnowledgeIncreaseId();
		KnowledgeMacro knowledge = new KnowledgeMacro();
		knowledge.setCname("名称");
		knowledge.setId(id);
		knowledge.setUid(111);
		knowledge.setStatus(1);
		knowledge.setReport_status(5);
		knowledge.setCname("不知道");
		knowledge.setContent("这是测试内容");
		KnowledgeMacro knowresult = KnowledgeMacroService
				.insertknowledge(knowledge);
		if (knowresult.getId() > 0) {
			// knowledgeBetweenService.insertKnowledgeRCategory(knowledge,
			// categoryid);

			System.out.println("knowledge" + knowresult.getId());
		}
	}

}
