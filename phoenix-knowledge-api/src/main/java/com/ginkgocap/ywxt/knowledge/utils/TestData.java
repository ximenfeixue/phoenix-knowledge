package com.ginkgocap.ywxt.knowledge.utils;

import com.ginkgocap.parasol.associate.model.Associate;
import com.ginkgocap.ywxt.knowledge.model.*;
import com.ginkgocap.ywxt.user.model.User;
import com.gintong.frame.util.dto.InterfaceResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chen Peifeng on 2016/1/23.
 */
public class TestData {

    private static User user = KnowledgeUtil.getDummyUser();
	public static void main(String[] args) throws Exception {

	}

    public static <T> InterfaceResult<T> resultObject(T responseData) throws Exception {
        return InterfaceResult.getSuccessInterfaceResultInstance(responseData);
    }

    public static InterfaceResult<DataCollection> knowledgeDetailObject() throws Exception {
        return InterfaceResult.getSuccessInterfaceResultInstance(dataCollection());
    }

    public static List<DataCollection> getDataCollectionList()
    {
        List<DataCollection> knowledgeList = new ArrayList<DataCollection>(10);
        for(int index = 0; index < 10; index++) {
            knowledgeList.add(dataCollection());
        }

        return knowledgeList;
    }

    public static DataCollection dataCollection(long userId,String title) {
        KnowledgeDetail knowledgeDetail = knowledgeDetail(userId, (short)2, title);
        KnowledgeReference knowledgeReference = referenceObject(title);

        DataCollection dataCollection = new DataCollection();
        dataCollection.setKnowledgeDetail(knowledgeDetail);
        dataCollection.setReference(knowledgeReference);
        dataCollection.setAsso(assoObject());

        return dataCollection;
    }

    public static DataCollection getDataCollection(long userId, short columnId,String title)
    {
        DataCollection data = new DataCollection();
        data.setKnowledgeDetail(knowledgeDetail(userId,columnId, title));
        data.setReference(referenceObject(title));

        return data;
    }

    public static DataCollection dataCollection() {

        return dataCollection(1234567L, null);
    }

    public static KnowledgeBase knowledgeBase()
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

    public static KnowledgeDetail knowledgeDetail(long userId,short columnId,String title)
    {
        long pictureId = 123456L;
        KnowledgeDetail knowledgeDetail = new KnowledgeDetail();
        knowledgeDetail.setOwnerId(userId);
        knowledgeDetail.setOwnerName("testUser");
        knowledgeDetail.setTitle(title == null ? "TestTitle" : title);
        knowledgeDetail.setContent("Knowledge Description");
        List<String> UrlIds= new ArrayList<String>();
        UrlIds.add("11122");
        knowledgeDetail.setMultiUrls(UrlIds);
        knowledgeDetail.setAttachmentUrls(UrlIds);
        knowledgeDetail.setColumnId(columnId);
        knowledgeDetail.setModifyUserId(userId);
        knowledgeDetail.setCreateTime(System.currentTimeMillis());
        knowledgeDetail.setModifyTime(System.currentTimeMillis());

        return knowledgeDetail;
    }

    private static KnowledgeReference referenceObject(String title)
    {
        String referenceJson = "{\"id\":1223,\"knowledgeId\":12344,\"articleName\":\"Test Title\",\"url\":\"http://travel.enorth.com.cn/system/2015/06/03/030277875_01.shtml\",\"websiteName\":\"transient\",\"status\":1,\"refDate\":1458901743546,\"createDate\":1458901743546,\"modifyDate\":1458901743546}";

        KnowledgeReference kReference = null;
        try {
            kReference = KnowledgeUtil.readValue( KnowledgeReference.class, referenceJson);
        } catch (IOException e) {
            e.printStackTrace();
        }
        kReference.setArticleName(title);
        return kReference;
    }

    public static Associate assoObject()
    {
        long System_AppId = 0L;
        long assocId = 2L; //,
        long sourceTypeId = 2L;
        long typeId = 3;

        Associate associate = new Associate();
        associate.setId(0);
        associate.setAppId(System_AppId);
        associate.setUserId(user.getId());

        associate.setSourceId(1l);
        associate.setSourceTypeId(sourceTypeId);

        associate.setAssocDesc("Asso Desction");
        associate.setAssocId(assocId);
        associate.setAssocMetadata("Asso meta Data");
        associate.setAssocTitle("Asso Title");
        associate.setAssocTypeId(1112L);
        associate.setCreateAt(System.currentTimeMillis());

        return associate;
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