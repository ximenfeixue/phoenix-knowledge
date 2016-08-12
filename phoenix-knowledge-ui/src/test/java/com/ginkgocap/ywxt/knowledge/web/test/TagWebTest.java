package com.ginkgocap.ywxt.knowledge.web.test;


public class TagWebTest extends BaseTestCase {

	private final String tagHost = openHostUrl;
	
	public void testCreateTag()
	{
		LogMethod();
		String path = "/tags/tags/createTag?tagType=8&tagName=Tag345" + this.getNextNum();
		try {
			String jsonNode = HttpRequestFullJson(HttpMethod.POST, tagHost+path, "{\"pid\":\"0\"}");
			System.out.println(jsonNode);
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
			System.out.println(jsonNode);
		} catch (Exception e) {
			//TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testDeleteTagSource()
	{
		LogMethod();
		long userId = 000L;
		long sourceId = 0000L;
		String path = String.format("/tags/source/deleteTagSource?userId=%d&id=%d",userId, sourceId);
		try {
			String jsonNode = HttpRequestFullJson(HttpMethod.DELETE, tagHost+path, "{\"pid\":\"0\"}");
			System.out.println(jsonNode);
		} catch (Exception e) {
			//TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testGetTagList()
	{
		LogMethod();
		String path = "/tags/tags/getTagList?tagType=8";
		try {
			String jsonNode = HttpRequestFullJson(HttpMethod.GET, tagHost+path, null);
			System.out.println(jsonNode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
