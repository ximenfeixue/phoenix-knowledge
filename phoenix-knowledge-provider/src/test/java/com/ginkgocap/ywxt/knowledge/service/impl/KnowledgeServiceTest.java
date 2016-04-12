package com.ginkgocap.ywxt.knowledge.service.impl;

import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.model.DataCollection;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeBase;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeReference;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeUtil;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeService;
import com.ginkgocap.ywxt.knowledge.utils.TestData;
import com.ginkgocap.ywxt.user.model.User;
import com.gintong.frame.util.dto.InterfaceResult;
import junit.framework.TestCase;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


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
        createKnowledge("KnowledgeServiceTest");
    }

    @Test
	public void testUpdate() {
        try {
            DataCollection dataCollection = createKnowledge("Update-KnowledgeServiceTest");
            KnowledgeBase knowledge = dataCollection.generateKnowledge();

            knowledge.setContent("Update-Content-KnowledgeServiceTest");
            knowledge.setModifyDate(System.currentTimeMillis());
            KnowledgeReference reference = dataCollection.getReference();
            if (reference != null) {
                reference.setKnowledgeId(knowledgeIdupdate);
                reference.setArticleName("update_article_name");
                reference.setModifyDate(System.currentTimeMillis());
            }
            dataCollection.serUserInfo(user);
            InterfaceResult<DataCollection> updateData = knowledgeService.update(dataCollection);
            assertResponseNoRetData(updateData);
        } catch (Exception e) {
            e.printStackTrace();
            TestCase.fail();
        }
	}

    @Test
	public void testDeleteByKnowledgeId() {
        try {
            DataCollection dataCollection = this.createKnowledge("test-delete-KnowledgeServiceTest");
            KnowledgeBase knowledge = dataCollection.getKnowledge();
            knowledgeService.deleteByKnowledgeId(knowledge.getKnowledgeId(), knowledge.getColumnId());
        } catch (Exception e) {
            e.printStackTrace();
            TestCase.fail();
        }
    }
	
	public void testDeleteByKnowledgeIds() {
        DataCollection data = null;
        List<Long> knowledgeIds = new ArrayList<Long>(5);
        for (int index = 1; index <= 5; index++) {
            data = this.createKnowledge("test-delete-KnowledgeServiceTest_"+index);
            knowledgeIds.add(data.getKnowledgeDetail().getId());
        }
        short columnId = data != null ? data.getKnowledgeDetail().getColumnId() :  2;
        try {
            knowledgeService.deleteByKnowledgeIds(knowledgeIds, columnId);
        } catch (Exception e) {
            e.printStackTrace();
            TestCase.fail();
        }
    }
	
	public void testGetDetailById() {
        DataCollection data = this.createKnowledge("KnowledgeServiceTest_testGetDetailById");
        try {
            long knowledgeId = data.getKnowledgeDetail().getId();
            short columnId = data.getKnowledgeDetail().getColumnId();
            knowledgeService.getDetailById(knowledgeId, columnId);
        } catch (Exception e) {
            e.printStackTrace();
            TestCase.fail();
        }
    }
	
	public void testGetBaseById() {
        DataCollection data = this.createKnowledge("KnowledgeServiceTest_"+ "testGetBaseById");
        try {
            knowledgeService.getBaseById(data.getKnowledgeDetail().getId());
        } catch (Exception e) {
            e.printStackTrace();
            TestCase.fail();
        }
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
        DataCollection dataCollection = null;
        try {
            dataCollection = TestData.dataCollection(user.getId(), title);
            InterfaceResult result = knowledgeService.insert(dataCollection);
            assertResponse(result);
            long knowledgeId = (Long)result.getResponseData();
            dataCollection.getKnowledgeDetail().setId(knowledgeId);
        } catch (Exception e) {
            e.printStackTrace();
            TestCase.fail();
        }

        return dataCollection;
    }

    private void assertResponseNoRetData(InterfaceResult data)
    {
        TestCase.assertNotNull(data);
    }

    private void assertResponse(InterfaceResult data)
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