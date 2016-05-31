package com.ginkgocap.ywxt.knowledge.dao;

import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeBase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2016/5/31.
 */
public class KnowledgeBaseDaoTest extends TestBase {

    @Autowired
    private KnowledgeMysqlDao knowledgeMysqlDao;

    @Test
    public void testInsertKnowledgeBase()
    {

    }

    @Test
    public void testGetKnowledgeBaseByKnowledgeIds()
    {
        List<Long> ids = new ArrayList<Long>(3);
        ids.add(3961321892872197L);
        ids.add(3961547701616645L);
        ids.add(3961551602319365L);
        try {
            List<KnowledgeBase> knowledgeBaseList = knowledgeMysqlDao.getByKnowledgeIds(ids);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
