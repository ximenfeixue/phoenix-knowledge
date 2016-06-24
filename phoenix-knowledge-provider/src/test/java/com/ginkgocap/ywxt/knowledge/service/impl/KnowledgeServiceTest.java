package com.ginkgocap.ywxt.knowledge.service.impl;

import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.model.DataCollection;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeBase;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeDetail;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeReference;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeOtherService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeService;
import com.ginkgocap.ywxt.knowledge.utils.TestData;
import com.gintong.frame.util.dto.InterfaceResult;
import junit.framework.TestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


public class KnowledgeServiceTest extends TestBase {

    @Autowired
	private KnowledgeService knowledgeService;

    @Autowired
    KnowledgeOtherService knowledgeOtherService;

    private static long knowledgeIdDelete = 0L;
    private static long knowledgeIdupdate = 0L;
    private static long knowledgeIdQuery = 0L;
    private static long Id = 0L;
    private static short columnId = 2;
    private static short type = 0;
    private static int start = 1;
    private static int size = 12;

    @Test
	public void testInsert() {
        System.out.println("===testInsert===");
        createKnowledge("KnowledgeServiceTest");
    }

    @Test
	public void testUpdate() {
        System.out.println("===testUpdate===");
        try {
            DataCollection dataCollection = createKnowledge("Update-KnowledgeServiceTest");
            KnowledgeBase knowledge = dataCollection.generateKnowledge();

            knowledge.setContent("Update-Content-KnowledgeServiceTest");
            knowledge.setModifyDate(System.currentTimeMillis());
            KnowledgeReference reference = dataCollection.getReference();
            if (reference != null) {
                reference.setKnowledgeId(knowledgeIdupdate);
                reference.setArticleName("update_article_name");
                reference.setModifyDate(System.currentTimeMillis());
            }
            dataCollection.serUserId(userId);
            InterfaceResult<DataCollection> updateData = knowledgeService.update(dataCollection);
            assertResponseNoRetData(updateData);
        } catch (Exception e) {
            e.printStackTrace();
            TestCase.fail();
        }
	}

    @Test
	public void testDeleteByKnowledgeId() {
        System.out.println("===testDeleteByKnowledgeId===");
        try {
            DataCollection dataCollection = this.createKnowledge("test-delete-KnowledgeServiceTest");
            KnowledgeBase knowledge = dataCollection.getKnowledge();
            InterfaceResult result = knowledgeService.deleteByKnowledgeId(knowledge.getKnowledgeId(), knowledge.getColumnId());
            assertResponseWithData(result);
        } catch (Exception e) {
            e.printStackTrace();
            TestCase.fail();
        }
    }

    @Test
	public void testDeleteByKnowledgeIds() {
        System.out.println("===testDeleteByKnowledgeIds===");

        DataCollection data = null;
        List<Long> knowledgeIds = new ArrayList<Long>(5);
        for (int index = 1; index <= 5; index++) {
            data = this.createKnowledge("test-delete-KnowledgeServiceTest_"+index);
            knowledgeIds.add(data.getKnowledgeDetail().getId());
        }
        short columnId = data != null ? data.getKnowledgeDetail().getColumnId() :  2;
        try {
            InterfaceResult result = knowledgeService.batchDeleteByKnowledgeIds(knowledgeIds, columnId);
            assertResponseWithData(result);
        } catch (Exception e) {
            e.printStackTrace();
            TestCase.fail();
        }
    }

    @Test
	public void testGetDetailById() {
        System.out.println("===testGetDetailById===");
        try {
            DataCollection data = this.createKnowledge("KnowledgeServiceTest_testGetDetailById");
            long knowledgeId = data.getKnowledgeDetail().getId();
            short columnId = data.getKnowledgeDetail().getColumnId();
            KnowledgeDetail result = knowledgeService.getDetailById(knowledgeId, columnId);
            TestCase.assertNotNull(result);
        } catch (Exception e) {
            e.printStackTrace();
            TestCase.fail();
        }
    }

    @Test
	public void testGetBaseById() {
        System.out.println("===testGetBaseById===");
        DataCollection data = this.createKnowledge("KnowledgeServiceTest_"+ "testGetBaseById");
        try {
            InterfaceResult<DataCollection> result = knowledgeService.getBaseById(data.getKnowledgeDetail().getId());
            assertResponseWithData(result);
        } catch (Exception e) {
            e.printStackTrace();
            TestCase.fail();
        }
    }

    @Test
	public void testGetBaseByIds() {
        System.out.println("===testGetBaseByIds===");
        List<Long> knowledgeIds = new ArrayList<Long>(1);
        DataCollection data = this.createKnowledge("KnowledgeServiceTest_"+ "testGetBaseById");
        try {
            knowledgeIds.add(data.getKnowledgeDetail().getId());
            System.out.println("----knowledgeIds :"+knowledgeIds.toString());
            List<KnowledgeBase> result = knowledgeService.getBaseByIds(knowledgeIds);
            checkListResult(result);
        } catch (Exception e) {
            e.printStackTrace();
            TestCase.fail();
        }
	}

    @Test
	public void testGetBaseAll() {
        System.out.println("===testGetBaseAll===");
        try {
            int start = 1;
            int end = 12;
            List<KnowledgeBase> result = knowledgeService.getBaseAll(start, end);
            checkListResult(result);
        } catch (Exception e) {
            e.printStackTrace();
            TestCase.fail();
        }
    }

    @Test
	public void testGetBaseByCreateUserId() {
        System.out.println("===testGetBaseByCreateUserId===");
        try {
            userId = 201035L; start = 1; size = 10;
            List<KnowledgeBase> result = knowledgeService.getBaseByCreateUserId(userId, start, size);
            checkListResult(result);
        } catch (Exception e) {
            e.printStackTrace();
            TestCase.fail();
        }
    }

    @Test
	public void testGetBaseByCreateUserIdAndColumnId()
    {
        System.out.println("===testGetBaseByCreateUserIdAndColumnId===");
        try {
            List<KnowledgeBase> result = knowledgeService.getBaseByCreateUserIdAndColumnId(userId, columnId, start, size);
            checkListResult(result);
        } catch (Exception e) {
            e.printStackTrace();
            TestCase.fail();
        }
    }

    @Test
	public void testGetBaseByCreateUserIdAndType()
    {
        System.out.println("===testGetBaseByCreateUserIdAndType===");
        try {
            List<KnowledgeBase> result = knowledgeService.getBaseByCreateUserIdAndColumnIdAndType(userId, columnId, type, start, size);
            checkListResult(result);
        } catch (Exception e) {
            e.printStackTrace();
            TestCase.fail();
        }
    }

    @Test
	public void testGetBaseByCreateUserIdAndColumnIdAndType()
    {
        System.out.println("===testGetBaseByCreateUserIdAndColumnIdAndType===");
        try {
            List<KnowledgeBase> result = knowledgeService.getBaseByCreateUserIdAndColumnIdAndType(userId, columnId, type, start, size);
            checkListResult(result);
        } catch (Exception e) {
            e.printStackTrace();
            TestCase.fail();
        }
    }

    @Test
	public void testGetBaseByType()
    {
        System.out.println("===testGetBaseByType===");
        try {
            List<KnowledgeBase> result = knowledgeService.getBaseByType(type, start, size);
            checkListResult(result);
        } catch (Exception e) {
            e.printStackTrace();
            TestCase.fail();
        }
    }

    @Test
	public void testGetBaseByColumnId()
    {
        System.out.println("===testGetBaseByColumnId===");
        try {
            List<KnowledgeBase> result = knowledgeService.getBaseByColumnId(columnId, start, size);
            checkListResult(result);
        } catch (Exception e) {
            e.printStackTrace();
            TestCase.fail();
        }
    }

    @Test
    public void testGetBaseByColumnIdAndKeyWord()
    {
        System.out.println("===testGetBaseByColumnIdAndKeyWord===");
        try {
            List<KnowledgeBase> result = knowledgeService.getBaseByColumnIdAndKeyWord("test", columnId, start, size);
            checkListResult(result);
        } catch (Exception e) {
            e.printStackTrace();
            TestCase.fail();
        }
    }

    @Test
	public void testGetBaseByColumnIdAndType()
    {
        System.out.println("===testGetBaseByColumnIdAndType===");
        try {
            InterfaceResult<List<DataCollection>> result = knowledgeService.getBaseByColumnIdAndType(columnId, type, start, size);
            //checkListResult(result);
        } catch (Exception e) {
            e.printStackTrace();
            TestCase.fail();
        }
    }


    @Test
    public void testGetAllByTagId()
    {
        System.out.println("===testGetBaseByColumnIdAndType===");
        try {
            long tagId = 3985603448537134L;
            List<KnowledgeBase> result = knowledgeService.getBaseByTagId(tagId, start, size);
            checkListResult(result);
        } catch (Exception e) {
            e.printStackTrace();
            TestCase.fail();
        }
    }

    /*
    @Test
    public void testGetAllByDirectoryId()
    {
        System.out.println("===testGetAllByDirectoryId===");
        try {
            long tagId = 3985603448537134L;
            List<KnowledgeBase> result = knowledgeService.getBaseByDirectoryId(1L, tagId, start, size);
            checkListResult(result);
        } catch (Exception e) {
            e.printStackTrace();
            TestCase.fail();
        }
    }*/

    @org.junit.Test
    public void testBatchTags()
    {
        System.out.println("===testGetAllByDirectoryId===");
        String jsonStr = "[{\"id\":1112323,\"title\":\"testBatchTags\",\"tagIds\":[3933811561988102,3933811356467203]},{\"id\":1112345,\"title\":\"testBatchTags\",\"tagIds\":[3933811561988102,3933811356467203]}]";
        try {
            knowledgeOtherService.batchTags(7L, jsonStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private DataCollection createKnowledge(String title)
    {
        DataCollection dataCollection = null;
        try {
            dataCollection = TestData.dataCollection(userId, title);
            InterfaceResult result = knowledgeService.insert(dataCollection);
            assertResponseWithData(result);
            long knowledgeId = (Long)result.getResponseData();
            dataCollection.getKnowledgeDetail().setId(knowledgeId);
        } catch (Exception e) {
            e.printStackTrace();
            TestCase.fail();
        }

        return dataCollection;
    }
	
}