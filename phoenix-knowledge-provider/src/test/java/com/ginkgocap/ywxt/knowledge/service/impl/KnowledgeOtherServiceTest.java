package com.ginkgocap.ywxt.knowledge.service.impl;

import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeCollect;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeOtherService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeService;
import com.gintong.frame.util.dto.InterfaceResult;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by gintong on 2016/8/17.
 */
public class KnowledgeOtherServiceTest extends TestBase
{
    @Autowired
    private KnowledgeOtherService knowledgeOtherService;

    private final long userId = 7L;

    @Test
    public void testCollectKnowledge()
    {
        long knowledgeId = System.currentTimeMillis();
        try {
            InterfaceResult result = knowledgeOtherService.collectKnowledge(userId, knowledgeId, 1, (short) 1);
            Assert.assertTrue("0".equals(result.getNotification().getNotifCode()));
            System.out.println(result.getResponseData());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetCollectKnowledgeCount()
    {
        try {
            long collectedCount = knowledgeOtherService.myCollectKnowledgeCount(userId);
            Assert.assertTrue(collectedCount > 0);
            System.out.println("collectedCount : "+collectedCount);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testGetCollectKnowledgeList()
    {
        try {
            final String keyWord = null;
            List<KnowledgeCollect> collectList = knowledgeOtherService.myCollectedKnowledgeByPage(userId, -1L, -1, 0, 10, keyWord);
            Assert.assertTrue(collectList != null && collectList.size() >0);
            System.out.println("Collected size: " + collectList.size());
            for (KnowledgeCollect collect : collectList) {
                StringBuffer content = new StringBuffer();
                content.append("collect: Id: ").append(collect.getId());
                content.append(" ownerId: ").append(collect.getOwnerId());
                content.append(" KnowledgeId: ").append(collect.getKnowledgeId());
                content.append(" columnType: ").append(collect.getColumnId());
                content.append(" title: ").append(collect.getKnowledgeTitle());
                System.out.println(String.format(content.toString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
