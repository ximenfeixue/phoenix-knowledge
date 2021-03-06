package com.ginkgocap.ywxt.knowledge.base;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeBase;
import com.ginkgocap.ywxt.knowledge.utils.KnowledgeUtil;
import com.gintong.frame.util.dto.InterfaceResult;
import junit.framework.TestCase;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class TestBase {

    private boolean printResult = true;
    protected static long userId = 0L;
    static {
        userId = KnowledgeUtil.getDummyUser().getId();
    }
	@BeforeClass
	public static void beforeClass(){
	}
	
	@Before
	public void before(){
	}
	
	@AfterClass
	public static void afterClass(){
	}
	
	@After
	public void after(){
	}
	
	@Test
	public void test(){
	}

    protected void checkListResult(InterfaceResult<List<KnowledgeBase>> result)
    {
        TestCase.assertNotNull(result);
        TestCase.assertNotNull(result.getResponseData());
        TestCase.assertTrue(result.getResponseData().size() > 0);
        if (printResult) {
            String json = KnowledgeUtil.writeObjectToJson(result.getResponseData());
            System.err.println(json);
        }
    }

    protected void checkListResult(List<KnowledgeBase> result)
    {
        TestCase.assertNotNull(result);
        TestCase.assertTrue(result.size() > 0);
        if (printResult) {
            String json = KnowledgeUtil.writeObjectToJson(result.toArray());
            System.err.println(json);
        }
    }

    protected void assertResponseNoRetData(InterfaceResult data)
    {
        TestCase.assertNotNull(data);
    }

    protected void assertResponseWithData(InterfaceResult data)
    {
        System.err.println(KnowledgeUtil.writeObjectToJson(data));
        TestCase.assertNotNull(data);
        TestCase.assertNotNull(data.getNotification());
        TestCase.assertEquals("0",data.getNotification().getNotifCode());
        TestCase.assertEquals("success",data.getNotification().getNotifInfo());
        TestCase.assertNotNull(data.getResponseData());
    }
}
