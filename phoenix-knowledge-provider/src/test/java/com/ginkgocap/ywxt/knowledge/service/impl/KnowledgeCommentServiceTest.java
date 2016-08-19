package com.ginkgocap.ywxt.knowledge.service.impl;

import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeComment;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeCount;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeCommentService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeCountService;
import com.ginkgocap.ywxt.knowledge.utils.TestData;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by gintong on 2016/8/13.
 */
public class KnowledgeCommentServiceTest extends TestBase {

    @Autowired
    KnowledgeCommentService knowledgeCommentService;

    private final long userId = 1234567L;
    private final long knowledgeId = 0L;
    private final int columnId = 1;

    @Test
    public void testGetKnowledgeCommentList()
    {
        List<KnowledgeComment> commentList = knowledgeCommentService.getKnowledgeCommentList(839017L);
        TestCase.assertTrue(commentList != null && commentList.size() > 0);
        System.out.println("Size: " + commentList.size());
        for (KnowledgeComment comment : commentList) {
            System.out.println("id: " + comment.getId() + " Time: " + comment.getCreateTime() + " comment: " + comment.getContent());
        }
    }

    @Test
    public void testAddKnowledgeComment()
    {
        KnowledgeComment comment = TestData.knowledgeComment(userId, knowledgeId, columnId, "");
        long commentId = knowledgeCommentService.create(comment);
        TestCase.assertTrue(commentId > 0);
        System.out.println("commentId: " + commentId);
    }

    @Test
    public void testDeleteKnowledgeComment()
    {
        boolean result = knowledgeCommentService.delete(0L, userId);
        Assert.assertFalse(result);
    }
}
