package com.ginkgocap.ywxt.knowledge.utils;

import com.ginkgocap.parasol.associate.model.Associate;
import com.ginkgocap.ywxt.knowledge.model.*;
import com.gintong.frame.util.dto.InterfaceResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2016/3/31.
 */
public final class DummyData {

    public static <T> InterfaceResult<T> resultObject(T responseData) throws Exception {
        return InterfaceResult.getSuccessInterfaceResultInstance(responseData);
    }

    public static InterfaceResult<DataCollection> knowledgeDetailObject() throws Exception {
        return InterfaceResult.getSuccessInterfaceResultInstance(dataCollectionObject());
    }

    public static List<DataCollection> getDataCollectionList()
    {
        List<DataCollection> knowledgeList = new ArrayList<DataCollection>(10);
        for(int index = 0; index < 10; index++) {
            knowledgeList.add(dataCollectionObject());
        }

        return knowledgeList;
    }


    private static DataCollection dataCollectionObject() {
        KnowledgeBase knowledgeBase = knowledgeBaseObject();
        KnowledgeReference knowledgeReference = referenceObject();

        DataCollection dataCollection = new DataCollection();
        dataCollection.setKnowledge(knowledgeBase);
        dataCollection.setReference(knowledgeReference);
        dataCollection.setAsso(assoList());

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

    public static DataCollection getDataCollection(short columnId,String title)
    {
        DataCollection data = new DataCollection();
        data.setKnowledgeDetail(knowledgeDetail(columnId, title));
        data.setReference(referenceObject(title));

        return data;
    }

    /*
    private ColumnCollection columnObject()
    {
        String columnInfo = "{\"id\":\"\",主键\"knowledgeId\":\"\",知识主键\"articleName\":\"\",引用资料文章名称\"url\":\"\",引用网址\"websiteName\":\"\",应用网址名称\"status\":\"\",标示本条资料是否有效，1：为有效，0：为无效\"refDate\":\"\",引用时间\"createDate\":\"\",创建时间\"modifyDate\":\"\",修改时间}";

        ColumnCollection columnObject = null;
        try {
            columnObject = KnowledgeUtil.readValue(columnInfo, ColumnCollection.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return columnObject;
    }*/

    private static KnowledgeReference referenceObject()
    {
        return referenceObject(null);
    }

    private static KnowledgeReference referenceObject(String title)
    {
        String referenceJson = "{\"id\":1223,\"knowledgeId\":12344,\"articleName\":\"Test Title\",\"url\":\"http://travel.enorth.com.cn/system/2015/06/03/030277875_01.shtml\",\"websiteName\":\"transient\",\"status\":1,\"refDate\":1458901743546,\"createDate\":1458901743546,\"modifyDate\":1458901743546}";

        KnowledgeReference kReference = null;
        try {
            kReference = KnowledgeUtil.readValue(KnowledgeReference.class, referenceJson);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (title != null) {
            kReference.setArticleName(title);
        }
        return kReference;
    }

    private static Associate assoObject()
    {
        return TestData.assoObject();
    }

    private static List<Associate> assoList()  {
        List<Associate> assoList = new ArrayList(1);
        assoList.add(TestData.assoObject());
        return assoList;
    }
}
