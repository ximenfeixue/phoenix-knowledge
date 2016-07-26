package com.ginkgocap.ywxt.knowledge.web.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.fasterxml.jackson.databind.JsonNode;
import com.ginkgocap.ywxt.knowledge.model.common.DataCollection;
import com.ginkgocap.ywxt.knowledge.model.common.KnowledgeDetail;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeReport;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeUtil;
import com.ginkgocap.ywxt.knowledge.model.common.ResItem;
import com.ginkgocap.ywxt.knowledge.utils.TestData;

public class TagWebTest extends BaseTestCase {

	private final String tagHost = "http://localhost:8088";
	
	public void testCreateTag()
	{
		LogMethod();
		String path = "/tags/tags/createTag?tagType=8&tagName=Tag" + this.getNextNum();

		try {
			String jsonNode = Util.HttpRequestFullJson(Util.HttpMethod.POST, tagHost+path, null);
			System.out.println(jsonNode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
