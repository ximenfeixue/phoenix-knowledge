package com.ginkgocap.ywxt.knowledge.service.impl;

import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeCollect;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeOtherService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeService;
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
    public void testGetCollectKnowledgeList()
    {
        try {
            List<KnowledgeCollect> collectList = knowledgeOtherService.myCollectKnowledge(userId, 0, 0, 10);
            Assert.assertTrue(collectList != null && collectList.size() >0);
            for (KnowledgeCollect collect : collectList) {
                String content = new StringBuffer().append("collect: Id: ").append(collect.getId()).append(" ownerId: ")
                        .append(collect.getOwnerId()).append("source: ").append(collect.getSource()).toString();
                System.out.println(String.format(content));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
