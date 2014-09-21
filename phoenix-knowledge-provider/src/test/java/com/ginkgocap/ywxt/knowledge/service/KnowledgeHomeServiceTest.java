package com.ginkgocap.ywxt.knowledge.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeHomeService;

/**
 * 知识首页测试用例
 */

public class KnowledgeHomeServiceTest extends TestBase {
    @Autowired
    private KnowledgeHomeService knowledgeHomeService;

    //获取首页列表
    @Test
    public void testGetHomeList() {
        knowledgeHomeService.getHomeList(0, 0, 1, 10, 0);
        knowledgeHomeService.getRankList(0, 0);
        knowledgeHomeService.getTypeList(0, 0);
    }

    //获取排行列表
    @Test
    public void testGetRankList() {
        knowledgeHomeService.getRankList(0, 0);
    }

    //获取分类列表
    @Test
    public void testGetTypeList() {
        knowledgeHomeService.getTypeList(0, 0);
    }

}
