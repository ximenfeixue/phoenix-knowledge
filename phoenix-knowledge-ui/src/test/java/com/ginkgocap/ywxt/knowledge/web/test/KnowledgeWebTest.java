package com.ginkgocap.ywxt.knowledge.web.test;

import com.fasterxml.jackson.databind.JsonNode;
import com.ginkgocap.ywxt.knowledge.model.DataCollection;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeDetail;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeUtil;
import com.ginkgocap.ywxt.knowledge.utils.TestData;
import org.junit.Test;

/**
 * Created by Admin on 2016/3/31.
 */
public class KnowledgeWebTest extends BaseTestCase {

    public static final String baseUrl = "http://localhost:8080/phoenix-knowledge/knowledge";

    @Test
    public void testCreateKnowledge()
    {
        createKnowledge("KnowledgeWebTest_testCreateKnowledge");
    }

    @Test
    public void testUpdateKnowledge()
    {
        LogMethod();
        try {
            DataCollection data = createKnowledge("KnowledgeWebTest_create");
            data.getKnowledgeDetail().setTitle("KnowledgeWebTest_Update");
            String knowledgeJson = KnowledgeUtil.writeObjectToJson(data);
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
        LogMethod();
        try {
            KnowledgeDetail data = createKnowledge("KnowledgeWebTest_testDeleteKnowledge").getKnowledgeDetail();
            String subUrl = "/" + data.getId() + "/" + data.getColumnId(); ///{id}/{columnId}
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
        LogMethod();
        try {
            KnowledgeDetail data = createKnowledge("KnowledgeWebTest_testKnowledgeDetail").getKnowledgeDetail();
            long knowledgeId = data.getId();
            short columnId = data.getColumnId();
            String subUrl = "/" + knowledgeId + "/" + columnId;  ///{id}/{columnId}
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
        LogMethod();
        try {
            String subUrl = "/all/1/10"; ////all/{start}/{size}
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
        LogMethod();
        try {
            String subUrl = "/all/2/12"; ///all/{columnId}/{start}/{size}
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
        LogMethod();
        try {
                            ///user/{start}/{size}
            String subUrl = "/user/1/2";
            JsonNode result = Util.HttpRequestResult(Util.HttpMethod.GET, baseUrl+subUrl, null);
            Util.checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testKnowledgeListByUserIdAndColumnId()
    {
        LogMethod();
        try {
            String subUrl = "/user/2/1/12";  //user/{columnId}/{start}/{size}
            JsonNode result = Util.HttpRequestResult(Util.HttpMethod.GET, baseUrl+subUrl, null);
            Util.checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    private DataCollection createKnowledge(String title)
    {
        DataCollection data = TestData.getDataCollection(userId, (short) 2, title);
        try {
            String knowledgeJson = KnowledgeUtil.writeObjectToJson(data);
            JsonNode response = Util.HttpRequestFull(Util.HttpMethod.POST, baseUrl, knowledgeJson);
            Util.checkResponse(response);
            long knowledgeId = Long.parseLong(Util.getResponseData(response));
            data.getKnowledgeDetail().setId(knowledgeId);
            data.getReference().setKnowledgeId(knowledgeId);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        return data;
    }
}
