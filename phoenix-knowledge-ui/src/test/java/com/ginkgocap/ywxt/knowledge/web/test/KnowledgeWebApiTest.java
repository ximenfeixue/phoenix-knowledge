package com.ginkgocap.ywxt.knowledge.web.test;

import com.fasterxml.jackson.databind.JsonNode;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * Created by Admin on 2016/3/31.
 */
public class KnowledgeWebApiTest extends TestCase {

    public static final String baseUrl = "http://localhost:8080/phoenix-knowledge/knowledge";


    protected static boolean skipTestCase = true;

    @Override
    protected void runTest() throws Throwable
    {
        if (!skipTestCase) {
            super.runTest();
        }
    }

    @Test
    public void testCreateKnowledge()
    {
        String knowledgeJson = TestData.createKnowlegeJson();
        try {
            JsonNode result = Util.HttpRequestResult(Util.HttpMethod.POST, baseUrl, knowledgeJson);
            Util.checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testUpdateKnowledge()
    {
        String knowledgeJson = TestData.updateKnowlegeJson();
        try {
            JsonNode result = Util.HttpRequestResult(Util.HttpMethod.PUT, baseUrl, knowledgeJson);
            Util.checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testDeleteKnowledge()
    {
        try {
                            ///{id}/{columnId}
            String subUrl = "123/6";
            JsonNode result = Util.HttpRequestResult(Util.HttpMethod.DELETE, baseUrl+subUrl, null);
            Util.checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testKnowledgeDetail()
    {
        try {
                            ///{id}/{columnId}
            String subUrl = "123/2";
            JsonNode result = Util.HttpRequestResult(Util.HttpMethod.GET, baseUrl+subUrl, null);
            Util.checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testAllKnowledgeList()
    {
        try {
                            ////all/{start}/{size}
            String subUrl = "all/1/10";
            JsonNode result = Util.HttpRequestResult(Util.HttpMethod.GET, baseUrl+subUrl, null);
            Util.checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testKnowledgeListByColumnId()
    {
        try {
                            ///all/{columnId}/{start}/{size}
            String subUrl = "all/2/10/25";
            JsonNode result = Util.HttpRequestResult(Util.HttpMethod.GET, baseUrl+subUrl, null);
            Util.checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testKnowledgeListByColumnIdByUserId()
    {
        try {
                            ///user/{start}/{size}
            String subUrl = "user/12/24";
            JsonNode result = Util.HttpRequestResult(Util.HttpMethod.GET, baseUrl+subUrl, null);
            Util.checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testKnowledgeListByColumnIdByUserIdAndColumnId()
    {
        try {
                           //user/{columnId}/{start}/{size}
            String subUrl = "user/2/1/5/10";
            JsonNode result = Util.HttpRequestResult(Util.HttpMethod.GET, baseUrl+subUrl, null);
            Util.checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
