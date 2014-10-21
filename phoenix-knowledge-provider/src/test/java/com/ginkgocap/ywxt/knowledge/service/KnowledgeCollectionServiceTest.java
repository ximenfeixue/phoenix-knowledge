package com.ginkgocap.ywxt.knowledge.service;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeCollectionService;

/**
 * 知识测试类
 * 
 * @author caihe
 * 
 */

public class KnowledgeCollectionServiceTest extends TestBase {

	public KnowledgeCollectionServiceTest() {
		System.out.println(123456);
	}

	@Autowired
	private KnowledgeCollectionService knowledgeCollectionService;

	@Test
	public void testinsertKnowledgeR() {
	}
	
	//返回收藏夹下知识的分页信息
	@Test
	public void queryKnowledgeAll() {
	    knowledgeCollectionService.queryKnowledgeAll("1","1", 10132l, 1, 10);
	}
}
