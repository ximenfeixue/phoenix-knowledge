package com.ginkgocap.ywxt.knowledge.service.impl;

import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeHomeService;
import junit.framework.TestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by gintong on 2016/7/22.
 */
public class KnowledgeHomeServiceTest extends TestBase {

    @Autowired
    private KnowledgeHomeService knowledgeHomeService;

    @Test
    public void testGetKnowledgeCountByUserIdAndColumnID() {
        String[] columnIds = new String[]{"12", "13", "14"};
        long count = knowledgeHomeService.getKnowledgeCountByUserIdAndColumnID(columnIds, 0, (short) 1);
        System.out.println("---Count: "+count);
        TestCase.assertTrue(count > 0);
    }

    @Test
    public void testGetKnowledge()
    {
        String[] columnIds = new String[]{"12", "13", "14"};
        List<Knowledge> knowledgeList = knowledgeHomeService.getKnowledge(columnIds, 0, (short)1, 0, 30);
        System.out.println("---knowledgeList: "+knowledgeList);
        TestCase.assertTrue(knowledgeList != null && knowledgeList.size() > 0);
    }
}
