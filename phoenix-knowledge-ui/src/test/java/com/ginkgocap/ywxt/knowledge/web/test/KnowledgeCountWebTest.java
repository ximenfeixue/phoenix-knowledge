package com.ginkgocap.ywxt.knowledge.web.test;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Test;

/**
 * Created by Chen Peifeng on 2016/5/24.
 */
public class KnowledgeCountWebTest extends BaseTestCase
{
    private static String baseUrl = hostUrl + "/knowledgeCount/";
    private long knowledgeId = 12345678L;
    @Test
    public void testUpdateClickCount()
    {
        LogMethod();
        try {
            String subUrl = "click/"+knowledgeId;
            JsonNode result = Util.HttpRequestResult(Util.HttpMethod.PUT, baseUrl+subUrl, null);
            Util.checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpdateShareCount()
    {
        LogMethod();
        try {
            String subUrl = "share/"+knowledgeId;
            JsonNode result = Util.HttpRequestResult(Util.HttpMethod.PUT, baseUrl+subUrl, null);
            Util.checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCollectClickCount()
    {
        LogMethod();
        try {
            String subUrl = "collect/"+knowledgeId;
            JsonNode result = Util.HttpRequestResult(Util.HttpMethod.PUT, baseUrl+subUrl, null);
            Util.checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCommentClickCount()
    {
        LogMethod();
        try {
            String subUrl = "comment/"+knowledgeId;
            JsonNode result = Util.HttpRequestResult(Util.HttpMethod.PUT, baseUrl+subUrl, null);
            Util.checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
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
    }
}
