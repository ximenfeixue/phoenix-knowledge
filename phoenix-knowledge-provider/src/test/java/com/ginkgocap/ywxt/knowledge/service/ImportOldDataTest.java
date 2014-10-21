package com.ginkgocap.ywxt.knowledge.service;

import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.ywxt.cloud.model.Case;
import com.ginkgocap.ywxt.cloud.model.InvestmentVersion;
import com.ginkgocap.ywxt.cloud.model.InvestmentWord;
import com.ginkgocap.ywxt.cloud.service.CaseService;
import com.ginkgocap.ywxt.cloud.service.InvestmentAuthenticationService;
import com.ginkgocap.ywxt.cloud.service.InvestmentCommonService;
import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeCase;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeInvestment;
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
	private CaseService caseService;
	
	@Autowired
	private KnowledgeMongoIncService knowledgeMongoIncService;
	
	@Autowired
	private KnowledgeInvestmentService knowledgeInvestmentService;
	@Autowired
	private KnowledgeCaseService knowledgeCaseService;
	
	@Test
	public void testIncId() {
		Long id=knowledgeMongoIncService.getKnowledgeIncreaseId();
		System.out.println(id);
	}
	
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
				k.setCreatetime(DateUtils.StringToDate(investmentWord.getCreateDate(), "yyyy-MM-dd HH:mm:ss"));
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
			InvestmentVersion version=investmentAuthenticationService.getFirstVersion(investmentWord.getId());
			if(version!=null){
				k.setContent(version.getContent()==null?"":version.getContent());
				k.setDesc(version.getCardExplain()==null?"":version.getCardExplain());
				if(version.getEditDate()!=null){
					k.setModifytime(DateUtils.StringToDate(version.getEditDate(), "yyyy-MM-dd HH:mm:ss"));
				}
			}
			k.setIsh(0);
			k.setColumnid("2");
			knowledgeInvestmentService.addKnowledgeInvestment(k);
		}
	}
	
	@Test
	public void testImportCase() {
		List<Case> caseList=caseService.getAll();
		for (Iterator iterator = caseList.iterator(); iterator.hasNext();) {
			KnowledgeCase k=new KnowledgeCase();
			Case case1 = (Case) iterator.next();
			k.setOid(case1.getId());
			k.setTitle(case1.getTitle());
			k.setCid(case1.getUser_id()==null?0:case1.getUser_id());
			k.setCname(case1.getUser_name());
			k.setUid(case1.getUser_id()==null?0:case1.getUser_id());
			k.setUname(case1.getUser_name());
			if(case1.getCreate_time()!=null){
				k.setCreatetime(DateUtils.StringToDate(case1.getCreate_time(), "yyyy-MM-dd HH:mm:ss"));
			}
			String investmentStatus=case1.getStatus().toString();
			if(investmentStatus.equals("0")){
				k.setStatus(4);
			}else if(investmentStatus.equals("1")){
				k.setStatus(4);
			}else if(investmentStatus.equals("2")){
				k.setStatus(5);
			}else{
				k.setStatus(5);
				k.setReport_status(1);
			}
			k.setDesc(case1.getSummary());
			k.setColumnid("4");
			knowledgeCaseService.addKnowledgeCase(k);
		}
	}
	
}
