package com.ginkgocap.ywxt.knowledge.dao;

import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeBase;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeType;
import junit.framework.TestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by oem on 8/17/17.
 */
public class KnowledgeIndexDaoTest extends TestBase {

    @Autowired
    private KnowledgeIndexDao knowledgeIndexDao;

    @Test
    public void testGetAllPublicKnowledge() {
        List<KnowledgeBase> baseList = knowledgeIndexDao.getAllPublicByPage((short)KnowledgeType.EMacro.value(), (short)0, null, 0, 200);
        TestCase.assertNotNull(baseList);
    }
}
