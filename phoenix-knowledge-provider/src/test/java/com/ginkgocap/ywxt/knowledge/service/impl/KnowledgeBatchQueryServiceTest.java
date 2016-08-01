package com.ginkgocap.ywxt.knowledge.service.impl;

import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeBatchQueryService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeHomeService;
import junit.framework.TestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by gintong on 2016/7/22.
 */
public class KnowledgeBatchQueryServiceTest extends TestBase {

    @Autowired
    private KnowledgeBatchQueryService knowledgeBatchQueryService;

    @Test
    public void testGetKnowledgeCountByUserIdAndColumnID() {
        String[] columnIds = new String[]{"12", "13", "14"};
        long count = knowledgeBatchQueryService.getKnowledgeCountByUserIdAndColumnID(columnIds, 0, (short) 1);
        System.out.println("---Count: "+count);
        TestCase.assertTrue(count > 0);
    }

    @Test
    public void testGetKnowledge()
    {
        String[] columnIds = new String[]{"12", "13", "14"};
        List<Knowledge> knowledgeList = knowledgeBatchQueryService.getKnowledge(columnIds, 0, (short)1, 0, 20);
        System.out.println("---knowledgeList: "+knowledgeList);
        TestCase.assertTrue(knowledgeList != null && knowledgeList.size() > 0);
    }

    @Test
    public void testGetAllByParam()
    {
        List<Knowledge> knowledgeList = knowledgeBatchQueryService.getAllByParam((short)1, 1, "资讯", 0, 0, 30);
        TestCase.assertTrue(knowledgeList != null && knowledgeList.size() > 0);
        System.out.println("---knowledgeList: "+knowledgeList.size());
    }
}
