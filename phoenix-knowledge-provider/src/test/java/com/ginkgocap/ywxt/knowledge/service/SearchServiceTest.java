package com.ginkgocap.ywxt.knowledge.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.ywxt.knowledge.base.TestBase;

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
		searchService.getUserTag(1l, "1");
	}
}
