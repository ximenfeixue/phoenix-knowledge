package com.ginkgocap.ywxt.knowledge.dao;

import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.model.common.DataCollect;
import com.ginkgocap.ywxt.knowledge.utils.TestData;
import junit.framework.TestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Admin on 2016/6/1.
 */
public class KnowledgeMongoTest extends TestBase {

    private static long userId = 1234567L;
    private static String dataBase = "knowledge-test";

    @Autowired
    private KnowledgeMongoDao knowledgeMongoDao;

    @Test
    public void testInsertKnowledge()
    {
        DataCollect data = TestData.getDataCollect(userId, (short) 2, "insertKnowledge");
        Knowledge detail = data.getKnowledgeDetail();
        detail.setId(System.currentTimeMillis());
        Knowledge newDetail = null;
        try {
            newDetail = knowledgeMongoDao.insert(detail);
        } catch (Exception e) {
            e.printStackTrace();
        }
        TestCase.assertNotNull(newDetail);
    }

    public static void updateKnowledge(Knowledge detail)
    {
    }

}
