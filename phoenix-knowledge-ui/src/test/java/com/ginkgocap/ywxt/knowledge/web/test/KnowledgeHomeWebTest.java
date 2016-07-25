package com.ginkgocap.ywxt.knowledge.web.test;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Created by gintong on 2016/7/24.
 */
public class KnowledgeHomeWebTest extends BaseTestCase
{
    private static String baseUrl = hostUrl + "/webknowledge";
    private long knowledgeId = 12345678L;

    public void testHomeSeparate ()
    {
        LogMethod();
        try {
            String subUrl = "/home/separate/1"; ///home/separate/{type}
            JsonNode result = Util.HttpRequestResult(Util.HttpMethod.GET, baseUrl+subUrl, null);
            Util.checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void testSeparate()
    {
    	LogMethod();
        try {
        	for (int type = 1; type <12; type ++) {
	            String subUrl = "home/separate/" + type;  ///home/separate/{type}
	            JsonNode result = Util.HttpRequestResult(Util.HttpMethod.GET, baseUrl+subUrl, null);
	            Util.checkRequestResultSuccess(result);
	        	}
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void testHomeGetHotTag()
    {
        LogMethod();
        try {
            String subUrl = "/home/getHotTag/8"; ////home/getHotTag/{count}
            JsonNode result = Util.HttpRequestResult(Util.HttpMethod.GET, baseUrl+subUrl, null);
            Util.checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testHomeGetHotList()
    {
        LogMethod();
        try {
            String subUrl = "/home/getHotList/1"; ////home/getHotList/{type}
            JsonNode result = Util.HttpRequestResult(Util.HttpMethod.GET, baseUrl+subUrl, null);
            Util.checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testHomeGetCommentList()
    {
        LogMethod();
        try {
            String subUrl = "/home/getCommentList/1"; ///home/getCommentList/{type}
            JsonNode result = Util.HttpRequestResult(Util.HttpMethod.GET, baseUrl+subUrl, null);
            Util.checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testHomeGetAggregationRead()
    {
        LogMethod();
        try {
            String subUrl = "/home/getAggregationRead/1/1/20"; ///home/getAggregationRead/{type}/{page}/{size}
            JsonNode result = Util.HttpRequestResult(Util.HttpMethod.GET, baseUrl+subUrl, null);
            Util.checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void testHomeGetDockingKnowledge()
    {
        LogMethod();
        try {
            String subUrl = "/home/getDockingKnowledge/1/{targetId}/0/20/1"; ///home/getDockingKnowledge/{targetType}/{targetId}/{page}/{size}/{scope}
            JsonNode result = Util.HttpRequestResult(Util.HttpMethod.GET, baseUrl+subUrl, null);
            Util.checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    public void test ()
    {

    }


    public void test ()
    {

    }*/

}
