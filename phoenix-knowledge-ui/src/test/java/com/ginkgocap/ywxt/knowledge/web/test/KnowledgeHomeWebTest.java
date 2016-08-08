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
            JsonNode result = HttpRequestResult(HttpMethod.GET, baseUrl+subUrl, null);
            checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void testHomeSeparateByType()
    {
    	LogMethod();
        try {
        	for (int type = 1; type <8; type ++) {
	            String subUrl = "/home/separate/" + type;  ///home/separate/{type}
	            JsonNode result = HttpRequestResult(HttpMethod.GET, baseUrl+subUrl, null);
	            checkRequestResultSuccess(result);
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
            JsonNode result = HttpRequestResult(HttpMethod.GET, baseUrl+subUrl, null);
            checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testHomeGetHotList()
    {
        LogMethod();
        try {
            String subUrl = "/home/getHotList/1"; ////home/getHotList/{type}
            JsonNode result = HttpRequestResult(HttpMethod.GET, baseUrl+subUrl, null);
            checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testHomeGetCommentList()
    {
        LogMethod();
        try {
            String subUrl = "/home/getCommentList/1"; ///home/getCommentList/{type}
            JsonNode result = HttpRequestResult(HttpMethod.GET, baseUrl+subUrl, null);
            checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testHomeGetAggregationRead()
    {
        LogMethod();
        try {
            String subUrl = "/home/getAggregationRead/1/1/20"; ///home/getAggregationRead/{type}/{page}/{size}
            JsonNode result = HttpRequestResult(HttpMethod.GET, baseUrl+subUrl, null);
            checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void testHomeGetDockingKnowledge()
    {
        LogMethod();
        try {
            String subUrl = "/home/getDockingKnowledge/1/{targetId}/0/20/1"; ///home/getDockingKnowledge/{targetType}/{targetId}/{page}/{size}/{scope}
            JsonNode result = HttpRequestResult(HttpMethod.GET, baseUrl+subUrl, null);
            checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    public void testGetRecommendedKnowledge ()
    {
    	LogMethod();
        try {
        	String subUrl = "/home/getRecommendedKnowledge/1/1/20";  ///home/getRecommendedKnowledge/{type}/{page}/{size}
	    	 JsonNode result = HttpRequestResult(HttpMethod.GET, baseUrl+subUrl, null);
	         checkRequestResultSuccess(result);
	     } catch (Exception e) {
	         e.printStackTrace();
	     }
    }

    /*
    public void test ()
    {

    }*/

}
