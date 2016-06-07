package com.ginkgocap.ywxt.knowledge.web.test;

import com.fasterxml.jackson.databind.JsonNode;
import com.ginkgocap.ywxt.knowledge.model.*;
import com.ginkgocap.ywxt.knowledge.utils.TestData;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chen Peifeng on 2016/3/31.
 */
public class KnowledgeWebTest extends BaseTestCase {

    public final String baseUrl =  hostUrl + "/knowledge";

    @Test
    public void testCreateKnowledge()
    {
        LogMethod();
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
            String subUrl = "/" + data.getId() + "/" + data.getColumnId(); ///delete/{id}/{columnId}
            JsonNode result = Util.HttpRequestResult(Util.HttpMethod.DELETE, baseUrl+subUrl, null);
            Util.checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testBatchDeleteKnowledge()
    {
        LogMethod();
        try {
            KnowledgeDetail data1 = createKnowledge("KnowledgeWebTest_testBatchDeleteKnowledge1").getKnowledgeDetail();
            KnowledgeDetail data2 = createKnowledge("KnowledgeWebTest_testBatchDeleteKnowledge2").getKnowledgeDetail();
            String knowledgeIds = "[" + data1.getId() + "," + data2.getId() + "]";
            String subUrl = "/batchDelete"; ///delete/{id}/{columnId}
            JsonNode result = Util.HttpRequestResult(Util.HttpMethod.PUT, baseUrl+subUrl, knowledgeIds);
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
            JsonNode result = Util.HttpRequestFull(Util.HttpMethod.GET, baseUrl + subUrl, null);
            Util.checkResponseWithData(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testAllKnowledge()
    {
        LogMethod();
        try {
            String subUrl = "/all/1/20/test"; ////all/{start}/{size}/{keyword}
            JsonNode result = Util.HttpRequestFull(Util.HttpMethod.GET, baseUrl + subUrl, null);
            Util.checkResponseWithData(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testAllByColumnId()
    {
        LogMethod();
        try {
            String subUrl = "/allByColumn/2/21/40"; ///all/{columnId}/{start}/{size}
            JsonNode result = Util.HttpRequestResult(Util.HttpMethod.GET, baseUrl+subUrl, null);
            Util.checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testAllByKeyWord()
    {
        LogMethod();
        try {
            String subUrl = "/allByKeyword/test/1/3"; ///all/{keyWord}/{columnId}/{start}/{size}
            JsonNode result = Util.HttpRequestResult(Util.HttpMethod.GET, baseUrl+subUrl, null);
            Util.checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testAllByColumnIdAndKeyWord()
    {
        LogMethod();
        try {
            String subUrl = "/allByKeywordAndColumn/test/2/1/3"; ////allByKeywordAndColumn/test/2/1/12"
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
            String subUrl = "/user/1/2"; ///user/{start}/{size}
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

    @Test
    public void testBatchTags()
    {
        LogMethod();
        try {
            List<ResItem> resItems = new ArrayList<ResItem>(2);
            ResItem resItem1 = TestData.getResItems("testBatchTags", 1112323L, new long[] {3933811561988102L, 3933811356467203L} );
            ResItem resItem2 = TestData.getResItems("testBatchTags", 1112345L, new long[] {3933811561988102L, 3933811356467203L} );
            resItems.add(resItem1);
            resItems.add(resItem2);
            String requestJson = KnowledgeUtil.writeObjectToJson(resItems);
            JsonNode result = Util.HttpRequestResult(Util.HttpMethod.POST, baseUrl + "/batchTags", requestJson);
            Util.checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

    }

    @Test
    public void testBatchCatalogs()
    {
        LogMethod();
        try {
            List<ResItem> resItems = new ArrayList<ResItem>(2);
            ResItem resItem1 = TestData.getResItems("testBatchCatalogs", 1112323L, new long[] {3933811561988102L, 3933811356467203L} );
            ResItem resItem2 = TestData.getResItems("testBatchCatalogs", 1112345L, new long[] {3933811561988102L, 3933811356467203L} );
            resItems.add(resItem1);
            resItems.add(resItem2);
            String requestJson = KnowledgeUtil.writeObjectToJson(resItems);
            JsonNode result = Util.HttpRequestResult(Util.HttpMethod.POST, baseUrl + "/batchCatalogs", requestJson);
            Util.checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testGetTagsByIds()
    {
        LogMethod();
        try {
            String subUrl = "/tagList";
            Long[] tagIds = new Long [] {3981267922321479L, 3981267939098696L, 3981290542202961L, 3981267964264526L, 3979800628953105L};
            JsonNode result = Util.HttpRequestResult(Util.HttpMethod.POST, baseUrl+subUrl, "[3973605390287002, 3973607483244706]");
            Util.checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        //ResItem resItem = TestData.getResItems("testBatchCatalogs", );
    }

    @Test
    public void testGetTagSourceCountByIds()
    {
        LogMethod();
        try {
            String subUrl = "/tagCount";
            Long[] tagIds = new Long [] {3956219358478388L, 3956238736162890L, 3956186739376159L};
            JsonNode result = Util.HttpRequestResult(Util.HttpMethod.POST, baseUrl+subUrl, "[3973605390287002, 3973607483244706]");
            Util.checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        //ResItem resItem = TestData.getResItems("testBatchCatalogs", );
    }

    @Test
    public void testGetDirectoryListByIds()
    {
        LogMethod();
        try {
            String subUrl = "/directoryList";
            Long[] directoryIds = new Long [] {3933417670705167L, 3933423765028884L, 3933423777611801L};
            JsonNode result = Util.HttpRequestResult(Util.HttpMethod.POST, baseUrl+subUrl, "[3933423765028884, 3933423777611801]");
            Util.checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        //ResItem resItem = TestData.getResItems("testBatchCatalogs", );
    }

    @Test
    public void testGetDirectoryCountByIds()
    {
        LogMethod();
        try {
            String subUrl = "/directoryCount";
            Long[] directoryIds = new Long [] {3933417670705167L, 3933423765028884L, 3933423777611801L};
            JsonNode result = Util.HttpRequestResult(Util.HttpMethod.POST, baseUrl+subUrl, "[3933423765028884, 3933423777611801]");
            Util.checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        //ResItem resItem = TestData.getResItems("testBatchCatalogs", );
    }

    @Test
    public void testGetKnowledgeRelatedResources()
    {
        LogMethod();
        try {
            String subUrl = "/knowledgeRelated/4/1/12/test";  //user/{columnId}/{start}/{size}
            JsonNode result = Util.HttpRequestResult(Util.HttpMethod.GET, baseUrl+subUrl, null);
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
