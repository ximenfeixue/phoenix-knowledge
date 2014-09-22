package com.ginkgocap.ywxt.knowledge.service;

import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.ywxt.cloud.model.InvestmentVersion;
import com.ginkgocap.ywxt.cloud.model.InvestmentWord;
import com.ginkgocap.ywxt.cloud.service.InvestmentAuthenticationService;
import com.ginkgocap.ywxt.cloud.service.InvestmentCommonService;
import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeInvestment;
import com.ginkgocap.ywxt.knowledge.service.idUtil.KnowledgeMongoIncService;
import com.ginkgocap.ywxt.knowledge.service.knowledge.KnowledgeCaseService;
import com.ginkgocap.ywxt.knowledge.service.knowledge.KnowledgeInvestmentService;
import com.ginkgocap.ywxt.knowledge.service.knowledge.KnowledgeMainService;
import com.ginkgocap.ywxt.utils.DateUtils;

/**
 * 知识测试类
 * 
 * @author zhangwei
 * @创建时间：2013-03-29 10:24:22
 */

public class ImportOldDataTest extends TestBase {

	public ImportOldDataTest() {
		System.out.println(123456);
	}

	@Autowired
	private KnowledgeMainService knowledgeMainService;
	@Autowired
	private InvestmentCommonService investmentCommonService;
	@Autowired
	private InvestmentAuthenticationService investmentAuthenticationService;

	@Autowired
	private KnowledgeMongoIncService knowledgeMongoIncService;
	
	@Autowired
	private KnowledgeInvestmentService knowledgeInvestmentService;
	@Autowired
	private KnowledgeCaseService knowledgeCaseService;

	@Test
	public void testImportInvestment() {
		List<InvestmentWord> list=investmentAuthenticationService.getAll();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			KnowledgeInvestment k=new KnowledgeInvestment();
			InvestmentWord investmentWord = (InvestmentWord) iterator.next();
			k.setOid(investmentWord.getId());
			k.setTitle(investmentWord.getTitle());
			k.setCid(investmentWord.getCreateUserId()==null?0:investmentWord.getCreateUserId());
			k.setCname(investmentWord.getCreateUserName());
			k.setUid(investmentWord.getCreateUserId()==null?0:investmentWord.getCreateUserId());
			k.setUname(investmentWord.getCreateUserName());
			if(investmentWord.getCreateDate()!=null){
				k.setCreatetime(DateUtils.StringToDate(investmentWord.getCreateDate(), "yyyy-MM-dd"));
			}
			String investmentStatus=investmentWord.getInvestmentStatus().toString();
			if(investmentStatus.equals("0")){
				k.setStatus(2);
			}else if(investmentStatus.equals("1")){
				k.setStatus(4);
			}else if(investmentStatus.equals("2")){
				k.setStatus(5);
			}else{
				k.setStatus(5);
				k.setReport_status(1);
			}
			k.setStatus(investmentWord.getInvestmentStatus());
			Long id=knowledgeMongoIncService.getKnowledgeIncreaseId();
			InvestmentVersion version=investmentAuthenticationService.getFirstVersion(investmentWord.getId());
			if(version!=null){
				k.setContent(version.getContent()==null?"":version.getContent());
				if(version.getEditDate()!=null){
					k.setModifytime(DateUtils.StringToDate(version.getEditDate(), "yyyy-MM-dd"));
				}
			}
			k.setIsh(0);
			knowledgeInvestmentService.addKnowledgeInvestment(k);
		}
	}
}
