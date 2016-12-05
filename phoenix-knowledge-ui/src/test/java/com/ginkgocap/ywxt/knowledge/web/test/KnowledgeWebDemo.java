package com.ginkgocap.ywxt.knowledge.web.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.ginkgocap.parasol.associate.model.Associate;
import com.ginkgocap.ywxt.knowledge.model.common.DataCollect;
import com.ginkgocap.ywxt.knowledge.model.common.KnowledgeDetail;
import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeReport;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeUtil;
import com.ginkgocap.ywxt.knowledge.utils.HttpClientHelper;
import com.ginkgocap.ywxt.knowledge.utils.TestData;

/**
 * Created by Admin on 2016/5/3.
 */
public class KnowledgeWebDemo extends BaseTestCase {

    private String requestJson;

    private static long KnowledgeId = 123456789L;
    private static String commentListUrl = null;
    private static ObjectMapper mapper = null;
    
    static {
        commentListUrl = baseCommentUrl + "list/";

        mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true) ;
        assoFilter = KnowledgeUtil.assoFilterProvider(Associate.class.getName());
    }

    public static void main(String[] args) throws Exception {
    	//bigData();
        login();
        pusUpdate();
    	/*
    	login(loginUrl);
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
        demo.knowledgeCommentDelete();*/
    }

	public static void bigData()
	{
		List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
		pairs.add(new BasicNameValuePair("page", String.valueOf(0)));
		pairs.add(new BasicNameValuePair("rows", String.valueOf(20)));
		pairs.add(new BasicNameValuePair("type", String.valueOf(1)));// 1,推荐 2,发现
		//Map<String, Object> model = new HashMap<String, Object>();
		try {
			String responseJson = HttpClientHelper.post("http://123.59.50.85:8090" + "/API/hotKno.do", pairs);
			System.err.println("responseJson："+responseJson);
			//model.put("list", PackingDataUtil.getRecommendResult(responseJson));
		} catch (Exception e) {
			System.err.println("connect big data service failed！");
			e.printStackTrace();
		}
	}
	
    public void createKnowledge()
    {
        LogMethod("创建知识", 2);
        requestJson = createKnowledgeRequestJson("testCreateKnowledge");
        try {
            HttpRequestFullJson(HttpMethod.POST, baseUrl, requestJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void updateKnowledge()
    {
        LogMethod("更新知识", 2);
        DataCollect data = createKnowledge("updateKnowledge", null);
        data.getKnowledgeDetail().setTitle("updateKnowledge_update");
        requestJson = KnowledgeUtil.writeObjectToJson(assoFilter, data);
        try {
            HttpRequestFullJson(HttpMethod.PUT, baseUrl, requestJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void deleteKnowledge()
    {
        LogMethod("删除知识: /{knowledgeId}/{columnId}", 2);
        try {
            Knowledge detail = createKnowledge("deleteKnowledge", null).getKnowledgeDetail();
            String subUrl = "/" + detail.getId() + "/" + detail.getColumnType(); ///{id}/{columnId}
            HttpRequestFullJson(HttpMethod.DELETE, baseUrl + subUrl, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void knowledgeDetail()
    {
        LogMethod("知识详情: /{knowledgeId}/{columnId}", 2);
        try {
            Knowledge detail = createKnowledge("knowledgeDetail", null).getKnowledgeDetail();
            long knowledgeId = detail.getId();
            String columnType = detail.getColumnType();
            String subUrl = "/" + knowledgeId + "/" + columnType;  ///{knowledgeId}/{columnId}
            HttpRequestFullJson(HttpMethod.GET, baseUrl + subUrl, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void allKnowledgeList()
    {
        LogMethod("所有知识: /all/{start}/{size}/{keyword}", 2);
        try {
            String subUrl = "/all/0/10/null"; ////all/{start}/{size}
            HttpRequestFullJson(HttpMethod.GET, baseUrl + subUrl, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void allCreatedKnowledgeList()
    {
    	LogMethod("所有创建知识: /allCreated/{start}/{size}/{keyword}", 2);
        try {
            String subUrl = "/allCreated/0/10/null";
            HttpRequestFullJson(HttpMethod.GET, baseUrl + subUrl, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void allCollectedKnowledgeList()
    {
    	LogMethod("所有收藏知识: /allCollected/{start}/{size}/{keyword}", 2);
        try {
            String subUrl = "/allCollected/0/10/null"; ////all/{start}/{size}
            HttpRequestFullJson(HttpMethod.GET, baseUrl + subUrl, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void allByColumnIdKnowledgeList()
    {
        LogMethod("根据栏目提取知识: /allByColumn/{columnId}/{start}/{size}", 2);
        try {
            String subUrl = "/all/2/0/12"; ///all/{columnId}/{start}/{size}
            HttpRequestFullJson(HttpMethod.GET, baseUrl + subUrl, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void allByKeyWordKnowledgeList()
    {
        LogMethod("根据关键字提取知识: /allByKeyword/{keyWord}/{start}/{size}", 2);
        try {
            String subUrl = "/allByKeyword/test/0/3"; //
            HttpRequestFullJson(HttpMethod.GET, baseUrl + subUrl, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void allByColumnIdAndKeyWordKnowledgeList()
    {
        LogMethod("根据栏目关键字提取知识: /allByColumnAndKeyword/{columnId}/{keyWord}/{start}/{size}", 2);
        try {
            String subUrl = "/allByKeywordAndColumn/test/2/0/12"; ///all/{columnId}/{start}/{size}/{keyWord}
            HttpRequestFullJson(HttpMethod.GET, baseUrl + subUrl, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void allByUserIdKnowledgeList()
    {
        LogMethod("提取当前用户的所有知识: /user/{start}/{size}", 2);
        try {
            String subUrl = "/user/0/2"; ///user/{start}/{size}
            HttpRequestFullJson(HttpMethod.GET, baseUrl + subUrl, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void knowledgeListByUserIdAndColumnId()
    {
        LogMethod("根据栏目提取当前用户的知识: /user/{columnId}/{start}/{size}", 2);
        try {
            String subUrl = "/user/2/0/12";  //user/{columnId}/{start}/{size}
            HttpRequestFullJson(HttpMethod.GET, baseUrl + subUrl, null);
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
            HttpRequestFullJson(HttpMethod.DELETE, baseUrl + subUrl, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void reportKnowledge()
    {
        LogMethod("举报知识: /report/{knowledgeId}/{columnId}", 2);
        // "/report/{knowledgeId}/{columnId}"
        try {
            DataCollect data = createKnowledge("reportKnowledge", null);
            Knowledge detail = data.getKnowledgeDetail();
            long knowledgeId = detail.getId();
            int columnType = KnowledgeUtil.parserColumnId(detail.getColumnType());
            String subUrl = "/report" + knowledAndColumnIdUrl(knowledgeId, columnType);
            KnowledgeReport report = TestData.knowledgeReport(userId, knowledgeId, columnType);
            String knowledgeJson = KnowledgeUtil.writeObjectToJson(report);
            HttpRequestFullJson(HttpMethod.POST, baseUrl + subUrl, knowledgeJson);
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
            JsonNode result = HttpRequestResult(HttpMethod.POST, baseUrl + subUrl, "[3956219358478388, 3956238736162890, 3956186739376159]");
            BaseTestCase.checkRequestResultSuccess(result);
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
            JsonNode result = HttpRequestResult(HttpMethod.POST, baseUrl + subUrl, "[3933417670705167, 3933423765028884, 3933423777611801]");
            BaseTestCase.checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testGetKnowledgeRelatedResources()
    {
        LogMethod("获取推荐知识", 2);
        try {
            String subUrl = "/knowledgeRelated/4/1/12/test";  //user/{columnId}/{start}/{size}
            JsonNode result = HttpRequestResult(HttpMethod.GET, baseUrl + subUrl, null);
            BaseTestCase.checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /////////////////////////////======Comment=============////////////////////////////
    private String collectKnowledge(String title)
    {
        String subUrl = "/collect" + knowledAndColumnIdUrl(title);// "/collect/{knowledgeId/{columnId}"
        try {
        	HttpRequestFullJson(HttpMethod.POST, baseUrl + subUrl, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return subUrl;
    }

    private String knowledAndColumnIdUrl(String testCase)
    {
        DataCollect data = createKnowledge(testCase, null);
        long knowledgeId = data.getKnowledgeDetail().getId();
        int columnId = KnowledgeUtil.parserColumnId(data.getKnowledgeDetail().getColumnType());

        return "/" + knowledgeId + "/" + columnId;
    }

    private String knowledAndColumnIdUrl(long knowledgeId, int columnType)
    {
        return  "/" + knowledgeId + "/" + columnType;
    }

    private String createKnowledgeRetJson(String title, String knowledgeJson)
    {
        String response = null;
        try {
            knowledgeJson = knowledgeJson == null ? createKnowledgeRequestJson(title) : knowledgeJson;
            response = HttpRequestFullJson(HttpMethod.POST, baseUrl, knowledgeJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    private String createKnowledgeRequestJson(String title)
    {
        DataCollect data = TestData.getDataCollect(userId, (short) 2, title);
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
            HttpRequestFullJson(HttpMethod.GET, URL, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void knowledgeCommentGetCount()
    {
        LogMethod("评论数量: /count/{KnowledgeId}", 2);
        try {
            createKnowledgeComment("knowledgeCommentGetCount", false);

            String URL = baseCommentUrl + "count/" + KnowledgeId;
            HttpRequestFullJson(HttpMethod.GET, URL, null);
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

            String URL = baseCommentUrl + commentId;
            HttpRequestFullJson(HttpMethod.DELETE, URL, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String createKnowledgeComment(String content,boolean debug)
    {
        try {
            String URL = baseCommentUrl + KnowledgeId;
            String jsonContent = BaseTestCase.getKnowledgeComment(KnowledgeId, content);
            return HttpRequestFullJson(HttpMethod.POST, URL, jsonContent);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static void pusUpdate()
    {
        try {
            login();
            String subUrl = "/mobileApp/updateVersion.json";
            String content = "{\"version\":231,\"updateUrl\":\"http://file.gintong.com/app/gintong.apk\"}";

            JsonNode result = null;
            if ("dev".equals(testEnv)) {
                result = HttpRequestResult(HttpMethod.POST, "http://192.168.120.135:8008/cross" + subUrl, content);
            }
            else if ("testOnline".equals(testEnv)) {
                    result = HttpRequestResult(HttpMethod.POST, "http://test.online.gintong.com/cross" + subUrl, content);
            }
            else if ("online".equals(testEnv)) {
                result = HttpRequestResult(HttpMethod.POST, "http://www.gintong.com/cross" + subUrl, content);
            }
            checkRequestResultSuccess(result);
        }catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    ///////////////////////=======Log Request/Response Message=======//////////////////////
    private static String LogMethod(String content, int stackLevel)
    {
        String methodName = Thread.currentThread().getStackTrace()[stackLevel].getMethodName();
        System.out.println("\r\n======= "+content + " " + methodName+" ========");
        return methodName;
    }

//    private String HttpRequestFullJson(String httpMethod,String urlString,String jsonContent)
//    {
//        try {
//            return BaseTestCase.HttpRequestFullJson(httpMethod, urlString, jsonContent);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public JsonNode HttpRequestResult(String httpMethod,String urlString,String jsonContent) throws Exception
//    {
//        return BaseTestCase.HttpRequestResult(httpMethod, urlString, jsonContent);
//    }

    public static JsonNode HttpRequestFull(String httpMethod,String urlString,String jsonContent) throws Exception
    {
        return BaseTestCase.HttpRequestFull(httpMethod, urlString, jsonContent);
    }


}
