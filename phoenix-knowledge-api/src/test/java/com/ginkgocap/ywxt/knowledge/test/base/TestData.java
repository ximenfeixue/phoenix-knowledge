package com.ginkgocap.ywxt.knowledge.test.base;

import com.ginkgocap.ywxt.asso.model.Asso;
import com.ginkgocap.ywxt.knowledge.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chen Peifeng on 2016/1/23.
 */
public class TestData {
	
	public static void main(String[] args) throws Exception {

	}


    public static DataCollection dataCollection() {
        KnowledgeDetail knowledgeDetail = knowledgeDetail((short)2, null);
        KnowledgeReference knowledgeReference = referenceObject();

        DataCollection dataCollection = new DataCollection();
        dataCollection.setKnowledgeDetail(knowledgeDetail);
        dataCollection.setReference(knowledgeReference);
        dataCollection.setAsso(assoObject());

        return dataCollection;
    }

    public static KnowledgeBase knowledgeBaseObject()
    {
        short columnId = (short)1;
        short typeId = (short)1;
        short auditStatus = (short)1;
        short reportStatus = (short)0;
        long pictureId = 123456L;
        KnowledgeBase knowledge = new KnowledgeBase();
        knowledge.setType(typeId);
        knowledge.setAuthor("testUser");
        knowledge.setTitle("TestTitle");
        knowledge.setContentDesc("Knowledge Description");
        knowledge.setPictureId(pictureId);
        knowledge.setAttachmentId(123456L);
        knowledge.setAuditStatus(auditStatus);
        knowledge.setColumnId(columnId);
        knowledge.setCreateUserId(1234567L);
        knowledge.setCreateUserName("TestUsetName");
        knowledge.setCreateDate(System.currentTimeMillis());
        knowledge.setPublicDate(System.currentTimeMillis());
        knowledge.setReportStatus(reportStatus);

        return knowledge;
    }

    public static KnowledgeDetail knowledgeDetail(short columnId,String title)
    {
        long ownerId = 123456L;
        long pictureId = 123456L;
        KnowledgeDetail knowledgeDetail = new KnowledgeDetail();
        knowledgeDetail.setOwnerId(ownerId);
        knowledgeDetail.setOwnerName("testUser");
        knowledgeDetail.setTitle(title == null ? "TestTitle" : title);
        knowledgeDetail.setContent("Knowledge Description");
        List<String> UrlIds= new ArrayList<String>();
        UrlIds.add("11122");
        knowledgeDetail.setMultiUrls(UrlIds);
        knowledgeDetail.setAttachmentUrls(UrlIds);
        knowledgeDetail.setColumnId(columnId);
        knowledgeDetail.setModifyUserId(ownerId);
        knowledgeDetail.setCreateTime(System.currentTimeMillis());
        knowledgeDetail.setModifyTime(System.currentTimeMillis());
        knowledgeDetail.setModifyUserId(ownerId);

        return knowledgeDetail;
    }

    private static KnowledgeReference referenceObject()
    {
        String referenceJson = "{\"id\":1223,\"knowledgeId\":12344,\"articleName\":\"Test Title\",\"url\":\"http://travel.enorth.com.cn/system/2015/06/03/030277875_01.shtml\",\"websiteName\":\"transient\",\"status\":1,\"refDate\":1458901743546,\"createDate\":1458901743546,\"modifyDate\":1458901743546}";

        KnowledgeReference kReference = null;
        try {
            kReference = KnowledgeUtil.readValue( KnowledgeReference.class, referenceJson);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return kReference;
    }

    private static Asso assoObject()
    {
        String assoJson =  "{\"assoId\":1,\"isVisable\":\"1\",\"ownerId\":\"10001\",\"type\":\"2\",\"value\":{\"r\":[{\"assoConnTagId\":5,\"assoId\":1,\"conn\":[{\"assoConnTagId\":5,\"assoId\":2,\"assoValueId\":25,\"businessId\":\"111115\",\"picPath\":\"\",\"title\":\"测试11115\",\"type\":\"r\"}],\"tag\":\"测试k1\",\"type\":\"r\"}],\"p\":[{\"assoConnTagId\":4,\"assoId\":1,\"conn\":[{\"assoConnTagId\":4,\"assoId\":2,\"assoValueId\":24,\"businessId\":\"111114\",\"picPath\":\"\",\"title\":\"测试11114\",\"type\":\"p\"}],\"tag\":\"测试k1\",\"type\":\"p\"}],\"o\":[{\"assoConnTagId\":3,\"assoId\":1,\"conn\":[{\"assoConnTagId\":3,\"assoId\":2,\"assoValueId\":23,\"businessId\":\"111113\",\"picPath\":\"\",\"title\":\"测试11113\",\"type\":\"o\"}],\"tag\":\"测试k1\",\"type\":\"o\"}],\"k\":[{\"assoConnTagId\":1,\"assoId\":1,\"conn\":[{\"assoConnTagId\":1,\"assoId\":2,\"assoValueId\":21,\"businessId\":\"111111\",\"picPath\":\"\",\"title\":\"测试11111\",\"type\":\"k\"},{\"assoConnTagId\":1,\"assoId\":2,\"assoValueId\":211,\"businessId\":\"111111\",\"picPath\":\"\",\"title\":\"测试11111\",\"type\":\"k\"}],\"tag\":\"测试k1\",\"type\":\"k\"},{\"assoConnTagId\":2,\"assoId\":1,\"conn\":[{\"assoConnTagId\":2,\"assoId\":2,\"assoValueId\":22,\"businessId\":\"111112\",\"picPath\":\"\",\"title\":\"测试11112\",\"type\":\"k\"}],\"tag\":\"测试k2\",\"type\":\"k\"}]}}}";

        Asso asso = null;
        try {
            asso = KnowledgeUtil.readValue( Asso.class, assoJson);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return asso;
    }

    //For Knowledge Comment
    public static KnowledgeComment knowledgeComment(Long knowledgeId) throws Exception
    {
        KnowledgeComment comment = new KnowledgeComment();
        comment.setOwnerId(12344567L);
        comment.setKnowledgeId(knowledgeId);
        comment.setOwnerName("DummyUserName");
        comment.setCreateTime(System.currentTimeMillis());
        comment.setContent("很不错，很好，很赞");
        comment.setVisible(1);
        return comment;
    }

    public static String knowledgeCommentJson(Long knowledgeId) throws Exception
    {
        return "{\"id\":0,\"knowledgeId\":"+ knowledgeId + ",\"ownerId\":12344567,\"ownerName\":\"DummyUserName\",\"createTime\":1454306782624,\"content\":\"很不错，很好，很赞\",\"visible\":1}";
    }
    //end
}