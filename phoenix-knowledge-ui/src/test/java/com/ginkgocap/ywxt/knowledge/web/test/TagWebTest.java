package com.ginkgocap.ywxt.knowledge.web.test;


import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeUtil;

public class TagWebTest extends BaseTestCase {

	private final String tagHost = openHostUrl;
	
	public void testCreateTag()
	{
		LogMethod();
		String path = "/tags/tags/createTag?tagType=8&tagName=Tag345" + this.getNextNum();
		try {
			String jsonNode = HttpRequestFullJson(HttpMethod.POST, tagHost+path, "{\"pid\":\"0\"}");
		} catch (Exception e) {
			//TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testDeleteTag()
	{
		LogMethod();
		String path = "/tags/tags/createTag?tagType=8&tagName=Tag345" + this.getNextNum();
		try {
			String jsonNode = HttpRequestFullJson(HttpMethod.POST, tagHost+path, "{\"pid\":\"0\"}");
		} catch (Exception e) {
			//TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testDeleteTagSource()
	{
		LogMethod();
		long userId = 441813L;
		Long [] ids = new Long[] {};
		for (long sourceId : ids) {
			String path = String.format("/tags/source/deleteTagSource?userId=%d&id=%d",userId, sourceId);
			try {
				String jsonNode = HttpRequestFullJson(HttpMethod.DELETE, tagHost+path, "{\"pid\":\"0\"}");
			} catch (Exception e) {
				//TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void testGetTagList()
	{
		LogMethod();
		String path = "/tags/tags/getTagList?tagType=8";
		try {
			String jsonNode = HttpRequestFullJson(HttpMethod.GET, tagHost+path, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void testGetSourceListByTagAndType()
	{
		LogMethod();
		final long tagId = 4009112635703448L;
		final String path = "/tags/source/getSourceListByTagAndType?userId=7&tagId=" + tagId + "&sourceType=8&start=0&count=10";
		try {
			String jsonNode = HttpRequestFullJson(HttpMethod.GET, tagHost+path, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void testTest()
	{
		String ipList = KnowledgeUtil.getLocalIp();
		String[] ips = ipList.split("\n");
		for (String IP : ips) {
			System.out.println("Host IP1: " + IP);
		}
		System.out.println("Host IP2: " + KnowledgeUtil.getLocalAddress());
	}
}
