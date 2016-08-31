package com.ginkgocap.ywxt.knowledge.web.test;

import org.junit.Test;

public class FileWebTest extends BaseTestCase {

	private final String fileHost = openHostUrl;

	@Test
	public void testUploadFile()
	{
		LogMethod();
		try {
			final String subUrl = "/file/upload?" + "taskId=" + getTaskId();
			final String usrHome = System.getProperty("user.home");
			final String fileSeparator = System.getProperty("file.separator");
			final String filePath = usrHome + fileSeparator + "111111.jpg";
			String jsonNode = HttpUpLoad(fileHost+subUrl, filePath);
			System.out.println(jsonNode);
		} catch (Exception e) {
			//TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String getTaskId()
	{
		//final String subUrl = "/file/getTaskId";
		return String.valueOf(System.currentTimeMillis());
	}
}
