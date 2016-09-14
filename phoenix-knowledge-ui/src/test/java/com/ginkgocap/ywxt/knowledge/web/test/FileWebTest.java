package com.ginkgocap.ywxt.knowledge.web.test;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

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

	@Test
	public void test()
	{
		String time = "2007-2-27";
		long timestamp = KnowledgeUtil.parserTimeToLong(time);
		System.out.println("Converted time: " + timestamp);
	}

	private String getTaskId()
	{
		//final String subUrl = "/file/getTaskId";
		return String.valueOf(System.currentTimeMillis());
	}
}
