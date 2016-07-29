package com.ginkgocap.ywxt.knowledge.web.test;

public class DirectoryWebTest extends BaseTestCase {

	private final String directoryHost = "http://192.168.130.200:8081";

	public void testCreateDirectoryRoot()
	{
		LogMethod();
		String path = "/directory/directory/createRootDirectory?rootType=8&name=Directory" + this.getNextNum();
		try {
			String jsonNode = Util.HttpRequestFullJson(Util.HttpMethod.POST, directoryHost+path, "fsdff");
			System.out.println(jsonNode);
		} catch (Exception e) {
			//TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public void testCreateDirectory()
	{
		LogMethod();
		String path = "/tags/tags/createTag?tagType=8&tagName=Tag345" + this.getNextNum();
		try {
			String jsonNode = Util.HttpRequestFullJson(Util.HttpMethod.POST, directoryHost+path, null);
			System.out.println(jsonNode);
		} catch (Exception e) {
			//TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testGetTypeList()
	{
		LogMethod();
		String path = "/directory/type/getTypeList";
		try {
			String jsonNode = Util.HttpRequestFullJson(Util.HttpMethod.GET, directoryHost+path, null);
			System.out.println(jsonNode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
