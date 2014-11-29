package com.ginkgocap.ywxt.knowledge.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.ywxt.knowledge.base.TestBase;

public class KnowledgeServiceTest extends TestBase {
	@Autowired
	private KnowledgeService knowledgeService;

	@Test
	public void deleteKnowledgeNew() {
	    knowledgeService.deleteKnowledgeNew("48499", 293, 10132);
	}
}
