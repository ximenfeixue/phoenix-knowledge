package com.ginkgocap.ywxt.knowledge.web.test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeComment;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeUtil;
import com.ginkgocap.ywxt.knowledge.utils.TestData;
import com.ginkgocap.ywxt.user.model.User;
import junit.framework.Assert;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Chen Peifeng on 2016/1/27.
 */
public final class Util {

    private static ObjectMapper mapper = null;
    private static User user;
    public static String sessionID = null;
    static {
        mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true) ;
        user = KnowledgeUtil.getDummyUser();
    }

    public static class HttpMethod
    {
        public static final String GET = "GET";
        public static final String POST = "POST";
        public static final String PUT = "PUT";
        public static final String DELETE = "DELETE";
    }

    public static enum KnowledgeDealType
    {
        ECreate,
        EUpdate,
        EDelete
    }

    public static class RetKey
    {
        public static final String Notification = "notification";
        public static final String NotifInfo = "notifInfo";
        public static final String NotifCode = "notifCode";
        public static final String RespData = "responseData";
    }



    public static String getJsonFile(String fileName)
    {
        return fileName + ".json";
    }

    public static String getUpdateJsonFile(String fileName)
    {
        return fileName + "Update.json";
    }

    public static String getDeleteJsonFile(String fileName)
    {
        return fileName + "Delete.json";
    }

    /*
    public static String getRequestContent(KnowledgeServicesType KnowledgeType, KnowledgeDealType KnowledgeDealType, String resPath)
    {
        String jsonPath = null;
        Class KnowledgeClass = KnowledgeUtil.getKnowledgeClass(KnowledgeType);
        resPath += KnowledgeClass.getSimpleName() + KnowledgeType.getTypeId();
        if (KnowledgeDealType == KnowledgeDealType.ECreate) {
            jsonPath = Util.getJsonFile(resPath);
        }
        else if(KnowledgeDealType == KnowledgeDealType.EUpdate)
        {
            jsonPath = Util.getUpdateJsonFile(resPath);
        }
        else if(KnowledgeDealType == KnowledgeDealType.EDelete)
        {
            jsonPath = Util.getDeleteJsonFile(resPath);
        }
        return KnowledgeUtil.getJsonContentFromFile(jsonPath);
    }

    public static String getKnowledgeCommentForUpdate(Long KnowledgeId)
    {
        return getKnowledgeComment(KnowledgeId, "CommentForUpdate");
       //return "{\"id\":0,\"KnowledgeId\":56687899,\"ownerId\":12344567,\"ownerName\":\"DummyUserName\",\"createTime\":1454306782624,\"content\":\"CommentForUpdate\",\"isVisible\":1}";
        //return convertKnowledgeCommentFromString(KnowledgeComment);
    }

    public static String getKnowledgeCommentForDelete(Long KnowledgeId)
    {
        return getKnowledgeComment(KnowledgeId, "CommentForDelete");
        //return "{\"id\":0,\"KnowledgeId\":56687899,\"ownerId\":12344567,\"ownerName\":\"DummyUserName\",\"createTime\":1454306782624,\"content\":\"CommentForDelete\",\"isVisible\":1}";
        //return convertKnowledgeCommentFromString(KnowledgeComment);
    }


    //private static KnowledgeComment convertKnowledgeCommentFromString(String KnowledgeComment)
    {
        KnowledgeComment comment = null;
        try {
            comment = objectMapper.readValue(KnowledgeComment, KnowledgeComment.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return comment;
    }*/

    public static String getKnowledgeComment(long KnowledgeId, String content)
    {
        return getKnowledgeComment(KnowledgeId, (short)0, content);
    }

    public static String getKnowledgeComment(long KnowledgeId, int columnId, String content)
    {
        columnId = columnId == 0 ? 2 : columnId;
        KnowledgeComment KnowledgeComment = TestData.knowledgeComment(user.getId(), KnowledgeId, columnId, content);
        return KnowledgeUtil.writeObjectToJson(KnowledgeComment);
    }

    public static void checkResponse(JsonNode notifNode)
    {
        JsonNode result = notifNode != null ? notifNode.get(RetKey.Notification) : notifNode;
        checkRequestResultSuccess(result);
    }

    public static void checkResponseWithData(JsonNode notifNode)
    {
        Assert.assertNotNull(notifNode);
        checkRequestResultSuccess(notifNode.get(RetKey.Notification));
        Assert.assertNotNull(notifNode.get(RetKey.RespData));
    }

    public static void checkRequestResultSuccess(JsonNode notifNode)
    {
        Assert.assertNotNull(notifNode);
        Assert.assertEquals(0, notifNode.get(RetKey.NotifCode).asInt());
        Assert.assertEquals("success", notifNode.get(RetKey.NotifInfo).textValue());
    }

    public static void checkRequestResultHaveError(JsonNode notifNode)
    {
        Assert.assertNotNull(notifNode);
        Assert.assertTrue(notifNode.get(RetKey.NotifCode).asInt()!=0);
        Assert.assertTrue(!"success".equals(notifNode.get(RetKey.NotifInfo).textValue()));
    }

    public static String getResponseData(JsonNode notifNode)
    {
        String retData = null;
        if (notifNode != null && notifNode.get(RetKey.RespData) != null) {
            retData = notifNode.get(RetKey.RespData).toString();
        }
        return retData;
    }

    public static JsonNode HttpRequestResult(String httpMethod,String urlString,String jsonContent) throws Exception
    {
        JsonNode node = HttpRequestFull(httpMethod, urlString, jsonContent);
        return node != null ? node.get(RetKey.Notification) : null;
    }

    public static JsonNode HttpRequestFull(String httpMethod,String urlString,String jsonContent) throws Exception
    {
        String response = HttpRequestFullJson(httpMethod, urlString, jsonContent);
        return response != null ? mapper.readTree(response) : null;
    }

    public static String HttpRequestFullJson(String httpMethod,String urlString,String jsonContent) throws Exception
    {
    	System.err.print("httpMethod: " + httpMethod + " Url: "+urlString+"\r\n");
        if (jsonContent != null) {
            System.err.print("request Content: " + jsonContent + "\r\n");
        }

        URL url = new URL(urlString);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();

        if (jsonContent != null) {
        	httpConn.setRequestProperty( "Content-Length",String.valueOf( jsonContent.length() ) );
        }
        httpConn.setRequestProperty("s","api");
        if (sessionID != null) {
        	httpConn.setRequestProperty("sessionID", sessionID);
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
	            System.out.println(inputLine);
	            break;
	        }
	        in.close();
        }

        return inputLine;
    }

    public static JsonNode getJsonNode(String jsonStr, String... values)
            throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true) ;

        JsonNode node = mapper.readTree(jsonStr);

        if (values != null && values.length > 0) {
            for (String v : values) {
                node = node.path(v);
            }
        }
        return node;
    }

    /*
    public static String updatePermissionInfo(String jsonContent,long KnowledgeId) throws Exception
    {
        JsonNode KnowledgeNode = getJsonNode(jsonContent, Constant.JsonNode.Knowledge);
        JsonNode assoNode = getJsonNode(jsonContent, Constant.JsonNode.Asso);

        KnowledgeFinanceInvestment Knowledge = KnowledgeUtil.readValue(KnowledgeNode.toString(), KnowledgeFinanceInvestment.class);
        Knowledge.setId(KnowledgeId);
        Knowledge.setTitle("Update Title Test");
        Knowledge.setNote("Update Note Test");

        String KnowledgeContent = objectMapper.writeValueAsString(Knowledge);

        jsonContent = "{\"Knowledge\":" + KnowledgeContent + ",\"asso\":" + assoNode.toString() + "," + "\"perms\"" + ":{\"resId\":" + KnowledgeId + "}}";

        KnowledgeNode = getJsonNode(jsonContent, Constant.JsonNode.Knowledge);
        assoNode = getJsonNode(jsonContent, Constant.JsonNode.Asso);
        return jsonContent;
    }*/

    public static void login(String loginUrl)
    {
        final String loginJson = "{\"clientID\":\"18311436234\",\"clientPassword\":\"GT4131929\",\"imei\":\"yss-3434-dsf55-22256\",\"version\":\"1.6.0.0609\",\"platform\":\"iPhone\",\"model\":\"iPhone 3G\",\"resolution\":\"480x320\",\"systemName\":\"iOS\",\"systemVersion\":\"1.5.7\",\"channelID\":\"10086111445441\",\"loginString\":\"liubang\",\"password\":\"MTExMTEx\"}";
        try {
            JsonNode retNode = HttpRequestFull(HttpMethod.POST, loginUrl, loginJson);
            if (retNode != null) {
                JsonNode jsonNode = retNode.get("responseData");
                sessionID = jsonNode != null ? jsonNode.get("sessionID").asText() : null;
                System.err.println("......sessionID: "+sessionID);
	            }
  
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
