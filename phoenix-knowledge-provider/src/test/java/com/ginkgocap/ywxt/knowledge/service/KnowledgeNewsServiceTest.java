package com.ginkgocap.ywxt.knowledge.service;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.ywxt.cloud.service.InvestmentAuthenticationService;
import com.ginkgocap.ywxt.cloud.service.InvestmentCommonService;
import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.model.ColumnKnowledge;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNews;
import com.ginkgocap.ywxt.knowledge.service.columnknowledge.ColumnKnowledgeService;
import com.ginkgocap.ywxt.knowledge.service.content.KnowledgeContentService;
import com.ginkgocap.ywxt.knowledge.service.idUtil.KnowledgeMongoIncService;
import com.ginkgocap.ywxt.knowledge.service.knowledge.KnowledgeMainService;
import com.ginkgocap.ywxt.knowledge.service.knowledgecategory.KnowledgeCategoryService;
import com.ginkgocap.ywxt.knowledge.service.news.KnowledgeNewsService;
import com.ginkgocap.ywxt.knowledge.service.userpermission.UserPermissionService;

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

	@Autowired
	private UserPermissionService userPermissionService;
	@Autowired
	private KnowledgeCategoryService knowledgeCategoryService;

	@Autowired
	private ColumnKnowledgeService columnKnowledgeService;

	@Test
	public void testinsertKnowledge() {
		long id = knowledgeMongoIncService.getKnowledgeIncreaseId();
		KnowledgeNews knowledge = new KnowledgeNews();
		knowledge.setId(id);
		knowledge.setUid(111);
		knowledge.setTitle("测试标题");
		knowledge.setCpathid("11");
		knowledge.setContent("新建测试内容");
		knowledge.setPic("D:\\pic.png");
		knowledge.setTaskid(123);
		knowledge.setStatus(4);
		knowledge.setReport_status(0);
		long[] categoryid = { 1, 2 };

		long[] receive_uid = { 1, 2 };
		int count = 0;
		KnowledgeNews knowresult = knowledgeNewsService
				.insertknowledge(knowledge);
		if (knowresult != null && knowresult.getId() > 0) {
			userPermissionService.insertUserPermission(receive_uid,
					knowresult.getId(), 123, 1, "mento", 111);
			knowledgeCategoryService.insertKnowledgeRCategory(
					knowresult.getId(), categoryid, 111, "测试标题", "author", 6,
					"share_author", new Date(), "标签", "know_desc", 11,
					"D:\\pic.png");

			ColumnKnowledge columnKnowledge = new ColumnKnowledge();
			columnKnowledgeService.insertColumnKnowledge(columnKnowledge);

			System.out.println("ID : " + knowresult.getId());
		}
	}

	@Test
	public void testdeleteKnowledge() {
		long[] ids = { 5, 6 };
		// long categoryid = 3;
		knowledgeNewsService.deleteKnowledge(ids);
		// knowledgeBetweenService.deleteKnowledgeRCategory(ids, categoryid);

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
	@Test
	public void selectByParam() {
	    knowledgeNewsService.selectByParam(1l, 1, 1l, null, 2, 10);
	    
	}
}
