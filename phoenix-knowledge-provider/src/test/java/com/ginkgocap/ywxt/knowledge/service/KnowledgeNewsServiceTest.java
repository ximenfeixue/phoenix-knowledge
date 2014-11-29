package com.ginkgocap.ywxt.knowledge.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.ywxt.cloud.service.InvestmentAuthenticationService;
import com.ginkgocap.ywxt.cloud.service.InvestmentCommonService;
import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNews;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNewsVO;
import com.ginkgocap.ywxt.user.model.User;

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
		KnowledgeNewsVO vo = new KnowledgeNewsVO();
		vo.setCatalogueIds("1,2,3,4");
		vo.setColumnid(2l);
		vo.setTitle("test测试");
		vo.setColumnType(""+7);
		vo.setContent("test正文");
		vo.setEssence("1");
		vo.setPic("/webserver/upload");
		vo.setSelectedIds("2:1,2,3&3:4,5,6&4:7,8,9");
		vo.setShareMessage("这篇文章真不错");
		vo.setSubmittype(1);
		vo.setTags("标签1，标签2，标签3");
		vo.setTaskId("2222");
		
		User user = new User();
		user.setId(1l);
		user.setName("大兄弟");
		knowledgeNewsService.insertknowledge(vo, user);
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
//		knowledgeNewsService.updateKnowledge(knowledge);

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
