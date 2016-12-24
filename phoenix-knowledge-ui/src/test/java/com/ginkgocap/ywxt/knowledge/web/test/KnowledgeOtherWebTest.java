package com.ginkgocap.ywxt.knowledge.web.test;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Created by Chen Peifeng on 2016/5/24.
 */
public class KnowledgeOtherWebTest extends BaseTestCase
{
    private long knowledgeId = 12345678L;

    @Test
    public void testFetchExternalKnowledgeUrl()
    {
        LogMethod();
        try {
        	String JsonContent = "{\"externalUrl\":\"http://china.huanqiu.com/hot/2016-09/9501178.html\",\"isCreate\":\"true\"}";
            String subUrl = "/fetchExternalKnowledgeUrl";
            JsonNode result = HttpRequestFull(HttpMethod.POST, baseOrtherUrl + subUrl, JsonContent);
            checkResponseWithData(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFetchKnowledgeByUrl()
    {
        LogMethod();
        try {
            String JsonContent = "{\"externalUrl\":\"http://china.huanqiu.com/hot/2016-09/9501178.html\",\"isCreate\":\"false\"}";
            String subUrl = "/fetchKnowledgeByUrl";
            JsonNode result = HttpRequestFull(HttpMethod.POST, baseOrtherUrl + subUrl, JsonContent);
            checkResponseWithData(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*
    @Test
    public void testFetchExternalKnowledgeUrl_Old()
    {
        LogMethod();
        try {
        	String JsonContent = "{\"externalUrl\":\"http://china.huanqiu.com/article/2016-08/9292069.html?from=bdwz\",\"isCreate\":\"false\"}";
            String subUrl = "http://test.online.gintong.com/cross/knowledge/fetchExternalKnowledgeUrl.json";
            JsonNode result = HttpRequestFull(HttpMethod.POST, subUrl, JsonContent);
            checkResponseWithData(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    @Test
    public void testShareCount()
    {
        LogMethod();
        try {
            String subUrl = "/shareCount/1/111111"; // /shareCount/{type}/{knowledgeId}
            JsonNode result = HttpRequestResult(HttpMethod.GET, baseOrtherUrl+subUrl, null);
            checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testCollectCount()
    {
        LogMethod();
        try {
            String subUrl = "/collectCount/1/111111"; // /shareCount/{type}/{knowledgeId}
            JsonNode result = HttpRequestResult(HttpMethod.GET, baseOrtherUrl+subUrl, null);
            checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testKnowledgeSync()
    {
        LogMethod();
        try {
            String subUrl = "/knowledgeSync"; // /shareCount/{type}/{knowledgeId}
            JsonNode result = HttpRequestResult(HttpMethod.GET, baseOrtherUrl+subUrl, null);
            checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    /*
    @Test
    public void testPush()
    {
        LogMethod();
        try {
        	String JsonContent = "";
            //String subUrl = "/fetchExternalKnowledgeUrl";
            JsonNode result = HttpRequestResult(HttpMethod.POST, "http://192.168.130.106:8080/dynamicNews/push", JsonContent);
            checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    /*
    public void testGetHotKnowledge()
    {
        LogMethod();
        try {
            String subUrl = "hot/"+10;
            JsonNode result = HttpRequestResult(HttpMethod.GET, baseOrtherUrl+subUrl, null);
            checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
