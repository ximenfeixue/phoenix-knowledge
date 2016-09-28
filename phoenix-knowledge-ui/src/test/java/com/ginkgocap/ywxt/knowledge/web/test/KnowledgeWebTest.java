package com.ginkgocap.ywxt.knowledge.web.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.ginkgocap.parasol.directory.model.Directory;
import com.ginkgocap.parasol.tags.model.Tag;
import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeReport;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeUtil;
import com.ginkgocap.ywxt.knowledge.model.common.DataCollect;
import com.ginkgocap.ywxt.knowledge.model.common.IdType;
import com.ginkgocap.ywxt.knowledge.model.common.ResItem;
import com.ginkgocap.ywxt.knowledge.utils.TestData;

public class KnowledgeWebTest extends BaseTestCase
{
    private final String testTitle = "世界就是数据，一切皆可相连，人与人、人与物、物与物互联互通";

    public void testCreateKnowledge() throws InterruptedException
    {
        LogMethod();
        createKnowledge(testTitle);
    }
    
    public void testCreateKnowledgeWithTag()
    {
    	LogMethod();
    	List<Long> idList = getTagList();
    	if ((idList != null && idList.size() > 0)) {
    		createKnowledgeWithTag("testCreateKnowledgeWithTag", idList.subList(0, 1));
    	}
    }
    
    public void testCreateKnowledgeWithDirectory()
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

    /*
    public void testCreateKnowledgeMore() throws InterruptedException
    {
        LogMethod();
        long begin = System.currentTimeMillis();
        for (int index = 0; index <100; index++) {
            createKnowledge(testTitle);
            System.out.print("Index: "+index);
        }
        long end = System.currentTimeMillis();
        System.out.println("Total Time: " + (end-begin));
    }*/

    public void testUpdateKnowledge()
    {
        LogMethod();
        try {
            DataCollect data = createKnowledge("KnowledgeWebTest_create");
            data.getKnowledgeDetail().setTitle("KnowledgeWebTest_Update");
            //data.getKnowledgeDetail().setTagList(getTagList());
            //data.getKnowledgeDetail().setDirectorys(getDirectoryList());
            String knowledgeJson = KnowledgeUtil.writeObjectToJson(assoFilter, data);
            JsonNode result = HttpRequestResult(HttpMethod.PUT, baseUrl, knowledgeJson);
            checkRequestResultSuccess(result);
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
            String subUrl = "/" + detail.getId() + "/" + detail.getColumnType(); ///delete/{id}/{columnId}
            JsonNode result = HttpRequestResult(HttpMethod.DELETE, baseUrl + subUrl, null);
            checkRequestResultSuccess(result);
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
            JsonNode result = HttpRequestResult(HttpMethod.PUT, baseUrl + subUrl, knowledgeIds);
            checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testBatchDeleteKnowKnowledge()
    {
        LogMethod();
        try {
            Knowledge detail1 = createKnowledge("KnowledgeWebTest_testBatchDeleteKnowledge1").getKnowledgeDetail();
            Knowledge detail2 = createKnowledge("KnowledgeWebTest_testBatchDeleteKnowledge2").getKnowledgeDetail();
            List<IdType> batchIds = new ArrayList<IdType>();
            batchIds.add(new IdType(detail1.getId(), KnowledgeUtil.parserColumnId(detail1.getColumnType())));
            batchIds.add(new IdType(detail2.getId(), KnowledgeUtil.parserColumnId(detail2.getColumnType())));
            final String knowledgeIds = KnowledgeUtil.writeObjectToJson(batchIds);
            
            String subUrl = "/batchDeleteKnow"; ///delete/{id}/{columnId}
            JsonNode result = HttpRequestResult(HttpMethod.PUT, baseUrl + subUrl, knowledgeIds);
            checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }


    public void testKnowledgeDetail()
    {
        LogMethod();
        try {
            //Knowledge detail = createKnowledge("KnowledgeWebTest_testKnowledgeDetail").getKnowledgeDetail();
            long knowledgeId = 31609281629003L; //detail.getId();
            int columnId = 1; //KnowledgeUtil.parserColumnId(detail.getColumnid());
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
            //Knowledge detail = createKnowledgeWithTagAndDirectory("KnowledgeWebTest_testKnowledgeDetail").getKnowledgeDetail();
            long knowledgeId = 31609281629003L; //detail.getId();
            int columnId = 1; //KnowledgeUtil.parserColumnId(detail.getColumnid());
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
            createKnowledge("考虑,考虑");
            String subUrl = "/all/0/10/null"; // + URLEncoder.encode("考虑", "UTF-8"); ////all/{start}/{size}/{keyword}
            //String urlStr =
            JsonNode result = HttpRequestFull(HttpMethod.GET, baseUrl + subUrl, null);
            checkResponseWithData(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testGetKnowledgeByColumnAndSourceOne()
    {
        LogMethod();
        try {
            //createKnowledge("考虑,考虑1");
            //createKnowledge("考虑,考虑2");
            String subUrl = "/allKnowledgeByColumnAndSource/1/1/2/1/10/-1"; //allKnowledgeByColumnAndSource/{type}{columnId}/{source}/{page}/{size}/{total}
            JsonNode result = HttpRequestFull(HttpMethod.GET, baseUrl + subUrl, null);
            checkResponseWithData(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
    
    public void testGetKnowledgeByColumnAndSource_All()
    {
        LogMethod();
        try {
        	List<Integer> coulmnIds = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 3018, 3019, 3020, 3021, 3022, 3023, 3024, 3025);
            for (int index = 0; index <coulmnIds.size(); index++) {
            	if (index == 8) {
            		continue;
            	}
            	System.out.println("type: " + index);
	            String subUrl = "/allKnowledgeByColumnAndSource" + String.format("/%d/%d/2/0/10/-1", index+1, coulmnIds.get(index)); ///allKnowledgeByColumnAndSource/{type}{columnId}/{source}/{page}/{size}/{total}
	            JsonNode result = HttpRequestFull(HttpMethod.GET, baseUrl + subUrl, null);
	            checkResponseWithData(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
    
    public void testGetKnowledgeByColumnAndSource()
    {
        LogMethod();
        try {
            String subUrl = "/allKnowledgeByColumnAndSource/11/11/2/1/20/-1"; ///allKnowledgeByColumnAndSource/{type}{columnId}/{source}/{page}/{size}/{total}
            //String urlStr =
            JsonNode result = HttpRequestFull(HttpMethod.GET, baseUrl + subUrl, null);
            checkResponseWithData(result);

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
    

    public void testGetKnowledgeByColumnAndSource_TestAllColumn()
    {
        LogMethod();
        try {
            //createKnowledge("考虑,考虑1");
            //createKnowledge("考虑,考虑2");
        	for (int index = 1; index < 12; index++ ) {
        		if (index != 9) {
		            String subUrl = "/allKnowledgeByColumnAndSource/" + index + "/" + index + "/2/1/20/-1"; ///allKnowledgeByColumnAndSource/{type}{columnId}/{source}/{page}/{size}/{total}
		            //String urlStr =
		            JsonNode result = HttpRequestFull(HttpMethod.GET, baseUrl + subUrl, null);
		            checkResponseWithData(result);
        		}
        	}
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
    
    public void testAllByPage()
    {
        LogMethod();
        try {
            String subUrl = "/allByPage/0/20/-1/null"; // /page/allCollected/{num}/{size}/{total}/{keyword}
            //String urlStr =
            JsonNode result = HttpRequestFull(HttpMethod.GET, baseUrl + subUrl, null);
            checkResponseWithData(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
    
    
	public void testKnowledgeCount() {
		LogMethod();
		try {
			String subUrl = "/my/count";
			JsonNode result = HttpRequestFull(HttpMethod.GET, baseUrl + subUrl, null);
			checkResponseWithData(result);
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
            createKnowledge("考虑,考虑");
            String subUrl = "/allCreated/0/10/考虑"; // + URLEncoder.encode("考虑", "UTF-8"); ////all/{start}/{size}/{keyword}
            //String urlStr =
            JsonNode result = HttpRequestFull(HttpMethod.GET, baseUrl + subUrl, null);
            checkResponseWithData(result);
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
            JsonNode result = HttpRequestFull(HttpMethod.GET, baseUrl + subUrl, null);
            checkResponseWithData(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
    
    public void testAllCollectedKnowledge()
    {
        LogMethod();
        try {
            String subUrl = "/allCollected/0/10/一切皆可"; // + URLEncoder.encode("考虑", "UTF-8"); ////all/{start}/{size}/{total}/{keyword}
            JsonNode result = HttpRequestFull(HttpMethod.GET, baseUrl + subUrl, null);
            checkResponseWithData(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
    
    public void testAllCollectedKnowledgeByPage()
    {
        LogMethod();
        try {
            //createKnowledge("考虑,考虑");
            //createKnowledge("考虑,考虑");
            String subUrl = "/allCollectedByPage/0/10/-1/null"; // + URLEncoder.encode("考虑", "UTF-8"); ////all/{start}/{size}/{total}/{keyword}
            //String urlStr =
            JsonNode result = HttpRequestFull(HttpMethod.GET, baseUrl + subUrl, null);
            checkResponseWithData(result);
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
            JsonNode result = HttpRequestResult(HttpMethod.GET, baseUrl + subUrl, null);
            checkRequestResultSuccess(result);
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
            JsonNode result = HttpRequestResult(HttpMethod.GET, baseUrl + subUrl, null);
            checkRequestResultSuccess(result);
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
            JsonNode result = HttpRequestResult(HttpMethod.GET, baseUrl + subUrl, null);
            checkRequestResultSuccess(result);
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
            JsonNode result = HttpRequestResult(HttpMethod.GET, baseUrl + subUrl, null);
            checkRequestResultSuccess(result);
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
            JsonNode result = HttpRequestResult(HttpMethod.GET, baseUrl + subUrl, null);
            checkRequestResultSuccess(result);
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
            //JsonNode result = HttpRequestResult(HttpMethod.GET, baseUrl+subUrl, null);
            //checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testCollectKnowledge()
    {
        LogMethod();
        collectKnowledge("KnowledgeWebTest_testKnowledgeCollect"+testTitle);
    }


    public void testCancelCollectedKnowledge()
    {
        LogMethod();
        try {
            String subUrl = collectKnowledge("KnowledgeWebTest_testCancelCollectedKnowledge");// "/collect/{knowledgeId/{columnId}"
            JsonNode result = HttpRequestResult(HttpMethod.DELETE, baseUrl + subUrl, null);
            checkRequestResultSuccess(result);
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
            JsonNode result = HttpRequestResult(HttpMethod.POST, baseUrl + subUrl, knowledgeJson);
            checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }


    public void testBatchTags()
    {
        LogMethod();
        try {
        	int batchSize = 2;
            List<Long> tagIds = getTagList();
            if (tagIds == null || tagIds.size() <= 0) {
            	for (int index = 0; index < batchSize; index++) {
            		tagIds = new ArrayList<Long>(batchSize);
	            	long tagId = createDirectory();
	            	tagIds.add(tagId);
            	}
            }
 
            if (tagIds.size() > 2) {
            	tagIds = tagIds.subList(0, 1);
            }
            List<ResItem> resItems = createBatchItem("KnowledgeWebTest_testBatchDirectorys", batchSize, tagIds);
            String requestJson = KnowledgeUtil.writeObjectToJson(resItems);
            JsonNode result = HttpRequestFull(HttpMethod.POST, baseUrl + "/batchTags", requestJson);
            checkResponseWithData(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

    }
    
    public void testBatchDirectorys()
    {
        LogMethod();
        try {
        	int batchSize = 2;
            List<Long> directoryIds = getDirectoryList();
            if (directoryIds == null || directoryIds.size() <= 0) {
            	for (int index = 0; index < batchSize; index++) {
	            	directoryIds = new ArrayList<Long>(batchSize);
	            	long directoryId = createDirectory();
	            	directoryIds.add(directoryId);
            	}
            }
 
            List<ResItem> resItems = createBatchItem("KnowledgeWebTest_testBatchDirectorys", batchSize, directoryIds);
            String requestJson = KnowledgeUtil.writeObjectToJson(resItems);
            JsonNode result = HttpRequestResult(HttpMethod.POST, baseUrl + "/batchCatalogs", requestJson);
            checkRequestResultSuccess(result);
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
            JsonNode result = HttpRequestResult(HttpMethod.POST, baseUrl + subUrl, "[3973605390287002, 3973607483244706]");
            checkRequestResultSuccess(result);
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
//            List<Long> tags = this.getTagList();
//            if (tags == null || tags.size() <= 0) {
//                System.err.println("can't get tags");
//                fail();
//            }
            JsonNode result = HttpRequestResult(HttpMethod.POST, baseUrl + subUrl, "[113, 177, 178]");
            checkRequestResultSuccess(result);
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
        	long tagId = 0L;
        	List<Long> idList = getTagList();
        	if (idList == null || idList.size() <= 0) {
	            tagId = createTag();
	        	idList = Arrays.asList(tagId);
	        	createKnowledgeWithTag("testGetAllByDirectoryId", idList);
        	}
        	else {
        		createKnowledgeWithTag("testGetAllByDirectoryId", idList.subList(0, 1));
        		tagId = idList.get(0);
        	}
            String subUrl = "/tag/" +tagId + "/0/10";  ///tag/{tagId}/{start}/{size}
            JsonNode result = HttpRequestFull(HttpMethod.GET, baseUrl + subUrl, null);
            checkResponseWithData(result);
            //String jsonContent = getResponseData(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
    
    public void testGetDirectoryList()
    {
    	LogMethod();
    	this.getDirectoryList();
    }

    public void testGetAllByDirectoryId()
    {
        LogMethod();
        try {
        	long directoryId = 3991724208947840L;
        	List<Long> idList = getDirectoryList();
        	if ((idList != null && idList.size() > 0)) {
        		createKnowledgeWithDirectoy("testGetAllByDirectoryId", idList.subList(0, 1));
        		directoryId = idList.get(0);
        	} else {
                System.err.println("can't get directory");
                fail();
            }
            String subUrl = "/byDirectory/" + directoryId + "/0/5";  ///directory/{directoryId}/{start}/{size}
            JsonNode result = HttpRequestResult(HttpMethod.GET, baseUrl + subUrl, null);
            checkRequestResultSuccess(result);
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
            JsonNode result = HttpRequestResult(HttpMethod.POST, baseUrl + subUrl, "[3933423765028884, 3933423777611801]");
            checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }


    public void testGetDirectoryCountByIds()
    {
        LogMethod();
        try {
            String subUrl = "/directoryCount";
            List<Long> ids = this.getDirectoryList();
            if (ids != null && ids.size() > 0) {
	            String directoryIds = this.getDirectoryList().toString();
	            directoryIds = "[" + directoryIds.substring(1, directoryIds.length()-1) + "]";
	            JsonNode result = HttpRequestResult(HttpMethod.POST, baseUrl + subUrl, "[3933423765028884, 3933423777611801]");
	            checkRequestResultSuccess(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        //ResItem resItem = TestData.getResItems("testBatchCatalogs", );
    }

    public void testGetKnowledgeRelatedResourcesOld()
    {
        LogMethod();
        try {
            String subUrl = "/knowledgeRelated.json";  //user/{columnId}/{start}/{size}
            JsonNode result = HttpRequestResult(HttpMethod.POST, baseUrl + subUrl, null);
            checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
    
    public void testGetKnowledgeRelatedResources()
    {
        LogMethod();
        try {
        	//createKnowledge("科技首页");
        	//createKnowledge("科技首页,科技首页");
            String subUrl = "/knowledgeRelated/2/0/12/科技";  //user/{columnId}/{start}/{size}
            JsonNode result = HttpRequestResult(HttpMethod.GET, baseUrl + subUrl, null);
            checkRequestResultSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
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
        
    private String collectKnowledge(String title)
    {
        String subUrl = "/collect" + knowledAndColumnIdUrl(title);// "/collect/{knowledgeId/{columnId}"
        try {
            JsonNode result = HttpRequestResult(HttpMethod.POST, baseUrl + subUrl, null);
            checkRequestResultSuccess(result);
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
    
    private void knowledgeDetail(final String baseUrl, long knowledgeId,int columnId) throws Exception
    {
        String subUrl = "/" + knowledgeId + "/" + columnId;  //{id}/{columnId}
        JsonNode result = HttpRequestFull(HttpMethod.GET, baseUrl + subUrl, null);
        System.out.println("Result: " + result);
        checkResponseWithData(result);
    }
    
    private void knowledgeDetailWeb(final String baseUrl, long knowledgeId,int columnId) throws Exception
    {
        String subUrl = "/web/" + knowledgeId + "/" + columnId;  ///{id}/{columnId}
        JsonNode result = HttpRequestFull(HttpMethod.GET, baseUrl + subUrl, null);
        System.out.println("Result: " + result);
        checkResponseWithData(result);
    }

    private long createTag()
    {
        long tagId = 0L;
        try {
            String subUrl = "/createTag/Tag" + getNextNum(); ///createTag/{tagName}
            JsonNode result = HttpRequestFull(HttpMethod.GET, baseUrl + subUrl, null);
            String retData = getResponseData(result);
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
            JsonNode result = HttpRequestFull(HttpMethod.GET, baseUrl + subUrl, null);
            String retData = getResponseData(result);
            Assert.assertFalse("null".equals(retData));
            System.out.println("tagId: " + retData);
            id = Long.parseLong(retData);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        return id;
    }
    
    private void createBatchTag(long knowledgeId,List<Long> tagIds)
    {
    	List<ResItem> resItems = createBatchItem("testBatchTags", 2, tagIds);
        String requestJson = KnowledgeUtil.writeObjectToJson(resItems);
        JsonNode result;
		try {
			result = HttpRequestFull(HttpMethod.POST, baseUrl + "/batchTags", requestJson);
			checkResponseWithData(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private List<ResItem> createBatchItem(String title, int size, List<Long> tagIds)
    {
    	if (size > 5) {
    		size = 5;
    	}
    	List<ResItem> resItems = new ArrayList<ResItem>(size);
    	for (int index = 0; index < 5; index++) {
    		Knowledge detail =  createKnowledge(title).getKnowledgeDetail();
    		long knowledgeId = detail.getId();
    		short type = KnowledgeUtil.parserShortType(detail.getColumnType());
    		ResItem item = TestData.getResItems(knowledgeId, type, tagIds);
    		resItems.add(item);
    	}
    	return resItems;
    }
    
    private List<Long> getTagList()
    {
        List<Long> IdList = null;
        try {
            String subUrl = "/getTagList"; ///createTag/{tagName}
            JsonNode result = HttpRequestFull(HttpMethod.GET, baseUrl + subUrl, null);
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
            String subUrl = "/getDirectoryList"; ///directoryList
            JsonNode result = HttpRequestFull(HttpMethod.GET, baseUrl + subUrl, null);
            String directoryContent = getResponseData(result);
            if (directoryContent != null && directoryContent.trim().length() > 0) {
            	List<JsonNode> directoryList  = KnowledgeUtil.readListValue(JsonNode.class, directoryContent);
            	if (directoryList != null && directoryList.size() > 0) {
	            	IdList = new ArrayList<Long>(directoryList.size());
	            	for (JsonNode node : directoryList) {
	            		JsonNode idNode = node.get("id");
	            		if (idNode != null) {
			            	long id = idNode.asLong();	            	
			            	IdList.add(id);
	            		}
	            	}
            	}
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        return IdList;
    }

    private List<Long> getIdList(JsonNode result)
    {
        checkResponseWithData(result);
        String idsJson = getResponseData(result);
        return (List<Long>)KnowledgeUtil.readValue(List.class, idsJson);
    }
    
    private List<Long> convertTagIdList(JsonNode result)
    {
        checkResponseWithData(result);
        String idsJson = getResponseData(result);
        if (idsJson != null && idsJson.trim().length() > 0) {
	        //List<Tag> tagList = KnowledgereadListValue(Tag.class, idsJson);
	        TypeReference javaType = new TypeReference<List<Tag>>(){};
	        List<Tag> tagList = KnowledgeUtil.readValue(javaType, idsJson);
	        if (tagList != null && tagList.size() > 0) {
	            List<Long> tagIds  = new ArrayList<Long>(tagList.size());
	            for (Tag tag : tagList) {
	                tagIds.add(tag.getId());
	            }
	            return tagIds;
	        }
        }
        return null;
    }
    
    private List<Long> convertDirectoryIdList(JsonNode result)
    {
        checkResponseWithData(result);
        String idsJson = getResponseData(result);
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
}