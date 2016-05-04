package com.ginkgocap.ywxt.knowledge.web.test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ginkgocap.ywxt.knowledge.model.DataCollection;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeDetail;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeReport;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeUtil;
import com.ginkgocap.ywxt.knowledge.utils.TestData;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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
    static {
        userId = 1234567L;
        //debugModel = System.getProperty("debugModel", "false").equals("true");
        hostUrl = System.getProperty("hostUrl", "http://192.168.120.135:8080/phoenix-knowledge");
        baseUrl =  hostUrl + "/knowledge";

        commentBaseUrl = hostUrl + "/knowledgeComment/";
        commentListUrl = commentBaseUrl + "list/";

        mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true) ;
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
        demo.knowledgeListByColumnId();
        demo.knowledgeListByColumnIdAndKeyWord();
        demo.knowledgeListByColumnIdByUserId();
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
            response = HttpRequestFullJson(HttpMethod.POST, baseUrl, requestJson, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void updateKnowledge()
    {
        LogMethod("更新知识", 2);
        DataCollection data = createKnowledge("updateKnowledge", null);
        data.getKnowledgeDetail().setTitle("updateKnowledge_update");
        requestJson = KnowledgeUtil.writeObjectToJson(data);
        try {
            response = HttpRequestFullJson(HttpMethod.PUT, baseUrl, requestJson, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void deleteKnowledge()
    {
        LogMethod("删除知识", 2);
        try {
            KnowledgeDetail data = createKnowledge("deleteKnowledge", null).getKnowledgeDetail();
            String subUrl = "/" + data.getId() + "/" + data.getColumnId(); ///{id}/{columnId}
            HttpRequestFullJson(HttpMethod.DELETE, baseUrl+subUrl, null, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void knowledgeDetail()
    {
        LogMethod("知识详情", 2);
        try {
            KnowledgeDetail data = createKnowledge("knowledgeDetail", null).getKnowledgeDetail();
            long knowledgeId = data.getId();
            short columnId = data.getColumnId();
            String subUrl = "/" + knowledgeId + "/" + columnId;  ///{knowledgeId}/{columnId}
            HttpRequestFullJson(HttpMethod.GET, baseUrl + subUrl, null, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void allKnowledgeList()
    {
        LogMethod("所有知识", 2);
        try {
            String subUrl = "/all/1/10"; ////all/{start}/{size}
            HttpRequestFullJson(HttpMethod.GET, baseUrl + subUrl, null, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void knowledgeListByColumnId()
    {
        LogMethod("根据栏目提取知识", 2);
        try {
            String subUrl = "/all/2/12"; ///all/{columnId}/{start}/{size}
            HttpRequestFullJson(HttpMethod.GET, baseUrl+subUrl, null, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void knowledgeListByColumnIdAndKeyWord()
    {
        LogMethod("根据栏目关键字提取知识", 2);
        try {
            String subUrl = "/all/test/2/1/12"; ///all/{keyWord}/{columnId}/{start}/{size}
            HttpRequestFullJson(HttpMethod.GET, baseUrl+subUrl, null, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void knowledgeListByColumnIdByUserId()
    {
        LogMethod("提取当前用户的所有知识", 2);
        try {
            String subUrl = "/user/1/2"; ///user/{start}/{size}
            HttpRequestFullJson(HttpMethod.GET, baseUrl+subUrl, null, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void knowledgeListByUserIdAndColumnId()
    {
        LogMethod("根据栏目提取当前用户的知识", 2);
        try {
            String subUrl = "/user/2/1/12";  //user/{columnId}/{start}/{size}
            HttpRequestFullJson(HttpMethod.GET, baseUrl+subUrl, null, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void knowledgeCollect()
    {
        LogMethod("收藏知识", 2);
        collectKnowledge("knowledgeCollect", true);
    }


    public void cancelCollectedKnowledge()
    {
        LogMethod("取消收藏", 2);
        try {
            String subUrl = collectKnowledge("cancelCollectedKnowledge", false);// "/collect/{knowledgeId/{columnId}"
            HttpRequestFullJson(HttpMethod.DELETE, baseUrl+subUrl, null, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void reportKnowledge()
    {
        LogMethod("举报知识", 2);
        // "/report/{knowledgeId}/{columnId}"
        try {
            DataCollection data = createKnowledge("reportKnowledge", null);
            long knowledgeId = data.getKnowledgeDetail().getId();
            short columnId = data.getKnowledgeDetail().getColumnId();
            String subUrl = "/report" + knowledAndColumnIdUrl(knowledgeId, columnId);
            KnowledgeReport report = TestData.knowledgeReport(userId, knowledgeId, columnId);
            String knowledgeJson = KnowledgeUtil.writeObjectToJson(report);
            HttpRequestFullJson(HttpMethod.POST, baseUrl + subUrl, knowledgeJson, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /////////////////////////////======Comment=============////////////////////////////




    private String collectKnowledge(String title, boolean debug)
    {
        String subUrl = "/collect" + knowledAndColumnIdUrl(title);// "/collect/{knowledgeId/{columnId}"
        try {
            HttpRequestFullJson(HttpMethod.POST, baseUrl + subUrl, null, debug);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return subUrl;
    }

    private String knowledAndColumnIdUrl(String testCase)
    {
        DataCollection data = createKnowledge(testCase, null);
        long knowledgeId = data.getKnowledgeDetail().getId();
        short columnId = data.getKnowledgeDetail().getColumnId();

        return "/" + knowledgeId + "/" + columnId;
    }

    private String knowledAndColumnIdUrl(long knowledgeId, short columnId)
    {
        return  "/" + knowledgeId + "/" + columnId;
    }

    private DataCollection createKnowledge(String title, String knowledgeJson)
    {
        DataCollection data = TestData.getDataCollection(userId, (short) 2, title);
        try {
            knowledgeJson = knowledgeJson == null ? KnowledgeUtil.writeObjectToJson(data) : knowledgeJson;
            JsonNode response = HttpRequestFull(HttpMethod.POST, baseUrl, knowledgeJson);
            long knowledgeId = Long.parseLong(Util.getResponseData(response));
            data.getKnowledgeDetail().setId(knowledgeId);
            data.getReference().setKnowledgeId(knowledgeId);
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
            response = HttpRequestFullJson(HttpMethod.POST, baseUrl, knowledgeJson, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    private String createKnowledgeRequestJson(String title)
    {
        DataCollection data = TestData.getDataCollection(userId, (short) 2, title);
        String knowledgeJson = KnowledgeUtil.writeObjectToJson(data);
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
            HttpRequestFullJson(Util.HttpMethod.GET, URL, null, true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void knowledgeCommentGetCount()
    {
        LogMethod("评论数量", 2);
        try {
            createKnowledgeComment("knowledgeCommentGetCount", false);

            String URL = commentBaseUrl + "count/" + KnowledgeId;
            HttpRequestFullJson(Util.HttpMethod.GET, URL, null, true);
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
            HttpRequestFullJson(Util.HttpMethod.DELETE, URL, null, true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String createKnowledgeComment(String content,boolean debug)
    {
        try {
            String URL = commentBaseUrl + KnowledgeId;
            String jsonContent = Util.getKnowledgeComment(KnowledgeId, content);
            return HttpRequestFullJson(Util.HttpMethod.POST, URL, jsonContent, debug);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    ///////////////////////////////=======Http Request tools=====================//////////////////
    public static JsonNode HttpRequestFull(String httpMethod,String urlString,String jsonContent) throws Exception
    {
        String response = HttpRequestFullJson(httpMethod, urlString, jsonContent, false);
        return response != null ? mapper.readTree(response) : null;
    }

    private static String HttpRequestFullJson(String httpMethod,String urlString,String jsonContent,boolean debug) throws Exception
    {
        debugModel = debug;
        Debug("HttpMethod: " + httpMethod + "   Url: " + urlString + "\r\n");
        if (jsonContent != null) {
            Debug("\r\nrequest Content:\r\n" + jsonContent + "\r\n");
        }

        URL url = new URL(urlString);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();

        if (jsonContent != null) {
            httpConn.setRequestProperty( "Content-Length",String.valueOf( jsonContent.length() ) );
        }
        httpConn.setRequestProperty("s","api");
        if (sessionId != null) {
            httpConn.setRequestProperty("sessionID", sessionId);
        }
        httpConn.setRequestProperty("Content-Type","text/xml; charset=utf-8");
        httpConn.setRequestMethod( httpMethod );
        httpConn.setDoOutput(true);
        httpConn.setDoInput(true);
        if (jsonContent != null) {
            OutputStream out = httpConn.getOutputStream();
            out.write( jsonContent.getBytes() );
            out.close();
        }

        String inputLine = null;
        String errorCode = httpConn.getHeaderField("errorCode");
        String errorMessage = httpConn.getHeaderField("errorMessage");
        if ((errorCode != null && Integer.valueOf(errorCode) > 0 )|| (errorMessage!= null && errorMessage.trim().length() > 0)) {
            inputLine = "{\"notification\":{\"notifCode\":" + errorCode + ",\"notifInfo\":\"" + errorMessage + "\"}}";
        }
        else {
            InputStreamReader isr = new InputStreamReader(httpConn.getInputStream(),"utf-8");
            BufferedReader in = new BufferedReader(isr);

            while ((inputLine = in.readLine()) != null) {
                break;
            }
            in.close();
        }

        Debug("\r\nresponse Content:\r\n" + inputLine + "\r\n");
        return inputLine;
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
