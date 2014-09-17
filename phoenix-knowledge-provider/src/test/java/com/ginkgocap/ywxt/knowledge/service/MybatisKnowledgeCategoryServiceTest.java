package com.ginkgocap.ywxt.knowledge.service;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.entity.UserCategoryTest;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNews;
import com.ginkgocap.ywxt.knowledge.service.content.KnowledgeContentService;
import com.ginkgocap.ywxt.knowledge.service.knowledgecategory.KnowledgeCategoryService;

/**
 * 知识测试类
 * 
 * @author caihe
 * 
 */

public class MybatisKnowledgeCategoryServiceTest extends TestBase {

	public MybatisKnowledgeCategoryServiceTest() {
		System.out.println(123456);
	}

	@Autowired
	private KnowledgeContentService knowledgeContentService;

	@Autowired
	private KnowledgeCategoryService knowledgeCategoryService;

	@Test
	public void testinsertKnowledgeR() {

		KnowledgeNews knowledge = new KnowledgeNews();
		// knowledge.setKnowledgetitle("测试名");
		// knowledge.setKnowledgesource("不知道");
		// knowledge.setPictureTaskId("111");
		// knowledge.setModifytime(new Date());
		// knowledge.setCreate_user_id(123);
		long[] categoryid = { 1, 2 };
		long knowledgeid = 1;
		long userid = 111;
		String title = "测试";
		String author = "浊试作者";
		int path = 1;
		String share_author = "我自己";
		Date createtime = new Date();
		String tag = "标签";
		String know_desc = "描述";
		long column_id = 111;
		String pic_path = "D:\\pic.gif";

		knowledgeCategoryService.insertKnowledgeRCategory(knowledgeid,
				categoryid, userid, title, author, path, share_author,
				createtime, tag, know_desc, column_id, pic_path);
	}

	@Test
	public void testdeleteKnowledgeR() {

		long[] knowledgeids = { 1, 2 };
		long categoryid = 3;

		UserCategoryTest userCategoryTest = new UserCategoryTest();
		userCategoryTest.setCreatetime(new Date());
		userCategoryTest.setCategoryname("不知道");
		userCategoryTest.setParentId((long) 111);
//		System.out.println(knowledgeCategoryService.insert(userCategoryTest));
	}
}
