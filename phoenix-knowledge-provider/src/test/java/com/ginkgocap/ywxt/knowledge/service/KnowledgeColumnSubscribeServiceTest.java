package com.ginkgocap.ywxt.knowledge.service;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeColumnSubscribe;

/** 
 * <p>订阅测试</p>  
 * @author guangyuan  
 * @since 1.2.2
 */
public class KnowledgeColumnSubscribeServiceTest extends TestBase {

    public static long TEST_USER_ID=71L;
    
    @Autowired
    KnowledgeColumnSubscribeService kcsService;
    
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
    public void testAdd() {
        try {
            
            KnowledgeColumnSubscribe kcs=new KnowledgeColumnSubscribe();
            kcs.setUserId(TEST_USER_ID);  //测试所用
            kcs.setColumnId(12); //12所代表的栏目   资讯--金融
            kcs.setColumnType(1+"");
            
            System.out.println(kcsService);
            kcsService.add(kcs);
//            kcs=kcsService.add(kcs);
//            System.out.println(kcs.getId());
            
            assertNotNull(kcs);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
       
    }

    @Test
    public void testDeleteByUIdAndKCId() {
        System.out.println(kcsService);
        fail("Not yet implemented");
    }

    @Test
    public void testDeleteByPK() {
        fail("Not yet implemented");
    }

    @Test
    public void testSelectByUserId() {
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
    public void testSelectSubKnowByKCListListOfKnowledgeColumnInt() {
    }

}
