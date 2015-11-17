package com.ginkgocap.ywxt.knowledge.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNewsVO;
import com.ginkgocap.ywxt.user.model.User;

public class KnowledgeServiceTest extends TestBase {
	@Autowired
	private KnowledgeService knowledgeService;

	@Test
	public void deleteKnowledgeNew() {
		
		
		KnowledgeNewsVO  vo =new KnowledgeNewsVO();
		
		vo.setSelectedIds("{\"dales\":[{\"id\":-1,\"name\":\"全平台\"}],\"zhongles\":[],\"xiaoles\":[],\"dule\":false}");
		vo.setTitle("111");
		vo.setColumnid("1");
		vo.setContent("ss");

		User user =new User();
		user.setId(36);
		user.setName("sss");
		vo.setColumnType("1");
		vo.setAsso("{\"r\":[],\"p\":[],\"o\":[],\"k\":[]}");
	    knowledgeService.insertknowledge(vo, user);
	}
}
