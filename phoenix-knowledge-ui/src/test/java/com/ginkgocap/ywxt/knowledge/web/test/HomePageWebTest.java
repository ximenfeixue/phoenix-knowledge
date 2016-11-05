package com.ginkgocap.ywxt.knowledge.web.test;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Created by gintong on 2016/11/5.
 */
public class HomePageWebTest extends BaseTestCase
{

    public void testHotList()
    {
        LogMethod();
        try {
            String subUrl = "/resource/hotList.json";
            String reqContent = "{\"page\":1,\"rows\":5,\"type\":\"4\",\"hot\":\"2\"}";
            JsonNode result = HttpRequestResult(HttpMethod.POST, "http://www.gintong.com/cross" + subUrl, reqContent);
            checkResponseWithData(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
