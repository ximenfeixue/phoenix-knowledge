package com.ginkgocap.ywxt.knowledge.service.impl;

import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.model.DataCollection;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeBase;
import com.ginkgocap.ywxt.knowledge.service.IKnowledgeService;
import com.ginkgocap.ywxt.knowledge.testData.TestData;
import com.ginkgocap.ywxt.user.model.User;
import com.gintong.frame.util.dto.InterfaceResult;
import org.junit.Test;

import javax.annotation.Resource;


public class KnowledgeServiceTest extends TestBase {

	@Resource
	private IKnowledgeService knowledgeService;

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
	public void insert() {
        DataCollection dataCollection = TestData.dataCollectionObject();
        try {
            InterfaceResult<DataCollection> data = knowledgeService.insert(dataCollection, user);
            DataCollection retDataCollection = data.getResponseData();
            columnId = retDataCollection.getKnowledge().getColumnId();
            Id = retDataCollection.getKnowledge().getId();
            knowledgeIdupdate = knowledgeIdQuery = retDataCollection.getKnowledge().getKnowledgeId();
            System.err.println(data.getResponseData());
            System.err.println("---------test done---------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
	public void upadte() {
        DataCollection dataCollection = TestData.dataCollectionObject();
        try {
            dataCollection.getKnowledge().setId(Id);
            dataCollection.getKnowledge().setKnowledgeId(knowledgeIdupdate);
            dataCollection.getKnowledge().setTitle("Update title");
            dataCollection.getKnowledge().setModifyDate(System.currentTimeMillis());
            dataCollection.getReference().setKnowledgeId(knowledgeIdupdate);
            dataCollection.getReference().setArticleName("update_article_name");
            dataCollection.getReference().setModifyDate(System.currentTimeMillis());
            knowledgeService.update(dataCollection, user);
            System.err.println("---------test done---------");
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

    @Test
	public void deleteByKnowledgeId() {
        KnowledgeBase knowledge = TestData.dataCollectionObject().getKnowledge();
        try {
            knowledgeService.deleteByKnowledgeId(knowledgeIdDelete, columnId, user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	public void deleteByKnowledgeIds() {
        //knowledgeService.getDetailById(user);
	}
	
	public void getDetailById() {
        //knowledgeService.getDetailById(user)
	}
	
	public void getBaseById() {
        //knowledgeService.getBaseById(user)
	}
	
	public void getBaseByIds() {
		
	}
	
	public void getBaseAll() {
		
	}
	
	public void getBaseByCreateUserId() {
		
	}
	
	public void getBaseByCreateUserIdAndColumnId() {
		
	}
	
	public void getBaseByCreateUserIdAndType() {
		
	}
	
	public void getBaseByCreateUserIdAndColumnIdAndType() {
		
	}
	
	public void getBaseByType() {
		
	}
	
	public void getBaseByColumnId() {
		
	}
	
	public void getBaseByColumnIdAndType() {
		
	}
	
	@Test
	public void test() {
		System.out.print("11111");
	}
	
}