package com.ginkgocap.ywxt.knowledge.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeStatics;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeIndustry;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNews;

/**
 * 知识首页测试用例
 */

public class KnowledgeHomeServiceTest extends TestBase {
    @Autowired
    private KnowledgeHomeService knowledgeHomeService;

    //获取首页列表
    @Test
    public void testGetHomeList() {
        int state=0;
        String columnid="12";
        Long userid = 1l;
        knowledgeHomeService.selectAllByParam(new KnowledgeNews(), 0, 1 + "", 10132l, 1, 10);
       System.out.println(1);
    }

    //获取排行列表
    @Test
    public void testGetRankList() {
        List<KnowledgeStatics> l=knowledgeHomeService.getRankHotList(1l);
        System.out.println(1);
    }
//
//    //获取分类列表
//    @Test
//    public void testGetTypeList() {
//        knowledgeHomeService.getTypeList(0, 0);
//    }
    //知识与人的关系
    @Test
    public void beRelation() {
//        System.out.println(knowledgeHomeService.beRelation(1,1,2, 10132));
    }
    @Test
    public void testGetTypeList() {
        knowledgeHomeService.selectAllKnowledgeCategoryByParam("1", "", 0, "000000007", 10132l, "",1, 1);
    }

    @Test
    public void selectAllByParam() {
        knowledgeHomeService.selectAllByParam(new KnowledgeNews(), 0, 51 + "", 10132l, 0, 20);
        knowledgeHomeService.selectAllByParam(new KnowledgeNews(), 0, 51 + "", 10132l, 20, 20);
        knowledgeHomeService.selectAllByParam(new KnowledgeNews(), 0, 51 + "", 10132l, 40, 20);
        knowledgeHomeService.selectAllByParam(new KnowledgeNews(), 0, 51 + "", 10132l, 60, 20);

        
    }
    @Test
    public void selectKnowledgeCategoryForImport() {
        List<Long> l=new ArrayList<Long>();
        l.add(1l);
        knowledgeHomeService.selectKnowledgeCategoryForImport(10132l, l, 1, 20);
    }
    @Test
    public void selectPlatform() {
    	knowledgeHomeService.selectRecommendedKnowledge(36l, 1, 20);
    }
    
    @Test
    public void updateStar() {
    	List<Long> knowledgeIds =Arrays.asList(25229L,27001L);
    	knowledgeHomeService.addUserStar(knowledgeIds, 36, 1);
    }
    

}
