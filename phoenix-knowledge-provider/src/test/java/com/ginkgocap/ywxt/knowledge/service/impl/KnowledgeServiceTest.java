package com.ginkgocap.ywxt.knowledge.service.impl;

import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.model.DataCollection;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeBase;
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
        user = TestData.getDummyUser();
    }

    @Test
	public void testInsert() {
        DataCollection dataCollection = TestData.dataCollectionObject();
        try {
            dataCollection.serUserInfo(user);
            InterfaceResult<DataCollection> data = knowledgeService.insert(dataCollection);
            DataCollection retDataCollection = data.getResponseData();
            columnId = retDataCollection.getKnowledge().getColumnId();
            Id = retDataCollection.getKnowledge().getId();
            knowledgeIdupdate = knowledgeIdQuery = retDataCollection.getKnowledge().getKnowledgeId();
            System.err.println(data.getResponseData());
        } catch (Exception e) {
            e.printStackTrace();
            TestCase.fail();
        }
    }

    @Test
	public void testUpadte() {
        DataCollection dataCollection = TestData.dataCollectionObject();
        try {
            dataCollection.generateKnowledge();
            dataCollection.getKnowledge().setId(Id);
            dataCollection.getKnowledge().setKnowledgeId(knowledgeIdupdate);
            dataCollection.getKnowledge().setTitle("Update title");
            dataCollection.getKnowledge().setModifyDate(System.currentTimeMillis());
            if (dataCollection.getReference() != null) {
                dataCollection.getReference().setKnowledgeId(knowledgeIdupdate);
                dataCollection.getReference().setArticleName("update_article_name");
                dataCollection.getReference().setModifyDate(System.currentTimeMillis());
            }
            dataCollection.serUserInfo(user);
            knowledgeService.update(dataCollection);
            System.err.println("---------test done---------");
        } catch (Exception e) {
            e.printStackTrace();
            TestCase.fail();
        }
	}

    @Test
	public void testDeleteByKnowledgeId() {
        KnowledgeBase knowledge = TestData.dataCollectionObject().getKnowledge();
        try {
            //dataCollection.serUserInfo(user);
            knowledgeService.deleteByKnowledgeId(knowledgeIdDelete, columnId);
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
	
	public void testGetBaseByColumnId() {
		
	}
	
	public void testGetBaseByColumnIdAndType() {
		
	}
	
}