package com.ginkgocap.ywxt.knowledge.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeContent;
import com.ginkgocap.ywxt.knowledge.service.content.KnowledgeContentService;

/**
 * <p>
 * 知识目录操作测试接口
 * </p>
 * <p>
 * 于2014-8-19 由 bianzhiwei 创建
 * </p>
 * 
 * @author <p>
 *         当前负责人 bianzhiwei
 *         </p>
 * @since <p>
 *        1.2.1-SNAPSHO
 *        </p>
 */

public class KnowledgeContentTest extends TestBase {
	@Autowired
	private KnowledgeContentService knowledgeContentService;

	@Test
	@Rollback(false)
	public void testKnowledgeContentInsert() throws Exception {

		KnowledgeContent knowledgeContent = new KnowledgeContent();

		knowledgeContent.setKnowledgeid(7894);
		knowledgeContent.setContent("测试");
		knowledgeContent.setPage_head("测试");
		knowledgeContent.setPage_count(5);
		KnowledgeContent kc = knowledgeContentService.insert(knowledgeContent);

		System.out.println(kc.getContent());
	}

}
