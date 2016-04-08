package com.ginkgocap.ywxt.knowledge.test.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ginkgocap.ywxt.knowledge.model.DataCollection;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeComment;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeUtil;
import com.ginkgocap.ywxt.knowledge.test.base.DataUtil;
import com.ginkgocap.ywxt.knowledge.test.base.TestData;
import junit.framework.TestCase;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by Chen Peifeng on 2016/1/23.
 */
public class GenerateJsonTest extends TestCase {

    public void serviceCreateJsonCheck(Object object)
    {
        try {
            if (object instanceof DataCollection) {
                System.out.println("Generate JSON Unit Test for: " + DataCollection.class.getSimpleName());
            }
            else if (object instanceof KnowledgeComment) {
                System.out.println("Generate JSON Unit Test for: " + KnowledgeComment.class.getSimpleName());
            }

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonConent = objectMapper.writeValueAsString(object);
            assertNotNull(jsonConent);
            //assertTrue(jsonConent.contains("area"));
            DataUtil.writeJsonData(object);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Requirement
    @Test
    public void testKnowledgeCreateJson() throws Exception
    {
        serviceCreateJsonCheck(TestData.dataCollection() );
    }

    @Test
    public void testServiceMedicalConsultCreateJson() throws Exception
    {
        serviceCreateJsonCheck( TestData.knowledgeComment(123456L) );
    }

    //KnowledgeComment
    @Test
    public void testKnowledgeCommentCreateJson() throws Exception
    {
        KnowledgeComment knowledgeComment = TestData.knowledgeComment(12345679L);
        System.out.println("Generate JSON Unit Test for: " + KnowledgeComment.class.getSimpleName());
        String jsonConent = KnowledgeUtil.writeObjectToJson(knowledgeComment);
        assertNotNull(jsonConent);
    }
}
