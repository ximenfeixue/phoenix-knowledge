package com.ginkgocap.ywxt.knowledge.service;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.ginkgocap.ywxt.cloud.model.InvestmentSynonym;
import com.ginkgocap.ywxt.cloud.model.InvestmentWord;
import com.ginkgocap.ywxt.cloud.service.InvestmentAuthenticationService;
import com.ginkgocap.ywxt.cloud.service.InvestmentCommonService;
import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeContent;
import com.ginkgocap.ywxt.knowledge.service.knowledge.KnowledgeContentService;
import com.ginkgocap.ywxt.knowledge.service.knowledge.KnowledgeMainService;
import com.ginkgocap.ywxt.util.DateFunc;

/**
 * 知识测试类
 * 
 * @author zhangwei
 * @创建时间：2013-03-29 10:24:22
 */

public class KnowledgeMainServiceTest extends TestBase {

	public KnowledgeMainServiceTest() {
		System.out.println(123456);
	}

	@Autowired
	private KnowledgeMainService knowledgeMainService;
	@Autowired
	private InvestmentCommonService investmentCommonService;
	@Autowired
	private InvestmentAuthenticationService investmentAuthenticationService;

	@Autowired
	private KnowledgeContentService knowledgeContentService;

	@Test
	public void testcheckName() {
		int knowledgetype = 2;
		String knowledgetitle = "测试标题";
		int count = knowledgeMainService
				.checkIndustryNameRepeat(knowledgetitle);
		if (count != 0) {
			System.out.println("名称可以使用");
		} else {
			System.out.println("名称已存在");
		}
	}

	@Test
	public void testSaveKnowledge() {
		Knowledge knowledge = new Knowledge();
		Short s = 2;
		InvestmentWord word = new InvestmentWord();
		word.setCreateUserId(1l);
		word.setCreateUserName("张巍");
		String title = "关于测15414试";
		word.setTitle(title);
		// 分类id
		word.setInvestmentClassifyId(1);
		// 等待审核
		word.setInvestmentStatus('0');
		word.setCreateDate(DateFunc.getDate());
		List<InvestmentSynonym> synonymList = investmentCommonService
				.parseSynonyms("呵呵");
		word.setSynonyms(synonymList);
		Long investmentId = investmentAuthenticationService
				.getInvestmentIdByTitle(title);
		if (investmentId != null) {
			System.err.println("'" + title + "'_已存在");
			return;
		}
	}

	@Test
	public void testmoveCategoryBatch() {
		long categoryid = 52;
		long knowledgeids[] = { 2, 3 };
		long categoryids[] = { 52, 53, 54 };
		knowledgeMainService.moveCategoryBatch(categoryid, knowledgeids,
				categoryids);
	}

	@Test
	public void testinsertKnowledge() {
		Knowledge knowledge = new Knowledge();
		knowledge.setModifytime(new Date());

		long[] categoryid = { 52, 53, 54 };

		Knowledge knowresult = knowledgeMainService.insertknowledge(knowledge,
				categoryid);
		if (knowresult.getId() > 0) {
			KnowledgeContent knowledgeContent = new KnowledgeContent();
			knowledgeContent.setKnowledgeid(knowresult.getId());
			knowledgeContent.setContent("这是测试内容");
			KnowledgeContent knowledgeContentResult = knowledgeContentService
					.insert(knowledgeContent);
			System.out.println("content" + knowledgeContentResult.getId());
			System.out.println("content : "
					+ knowledgeContentResult.getContent());
			if (knowledgeContentResult.getId() > 0) {
				// knowledgeMainService.insertKnowledgeRCategory(knowledge,
				// categoryid);
			}
		}
		System.out.println("knowledge" + knowresult.getId());
	}

	@Test
	public void testdeleteBatch() {
		long ids[] = { 5575, 5586 };
		long categoryid = 42;
		int count = knowledgeMainService.deleteKnowledge(ids, categoryid);
		if (count > 0) {
			System.out.println("删除成功");
		} else {
			System.out.println("删除失败");
		}
	}

	@Test
	public void testupdateknowledge() {
		Knowledge knowledge = new Knowledge();

		knowledge.setId(5576);
		knowledge.setModifytime(new Date());
		long[] categoryids = {};
		long categoryid = 42;
		int count = knowledgeMainService.updateKnowledge(knowledge, categoryid,
				categoryids);
		if (count > 0) {
			System.out.println("编辑成功");
		} else {
			System.out.println("编辑失败");
		}
	}

	@Test
	public void testdeleteKnowledgeRCategory() {
		long knowledgeid = 5586;
		long categoryid = 54;
		int count = knowledgeMainService.deleteKnowledgeRCategory(knowledgeid,
				categoryid);
		if (count > 0) {
			System.out.println("删除成功");
		} else {
			System.out.println("删除失败");
		}
	}
}
