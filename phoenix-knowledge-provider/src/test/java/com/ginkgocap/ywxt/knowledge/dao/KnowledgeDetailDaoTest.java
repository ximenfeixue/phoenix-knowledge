package com.ginkgocap.ywxt.knowledge.dao;

import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeComment;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeDetail;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeUtil;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeCommentService;
import com.ginkgocap.ywxt.knowledge.service.common.KnowledgeCommonService;
import com.ginkgocap.ywxt.knowledge.utils.TestData;
import junit.framework.TestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chen Peifeng on 2016/4/7.
 */
public class KnowledgeDetailDaoTest extends TestBase {
    @Autowired
    private KnowledgeMongoDao knowledgeMongoDao;

    @Autowired
    private KnowledgeCommentService knowledgeCommentService;

    private static KnowledgeDetail updateKnowledgeDetail = null;

    @Test
    public void testInsert()
    {
        KnowledgeDetail knowledgeDetail = TestData.knowledgeDetail(userId, 2, "KnowledgeMongoDaoTest");
        try {
            knowledgeDetail.setCategoryIds(null);
            updateKnowledgeDetail = knowledgeMongoDao.insert(knowledgeDetail);
            TestCase.assertNotNull(updateKnowledgeDetail);
        } catch (Exception e) {
            e.printStackTrace();
            TestCase.fail();
        }
    }

    @Test
    public void testInsertList() {
        List<KnowledgeDetail> knowledgeDetailList = new ArrayList<KnowledgeDetail>(2);
        knowledgeDetailList.add(TestData.knowledgeDetail(userId, 3, "KnowledgeMongoDaoTest"));
        knowledgeDetailList.add(TestData.knowledgeDetail(userId, 4, "KnowledgeMongoDaoTest"));
        try {
            List<KnowledgeDetail> knowledgeList = knowledgeMongoDao.insertList(knowledgeDetailList);
            TestCase.assertNotNull(knowledgeList);
        } catch (Exception e) {
            e.printStackTrace();
            TestCase.fail();
        }
    }

    @Test
    public void testUpdate() {
        try {
            if (updateKnowledgeDetail == null) {
                updateKnowledgeDetail = createKnowledge(2);
            }
            if (updateKnowledgeDetail != null) {
                updateKnowledgeDetail.setTitle("Update-Title");
                updateKnowledgeDetail.setContent("Update-Content-Update");
                updateKnowledgeDetail.setModifyTime(System.currentTimeMillis());
            }
            KnowledgeDetail knowledgeDetail = knowledgeMongoDao.update(updateKnowledgeDetail);
            TestCase.assertNotNull(knowledgeDetail);
        } catch (Exception e) {
            e.printStackTrace();
            TestCase.fail();
        }
    }

    @Test
    public  void testInsertAfterDelete() {
        try {
            KnowledgeDetail knowledgeDetail = createKnowledge(2);
            KnowledgeDetail knowledge = knowledgeMongoDao.insertAfterDelete(knowledgeDetail);
            TestCase.assertNotNull(knowledge);
        } catch (Exception e) {
            TestCase.fail();
            e.printStackTrace();
        }
    }

    @Test
    public  void testDeleteByIdAndColumnId() {
        try {
            int columnId = 2;
            KnowledgeDetail knowledgeDetail = createKnowledge(columnId);
            long Id = knowledgeDetail.getId();
            int ret = knowledgeMongoDao.deleteByIdAndColumnId(Id, columnId);
            TestCase.assertTrue(ret==0);
        } catch (Exception e) {
            e.printStackTrace();
            TestCase.fail();
        }
    }

    @Test
    public void testDeleteByIdsAndColumnId() {
        try {
            int columnId = 2;
            List<Long> ids = createKnowledgeList(2, columnId);
            int ret = knowledgeMongoDao.deleteByIdsAndColumnId(ids, columnId);
            TestCase.assertTrue(ret==0);
        } catch (Exception e) {
            e.printStackTrace();
            TestCase.fail();
        }
    }

    @Test
    public  void testDeleteByCreateUserIdAndColumnId() {
        try {
            KnowledgeDetail knowledgeDetail = createKnowledge(2);
            long createUserId = knowledgeDetail.getOwnerId();
            int columnId = knowledgeDetail.getColumnId();
            int ret = knowledgeMongoDao.deleteByCreateUserIdAndColumnId(createUserId, columnId);
            TestCase.assertTrue(ret==0);
        } catch (Exception e) {
            e.printStackTrace();
            TestCase.fail();
        }
    }

    @Test
    public  void testGetByIdAndColumnId() {
        try {
            int columnId = 2;
            KnowledgeDetail knowledgeDetail = createKnowledge(columnId);
            long Id = knowledgeDetail.getId();
            KnowledgeDetail knowledge = knowledgeMongoDao.getByIdAndColumnId(Id, columnId);
            TestCase.assertNotNull(knowledge);
        } catch (Exception e) {
            e.printStackTrace();
            TestCase.fail();
        }
    }

    @Test
    public void testGetByIdsAndColumnId()
    {
        try {
            int columnId = 2;
            List<Long> ids = createKnowledgeList(3, columnId);
            List<KnowledgeDetail> knowledgeList = knowledgeMongoDao.getByIdsAndColumnId(ids, columnId);
            TestCase.assertTrue(knowledgeList != null && knowledgeList.size()==3);
        } catch (Exception e) {

            e.printStackTrace();
            TestCase.fail();
        }
    }

    @Test
    public void testGetCommentList()
    {
        List<KnowledgeComment> commentList = knowledgeCommentService.getKnowledgeCommentList(11111111L);
        String json = KnowledgeUtil.writeObjectToJson(commentList);
        System.out.println(json);
    }

    @Test
    public void testGetNoDirectory()
    {
        List<KnowledgeDetail> knowledgeDetailList = knowledgeMongoDao.getNoDirectory(userId, 10, 10);
        String json = KnowledgeUtil.writeObjectToJson(knowledgeDetailList);
        System.out.println(json);
    }

    private KnowledgeDetail createKnowledge(int columnId)
    {
        KnowledgeDetail createdKnowledge = null;
        try {
            KnowledgeDetail knowledgeDetail = TestData.knowledgeDetail(userId, columnId, "KnowledgeMongoDaoTest");
            createdKnowledge = knowledgeMongoDao.insert(knowledgeDetail);
            TestCase.assertNotNull(createdKnowledge);
        } catch (Exception e) {
            TestCase.fail();
            e.printStackTrace();
        }
        return createdKnowledge;
    }

    private List<Long> createKnowledgeList(int count,int columnId)
    {
        List<Long> ids = new ArrayList<Long>(count);
        for (int index = 0; index < count; ++index) {
            KnowledgeDetail knowledgeDetail = createKnowledge(columnId);
            if (knowledgeDetail != null) {
                ids.add(knowledgeDetail.getId());
            }
        }

        return ids;
    }
}
