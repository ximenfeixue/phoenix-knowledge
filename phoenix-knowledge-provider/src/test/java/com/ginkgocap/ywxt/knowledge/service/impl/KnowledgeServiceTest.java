package com.ginkgocap.ywxt.knowledge.service.impl;

import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeUtil;
import com.ginkgocap.ywxt.knowledge.model.common.DataCollect;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeBase;
import com.ginkgocap.ywxt.knowledge.model.common.KnowledgeReference;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeService;
import com.ginkgocap.ywxt.knowledge.service.common.KnowledgeCommonService;
import com.ginkgocap.ywxt.knowledge.utils.TestData;
import com.gintong.frame.util.dto.InterfaceResult;
import junit.framework.TestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class KnowledgeServiceTest extends TestBase {

    @Autowired
	private KnowledgeService knowledgeService;

    @Autowired
    KnowledgeCommonService knowledgeCommonService;

    private long userId = 200562;
    private long knowledgeId = 317030311004641L;
    private short type = 1;

    private static long knowledgeIdDelete = 0L;
    private static long knowledgeIdupdate = 0L;
    private static long knowledgeIdQuery = 0L;
    private static long Id = 0L;
    private static short columnId = 2;
    private static int start = 1;
    private static int size = 12;

    @Test
	public void testInsert() {
        System.out.println("===testInsert===");
        DataCollect collect = createKnowledge("KnowledgeServiceTest");
        String data = KnowledgeUtil.writeObjectToJson(collect.getKnowledgeDetail());
        System.out.print(data);
    }

    @Test
    public void testInsertDetail() {
        System.out.println("===testInsertDetail===");
        DataCollect dataCollect = TestData.getDataCollect(userId, 1, "testInsertDetail");
        Knowledge detail = dataCollect.getKnowledgeDetail();
        Knowledge savedDetail = this.insertKnowledgeDetail(detail);
        //detail.setTitle("testInsertDetail_Update");
        //savedDetail = this.insertKnowledgeDetail(detail);
    }

    @Test
    public void testAddTag() {
        System.out.println("===testAddTag===");
        try {
            final List<Long> ids = Arrays.asList(333L, 444L);
            knowledgeService.addTag(userId, knowledgeId, type, ids);
        } catch (Exception ex) {
            ex.printStackTrace();
            TestCase.fail();
        }
    }

    @Test
    public void testAddDirectory() {
        System.out.println("===testAddDirectory===");
        try {
            final List<Long> ids = Arrays.asList(333L, 444L);
            knowledgeService.addDirectory(userId, knowledgeId, type, ids);
        } catch (Exception ex) {
            ex.printStackTrace();
            TestCase.fail();
        }
    }

    @Test
	public void testUpdate() {
        System.out.println("===testUpdate===");
        try {
            DataCollect dataCollect = createKnowledge("Update-KnowledgeServiceTest");
            Knowledge detail = dataCollect.getKnowledgeDetail();

            detail.setColumnType("2");
            detail.setContent("Update-Content-KnowledgeServiceTest");
            detail.setModifytime(String.valueOf(System.currentTimeMillis()));
            KnowledgeReference reference = dataCollect.getReference();
            if (reference != null) {
                reference.setKnowledgeId(knowledgeIdupdate);
                reference.setArticleName("update_article_name");
                reference.setModifyDate(System.currentTimeMillis());
            }
            dataCollect.setOldType(1);
            dataCollect.serUserId(userId);
            InterfaceResult<Knowledge> updateData = knowledgeService.update(dataCollect);
            assertResponseNoRetData(updateData);
            System.out.println(updateData);
        } catch (Exception e) {
            e.printStackTrace();
            TestCase.fail();
        }
	}

    @Test
	public void testDeleteByKnowledgeId()
    {
        System.out.println("===testDeleteByKnowledgeId===");
        try {
            DataCollect dataCollect = this.createKnowledge("test-delete-KnowledgeServiceTest");
            KnowledgeBase knowledge = dataCollect.getKnowledge();
            InterfaceResult result = knowledgeService.deleteByKnowledgeId(knowledge.getKnowledgeId(), knowledge.getColumnId());
            assertResponseWithData(result);
        } catch (Exception e) {
            e.printStackTrace();
            TestCase.fail();
        }
    }

    @Test
	public void testDeleteByKnowledgeIds()
    {
        System.out.println("===testDeleteByKnowledgeIds===");

        DataCollect data = null;
        List<Long> knowledgeIds = new ArrayList<Long>(5);
        for (int index = 1; index <= 5; index++) {
            data = this.createKnowledge("test-delete-KnowledgeServiceTest_"+index);
            knowledgeIds.add(data.getKnowledgeDetail().getId());
        }
        int columnId = data != null ? Integer.valueOf(data.getKnowledgeDetail().getColumnid()) :  2;
        try {
            InterfaceResult result = knowledgeService.batchDeleteByKnowledgeIds(knowledgeIds, columnId);
            assertResponseWithData(result);
        } catch (Exception e) {
            e.printStackTrace();
            TestCase.fail();
        }
    }

    @Test
    public void testDeleteTag() {
        System.out.println("===testDeleteTag===");
        try {
            final List<Long> ids = Arrays.asList(333L, 444L);
            knowledgeService.deleteTag(userId, knowledgeId, type, ids);
        } catch (Exception ex) {
            ex.printStackTrace();
            TestCase.fail();
        }
    }

    @Test
    public void testDeleteDirectory() {
        System.out.println("===testDeleteDirectory===");
        try {
            final List<Long> ids = Arrays.asList(333L, 444L);
            knowledgeService.deleteDirectory(userId, knowledgeId, type, ids);
        } catch (Exception ex) {
            ex.printStackTrace();
            TestCase.fail();
        }
    }

    @Test
	public void testGetDetailById() {
        System.out.println("===testGetDetailById===");
        try {
            //DataCollect data = this.createKnowledge("KnowledgeServiceTest_testGetDetailById");
            long knowledgeId = 2971163; //data.getKnowledgeDetail().getId();
            int columnId = 1; //Integer.valueOf(data.getKnowledgeDetail().getColumnid());
            Knowledge result = knowledgeService.getDetailById(knowledgeId, columnId);
            TestCase.assertNotNull(result);
        } catch (Exception e) {
            e.printStackTrace();
            TestCase.fail();
        }
    }

    @Test
	public void testGetBaseById() {
        System.out.println("===testGetBaseById===");
        DataCollect data = this.createKnowledge("KnowledgeServiceTest_"+ "testGetBaseById");
        try {
            InterfaceResult<DataCollect> result = knowledgeService.getBaseById(data.getKnowledgeDetail().getId());
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
        DataCollect data = this.createKnowledge("KnowledgeServiceTest_"+ "testGetBaseById");
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
            InterfaceResult<List<DataCollect>> result = knowledgeService.getBaseByColumnIdAndType(columnId, type, start, size);
            //checkListResult(result);
        } catch (Exception e) {
            e.printStackTrace();
            TestCase.fail();
        }
    }

    @Test
    public void testGetKnowledgeCount()
    {
        try {
            int size = knowledgeService.getKnowledgeCount(201003L);
            System.out.println("Count: " + size);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetKnowledgeSequenceId()
    {
        for (int index = 0; index <10000; index++) {
           long sequenceId = knowledgeCommonService.getKnowledgeSequenceId();
            System.out.println("sequenceId: " + sequenceId);
        }
    }

    /*
    @Test
    public void testGetAllByTagId()
    {
        System.out.println("===testGetBaseByColumnIdAndType===");
        try {
            long tagId = 3985603448537134L;
            List<KnowledgeBase> result = knowledgeService(tagId, start, size);
            checkListResult(result);
        } catch (Exception e) {
            e.printStackTrace();
            TestCase.fail();
        }
    }

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


    private DataCollect createKnowledge(String title)
    {
        DataCollect dataCollect = null;
        try {
            dataCollect = TestData.getDataCollect(userId, 1, title);
            InterfaceResult result = knowledgeService.insert(dataCollect);
            assertResponseWithData(result);
            long knowledgeId = (Long)result.getResponseData();
            dataCollect.getKnowledgeDetail().setId(knowledgeId);
        } catch (Exception e) {
            e.printStackTrace();
            TestCase.fail();
        }

        return dataCollect;
    }

    private Knowledge insertKnowledgeDetail(Knowledge detail)
    {
        try {
            return knowledgeService.insert(detail);
        } catch (Exception e) {
            e.printStackTrace();
            TestCase.fail();
        }
        return null;
    }
	
}