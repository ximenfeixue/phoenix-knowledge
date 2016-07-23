package com.ginkgocap.ywxt.knowledge.web.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.ginkgocap.parasol.directory.model.Directory;
import junit.framework.Assert;

import com.fasterxml.jackson.databind.JsonNode;
import com.ginkgocap.parasol.tags.model.Tag;
import com.ginkgocap.parasol.tags.model.TagSource;
import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeReport;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeUtil;
import com.ginkgocap.ywxt.knowledge.model.common.DataCollect;
import com.ginkgocap.ywxt.knowledge.model.common.ResItem;
import com.ginkgocap.ywxt.knowledge.utils.TestData;

public class KnowledgeWebTest extends BaseTestCase
{
    public final String baseUrl =  hostUrl + "/knowledge";

    public void testCreateKnowledge()
    {
        LogMethod();
        createKnowledge("如果谷歌是对的，苹果将在劫难逃！如果谷歌是对的");
    }
    
    public void testCreateKnowledgeWithTag()
    {
    	LogMethod();
    	List<Long> idList = getTagList();
    	if ((idList != null && idList.size() > 0)) {
    		createKnowledgeWithTag("testCreateKnowledgeWithTag", idList.subList(0, 1));
    	}
    }
    
    public void testCreateKnowledgeWithDirectoy()
    {
    	LogMethod();
    	List<Long> idList = getDirectoryList();
    	if ((idList != null && idList.size() > 0)) {
    		createKnowledgeWithDirectoy("testCreateKnowledgeWithTag", idList.subList(0, 1));
    	}
    }
    
    public void testCreateKnowledgeWithTagAndDirectory()
    {
    	LogMethod();
    	createKnowledgeWithTagAndDirectory("testCreateKnowledgeWithTagAndDirectory");
    }

    public void testUpdateKnowledge()
    {
        LogMethod();
        try {
            DataCollect data = createKnowledge("KnowledgeWebTest_create");
            data.getKnowledgeDetail().setTitle("KnowledgeWebTest_Update");
            data.getKnowledgeDetail().setTagList(getTagList());
            data.getKnowledgeDetail().setDirectorys(getDirectoryList());
            String knowledgeJson = KnowledgeUtil.writeObjectToJson(assofilterProvider, data);
            JsonNode result = Util.HttpRequestResult(Util.HttpMethod.PUT, baseUrl, knowledgeJson);
            Util.checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    
    public void testDeleteKnowledge()
    {
        LogMethod();
        try {
            Knowledge detail = createKnowledge("KnowledgeWebTest_testDeleteKnowledge").getKnowledgeDetail();
            String subUrl = "/" + detail.getId() + "/" + detail.getColumnid(); ///delete/{id}/{columnId}
            JsonNode result = Util.HttpRequestResult(Util.HttpMethod.DELETE, baseUrl+subUrl, null);
            Util.checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testBatchDeleteKnowledge()
    {
        LogMethod();
        try {
            Knowledge detail1 = createKnowledge("KnowledgeWebTest_testBatchDeleteKnowledge1").getKnowledgeDetail();
            Knowledge detail2 = createKnowledge("KnowledgeWebTest_testBatchDeleteKnowledge2").getKnowledgeDetail();
            String knowledgeIds = "[" + detail1.getId() + "," + detail2.getId() + "]";
            String subUrl = "/batchDelete"; ///delete/{id}/{columnId}
            JsonNode result = Util.HttpRequestResult(Util.HttpMethod.PUT, baseUrl+subUrl, knowledgeIds);
            Util.checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }


    public void testKnowledgeDetail()
    {
        LogMethod();
        try {
            Knowledge detail = createKnowledge("KnowledgeWebTest_testKnowledgeDetail").getKnowledgeDetail();
            //data.setContent("");
            long knowledgeId = detail.getId();
            int columnId = Integer.valueOf(detail.getColumnid());
            //String subUrl = "/" + knowledgeId + "/" + columnId;  ///{id}/{columnId}
            knowledgeDetail(baseUrl, knowledgeId, columnId);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
    
    public void testKnowledgeDetailWeb()
    {
        LogMethod();
        try {
        	
            Knowledge detail = createKnowledgeWithTagAndDirectory("KnowledgeWebTest_testKnowledgeDetail").getKnowledgeDetail();
            long knowledgeId = detail.getId();
            int columnId = Integer.valueOf(detail.getColumnid());
            //String subUrl = "/" + knowledgeId + "/" + columnId;  ///web/{knowledgeId}/{columnId}
            knowledgeDetailWeb(baseUrl, knowledgeId, columnId);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
    
    
    
    public void testMultiThreadKnowledgeDetail()
    {
        LogMethod();
        try {
        	Knowledge detail = createKnowledge("KnowledgeWebTest_testMultiThreadKnowledgeDetail").getKnowledgeDetail();
            final long knowledgeId = detail.getId();
            final int columnId = Integer.valueOf(detail.getColumnid());
            knowledgeDetail(baseUrl, knowledgeId, columnId);
            /*
        	for (int index = 0; index < 1; index++) {
        		new Thread(new Runnable() {

					@Override
					public void run() {
			            try {
							knowledgeDetail(baseUrl, knowledgeId, columnId);
							Thread.sleep(4);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}}).start();
        	}*/

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testAllKnowledge()
    {
        LogMethod();
        try {
            //createKnowledge("考虑,考虑");
            //createKnowledge("考虑,考虑");
            String subUrl = "/all/0/10/null"; // + URLEncoder.encode("考虑", "UTF-8"); ////all/{start}/{size}/{keyword}
            //String urlStr =
            JsonNode result = Util.HttpRequestFull(Util.HttpMethod.GET, baseUrl + subUrl, null);
            Util.checkResponseWithData(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
    
    public void testGetKnowledgeByColumnAndSource()
    {
        LogMethod();
        try {
            //createKnowledge("考虑,考虑1");
            //createKnowledge("考虑,考虑2");
            String subUrl = "/allKnowledgeByColumnAndSource/1/1/2/3/20/110"; ///allKnowledgeByColumnAndSource/{type}{columnId}/{source}/{page}/{size}/{total}
            //String urlStr =
            JsonNode result = Util.HttpRequestFull(Util.HttpMethod.GET, baseUrl + subUrl, null);
            Util.checkResponseWithData(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
    
    public void testAllKnowledgeByPage()
    {
        LogMethod();
        try {
            String subUrl = "/allByPage/0/100/369/null"; // /page/allCollected/{num}/{size}/{total}/{keyword}
            //String urlStr =
            JsonNode result = Util.HttpRequestFull(Util.HttpMethod.GET, baseUrl + subUrl, null);
            Util.checkResponseWithData(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
    
    
	public void testKnowledgeCount() {
		LogMethod();
		try {
			String subUrl = "/my/count";
			JsonNode result = Util.HttpRequestFull(Util.HttpMethod.GET, baseUrl+subUrl, null);
			Util.checkResponseWithData(result);
		} catch (Exception e) {
            e.printStackTrace();
            fail();
        }
	}

    public void testAllCreatedKnowledge()
    {
        LogMethod();
        try {
            //createKnowledge("考虑,考虑");
            //createKnowledge("考虑,考虑");
            String subUrl = "/allCreated/0/10/test"; // + URLEncoder.encode("考虑", "UTF-8"); ////all/{start}/{size}/{keyword}
            //String urlStr =
            JsonNode result = Util.HttpRequestFull(Util.HttpMethod.GET, baseUrl + subUrl, null);
            Util.checkResponseWithData(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
    
    public void testAllCreatedKnowledgeByPage()
    {
        LogMethod();
        try {
            //createKnowledge("考虑,考虑");
            //createKnowledge("考虑,考虑");
            String subUrl = "/allCreatedByPage/0/10/-1/null"; // + URLEncoder.encode("考虑", "UTF-8"); ////all/{start}/{size}/{keyword}
            //String urlStr =
            JsonNode result = Util.HttpRequestFull(Util.HttpMethod.GET, baseUrl + subUrl, null);
            Util.checkResponseWithData(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
    
    public void testAllCollectedKnowledge()
    {
        LogMethod();
        try {
            //createKnowledge("考虑,考虑");
            //createKnowledge("考虑,考虑");
            String subUrl = "/allCollected/0/10/null"; // + URLEncoder.encode("考虑", "UTF-8"); ////all/{start}/{size}/{total}/{keyword}
            //String urlStr =
            JsonNode result = Util.HttpRequestFull(Util.HttpMethod.GET, baseUrl + subUrl, null);
            Util.checkResponseWithData(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
    
    public void testAllCollectedByPageKnowledge()
    {
        LogMethod();
        try {
            //createKnowledge("考虑,考虑");
            //createKnowledge("考虑,考虑");
            String subUrl = "/allCollectedByPage/1/10/-1/null"; // + URLEncoder.encode("考虑", "UTF-8"); ////all/{start}/{size}/{total}/{keyword}
            //String urlStr =
            JsonNode result = Util.HttpRequestFull(Util.HttpMethod.GET, baseUrl + subUrl, null);
            Util.checkResponseWithData(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testAllByColumnId()
    {
        LogMethod();
        try {
            String subUrl = "/allByColumn/2/0/20"; ///all/{columnId}/{start}/{size}
            JsonNode result = Util.HttpRequestResult(Util.HttpMethod.GET, baseUrl + subUrl, null);
            Util.checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }


    public void testAllByKeyWord()
    {
        LogMethod();
        try {
            String subUrl = "/allByKeyword/考虑/0/3"; ///all/{keyWord}/{columnId}/{start}/{size}
            JsonNode result = Util.HttpRequestResult(Util.HttpMethod.GET, baseUrl + subUrl, null);
            Util.checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testAllByColumnIdAndKeyWord()
    {
        LogMethod();
        try {
            String subUrl = "/allByColumnAndKeyword/2/考虑/0/3"; ////allByKeywordAndColumn/test/2/1/12"
            JsonNode result = Util.HttpRequestResult(Util.HttpMethod.GET, baseUrl+subUrl, null);
            Util.checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }


    public void testAllByByUserId()
    {
        LogMethod();
        try {
            String subUrl = "/user/0/10"; ///user/{start}/{size}
            JsonNode result = Util.HttpRequestResult(Util.HttpMethod.GET, baseUrl+subUrl, null);
            Util.checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }


    public void testAllByUserIdAndColumnId()
    {
        LogMethod();
        try {
            String subUrl = "/user/2/0/12";  //user/{columnId}/{start}/{size}
            JsonNode result = Util.HttpRequestResult(Util.HttpMethod.GET, baseUrl+subUrl, null);
            Util.checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
    
    public void testAllNoDirectory()
    {
        LogMethod();
        try {
            String subUrl = "/allNoDirectory/0/12";  ///allNoDirectory/{start}/{size}
            JsonNode result = Util.HttpRequestResult(Util.HttpMethod.GET, baseUrl+subUrl, null);
            Util.checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testCollectKnowledge()
    {
        LogMethod();
        collectKnowledge("KnowledgeWebTest_testKnowledgeCollect");
    }


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


    public void testReportKnowledge()
    {
        LogMethod();
        DataCollect data = createKnowledge("KnowledgeWebTest_testReportKnowledge");
        // "/report/{knowledgeId}/{columnId}"
        try {
            long knowledgeId = data.getKnowledgeDetail().getId();
            int columnId = Integer.valueOf(data.getKnowledgeDetail().getColumnid());
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


    public void testBatchTags()
    {
        LogMethod();
        try {
        	//DataCollect data = createKnowledge("KnowledgeWebTest_testBatchTags");
            List<ResItem> resItems = new ArrayList<ResItem>(2);
            List<Long> tagIds = getTagList();
            if (tagIds == null || tagIds.size() <= 0) {
                System.out.println("No tag to added");
                fail();
            }
            long [] tagIdList = convertList(tagIds);
            long knowledgeId = createKnowledge("testBatchTags").getKnowledgeDetail().getId();
//            if (data != null ) {
//            	knowledgeId = data.getKnowledgeDetail().getId();
//            }
            ResItem resItem1 = TestData.getResItems("testBatchTags", knowledgeId, tagIdList);
            //ResItem resItem2 = TestData.getResItems("testBatchTags", 1112345L, tagIdList);
            resItems.add(resItem1);
            //resItems.add(resItem2);
            String requestJson = KnowledgeUtil.writeObjectToJson(resItems);

            /*
            ObjectMapper mapper = new ObjectMapper();
            TypeReference javaType = new TypeReference<List<ResItem>>() {};

            List<ResItem> tagItems =  KnowledgeUtil.readValue(javaType, requestJson);
            //List<ResItem> beanList = mapper.readValue(requestJson, new TypeReference<List<ResItem>>() {});

            for (ResItem tagItem : tagItems) {
                String title = tagItem.getTitle();
                long knowledgeId = tagItem.getId();
                List<Long> tagIds = tagItem.getTagIds();
                for (Long tagId : tagIds) {
                    System.out.print(tagId);
                }
            }*/
            //System.out.print(beanList);
            JsonNode result = Util.HttpRequestFull(Util.HttpMethod.POST, baseUrl + "/batchTags", requestJson);
            Util.checkResponseWithData(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

    }
    
    public void testMutilBatchTags()
    {
        LogMethod();
        try {
        	DataCollect data = createKnowledge("KnowledgeWebTest_testBatchTags");
            List<ResItem> resItems = new ArrayList<ResItem>(2);
            List<Long> tagIds = getTagList();
            if (tagIds == null || tagIds.size() <= 0) {
                System.err.println("No tag added");
                fail();
            }
            long [] tagIdList = convertList(tagIds);
            long knowledgeId = 0L;
            if (data != null ) {
            	knowledgeId = data.getKnowledgeDetail().getId();
            }
            
            for (int index = 0; index < 1; index ++) {
                long tagId = tagIds.get(index);
            	createBatchTag(knowledgeId, new long[]{tagId});
                Thread.sleep(3);
            }
            ResItem resItem1 = TestData.getResItems("testBatchTags", knowledgeId, tagIdList);
            resItems.add(resItem1);
            String requestJson = KnowledgeUtil.writeObjectToJson(resItems);
            JsonNode result = Util.HttpRequestFull(Util.HttpMethod.POST, baseUrl + "/batchTags", requestJson);
            Util.checkResponseWithData(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

    }


    public void testBatchCatalogs()
    {
        LogMethod();
        try {
        	DataCollect data = createKnowledge("KnowledgeWebTest_testBatchTags");
            List<ResItem> resItems = new ArrayList<ResItem>(2);
            List<Long> directoryIds = getDirectoryList();
            if (directoryIds == null || directoryIds.size() <= 0) {
                System.err.println("No directory added");
                fail();
            }
            long [] IdList = convertList(directoryIds);

            long knowledgeId = 0L;
            if (data != null ) {
            	knowledgeId = data.getKnowledgeDetail().getId();
            }
            ResItem resItem1 = TestData.getResItems("testBatchCatalogs", knowledgeId, IdList);
            //ResItem resItem2 = TestData.getResItems("testBatchCatalogs", 1112345L, IdList);
            resItems.add(resItem1);
            //resItems.add(resItem2);
            String requestJson = KnowledgeUtil.writeObjectToJson(resItems);
            JsonNode result = Util.HttpRequestResult(Util.HttpMethod.POST, baseUrl + "/batchCatalogs", requestJson);
            Util.checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }


    public void testGetTagsByIds()
    {
        LogMethod();
        try {
            String subUrl = "/tagList";
            List<Long> tags = this.getTagList();
            if (tags == null || tags.size() <= 0) {
                System.err.println("can't get tags");
                fail();
            }
            JsonNode result = Util.HttpRequestResult(Util.HttpMethod.POST, baseUrl + subUrl, "[3973605390287002, 3973607483244706]");
            Util.checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }


    public void testGetTagSourceCountByIds()
    {
        LogMethod();
        try {
            String subUrl = "/tagCount";
            List<Long> tags = this.getTagList();
            if (tags == null || tags.size() <= 0) {
                System.err.println("can't get tags");
                fail();
            }
            JsonNode result = Util.HttpRequestResult(Util.HttpMethod.POST, baseUrl+subUrl, "[3973605390287002, 3973607483244706]");
            Util.checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        //ResItem resItem = TestData.getResItems("testBatchCatalogs", );
    }


    public void testGetAllByTagId()
    {
        LogMethod();
        try {
        	long tagId = 3995008890044562L;
        	List<Long> idList = new ArrayList<Long>(); //createTag();
        	idList.add(tagId);
        	createKnowledgeWithTag("testGetAllByDirectoryId", idList);
        	/*if ((idList != null && idList.size() > 0)) {
        		createKnowledgeWithTag("testGetAllByDirectoryId", idList.subList(0, 1));
        		tagId = idList.get(0);
        	}*/
            String subUrl = "/tag/" +tagId + "/0/10";  ///tag/{tagId}/{start}/{size}
            JsonNode result = Util.HttpRequestResult(Util.HttpMethod.GET, baseUrl+subUrl, null);
            Util.checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testGetAllByDirectoryId()
    {
        LogMethod();
        try {
        	long directoryId = 3990305410121740L;
        	List<Long> idList = getDirectoryList();
        	if ((idList != null && idList.size() > 0)) {
        		createKnowledgeWithDirectoy("testGetAllByDirectoryId", idList.subList(0, 1));
        		directoryId = idList.get(0);
        	} else {
                System.err.println("can't get directory");
                fail();
            }
            String subUrl = "/byDirectory/" + directoryId + "/0/10";  ///directory/{directoryId}/{start}/{size}
            JsonNode result = Util.HttpRequestResult(Util.HttpMethod.GET, baseUrl+subUrl, null);
            Util.checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

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

    public void testGetKnowledgeRelatedResources()
    {
        LogMethod();
        try {
            String subUrl = "/knowledgeRelated/2/0/12/test";  //user/{columnId}/{start}/{size}
            JsonNode result = Util.HttpRequestResult(Util.HttpMethod.GET, baseUrl+subUrl, null);
            Util.checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
    
    public void testGetRecommendedKnowledge()
    {
        LogMethod();
        try {
        	//type: 1,推荐 2,发现
            String subUrl = "/getRecommendedKnowledge/2/0/12";  //getRecommendedKnowledge/{type}/{start}/{size}
            JsonNode result = Util.HttpRequestResult(Util.HttpMethod.GET, baseUrl+subUrl, null);
            Util.checkRequestResultSuccess(result);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
           
    }
    
    public void testCreateTag()
    {
    	LogMethod();
        createTag();
    }	
    
    public void testCreateDirectory()
    {
    	LogMethod();
        createDirectory();
    }
    
    public void testSeparate()
    {
    	LogMethod();
        try {
        	for (int type = 1; type <12; type ++) {
	            String subUrl = "home/separate/" + type;  ///home/separate/{type}
	            JsonNode result = Util.HttpRequestResult(Util.HttpMethod.GET, baseUrl+subUrl, null);
	            Util.checkRequestResultSuccess(result);
	        	}
        } catch (Exception ex) {
            ex.printStackTrace();
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
        DataCollect data = createKnowledge(testCase);
        long knowledgeId = data.getKnowledgeDetail().getId();
        int columnId = Integer.parseInt(data.getKnowledgeDetail().getColumnid());

        return "/" + knowledgeId + "/" + columnId;
    }

    private String knowledAndColumnIdUrl(long knowledgeId, int columnId)
    {
        return  "/" + knowledgeId + "/" + columnId;
    }
    
    private DataCollect createKnowledge(String title)
    {
    	return createKnowledge(title, null, null);
    }
    
    private DataCollect createKnowledgeWithTag(String title,List<Long> tagIds)
    {
    	return createKnowledge(title, tagIds, null);
    }
    
    private DataCollect createKnowledgeWithDirectoy(String title,List<Long> directoyIds)
    {
    	return createKnowledge(title, null, directoyIds);
    }
    
    private DataCollect createKnowledgeWithTagAndDirectory(String title)
    {
        return createKnowledge(title, getTagList(), getDirectoryList());
    }

    private DataCollect createKnowledge(String title,List<Long> tagIds,List<Long> directoryIds)
    {
        DataCollect data = TestData.getDataCollect(userId, 1, title);
        try {
            if (data != null && data.getKnowledgeDetail() != null) {
                data.getKnowledgeDetail().setTagList(tagIds);
                data.getKnowledgeDetail().setDirectorys(directoryIds);
                //data.getKnowledgeDetail().setContent(content);
                String knowledgeJson = KnowledgeUtil.writeObjectToJson(assofilterProvider, data);
                JsonNode response = Util.HttpRequestFull(Util.HttpMethod.POST, baseUrl, knowledgeJson);
                Util.checkResponseWithData(response);
                String retData = Util.getResponseData(response);
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
    
    private void knowledgeDetail(final String baseUrl, long knowledgeId,int columnId) throws Exception
    {
        String subUrl = "/" + knowledgeId + "/" + columnId;  //{id}/{columnId}
        JsonNode result = Util.HttpRequestFull(Util.HttpMethod.GET, baseUrl + subUrl, null);
        System.out.println("Result: " + result);
        Util.checkResponseWithData(result);
    }
    
    private void knowledgeDetailWeb(final String baseUrl, long knowledgeId,int columnId) throws Exception
    {
        String subUrl = "/web/" + knowledgeId + "/" + columnId;  ///{id}/{columnId}
        JsonNode result = Util.HttpRequestFull(Util.HttpMethod.GET, baseUrl + subUrl, null);
        System.out.println("Result: " + result);
        Util.checkResponseWithData(result);
    }

    private long createTag()
    {
        long tagId = 0L;
        try {
            String subUrl = "/createTag/Tag" + getNextNum(); ///createTag/{tagName}
            JsonNode result = Util.HttpRequestFull(Util.HttpMethod.GET, baseUrl + subUrl, null);
            String retData = Util.getResponseData(result);
            Assert.assertFalse("null".equals(retData));
            System.out.println("tagId: " + retData);
            tagId = Long.parseLong(retData);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        return tagId;
    }

    private long createDirectory()
    {
    	long id = 0L;
        try {
            String subUrl = "/createDirectory/Directory" + getNextNum(); ///createTag/(tagType)/{tagName}
            JsonNode result = Util.HttpRequestFull(Util.HttpMethod.GET, baseUrl + subUrl, null);
            String retData = Util.getResponseData(result);
            Assert.assertFalse("null".equals(retData));
            System.out.println("tagId: " + retData);
            id = Long.parseLong(retData);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        return id;
    }
    
    private void createBatchTag(long knowledgeId,long [] tagIds)
    {
    	List<ResItem> resItems = new ArrayList<ResItem>(1);
        ResItem resItem1 = TestData.getResItems("testBatchTags", knowledgeId, tagIds);
        resItems.add(resItem1);
        String requestJson = KnowledgeUtil.writeObjectToJson(resItems);
        JsonNode result;
		try {
			result = Util.HttpRequestFull(Util.HttpMethod.POST, baseUrl + "/batchTags", requestJson);
			Util.checkResponseWithData(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
    
    private List<Long> getTagList()
    {
        List<Long> IdList = null;
        try {
            String subUrl = "/getTagList"; ///createTag/{tagName}
            JsonNode result = Util.HttpRequestFull(Util.HttpMethod.GET, baseUrl + subUrl, null);
            IdList = convertTagIdList(result);
            Assert.assertTrue(IdList != null && IdList.size() > 0);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        return IdList;
    }

    private List<Long> getDirectoryList()
    {
        List<Long> IdList = null;
        try {
            String subUrl = "/createDirectory/Directory" + getNextNum(); ///createTag/(tagType)/{tagName}
            JsonNode result = Util.HttpRequestFull(Util.HttpMethod.GET, baseUrl + subUrl, null);
            IdList = convertDirectoryIdList(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        return IdList;
    }

    private List<Long> getIdList(JsonNode result)
    {
        Util.checkResponseWithData(result);
        String idsJson = Util.getResponseData(result);
        return (List<Long>)KnowledgeUtil.readValue(List.class, idsJson);
    }
    
    private List<Long> convertTagIdList(JsonNode result)
    {
        Util.checkResponseWithData(result);
        String idsJson = Util.getResponseData(result);
        List<Tag> tagList = KnowledgeUtil.readListValue(Tag.class, idsJson);
        if (tagList != null && tagList.size() > 0) {
            List<Long> tagIds  = new ArrayList<Long>(tagList.size());
            for (Tag tag : tagList) {
                tagIds.add(tag.getId());
            }
            return tagIds;
        }
        return null;
    }
    
    private List<Long> convertDirectoryIdList(JsonNode result)
    {
        Util.checkResponseWithData(result);
        String idsJson = Util.getResponseData(result);
        List<Directory> directoryList = KnowledgeUtil.readListValue(Directory.class, idsJson);
        if (directoryList != null && directoryList.size() > 0) {
            List<Long> ids  = new ArrayList<Long>(directoryList.size());
            for (Directory item : directoryList) {
                ids.add(item.getId());
            }
            return ids;
        }
        return null;
    }

    private long [] convertList(List<Long> tagIds)
    {
        long [] tagIdList = new long[tagIds.size()];
        for (int index = 0; index < tagIds.size(); index++) {
            tagIdList[index] = tagIds.get(index);
        }
        return tagIdList;
    }

    private int getNextNum()
    {
        int max=50;
        int min=1;
        Random random = new Random();

        return random.nextInt(max)%(max-min+1) + min;
    }
}