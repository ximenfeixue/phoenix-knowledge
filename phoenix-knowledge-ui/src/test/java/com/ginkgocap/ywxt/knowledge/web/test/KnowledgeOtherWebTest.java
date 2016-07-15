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
    public void testfetchExternalKnowledgeUrlByUrl()
    {
        LogMethod();
        try {
        	String JsonContent = "{\"externalUrl\":\"http://tech.sina.com.cn/it/2016-07-12/doc-ifxtwiht3615423.shtml\",\"isCreate\":\"true\"}";
            String subUrl = "/fetchExternalKnowledgeUrl";
            JsonNode result = Util.HttpRequestResult(Util.HttpMethod.POST, baseUrl+subUrl, JsonContent);
            Util.checkRequestResultSuccess(result);
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
