package com.ginkgocap.ywxt.knowledge.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNewsVO;
import com.ginkgocap.ywxt.user.model.User;

public class SearchServiceTest extends TestBase {
	@Autowired
	private SearchService searchService;

	@Test
	public void testSearchUserTags() {
		searchService.getUserTag(null, "1");
	}
	@Test
	public void testSearch() {
		searchService.getUserTag(1l, "1");
	}
	@Test
	public void testSearchTags() {
		searchService.getUserTag(1l, "1");
	}
	@Test
	public void testSearchshare() {
		KnowledgeNewsVO vo = new KnowledgeNewsVO();
		vo.setkId(111l);
		vo.setColumnType("1");
		vo.setColumnid("2222");
		vo.setTitle("title");
		vo.setPic("/web/cover");
		vo.setDesc("desc");
		vo.setContent("content");
		vo.setTags("ddd,22");
		vo.setEssence("1");
		
		User user = new User();
		user.setId(111);
		searchService.shareToJinTN(user.getId(), vo);
	}
}

