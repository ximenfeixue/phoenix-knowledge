package com.ginkgocap.ywxt.knowledge.web.test;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.codec.Base64;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.ginkgocap.parasol.associate.model.Associate;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeComment;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeUtil;
import com.ginkgocap.ywxt.knowledge.model.common.DataCollect;
import com.ginkgocap.ywxt.knowledge.utils.HttpClientHelper;
import com.ginkgocap.ywxt.knowledge.utils.TestData;
import com.ginkgocap.ywxt.user.model.User;

/**
 * Created by Chen Peifeng on 2016/2/16.
 */
public abstract class BaseTestCase extends TestCase
{
	protected static boolean web = true;
    protected static boolean noTestHost = false;
    protected static boolean debugModel = false;
    protected static boolean runTestCase = false;
    protected static long userId;
    protected static SimpleFilterProvider assoFilter = null;
    protected static String hostUrl = null;
    protected static String loginUrl = null;
    protected static String knowUrl = null;
    protected static String baseUrl =  null;
    protected static String baseOrtherUrl = null;
    protected static String baseHomeUrl = null;
    protected static String baseCommentUrl = null;
    
    protected static String openHostUrl = null;
    private final static String [] envArray = new String[] {"local", "dev", "testOnline", "online"};
    
    public final static String testEnv = envArray[3];
    
    static {
        //-DdebugModel=true -DrunTestCase=true -DhostUrl=http://192.168.120.135:8080
        userId = KnowledgeUtil.getDummyUser().getId();
        debugModel = System.getProperty("debugModel", "true").equals("true");
        runTestCase = System.getProperty("runTestCase", "true").equals("true");
        if ("local".equals(testEnv)) {
            hostUrl = System.getProperty("hostUrl", "http://192.168.101.131:8008/cross");
            loginUrl = hostUrl + getLoginUrl(web);
            knowUrl = System.getProperty("hostUrl", "http://localhost:8080");
        }
        else if ("dev".equals(testEnv)) {
            hostUrl = System.getProperty("hostUrl", "http://192.168.101.131:8008/cross");
            loginUrl = hostUrl + getLoginUrl(web);
            knowUrl = System.getProperty("hostUrl", "http://192.168.120.135:8080/cross");
        }
        else if ("testOnline".equals(testEnv)) {
            hostUrl = System.getProperty("hostUrl", "http://test.online.gintong.com/cross");
            loginUrl = hostUrl + getLoginUrl(web);
            knowUrl = System.getProperty("hostUrl", "http://test.online.gintong.com/cross/newknowledge");
            openHostUrl = System.getProperty("openHostUrl", "http://api.test.gintong.com");
            //hostUrl = System.getProperty("hostUrl", "http://192.168.101.131:3017");
            //hostUrl = System.getProperty("hostUrl", "http://192.168.130.103:8080");
        }
        else if ("online".equals(testEnv)) {
            loginUrl = "http://www.gintong.com/cross" + getLoginUrl(web);
            knowUrl = System.getProperty("hostUrl", "http://www.gintong.com/cross/newknowledge");
            openHostUrl = System.getProperty("openHostUrl", "http://api.gintong.com");
            //hostUrl = System.getProperty("hostUrl", "http://192.168.101.131:3017");
            //hostUrl = System.getProperty("hostUrl", "http://192.168.130.103:8080");
        }
        baseUrl = knowUrl + "/knowledge";
        baseOrtherUrl = knowUrl + "/knowledgeOther";
        baseHomeUrl = knowUrl + "/webknowledge";
        baseCommentUrl = knowUrl + "/knowledgeComment/";
        
        assoFilter = KnowledgeUtil.assoFilterProvider(Associate.class.getName());
    }

    @Override
    protected void runTest() throws Throwable
    {
    	login();
        if (runTestCase) {
            super.runTest();
        }
    }

    //For if no webservice opened skip this test case and package success
    protected void writeException(Exception e)
    {
        if (e.getMessage().contains("Connection refused")) {
            noTestHost = true;
            System.err.println("No opened web service for test....");
        }
        System.err.println("Exception: " + e.getMessage());
        //e.printStackTrace();
    }

    protected void tryFail()
    {
        if (!noTestHost) {
            fail();
        }
    }

    protected void checkResult(JsonNode notifNode)
    {
        if (!noTestHost) {
            checkRequestResultSuccess(notifNode);
        }
    }

    protected void checkResultWithData(JsonNode notifNode)
    {
        if (!noTestHost) {
            checkResponseWithData(notifNode);
        }
    }
    
    protected void checkResultFail(JsonNode notifNode)
    {
        if (!noTestHost) {
            checkRequestResultHaveError(notifNode);
        }
    }
    
    protected void LOG(String logMesage)
    {
    	if (debugModel) {
	    	//Just for show the message
	    	System.err.println("======= "+logMesage+" ========");
    	}
    }
    
    protected static void LOGINFO(Object logMesage)
    {
    	//Just for show the message
    	System.out.println(logMesage);
    }

    protected String LogMethod()
    {
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        if (debugModel) {
            System.out.println("======= "+methodName+" ========");
        }
        return methodName;
    }
    
    protected int getNextNum()
    {
        int max=1500;
        int min=1;
        Random random = new Random();

        return random.nextInt(max)%(max-min+1) + min;
    }
    //end

    private static User user = KnowledgeUtil.getDummyUser();;
    private static String sessionID = null;

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


    public static String getKnowledgeComment(long KnowledgeId, String content)
    {
        return getKnowledgeComment(KnowledgeId, (short)0, content);
    }

    public static String getKnowledgeComment(long KnowledgeId, int columnId, String content)
    {
        return getKnowledgeComment(KnowledgeId, columnId, content, true);
    }

    public static String getKnowledgeComment(long KnowledgeId, int columnId, String content, boolean visible)
    {
        columnId = columnId == 0 ? 2 : columnId;
        KnowledgeComment KnowledgeComment = TestData.knowledgeComment(user.getId(), KnowledgeId, columnId, content, visible);
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
        System.out.print(notifNode+"\r\n");
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
        return response != null ? KnowledgeUtil.readTree(response) : null;
    }

    
    public static String HttpRequestFullJson(String httpMethod,String urlString,String jsonContent) throws Exception
    {
        System.err.print("Method: " + httpMethod + "\r\nUrl: "+urlString+"\r\n" + "Body: " + jsonContent + "\r\n");
        Map<String,String> headers = new HashMap<String, String>(5);
        System.out.println("sessionID: " + sessionID);
        headers.put("s", web ? "web" : "api");
        if (sessionID != null) {
        	headers.put("sessionID", sessionID);
        }
        headers.put("Content-type", "application/json");
        if (HttpMethod.GET.equals(httpMethod)) {
            return HttpClientHelper.GET(urlString, headers);
        }
        else if (HttpMethod.POST.equals(httpMethod)) {
            return HttpClientHelper.POST(urlString, jsonContent, headers);
        }
        else if (HttpMethod.PUT.equals(httpMethod)) {
            return HttpClientHelper.PUT(urlString, jsonContent, headers);
        }
        else if (HttpMethod.DELETE.equals(httpMethod)) {
            return HttpClientHelper.DELETE(urlString, headers);
        }
        return "Unknow http method";
    }

    /*
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
        httpConn.setRequestProperty("s", (web ? "web" : "api"));
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
    }*/

    public static String HttpUpLoad(final String requestUrl, final String filePath) throws IOException {
        //分割符
        String boundary1="-----------------------------32034106127045";
        String boundary2="-------------------------------32034106127045";
        //换行符
        String enter="\r\n";
        System.out.println("Upload url: "+requestUrl);
        URL url = new URL(requestUrl);
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.print("file not existing.");
            return null;
        }
        HttpURLConnection httpUrlConn = (HttpURLConnection)url.openConnection();
        httpUrlConn.setDoOutput(true);
        httpUrlConn.setDoInput(true);
        //设置请求头
        httpUrlConn.setRequestProperty("Content-Type", "multipart/form-data; boundary="+boundary1);
        httpUrlConn.setRequestMethod(HttpMethod.POST);
        OutputStream outputStream = httpUrlConn.getOutputStream();

        StringBuilder sb=new StringBuilder();
        sb.append(boundary2);
        sb.append(enter);
        String disposition="Content-Disposition: form-data; name=\"file\"; filename=\""+file.getName()+"\"";
        sb.append(disposition);
        sb.append(enter);
        //更具不同的类型经行设置
        sb.append("Content-Type: image/jpeg");
        sb.append(enter);
        sb.append(enter);
        outputStream.write(sb.toString().getBytes());
        byte[] b=new byte[1024];
        BufferedInputStream in=new  BufferedInputStream(new FileInputStream(file)) ;
        int c=-1;
        while((c=in.read(b))!=-1){
            outputStream.write(b,0,c);
        }
        in.close();
        outputStream.write(enter.getBytes());
        outputStream.write((boundary2+"--").getBytes());
        outputStream.close();

        StringBuffer buffer = new StringBuffer();
        InputStream inputStream = httpUrlConn.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String errorCode = httpUrlConn.getHeaderField("errorCode");
        String errorMessage = httpUrlConn.getHeaderField("errorMessage");
        if ((errorCode != null && Integer.valueOf(errorCode) > 0 )|| (errorMessage!= null && errorMessage.trim().length() > 0)) {
            buffer.append("{\"notification\":{\"notifCode\":" + errorCode + ",\"notifInfo\":\"" + errorMessage + "\"}}");
        } else {
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();
        }
        return buffer.toString();
    }
    public static JsonNode getJsonNode(String jsonStr, String... values)  throws Exception {
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
    
    protected DataCollect createKnowledge(String title, String knowledgeJson)
    {
        DataCollect data = TestData.getDataCollect(userId, (short)2, title);
        try {
            knowledgeJson = knowledgeJson == null ? KnowledgeUtil.writeObjectToJson(assoFilter, data) : knowledgeJson;
            JsonNode response = HttpRequestFull(HttpMethod.POST, baseUrl, knowledgeJson);
            String retData = BaseTestCase.getResponseData(response);
            if (StringUtils.isBlank(retData)) {
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


    protected DataCollect createKnowledge(String title,List<Long> tagIds,List<Long> directoryIds)
    {
        DataCollect data = TestData.getDataCollect(userId, 1, title);
        try {
            if (data != null && data.getKnowledgeDetail() != null) {
                data.getKnowledgeDetail().setTagList(tagIds);
                data.getKnowledgeDetail().setDirectorys(directoryIds);
                data.getKnowledgeDetail().setColumnid("153");
                //data.getKnowledgeDetail().setContent(content);
                String knowledgeJson = KnowledgeUtil.writeObjectToJson(assoFilter, data);
                JsonNode response = HttpRequestFull(HttpMethod.POST, baseUrl, knowledgeJson);
                checkResponseWithData(response);
                String retData = getResponseData(response);
                Assert.assertFalse("null".equals(retData));
                if (retData != null && !"null".equals(retData)) {
	                long knowledgeId = Long.parseLong(retData);
	                data.getKnowledgeDetail().setId(knowledgeId);
	                data.getReference().setKnowledgeId(knowledgeId);
                }
            }
            else {
                fail();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        return data;
    }
    
    protected static void login(String loginUrl)
    {
    	//sessionID = "d64406fe-a860-490e-913a-519236a775a3";
        if (sessionID == null || sessionID.trim().isEmpty()) {
            final String loginJson = getLoginJson(web);
            try {
                JsonNode retNode = HttpRequestFull(HttpMethod.POST, loginUrl, loginJson);
                if (retNode != null) {
                    JsonNode jsonNode = retNode.get("responseData");
                    if (jsonNode != null) {
                        JsonNode sessionIDNode = jsonNode.get("sessionID");
                        if (sessionIDNode == null) {
                            sessionIDNode = jsonNode.get("sessionId");
                        }
                        sessionID = jsonNode != null ? sessionIDNode.asText() : null;
                    }
                    System.err.println("......sessionID: " + sessionID);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected static void login()
    {
        login(loginUrl);
    }
    
    private static String getLoginUrl(boolean web)
    {
    	return web ? "/web/login.json" : "/login/loginConfiguration.json";
    }
    
    private static String getLoginJson(boolean web)
    {
    	final String userName = "online".equals(testEnv) ? "18611386946" : "18211081791";
        final String passWord = "online".equals(testEnv) ? "sa#123" : "MTExMTEx";
    	String newPassWord = new String(Base64.encode(passWord.getBytes()));
    	String webLoginJson = String.format("{\"username\":\"%s\",\"password\":\"%s\",\"vCode\":\"\",\"index\":0}", userName, passWord);
    	String apiLoginJson = String.format("{\"clientID\":\"18211081791\",\"clientPassword\":\"GT4131929\",\"imei\":\"yss-3434-dsf55-22256\",\"version\":\"1.6.0.0609\",\"platform\":\"iPhone\",\"model\":\"iPhone 3G\",\"resolution\":\"480x320\",\"systemName\":\"iOS\",\"systemVersion\":\"1.5.7\",\"channelID\":\"10086111445441\",\"loginString\":\"%s\",\"password\":\"%s\"}", userName, newPassWord);
    	return web ? webLoginJson : apiLoginJson;
    }
}
