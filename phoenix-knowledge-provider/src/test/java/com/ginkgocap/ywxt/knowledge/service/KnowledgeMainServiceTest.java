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
import com.ginkgocap.ywxt.knowledge.service.content.KnowledgeContentService;
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
		String knowledgetitle = "不知";
		int count = knowledgeMainService.checkNameRepeat(knowledgetype,
				knowledgetitle);
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
		knowledge.setKnowledgeauthor("张巍11");
		knowledge.setKnowledgetype(s);
		knowledge.setKnowledgeid(34l);
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
	public void testdeletetegory() {
		long knowledgeid[] = { 1, 2, 3 };
		long categoryid = 3;
		int count = knowledgeMainService.deleteKnowledgeRCategory(knowledgeid,
				categoryid);
		System.out.println(count + "是否成功");
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

		knowledge.setUserId(111012);
		knowledge.setKnowledgetype(1);
		knowledge.setKnowledgeauthor("测试名称");
		knowledge.setKnowledgetitle("测试名");
		knowledge.setKnowledgesource("不知道");
		knowledge.setKnowledgedesc("测试");
		knowledge.setClicknum(123);
		knowledge.setEssence("1");
		knowledge.setPictureTaskId("111");
		knowledge.setVisible("0");
		knowledge.setPubdate(new Date());
		knowledge.setModifytime(new Date());
		knowledge.setClicknum(20);
		knowledge.setAttatchmentTaskId("123");

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
				knowledgeMainService.insertKnowledgeRCategory(knowledge,
						categoryid);
			}
		}
		System.out.println("knowledge" + knowresult.getId());
	}

	@Test
	public void testdeleteBatch() {
		String ids[] = { "5574" };
		long category_id = 0;
		int count = knowledgeMainService.deleteKnowledge(ids, category_id);
		if (count > 0) {
			System.out.println("删除成功");
		} else {
			System.out.println("删除失败");
		}
	}

	@Test
	public void testupdateknowledge() {
		Knowledge knowledge = new Knowledge();

		knowledge.setId(5571);
		knowledge.setUserId(111012);
		knowledge.setKnowledgetype(1);
		knowledge.setKnowledgeauthor("修改后的测试");
		knowledge.setKnowledgetitle("修改后测试名");
		knowledge.setKnowledgesource("修改后不知道");
		knowledge.setKnowledgedesc("修改后测试");
		knowledge.setClicknum(123);
		knowledge.setEssence("1");
		knowledge.setPictureTaskId("111");
		knowledge.setVisible("0");
		knowledge.setPubdate(new Date());
		knowledge.setModifytime(new Date());
		knowledge.setClicknum(20);
		knowledge.setAttatchmentTaskId("123");
		int count = knowledgeMainService.updateKnowledge(knowledge);
		if (count > 0) {
			System.out.println("编辑成功");
		} else {
			System.out.println("编辑失败");
		}
	}
}
