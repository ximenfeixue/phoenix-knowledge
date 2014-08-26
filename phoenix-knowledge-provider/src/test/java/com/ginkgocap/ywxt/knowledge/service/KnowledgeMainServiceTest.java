package com.ginkgocap.ywxt.knowledge.service;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.ywxt.cloud.model.InvestmentSynonym;
import com.ginkgocap.ywxt.cloud.model.InvestmentWord;
import com.ginkgocap.ywxt.cloud.service.InvestmentAuthenticationService;
import com.ginkgocap.ywxt.cloud.service.InvestmentCommonService;
import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.service.knowledge.KnowledgeMainService;
import com.ginkgocap.ywxt.util.DateFunc;

/**
 * 知识测试类
 * @author zhangwei
 * @创建时间：2013-03-29 10:24:22
 */

public class KnowledgeMainServiceTest extends TestBase{
    
	public KnowledgeMainServiceTest() {
		System.out.println(123456);
	}
	
	@Autowired
	private KnowledgeMainService knowledgeMainService;
	@Autowired
	private InvestmentCommonService investmentCommonService;
	@Autowired
	private InvestmentAuthenticationService investmentAuthenticationService;

    @Test
    public void testSaveKnowledge(){
    	Knowledge knowledge=new Knowledge();
    	Short s=2;
    	knowledge.setKnowledgeauthor("张巍11");
    	knowledge.setKnowledgetype(s);
    	knowledge.setKnowledgeid(34l);
    	InvestmentWord word=new InvestmentWord();
    	word.setCreateUserId(1l);
		word.setCreateUserName("张巍");
		String title = "关于测114试";
		word.setTitle(title);
		// 分类id
		word.setInvestmentClassifyId(1);
		// 等待审核
		word.setInvestmentStatus('0');
		word.setCreateDate(DateFunc.getDate());
		List<InvestmentSynonym> synonymList = investmentCommonService
				.parseSynonyms("呵呵");
		word.setSynonyms(synonymList);
		Long investmentId = investmentAuthenticationService.getInvestmentIdByTitle(title);
		if(investmentId != null){
			System.err.println("'"+title+"'_已存在");
			return;
		}
		Long id=knowledgeMainService.saveKnowledge(knowledge, word);
    }
 }
