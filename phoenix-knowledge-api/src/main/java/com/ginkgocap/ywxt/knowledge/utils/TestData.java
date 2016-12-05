package com.ginkgocap.ywxt.knowledge.utils;

import com.ginkgocap.parasol.associate.model.Associate;
import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeComment;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeReport;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeUtil;
import com.ginkgocap.ywxt.knowledge.model.common.DataCollect;
import com.ginkgocap.ywxt.knowledge.model.common.KnowledgeReference;
import com.ginkgocap.ywxt.knowledge.model.common.ResItem;
import com.ginkgocap.ywxt.knowledge.model.common.SelfField;
import com.ginkgocap.ywxt.user.model.User;
import com.gintong.common.phoenix.permission.ResourceType;
import com.gintong.common.phoenix.permission.entity.Permission;
import com.gintong.frame.util.dto.InterfaceResult;

import java.util.ArrayList;
import java.util.Arrays;
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
    
    public static DataCollect getDataCollect(long userId, int columnId,String title)
    {
        DataCollect data = new DataCollect();
        data.setKnowledgeDetail(knowledge(userId,columnId, title));
        data.setReference(referenceObject(title));
        data.setPermission(permission(0, userId));
        data.setAsso(assoList());
        data.setUpdateDynamic((short)1);

        return data;
    }

    public static ResItem getResItems(long resId, short type, List<Long> tagIds)
    {
        ResItem resItem = new ResItem();
        resItem.setResId(resId);
        resItem.setType(type);
        resItem.setIds(tagIds);

        return resItem;
    }

    public static Knowledge knowledge(long userId,int columnId,String title)
    {
        Knowledge detail = new Knowledge();
        detail.setCid(userId);
        detail.setCname("testUser");
        detail.setCid(userId);
        detail.setCname("testUser");
        detail.setTitle(title == null ? "TestTitle" : title);
        detail.setContent("Knowledge Description");
        List<String> UrlIds= new ArrayList<String>();
        UrlIds.add("11122");
        detail.setMultiUrls(UrlIds);
        detail.setAttachUrls(UrlIds);
        detail.setColumnType(String.valueOf(columnId));
        detail.setColumnid(String.valueOf(columnId));
        detail.setModifytime(String.valueOf(System.currentTimeMillis()));
        detail.setCreatetime(String.valueOf(System.currentTimeMillis()));
        detail.setSelfDef(createSelfField());

        return detail;
    }


    private static KnowledgeReference referenceObject(String title)
    {
        String referenceJson = "{\"id\":1223,\"knowledgeId\":12344,\"articleName\":\"Test Title\",\"url\":\"http://travel.enorth.com.cn/system/2015/06/03/030277875_01.shtml\",\"websiteName\":\"transient\",\"status\":1,\"refDate\":1458901743546,\"createDate\":1458901743546,\"modifyDate\":1458901743546}";

        KnowledgeReference kReference = KnowledgeUtil.readValue(KnowledgeReference.class, referenceJson);
        if (kReference != null) {
            kReference.setArticleName(title);
        }
        return kReference;
    }

    public static Permission permission(long knowledgeId, long ownerId)
    {
        Permission permission = new Permission();
        permission.setResId(knowledgeId);
        permission.setResType(ResourceType.KNOW.getVal());
        permission.setResOwnerId(ownerId);
        permission.setPublicFlag(1);//公开1，私密-0
        permission.setShareFlag(1);//可分享1,不可分享-0
        permission.setConnectFlag(-1);//可对接1,不可对接-0

        return permission;
    }

    public static Associate assoObject()
    {
        return assoObject("Asso Desction");
    }

    public static Associate assoObject(String name)
    {
        long System_AppId = 1L;
        long assocId = 2L; //,
        long sourceTypeId = KnowledgeConstant.DEFAULT_SOURCE_TYPE;
        long typeId = 8L;

        Associate associate = new Associate();
        associate.setId(0);
        associate.setAppId(System_AppId);
        associate.setUserId(user.getId());

        associate.setSourceId(1l);
        associate.setSourceTypeId(sourceTypeId);

        associate.setAssocDesc(name);
        associate.setAssocId(assocId);
        associate.setAssocMetadata("{\"userPicPath\":\"test.online.gintong.com/cross/img/user/image/?module=customer&uId=21&userId=21\",\"columnType\":1,\"type\":2}");
        associate.setAssocTitle("Asso Title");
        associate.setAssocTypeId(typeId);
        associate.setCreateAt(System.currentTimeMillis());

        return associate;
    }

    public static List<Associate> assoList()
    {
        List<Associate> associateList = new ArrayList<Associate>(1);
        associateList.add(assoObject("assoList_1"));
        associateList.add(assoObject("assoList_2"));

        return associateList;
    }

    //For Knowledge Comment
    public static KnowledgeComment knowledgeComment(long userId, long knowledgeId, int columnId) throws Exception
    {
        return knowledgeComment(userId, knowledgeId, columnId, null, true);
    }

    public static KnowledgeComment knowledgeComment(long userId, long knowledgeId, int columnId, String content)
    {
        return knowledgeComment(userId, knowledgeId, columnId, content, true);
    }

    public static KnowledgeComment knowledgeComment(long userId, long knowledgeId, int columnId, String content, boolean visible)
    {
        KnowledgeComment comment = new KnowledgeComment();
        comment.setOwnerId(userId);
        comment.setKnowledgeId(knowledgeId);
        comment.setColumnId(columnId);
        comment.setOwnerName("DummyUserName");
        comment.setCreateTime(System.currentTimeMillis());
        comment.setContent(content == null ? "Very Good" : content);
        comment.setVisible(visible ? 1 : 0);
        return comment;
    }

    public static KnowledgeReport knowledgeReport(long userId, long knowledgeId, int columnId) throws Exception
    {
        KnowledgeReport report = new KnowledgeReport();
        report.setId(0);
        report.setColumnId(columnId);
        report.setKnowledgeId(knowledgeId);
        report.setUserId(userId);
        report.setUserName("DummyUserName");
        report.setCreateTime(System.currentTimeMillis());
        report.setReason("Sexy Content");
        report.setContent("Sexy Content， Sexy Content");
        return report;
    }

    private static List<SelfField> createSelfField()
    {
        return Arrays.asList(new SelfField("name1","value1"), new SelfField("name2","value2"));
    }

    //end
}