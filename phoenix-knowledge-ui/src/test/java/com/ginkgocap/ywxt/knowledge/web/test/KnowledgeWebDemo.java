package com.ginkgocap.ywxt.knowledge.web.test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.ginkgocap.parasol.associate.model.Associate;
import com.ginkgocap.ywxt.knowledge.model.DataCollection;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeDetail;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeReport;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeUtil;
import com.ginkgocap.ywxt.knowledge.utils.TestData;

/**
 * Created by Admin on 2016/5/3.
 */
public class KnowledgeWebDemo {

    private String requestJson;
    private String response;

    private static long KnowledgeId = 123456789L;
    private static boolean debugModel = false;
    private static long userId;
    private static String hostUrl = null;
    private static String baseUrl = null;
    private static String commentBaseUrl = null;
    private static String commentListUrl = null;
    private static String sessionId = null;
    private static ObjectMapper mapper = null;
    private static SimpleFilterProvider assoFilter = null;
    static {
        userId = KnowledgeUtil.getDummyUser().getId();
        //debugModel = System.getProperty("debugModel", "false").equals("true");
        hostUrl = System.getProperty("hostUrl", "http://192.168.120.135:8080");
        baseUrl =  hostUrl + "/knowledge";

        commentBaseUrl = hostUrl + "/knowledgeComment/";
        commentListUrl = commentBaseUrl + "list/";

        mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true) ;
        assoFilter = KnowledgeUtil.assoFilterProvider(Associate.class.getName());
    }

    public static class HttpMethod
    {
        public static final String GET = "GET";
        public static final String POST = "POST";
        public static final String PUT = "PUT";
        public static final String DELETE = "DELETE";
    }

    public static void main(String[] args) throws Exception {
        KnowledgeWebDemo demo = new KnowledgeWebDemo();
        demo.createKnowledge();
        demo.updateKnowledge();
        demo.deleteKnowledge();
        demo.knowledgeDetail();
        demo.allKnowledgeList();
        demo.allCreatedKnowledgeList();
        demo.allCollectedKnowledgeList();
        demo.allByColumnIdKnowledgeList();
        demo.allByKeyWordKnowledgeList();
        demo.allByColumnIdAndKeyWordKnowledgeList();
        demo.allByUserIdKnowledgeList();
        demo.knowledgeListByUserIdAndColumnId();
        demo.knowledgeCollect();
        demo.cancelCollectedKnowledge();
        demo.reportKnowledge();

        //Comment
        demo.knowledgeCommentCreate();
        demo.knowledgeCommentGetList();
        demo.knowledgeCommentGetCount();
        demo.knowledgeCommentDelete();
    }


    public void createKnowledge()
    {
        LogMethod("创建知识", 2);
        requestJson = createKnowledgeRequestJson("testCreateKnowledge");
        try {
            response = Util.HttpRequestFullJson(HttpMethod.POST, baseUrl, requestJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void updateKnowledge()
    {
        LogMethod("更新知识", 2);
        DataCollection data = createKnowledge("updateKnowledge", null);
        data.getKnowledgeDetail().setTitle("updateKnowledge_update");
        requestJson = KnowledgeUtil.writeObjectToJson(assoFilter, data);
        try {
            response = Util.HttpRequestFullJson(HttpMethod.PUT, baseUrl, requestJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void deleteKnowledge()
    {
        LogMethod("删除知识: /{knowledgeId}/{columnId}", 2);
        try {
            KnowledgeDetail data = createKnowledge("deleteKnowledge", null).getKnowledgeDetail();
            String subUrl = "/" + data.getId() + "/" + data.getColumnId(); ///{id}/{columnId}
            Util.HttpRequestFullJson(HttpMethod.DELETE, baseUrl+subUrl, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void knowledgeDetail()
    {
        LogMethod("知识详情: /{knowledgeId}/{columnId}", 2);
        try {
            KnowledgeDetail data = createKnowledge("knowledgeDetail", null).getKnowledgeDetail();
            long knowledgeId = data.getId();
            int columnId = data.getColumnId();
            String subUrl = "/" + knowledgeId + "/" + columnId;  ///{knowledgeId}/{columnId}
            Util.HttpRequestFullJson(HttpMethod.GET, baseUrl + subUrl, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void allKnowledgeList()
    {
        LogMethod("所有知识: /all/{start}/{size}/{keyword}", 2);
        try {
            String subUrl = "/all/0/10/null"; ////all/{start}/{size}
            Util.HttpRequestFullJson(HttpMethod.GET, baseUrl + subUrl, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void allCreatedKnowledgeList()
    {
    	LogMethod("所有创建知识: /allCreated/{start}/{size}/{keyword}", 2);
        try {
            String subUrl = "/allCreated/0/10/null";
            Util.HttpRequestFullJson(HttpMethod.GET, baseUrl + subUrl, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void allCollectedKnowledgeList()
    {
    	LogMethod("所有收藏知识: /allCollected/{start}/{size}/{keyword}", 2);
        try {
            String subUrl = "/allCollected/0/10/null"; ////all/{start}/{size}
            Util.HttpRequestFullJson(HttpMethod.GET, baseUrl + subUrl, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void allByColumnIdKnowledgeList()
    {
        LogMethod("根据栏目提取知识: /allByColumn/{columnId}/{start}/{size}", 2);
        try {
            String subUrl = "/all/2/0/12"; ///all/{columnId}/{start}/{size}
            Util.HttpRequestFullJson(HttpMethod.GET, baseUrl+subUrl, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void allByKeyWordKnowledgeList()
    {
        LogMethod("根据关键字提取知识: /allByKeyword/{keyWord}/{start}/{size}", 2);
        try {
            String subUrl = "/allByKeyword/test/0/3"; //
            Util.HttpRequestFullJson(HttpMethod.GET, baseUrl+subUrl, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void allByColumnIdAndKeyWordKnowledgeList()
    {
        LogMethod("根据栏目关键字提取知识: /allByColumnAndKeyword/{columnId}/{keyWord}/{start}/{size}", 2);
        try {
            String subUrl = "/allByKeywordAndColumn/test/2/0/12"; ///all/{columnId}/{start}/{size}/{keyWord}
            Util.HttpRequestFullJson(HttpMethod.GET, baseUrl+subUrl, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void allByUserIdKnowledgeList()
    {
        LogMethod("提取当前用户的所有知识: /user/{start}/{size}", 2);
        try {
            String subUrl = "/user/0/2"; ///user/{start}/{size}
            Util.HttpRequestFullJson(HttpMethod.GET, baseUrl+subUrl, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void knowledgeListByUserIdAndColumnId()
    {
        LogMethod("根据栏目提取当前用户的知识: /user/{columnId}/{start}/{size}", 2);
        try {
            String subUrl = "/user/2/0/12";  //user/{columnId}/{start}/{size}
            Util.HttpRequestFullJson(HttpMethod.GET, baseUrl+subUrl, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void knowledgeCollect()
    {
        LogMethod("收藏知识: /collect/{knowledgeId/{columnId}", 2);
        collectKnowledge("knowledgeCollect");
    }


    public void cancelCollectedKnowledge()
    {
        LogMethod("取消收藏: /collect/{knowledgeId/{columnId}", 2);
        try {
            String subUrl = collectKnowledge("cancelCollectedKnowledge");// "/collect/{knowledgeId/{columnId}"
            Util.HttpRequestFullJson(HttpMethod.DELETE, baseUrl+subUrl, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void reportKnowledge()
    {
        LogMethod("举报知识: /report/{knowledgeId}/{columnId}", 2);
        // "/report/{knowledgeId}/{columnId}"
        try {
            DataCollection data = createKnowledge("reportKnowledge", null);
            long knowledgeId = data.getKnowledgeDetail().getId();
            int columnId = data.getKnowledgeDetail().getColumnId();
            String subUrl = "/report" + knowledAndColumnIdUrl(knowledgeId, columnId);
            KnowledgeReport report = TestData.knowledgeReport(userId, knowledgeId, columnId);
            String knowledgeJson = KnowledgeUtil.writeObjectToJson(report);
            Util.HttpRequestFullJson(HttpMethod.POST, baseUrl + subUrl, knowledgeJson);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void testGetTagsByIds()
    {
        LogMethod("批量获取标签", 2);
        try {
            String subUrl = "/tagList";
            Long[] tagIds = new Long [] {3956219358478388L, 3956238736162890L, 3956186739376159L};
            JsonNode result = Util.HttpRequestResult(Util.HttpMethod.POST, baseUrl+subUrl, "[3956219358478388, 3956238736162890, 3956186739376159]");
            Util.checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testGetDirectoryListByIds()
    {
        LogMethod("批量获取目录", 2);
        try {
            String subUrl = "/directoryList";
            Long[] directoryIds = new Long [] {3933417670705167L, 3933423765028884L, 3933423777611801L};
            JsonNode result = Util.HttpRequestResult(Util.HttpMethod.POST, baseUrl+subUrl, "[3933417670705167, 3933423765028884, 3933423777611801]");
            Util.checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testGetKnowledgeRelatedResources()
    {
        LogMethod("获取推荐知识", 2);
        try {
            String subUrl = "/knowledgeRelated/4/1/12/test";  //user/{columnId}/{start}/{size}
            JsonNode result = Util.HttpRequestResult(Util.HttpMethod.GET, baseUrl+subUrl, null);
            Util.checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /////////////////////////////======Comment=============////////////////////////////
    private String collectKnowledge(String title)
    {
        String subUrl = "/collect" + knowledAndColumnIdUrl(title);// "/collect/{knowledgeId/{columnId}"
        try {
        	Util.HttpRequestFullJson(HttpMethod.POST, baseUrl + subUrl, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return subUrl;
    }

    private String knowledAndColumnIdUrl(String testCase)
    {
        DataCollection data = createKnowledge(testCase, null);
        long knowledgeId = data.getKnowledgeDetail().getId();
        int columnId = data.getKnowledgeDetail().getColumnId();

        return "/" + knowledgeId + "/" + columnId;
    }

    private String knowledAndColumnIdUrl(long knowledgeId, int columnId)
    {
        return  "/" + knowledgeId + "/" + columnId;
    }

    private DataCollection createKnowledge(String title, String knowledgeJson)
    {
        DataCollection data = TestData.getDataCollection(userId, (short) 2, title);
        try {
            knowledgeJson = knowledgeJson == null ? KnowledgeUtil.writeObjectToJson(assoFilter, data) : knowledgeJson;
            JsonNode response = Util.HttpRequestFull(HttpMethod.POST, baseUrl, knowledgeJson);
            String retData = Util.getResponseData(response);
            if (retData == null || "null".equals(retData) || retData.trim().isEmpty()) {
            	System.err.println("Create Knowledge failed....");
            }
            else {
	            long knowledgeId = Long.parseLong(retData);
	            data.getKnowledgeDetail().setId(knowledgeId);
	            data.getReference().setKnowledgeId(knowledgeId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    private String createKnowledgeRetJson(String title, String knowledgeJson)
    {
        String response = null;
        try {
            knowledgeJson = knowledgeJson == null ? createKnowledgeRequestJson(title) : knowledgeJson;
            response = Util.HttpRequestFullJson(HttpMethod.POST, baseUrl, knowledgeJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    private String createKnowledgeRequestJson(String title)
    {
        DataCollection data = TestData.getDataCollection(userId, (short) 2, title);
        String knowledgeJson = KnowledgeUtil.writeObjectToJson(assoFilter, data);
        return knowledgeJson;
    }

    ///////////////////////////////=========Comment=========//////////////////////////
    public void knowledgeCommentCreate()
    {
        LogMethod("添加评论", 2);
        createKnowledgeComment("knowledgeCommentCreate", true);
    }

    public void knowledgeCommentGetList()
    {
        LogMethod("评论列表", 2);
        try {
            createKnowledgeComment("knowledgeCommentGetList", false);

            String URL = commentListUrl + KnowledgeId;
            Util.HttpRequestFullJson(Util.HttpMethod.GET, URL, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void knowledgeCommentGetCount()
    {
        LogMethod("评论数量: /count/{KnowledgeId}", 2);
        try {
            createKnowledgeComment("knowledgeCommentGetCount", false);

            String URL = commentBaseUrl + "count/" + KnowledgeId;
            Util.HttpRequestFullJson(Util.HttpMethod.GET, URL, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void knowledgeCommentDelete()
    {
        LogMethod("删除评论", 2);
        long commentId = 0L;
        try {
            String response = createKnowledgeComment("knowledgeCommentDelete", false);
            JsonNode jsonNode = mapper.readTree(response);
            if (jsonNode.get("responseData") != null) {
                commentId = jsonNode.get("responseData").longValue();
            }

            String URL = commentBaseUrl + commentId;
            Util.HttpRequestFullJson(Util.HttpMethod.DELETE, URL, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String createKnowledgeComment(String content,boolean debug)
    {
        try {
            String URL = commentBaseUrl + KnowledgeId;
            String jsonContent = Util.getKnowledgeComment(KnowledgeId, content);
            return Util.HttpRequestFullJson(Util.HttpMethod.POST, URL, jsonContent);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    ///////////////////////=======Log Request/Response Message=======//////////////////////
    private static void Debug(String message)
    {
        if (debugModel) {
            System.out.print(message);
        }
    }

    private static void Info(String message)
    {
        System.out.print(message);
    }

    private static void LogRequest(String message)
    {
        System.err.print("Request: " + message + "\r\n");
    }

    private static void LogResponse(String message)
    {
        System.err.print("Response: " + message);
    }

    private static String LogMethod(String content, int stackLevel)
    {
        String methodName = Thread.currentThread().getStackTrace()[stackLevel].getMethodName();
        System.out.println("\r\n======= "+content + " " + methodName+" ========");
        return methodName;
    }

    private static String LogMethod(int stackLevel)
    {
        String methodName = Thread.currentThread().getStackTrace()[stackLevel].getMethodName();
        System.out.println("\r\n======= "+methodName+" ========");
        return methodName;
    }

}
