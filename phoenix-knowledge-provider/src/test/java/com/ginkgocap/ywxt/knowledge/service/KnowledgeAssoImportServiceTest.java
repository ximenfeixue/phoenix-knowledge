package com.ginkgocap.ywxt.knowledge.service;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.thread.NoticeThreadPool;

/**
 * 知识测试类
 * 
 * @author zhangwei
 * @创建时间：2013-03-29 10:24:22
 */

public class KnowledgeAssoImportServiceTest extends TestBase {

	@Autowired
	private KnowledgeAssoImportService knowledgeAssoImportService;

	@Autowired
	private NoticeThreadPool noticeThreadPool;
	@Test
	public void testcheckName() {

//		NoticeThreadPool nt = new NoticeThreadPool();
		
//		knowledgeAssoImportService.importasso();
//		nt.start();
//		nt.aaa();
		noticeThreadPool.importData();
	}

}
