package com.ginkgocap.ywxt.knowledge.web.test;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Created by Chen Peifeng on 2016/5/24.
 */
public class KnowledgeOtherWebTest extends BaseTestCase
{
    private static String baseUrl = hostUrl + "/knowledgeOther";
    private long knowledgeId = 12345678L;

    @Test
    public void testFetchExternalKnowledgeUrl()
    {
        LogMethod();
        try {
        	String JsonContent = "{\"externalUrl\":\"http://sports.china.com/sportsnews/nba/11172837/20160704/22984107.html\",\"isCreate\":\"false\"}";
            String subUrl = "/fetchExternalKnowledgeUrl";
            JsonNode result = Util.HttpRequestFull(Util.HttpMethod.POST, baseUrl + subUrl, JsonContent);
            Util.checkResponseWithData(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testShareCount()
    {
        LogMethod();
        try {
            String subUrl = "/shareCount/1/111111"; // /shareCount/{type}/{knowledgeId}
            JsonNode result = Util.HttpRequestResult(Util.HttpMethod.GET, baseUrl+subUrl, null);
            Util.checkRequestResultSuccess(result);
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
            JsonNode result = Util.HttpRequestResult(Util.HttpMethod.GET, baseUrl+subUrl, null);
            Util.checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPush()
    {
        LogMethod();
        try {
        	String JsonContent = "";
            String subUrl = "/fetchExternalKnowledgeUrl";
            JsonNode result = Util.HttpRequestResult(Util.HttpMethod.POST, "http://192.168.130.106:8080/dynamicNews/push", JsonContent);
            Util.checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpdatePass()
    {
        LogMethod();
        try {
            String subUrl = "/update/7/MTExMTEx";
            JsonNode result = Util.HttpRequestResult(Util.HttpMethod.GET, baseUrl+subUrl, null);
            Util.checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    public void testGetHotKnowledge()
    {
        LogMethod();
        try {
            String subUrl = "hot/"+10;
            JsonNode result = Util.HttpRequestResult(Util.HttpMethod.GET, baseUrl+subUrl, null);
            Util.checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
