package com.ginkgocap.ywxt.knowledge.service.impl;

import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeComment;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeCount;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeCommentService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeCountService;
import junit.framework.TestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by gintong on 2016/8/13.
 */
public class KnowledgeCommentServiceTest  extends TestBase {

    @Autowired
    KnowledgeCommentService knowledgeCommentService;

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
}
