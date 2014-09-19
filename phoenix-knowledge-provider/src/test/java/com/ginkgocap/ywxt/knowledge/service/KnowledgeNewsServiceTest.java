package com.ginkgocap.ywxt.knowledge.service;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.ywxt.cloud.service.InvestmentAuthenticationService;
import com.ginkgocap.ywxt.cloud.service.InvestmentCommonService;
import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.entity.ColumnKnowledge;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNews;
import com.ginkgocap.ywxt.knowledge.service.knowledge.ColumnKnowledgeService;
import com.ginkgocap.ywxt.knowledge.service.knowledge.KnowledgeCategoryService;
import com.ginkgocap.ywxt.knowledge.service.knowledge.KnowledgeContentService;
import com.ginkgocap.ywxt.knowledge.service.knowledge.KnowledgeMainService;
import com.ginkgocap.ywxt.knowledge.service.knowledge.KnowledgeMongoIncService;
import com.ginkgocap.ywxt.knowledge.service.knowledge.KnowledgeNewsService;
import com.ginkgocap.ywxt.knowledge.service.knowledge.UserPermissionService;
import com.ginkgocap.ywxt.knowledge.util.Constants;

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
		knowledge.setTag("不知道,不知道");
		long[] categoryid = { 1, 2 };

		long[] receive_uid = { 1, 2 };
		int count = 0;
		KnowledgeNews knowresult = knowledgeNewsService
				.insertknowledge(knowledge);
		if (knowresult != null && knowresult.getId() > 0) {
			count = userPermissionService.insertUserPermission(receive_uid,
					knowresult.getId(), 123, 1, "mento", (short) 111);
			if (count > 0) {
				count = knowledgeCategoryService.insertKnowledgeRCategory(
						knowresult.getId(), categoryid, 111, "测试标题", "author",
						6, "share_author", new Date(), "标签", "know_desc", 11,
						"D:\\pic.png");
				if (count > 0) {
					ColumnKnowledge columnKnowledge = new ColumnKnowledge();
					columnKnowledge.setColumnId((long) 111);
					columnKnowledge.setKnowledgeId((long) 21);
//					columnKnowledge
//							.setType((short) Constants.KnowledgeType.NEWS.v());
					columnKnowledge.setUserId(knowresult.getId());
					count = columnKnowledgeService
							.insertColumnKnowledge(columnKnowledge);
					if (count > 0) {

						System.out.println("成功");
					} else {
						System.out.println("栏目失败");
					}
				} else {
					System.out.println("目录失败");
				}
			} else {
				System.out.println("权限失败");
			}

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
