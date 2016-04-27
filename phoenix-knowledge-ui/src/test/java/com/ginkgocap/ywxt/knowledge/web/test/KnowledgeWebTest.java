package com.ginkgocap.ywxt.knowledge.web.test;

import com.fasterxml.jackson.databind.JsonNode;
import com.ginkgocap.ywxt.knowledge.model.DataCollection;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeDetail;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeReport;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeUtil;
import com.ginkgocap.ywxt.knowledge.utils.TestData;
import org.junit.Test;

/**
 * Created by Admin on 2016/3/31.
 */
public class KnowledgeWebTest extends BaseTestCase {

    public final String baseUrl =  hostUrl + "/knowledge";

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
            JsonNode result = Util.HttpRequestFull(Util.HttpMethod.GET, baseUrl+subUrl, null);
            Util.checkResponseWithData(result);
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
            JsonNode result = Util.HttpRequestFull(Util.HttpMethod.GET, baseUrl+subUrl, null);
            Util.checkResponseWithData(result);
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
    public void testKnowledgeListByColumnIdAndKeyWord()
    {
        LogMethod();
        try {
            String subUrl = "/all/test/2/1/12"; ///all/{keyWord}/{columnId}/{start}/{size}
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

    @Test
    public void testKnowledgeCollect()
    {
        LogMethod();
        collectKnowledge("KnowledgeWebTest_testKnowledgeCollect");
    }

    @Test
    public void testCancelCollectedKnowledge()
    {
        LogMethod();
        try {
            String subUrl = collectKnowledge("KnowledgeWebTest_testCancelCollectedKnowledge");// "/collect/{knowledgeId/{columnId}"
            JsonNode result = Util.HttpRequestResult(Util.HttpMethod.DELETE, baseUrl+subUrl, null);
            Util.checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testReportKnowledge()
    {
        LogMethod();
        DataCollection data = createKnowledge("KnowledgeWebTest_testReportKnowledge");
        // "/report/{knowledgeId}/{columnId}"
        try {
            long knowledgeId = data.getKnowledgeDetail().getId();
            short columnId = data.getKnowledgeDetail().getColumnId();
            String subUrl = "/report" + knowledAndColumnIdUrl(knowledgeId, columnId);
            KnowledgeReport report = TestData.knowledgeReport(userId, knowledgeId, columnId);
            String knowledgeJson = KnowledgeUtil.writeObjectToJson(report);
            JsonNode result = Util.HttpRequestResult(Util.HttpMethod.POST, baseUrl+subUrl, knowledgeJson);
            Util.checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

    }

    private String collectKnowledge(String title)
    {
        String subUrl = "/collect" + knowledAndColumnIdUrl(title);// "/collect/{knowledgeId/{columnId}"
        try {
            JsonNode result = Util.HttpRequestResult(Util.HttpMethod.POST, baseUrl+subUrl, null);
            Util.checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        return subUrl;
    }

    private String knowledAndColumnIdUrl(String testCase)
    {
        DataCollection data = createKnowledge(testCase);
        long knowledgeId = data.getKnowledgeDetail().getId();
        short columnId = data.getKnowledgeDetail().getColumnId();

        return "/" + knowledgeId + "/" + columnId;
    }

    private String knowledAndColumnIdUrl(long knowledgeId, short columnId)
    {
        return  "/" + knowledgeId + "/" + columnId;
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
