package com.ginkgocap.ywxt.knowledge.service.impl;

import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeCount;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeCountService;
import junit.framework.TestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by gintong on 2016/7/22.
 */
public class KnowledgeCountServiceTest extends TestBase {

    @Autowired
    KnowledgeCountService knowledgeCountService;

    @Test
    public void testGetKnowledgeCount()
    {
        List<KnowledgeCount> countList = knowledgeCountService.getHotKnowledge(20);
        TestCase.assertTrue(countList != null && countList.size() > 0);
        System.out.println("Size: " + countList.size());
    }

    @Test
    public void testGetKnowledgeCountByPage()
    {
        List<KnowledgeCount> countList = knowledgeCountService.getHotKnowledgeByPage(0, 20);
        TestCase.assertTrue(countList != null && countList.size() > 0);
        System.out.println("Size: " + countList.size());
    }
}
