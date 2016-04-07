package com.ginkgocap.ywxt.knowledge.dao;

import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeDetail;
import com.ginkgocap.ywxt.knowledge.testData.TestData;
import junit.framework.TestCase;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2016/4/7.
 */
public class KnowledgeMongoDaoTest  extends TestBase {
    @Resource
    private KnowledgeMongoDao knowledgeMongoDao;

    KnowledgeDetail updateKnowledgeDetail = null;

    @Test
    public void testInsert()
    {
        KnowledgeDetail knowledgeDetail = TestData.knowledgeDetail((short)2);
        try {
            updateKnowledgeDetail = knowledgeMongoDao.insert(knowledgeDetail);
            TestCase.assertNotNull(updateKnowledgeDetail);
        } catch (Exception e) {
            TestCase.fail();
            e.printStackTrace();
        }
    }

    @Test
    public void testInsertList() {
        List<KnowledgeDetail> knowledgeDetailList = new ArrayList<KnowledgeDetail>();
        knowledgeDetailList.add(TestData.knowledgeDetail((short)3));
        knowledgeDetailList.add(TestData.knowledgeDetail((short)4));
        try {
            List<KnowledgeDetail> knowledgeList = knowledgeMongoDao.insertList(knowledgeDetailList);
            TestCase.assertNotNull(knowledgeList);
        } catch (Exception e) {
            TestCase.fail();
            e.printStackTrace();
        }
    }



    public void testUpdate() {
        try {
            updateKnowledgeDetail.setTitle("Update-Title");
            updateKnowledgeDetail.setContent("Update-Content-Update");
            updateKnowledgeDetail.setModifyTime(System.currentTimeMillis());
            KnowledgeDetail knowledgeDetail = knowledgeMongoDao.update(updateKnowledgeDetail);
            TestCase.assertNotNull(knowledgeDetail);
        } catch (Exception e) {
            TestCase.fail();
            e.printStackTrace();
        }
    }


    public  void testInsertAfterDelete() {
        KnowledgeDetail knowledgeDetail = TestData.knowledgeDetail((short)3);
        try {
            KnowledgeDetail knowledge = knowledgeMongoDao.insertAfterDelete(knowledgeDetail, 1112L);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public  void testDeleteByIdAndColumnId() {
        long id = 1112L;
        short columnId = 2;
        try {
            int ret = knowledgeMongoDao.deleteByIdAndColumnId(id, columnId);
        } catch (Exception e) {
            TestCase.fail();
            e.printStackTrace();
        }
    }


    public void testDeleteByIdsAndColumnId() {
        List<Long> ids = new ArrayList<Long>();
        short columnId = 2;
        try {
            int ret = knowledgeMongoDao.deleteByIdsAndColumnId(ids, columnId);
        } catch (Exception e) {
            TestCase.fail();
            e.printStackTrace();
        }
    }



    public  void testDeleteByCreateUserIdAndColumnId() {
        long createUserId = 1112L;
        short columnId = 2;
        try {
            int ret = knowledgeMongoDao.deleteByCreateUserIdAndColumnId(createUserId, columnId);
        } catch (Exception e) {
            TestCase.fail();
            e.printStackTrace();
        }
    }


    public  void testGetByIdAndColumnId() {
        long id = 1112L;
        short columnId = 2;
        try {
            KnowledgeDetail knowledgeDetail = knowledgeMongoDao.getByIdAndColumnId(id, columnId);
        } catch (Exception e) {
            TestCase.fail();
            e.printStackTrace();
        }
    }


    public  void testGetByIdsAndColumnId()
    {
        List<Long> ids = new ArrayList<Long>();
        short columnId = 2;
        try {
            List<KnowledgeDetail> knowledgeDetailList = knowledgeMongoDao.getByIdsAndColumnId(ids, columnId);
        } catch (Exception e) {
            TestCase.fail();
            e.printStackTrace();
        }
    }
}
