package com.ginkgocap.ywxt.knowledge.service.impl;

import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeBase;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeType;
import com.ginkgocap.ywxt.knowledge.utils.KnowledgeUtil;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeBatchQueryService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeService;
import junit.framework.TestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by gintong on 2016/7/22.
 */
public class KnowledgeBatchQueryServiceTest extends TestBase {

    @Autowired
    private KnowledgeBatchQueryService knowledgeBatchQueryService;

    @Autowired
    private KnowledgeService knowledgeService;

    @Test
    public void testGetKnowledgeCountByUserIdAndColumnID() {
        String[] columnIds = new String[]{"12", "13", "14"};
        long count = knowledgeBatchQueryService.getKnowledgeCountByUserIdAndColumnID(columnIds, 0, (short) 1);
        System.out.println("---Count: "+count);
        TestCase.assertTrue(count > 0);
    }

    @Test
    public void testGetKnowledge()
    {
        String[] columnIds = new String[]{"12", "13", "14"};
        List<Knowledge> knowledgeList = knowledgeBatchQueryService.getKnowledgeDetailList(columnIds, 0, (short)1, 0, 20);
        System.out.println("---knowledgeList: "+knowledgeList);
        TestCase.assertTrue(knowledgeList != null && knowledgeList.size() > 0);
    }

    @Test
    public void testGetAllByParam()
    {
        List<Knowledge> knowledgeList = knowledgeBatchQueryService.getAllByParam((short) 5, 5, "新材料", 0, 0, 30);
        TestCase.assertTrue(knowledgeList != null && knowledgeList.size() > 0);
        System.out.println("---knowledgeList: "+knowledgeList.size());
    }

    @Test
    public void testGetAllPlatform()
    {
        List<Knowledge> knowledgeList = knowledgeBatchQueryService.selectPlatform((short) 1, 1, "资讯", 0, 0, 30);
        TestCase.assertTrue(knowledgeList != null && knowledgeList.size() > 0);
        System.out.println("---knowledgeList: "+knowledgeList.size());
        String jsonContent = KnowledgeUtil.writeObjectToJson(knowledgeList);
        System.out.println("---jsonContent: " + jsonContent);
    }

    @Test
    public void testGetAllPlatform_All()
    {
        for (int index = 1; index < 12; index++) {
            if (index == 9) {
                continue;
            }
            KnowledgeType type = KnowledgeType.knowledgeType(index);
            System.out.println("Index: "+index + " type: "+type);
            List<Knowledge> knowledgeList = knowledgeBatchQueryService.selectPlatform((short) type.value(), type.value(), type.typeName(), 0, 0, 30);
            TestCase.assertTrue(knowledgeList != null && knowledgeList.size() > 0);
            System.out.println("---knowledgeList: " + knowledgeList.size());
            //String jsonContent = KnowledgeUtil.writeObjectToJson(knowledgeList);
            //System.out.println("---jsonContent: " + jsonContent);
        }
    }

    @Test
    public void testGetAllByPage()
    {
        List<KnowledgeBase> knowledgeList = knowledgeBatchQueryService.getAllByPage(1, (short)1, (short)-1, null, 0, 30);
        TestCase.assertTrue(knowledgeList != null && knowledgeList.size() > 0);
        System.out.println("---knowledgeList: " + knowledgeList.size());
        //String jsonContent = KnowledgeUtil.writeObjectToJson(knowledgeList);
        //System.out.println("---jsonContent: " + jsonContent);
    }
}
