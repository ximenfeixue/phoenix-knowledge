package com.ginkgocap.ywxt.knowledge.service;

import static org.junit.Assert.*; 

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeColumnSubscribe;
import com.ginkgocap.ywxt.knowledge.service.ColumnSubscribeService;

/** 
 * <p>订阅测试</p>  
 * @author guangyuan  
 * @since 1.2.2
 */
public class ColumnSubscribeServiceTest extends TestBase {

    public static long TEST_USER_ID = 71L;

    @Autowired
    ColumnSubscribeService kcsService;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
    
    @Test
    public void testFindSubKnowledge(){
    	System.out.println(kcsService.selectMySubscribe(1, "1", 1, 1, 10));
    }

    @Test
    @Rollback(false)
    public void testquery() {
        kcsService.selectRankList(6, 10132);
        kcsService.selectAllList(10132l, (short) 1l);
        com.ginkgocap.ywxt.knowledge.entity.KnowledgeColumnSubscribe en =new KnowledgeColumnSubscribe();
        en.setColumnId(1l);
        en.setUserId(10132l);
      // kcsService.add(en);
    }

    @Test
    public void testAdd() {
        long id = -1;

        try {
            /*
            KnowledgeColumnSubscribe kcs=new KnowledgeColumnSubscribe();
            //            kcs.setUserId(TEST_USER_ID);  //测试所用
            kcs.setUserId(79l);  //测试所用
            kcs.setColumnId(12); //12所代表的栏目   资讯--金融
            //            kcs.setColumnType(1+"");
            //            System.out.println(kcsService);
            //            kcs=kcsService.add(kcs);
            System.out.println(kcs.getId());
            id=kcs.getId();
            assertNotNull(kcs);*/

            //更改为mybatis后
            com.ginkgocap.ywxt.knowledge.entity.KnowledgeColumnSubscribe ekcs = new com.ginkgocap.ywxt.knowledge.entity.KnowledgeColumnSubscribe();
            ekcs.setUserId(81l);
            ekcs.setColumnId(12l);
            ekcs = kcsService.add(ekcs);
            assertNotNull(ekcs);

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        } finally {
            if (id > 0) {
                //                kcsService.deleteByUIdAndKCId(72l, 12);
                //                kcsService.deleteByPK(id);
            }

        }

    }

    @Test
    public void testPrepareData() {

        //        for (int i = 0; i < 4; i++) {
        //            KnowledgeColumnSubscribe kcs=new KnowledgeColumnSubscribe();
        //            
        //            kcs.setUserId(72l);  
        //            kcs.setColumnId(13+i); //12所代表的栏目   资讯--金融
        //            kcs.setColumnType(1+"");
        //            kcs=kcsService.add(kcs);
        //        }

        //        for (int i = 0; i < 5; i++) {
        //            KnowledgeColumnSubscribe kcs=new KnowledgeColumnSubscribe();
        //            kcs.setUserId(71);  
        //            kcs.setColumnId(13+i); //13所代表的栏目   资讯--股票
        //            kcs.setColumnType(1+"");
        //            kcs=kcsService.add(kcs);
        //        }

        //        for (int i = 0; i < 5; i++) {
        //            KnowledgeColumnSubscribe kcs=new KnowledgeColumnSubscribe();
        //            
        //            kcs.setUserId(73+i);  
        //            kcs.setColumnId(13); //13所代表的栏目   资讯--股票 
        //            kcs.setColumnType(1+"");
        //            kcs=kcsService.add(kcs);
        //        }
        //        
        //        for (int i = 0; i < 5; i++) {
        //            KnowledgeColumnSubscribe kcs=new KnowledgeColumnSubscribe();
        //            
        //            kcs.setUserId(71+i);  
        //            kcs.setColumnId(21); //投融工具--金融理论
        //            kcs.setColumnType(2+"");
        //            kcs=kcsService.add(kcs);
        //        }

    }

    @Test
    public void isExist() {
        try {
            assertTrue(kcsService.isExist(72l, 13l));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }

    }

    @Test
    public void testDeleteByUIdAndKCId() {
        //        fail("Not yet implemented");
    }

    @Test
    public void testDeleteByPK() {
    }

    @Test
    public void testSelectByUserId() {
        //        List<KnowledgeColumnSubscribe> list=kcsService.selectByUserId(72);
        //        for (KnowledgeColumnSubscribe kcs : list) {
        //            System.out.println(kcs.getColumnId());
        //        }
    }

    @Test
    public void testSelectKCListByUserId() {
    }

    @Test
    public void testSelectUserIdListByKc() {
    }

    @Test
    public void testSelectByKCId() {
    }

    @Test
    public void testCountByKC() {
    }

    @Test
    public void testCountAllByKC() {
    }

    @Test
    public void testCountByUserId() {
    }

    @Test
    public void testSelectSubKnowByUserIdLong() {
    }

    @Test
    public void testSelectSubKnowByUserIdLongInt() {
    }

    @Test
    public void testSelectSubKnowByKCListListOfKnowledgeColumn() {
    }
    @Test
    public void testUpdateSubscribeCount() {
        long i=kcsService.updateSubscribeCount(1);
        System.out.println(i);
    }

    @Test
    public void testSelectSubKnowByKCListListOfKnowledgeColumnInt() {
    }

}
