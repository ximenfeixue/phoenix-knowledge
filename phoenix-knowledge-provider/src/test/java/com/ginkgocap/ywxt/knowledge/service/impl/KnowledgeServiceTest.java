package com.ginkgocap.ywxt.knowledge.service.impl;

import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.model.DataCollection;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeBase;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeReference;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeUtil;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeService;
import com.ginkgocap.ywxt.knowledge.testData.TestData;
import com.ginkgocap.ywxt.user.model.User;
import com.gintong.frame.util.dto.InterfaceResult;
import junit.framework.TestCase;
import org.junit.Test;

import javax.annotation.Resource;


public class KnowledgeServiceTest extends TestBase {

	@Resource
	private KnowledgeService knowledgeService;

    private static long knowledgeIdDelete = 0L;
    private static long knowledgeIdupdate = 0L;
    private static long knowledgeIdQuery = 0L;
    private static long Id = 0L;
    private static short columnId = 0;

    private static User user = null;
    static {
        user = KnowledgeUtil.getDummyUser();
    }

    @Test
	public void testInsert() {
        createKnowledge("testInsert");
    }

    @Test
	public void testUpdate() {

        try {
            DataCollection dataCollection = createKnowledge("testUpdate");
            KnowledgeBase knowledge = dataCollection.generateKnowledge();
            //TestCase.assertNotNull(reference);

            knowledge.setContent("testUpdate-Update-Content");
            knowledge.setModifyDate(System.currentTimeMillis());
            KnowledgeReference reference = dataCollection.getReference();
            if (reference != null) {
                reference.setKnowledgeId(knowledgeIdupdate);
                reference.setArticleName("update_article_name");
                reference.setModifyDate(System.currentTimeMillis());
            }
            dataCollection.serUserInfo(user);
            InterfaceResult<DataCollection> updateData = knowledgeService.update(dataCollection);
            assertResponse(updateData);
        } catch (Exception e) {
            e.printStackTrace();
            TestCase.fail();
        }
	}

    @Test
	public void testDeleteByKnowledgeId() {
        try {
            DataCollection dataCollection = this.createKnowledge("testDeleteByKnowledgeId");
            KnowledgeBase knowledge = dataCollection.getKnowledge();
            knowledgeService.deleteByKnowledgeId(knowledge.getKnowledgeId(), knowledge.getColumnId());
        } catch (Exception e) {
            e.printStackTrace();
            TestCase.fail();
        }
    }
	
	public void testDeleteByKnowledgeIds() {
        //knowledgeService.getDetailById(user);
	}
	
	public void testGetDetailById() {
        //knowledgeService.getDetailById(user)
	}
	
	public void testGetBaseById() {
        //knowledgeService.getBaseById(user)
	}
	
	public void testGetBaseByIds() {
		
	}
	
	public void testGetBaseAll() {
		
	}
	
	public void testGetBaseByCreateUserId() {
		
	}
	
	public void testGetBaseByCreateUserIdAndColumnId() {
		
	}
	
	public void testGetBaseByCreateUserIdAndType() {
		
	}
	
	public void testGetBaseByCreateUserIdAndColumnIdAndType() {
		
	}
	
	public void testGetBaseByType() {
		
	}
	
	public void testGetBaseByColumnId()
    {
		
	}
	
	public void testGetBaseByColumnIdAndType()
    {
        //knowledgeService.getBaseByColumnIdAndType()
	}

    private DataCollection createKnowledge(String title)
    {
        InterfaceResult<DataCollection> data = null;
        try {
            DataCollection dataCollection = TestData.dataCollection(user, title);
            data = knowledgeService.insert(dataCollection);
            assertResponse(data);
            assertKnowledge(data.getResponseData());
        } catch (Exception e) {
            e.printStackTrace();
            TestCase.fail();
        }

        return data.getResponseData();
    }

    private void assertResponse(InterfaceResult<DataCollection> data)
    {
        TestCase.assertNotNull(data);
        TestCase.assertNotNull(data.getResponseData());
    }

    private void assertKnowledge(DataCollection data)
    {
        TestCase.assertNotNull(data);
        TestCase.assertNotNull(data.getKnowledge());
        TestCase.assertTrue( data.getKnowledge().getId()>0 && data.getKnowledge().getKnowledgeId()>=0 );
    }
	
}