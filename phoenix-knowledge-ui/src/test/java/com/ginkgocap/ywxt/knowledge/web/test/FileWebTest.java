package com.ginkgocap.ywxt.knowledge.web.test;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeUtil;
import com.ginkgocap.ywxt.knowledge.utils.HttpClientHelper;
import com.ginkgocap.ywxt.knowledge.utils.PackingDataUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.*;

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
	public void testRecommendFriend()
	{
		LogMethod();
		try {
			final String subUrl = "/friend/recommendFriend.json";
			String jsonNode = HttpRequestFullJson(HttpMethod.GET, hostUrl + subUrl, null);
			//System.out.println(jsonNode);
		} catch (Exception e) {
			//TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testMoveFile()
	{
		LogMethod();
		try {
			final String subUrl = "/friend/recommendFriend.json";
			String jsonNode = HttpRequestFullJson(HttpMethod.POST, "http://192.168.130.124:8080/phoenix-file-ui-2.1.0/fileManager/moveFile.json", "{\"categoryId\":1213,\"toCategoryId\":1221312,\"fileId\":\"dqwew\"}");
			System.out.println(jsonNode);
		} catch (Exception e) {
			//TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Test
	public void testCreatePermission()
	{
		LogMethod();
		try {
			final String subUrl = "/friend/recommendFriend.json";
			final String content = "{\"resId\":3248,\"resType\":11,\"resAccRule\":\"\",\"resOwnerId\":16405483,\"publicFlag\":0,\"connectFlag\":0,\"shareFlag\":1,\"perTime\":\"\",\"appId\":\"\"}";
			String jsonNode = HttpRequestFullJson(HttpMethod.POST, "http://192.168.130.126:8088/permission/create", content);
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
