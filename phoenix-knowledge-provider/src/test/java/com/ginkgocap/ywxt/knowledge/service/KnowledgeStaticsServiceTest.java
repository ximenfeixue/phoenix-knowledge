package com.ginkgocap.ywxt.knowledge.service;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeStatics;
import com.ginkgocap.ywxt.knowledge.service.knowledgecategory.KnowledgeCategoryService;
import com.ginkgocap.ywxt.knowledge.service.knowledgestatics.KnowledgeStaticsService;

/**
 * 知识测试类
 * 
 * @author caihe
 * 
 */

public class KnowledgeStaticsServiceTest extends TestBase {

    public KnowledgeStaticsServiceTest() {
        System.out.println(123456);
    }

    @Autowired
    private KnowledgeStaticsService knowledgeStaticsService;

    @Autowired
    private KnowledgeCategoryService knowledgeCategoryService;

    @Test
    public void testinsertKnowledgeR() {

        KnowledgeStatics knowledgeStatics = new KnowledgeStatics();
        knowledgeStatics.setKnowledgeid(53);
        //		knowledgeStatics.setCollectionCount(0);
        //		knowledgeStatics.setCommentCount(0);
        //		knowledgeStatics.setShareCount(0);
        //		knowledgeStatics.setClickCount(0);
        knowledgeStaticsService.insertKnowledgeStatics(knowledgeStatics);
    }

    //测试获取评论排行列表
    @Test
    public void testSelectRankList() {
        List<KnowledgeStatics> l = knowledgeStaticsService.selectRankList(1);
        for (KnowledgeStatics k : l) {
            System.out.println(k.getCommentCount());
        }
    }

}
