package com.ginkgocap.ywxt.knowledge.controller;

import com.ginkgocap.ywxt.asso.model.Asso;
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
    }

    private static KnowledgeReference referenceObject()
    {
        String referenceJson = "{\"id\":1223,\"knowledgeId\":12344,\"articleName\":\"Test Title\",\"url\":\"http://travel.enorth.com.cn/system/2015/06/03/030277875_01.shtml\",\"websiteName\":\"transient\",\"status\":1,\"refDate\":1458901743546,\"createDate\":1458901743546,\"modifyDate\":1458901743546}";

        KnowledgeReference kReference = null;
        try {
            kReference = KnowledgeUtil.readValue(referenceJson, KnowledgeReference.class);
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
            asso = KnowledgeUtil.readValue(assoJson, Asso.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return asso;
    }

}
