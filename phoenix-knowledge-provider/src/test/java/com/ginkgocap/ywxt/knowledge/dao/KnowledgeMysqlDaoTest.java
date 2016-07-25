package com.ginkgocap.ywxt.knowledge.dao;

import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeBase;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeUtil;
import com.ginkgocap.ywxt.knowledge.model.common.DataCollect;
import com.ginkgocap.ywxt.knowledge.utils.TestData;
import junit.framework.TestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by gintong on 2016/7/22.
 */
public class KnowledgeMysqlDaoTest extends TestBase {

    @Autowired
    KnowledgeMysqlDao knowledgeMysqlDao;

    @Test
    public void testGetAll()
    {
        try {
            for (int index = 0; index <1044; index++) {
                System.err.println("Index: " + index);
                List<KnowledgeBase> baseList = knowledgeMysqlDao.getAll(0, 20);
                System.err.println("Size: " + baseList.size());
                String jsContent = KnowledgeUtil.writeObjectToJson(baseList);
                System.out.println(jsContent);
                TestCase.assertTrue(baseList != null && baseList.size() > 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInsert()
    {
        try {
            for(int index = 0; index <10; index++) {
                DataCollect data = TestData.getDataCollect(1234567L, 1, "testInsert");
                data.generateKnowledge();
                KnowledgeBase base = data.getKnowledge();
                base.setId(System.currentTimeMillis());
                base.setKnowledgeId(System.currentTimeMillis());
                KnowledgeBase newBase = knowledgeMysqlDao.insert(data.getKnowledge());
                System.out.println(newBase);
                TestCase.assertTrue(newBase != null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
