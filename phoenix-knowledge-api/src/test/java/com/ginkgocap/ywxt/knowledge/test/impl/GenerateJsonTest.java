package com.ginkgocap.ywxt.knowledge.test.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ginkgocap.ywxt.knowledge.model.*;
import com.ginkgocap.ywxt.knowledge.model.common.DataCollection;
import com.ginkgocap.ywxt.knowledge.model.common.ResItem;
import com.ginkgocap.ywxt.knowledge.utils.TestData;
import com.ginkgocap.ywxt.knowledge.utils.TestDataUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Chen Peifeng on 2016/1/23.
 */
public class GenerateJsonTest extends BaseTestCase {

    public void serviceCreateJsonCheck(Object object)
    {
        try {
            if (object instanceof DataCollection) {
                System.out.println("Generate JSON Unit Test for: " + DataCollection.class.getSimpleName());
            }
            else if (object instanceof KnowledgeComment) {
                System.out.println("Generate JSON Unit Test for: " + KnowledgeComment.class.getSimpleName());
            }
            else if (object instanceof KnowledgeReport) {
                System.out.println("Generate JSON Unit Test for: " + KnowledgeReport.class.getSimpleName());
            }

            //FilterProvider idFilterProvider = new SimpleFilterProvider().addFilter("userFilter", SimpleBeanPropertyFilter.filterOutAllExcept(new String[]{"id"}));
            String jsonConent = TestDataUtil.writeJsonData(object);
            assertNotNull(jsonConent);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void testAssoCreateJson() throws Exception
    {
        serviceCreateJsonCheck(TestData.assoObject() );
    }


    public void testKnowledgeCreateJson() throws Exception
    {
        serviceCreateJsonCheck(TestData.dataCollection() );
    }


    public void testKnowledgeCommentCreateJson() throws Exception
    {
        serviceCreateJsonCheck( TestData.knowledgeComment(userId, knowledgeId(), columnId) );
    }

    public void testKnowledgeReportCreateJson() throws Exception
    {
        serviceCreateJsonCheck( TestData.knowledgeReport(userId, knowledgeId(), columnId) );
    }

    public void testBatchTags()
    {
        List<ResItem> resItems = new ArrayList<ResItem>(2);
        List<Long> tagIds = Arrays.asList(3933811561988102L, 3933811356467203L);
        ResItem resItem1 = TestData.getResItems(1112323L, (short) 1, tagIds);
        ResItem resItem2 = TestData.getResItems(1112345L, (short)1, tagIds);
        resItems.add(resItem1);
        resItems.add(resItem2);
        String requestJson = KnowledgeUtil.writeObjectToJson(resItems);

        serviceCreateJsonCheck( resItems );
    }
}
