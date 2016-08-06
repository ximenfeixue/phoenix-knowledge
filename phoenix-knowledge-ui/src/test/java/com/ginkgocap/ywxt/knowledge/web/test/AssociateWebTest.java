package com.ginkgocap.ywxt.knowledge.web.test;

public class AssociateWebTest extends BaseTestCase {

	private final String associateHost = openHostUrl;

	public void testCreateAssociate()
	{
		LogMethod();
		String path = "/associate/associate/createAssociate?" + this.getNextNum();
		try {
			String jsonNode = HttpRequestFullJson(HttpMethod.POST, associateHost+path, "fsdff");
			System.out.println(jsonNode);
		} catch (Exception e) {
			//TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public void testDeleteAssociate()
	{
		LogMethod();
		String path = "/associate/associate/deleteAssociate";
		try {
			String jsonNode = HttpRequestFullJson(HttpMethod.POST, associateHost+path, null);
			System.out.println(jsonNode);
		} catch (Exception e) {
			//TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testGetAssociateDetail()
	{
		LogMethod();
		String path = "/associate/associate/getAssociateDetail";
		try {
			String jsonNode = HttpRequestFullJson(HttpMethod.GET, associateHost+path, null);
			System.out.println(jsonNode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testGetSourceAssociates()
	{
		LogMethod();
		String path = "/associate/associate/getSourceAssociates";
		try {
			String jsonNode = HttpRequestFullJson(HttpMethod.GET, associateHost+path, null);
			System.out.println(jsonNode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
