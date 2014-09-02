package com.ginkgocap.ywxt.knowledge.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.ginkgocap.ywxt.cloud.service.InvestmentAuthenticationService;
import com.ginkgocap.ywxt.cloud.service.InvestmentCommonService;
import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.service.knowledge.KnowledgeMainService;

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

	//
	// @Test
	// public void testSaveKnowledge(){
	// Knowledge knowledge=new Knowledge();
	// Short s=2;
	// knowledge.setKnowledgeauthor("张巍11");
	// knowledge.setKnowledgetype(s);
	// knowledge.setKnowledgeid(34l);
	// InvestmentWord word=new InvestmentWord();
	// word.setCreateUserId(1l);
	// word.setCreateUserName("张巍");
	// String title = "关于测114试";
	// word.setTitle(title);
	// // 分类id
	// word.setInvestmentClassifyId(1);
	// // 等待审核
	// word.setInvestmentStatus('0');
	// word.setCreateDate(DateFunc.getDate());
	// List<InvestmentSynonym> synonymList = investmentCommonService
	// .parseSynonyms("呵呵");
	// word.setSynonyms(synonymList);
	// Long investmentId =
	// investmentAuthenticationService.getInvestmentIdByTitle(title);
	// if(investmentId != null){
	// System.err.println("'"+title+"'_已存在");
	// return;
	// }
	// Long id=knowledgeMainService.saveKnowledge(knowledge, word);
	// }

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
	@Rollback(false)
	public void testmovecategory() {
		long knowledgeid = 6478;
		String sortId = "000000001000000001";
		long categoryid = 46;
		knowledgeMainService.moveCategory(knowledgeid, categoryid, sortId);
	}

	@Test
	public void testdeletetegory() {
		long knowledgeid = 6478;
		long categoryid = 46;
		int count = knowledgeMainService.deleteKnowledgeRCategory(knowledgeid,
				categoryid);
		System.out.println(count + "是否成功");
	}

	@Test
	public void testmoveCategoryBatch() {
		long knowledgeid = 3;
		long categoryid = 52;
		long knowledgeids[] = { 3,4,5 };
		long categoryids[] = { 52,53,54 };
		knowledgeMainService.moveCategoryBatch(knowledgeid, categoryid,
				knowledgeids, categoryids);
	}

}
