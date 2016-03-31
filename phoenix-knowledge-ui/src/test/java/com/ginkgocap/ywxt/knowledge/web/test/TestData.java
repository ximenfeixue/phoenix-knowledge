package com.ginkgocap.ywxt.knowledge.web.test;

public final class TestData {

    public static String createKnowlegeJson()
    {
        return "{\"id\":\"主键\",\"title\":\"标题\",\"type\":\"类型，0为系统创建,1为用户创建\",\" columnId \":1,\" content \":\"描述,知识正文内容\",\" contentDesc \":\"描述简略，一般存储描述的前50个字\",\" attachmentId \":\"附件ID \",\" pictureId \":\"图片ID\",\" author \":\"作者\",\" createUserId \":\"创建人ID \",\" createUserName \":\"创建人名称 \",\" createDate \":\"创建时间 \",\" modifyUserId \":\"修改人ID \",\" modifyUserName \":\"修改人名称 \",\" modifyDate \":\"修改时间 \",\" publicDate \":\"发布时间 \",\" status \":\"状态(0为无效/删除，1为有效,2为草稿,3,回收站)\",\" auditStatus \":\" 审核状态(0：未通过，1：审核中，2：审核通过)\",\" reportStatus \":\" 举报状态(3：举报审核未通过，即无非法现象，2：举报审核通过，1 : 未被举报，0：已被举报)\"}";
    }

    public static String updateKnowlegeJson()
    {
        return "{\"id\":\"主键\",\"title\":\"标题\",\"type\":\"类型，0为系统创建,1为用户创建\",\" columnId \":1,\" content \":\"描述,知识正文内容\",\" contentDesc \":\"描述简略，一般存储描述的前50个字\",\" attachmentId \":\"附件ID \",\" pictureId \":\"图片ID\",\" author \":\"作者\",\" createUserId \":\"创建人ID \",\" createUserName \":\"创建人名称 \",\" createDate \":\"创建时间 \",\" modifyUserId \":\"修改人ID \",\" modifyUserName \":\"修改人名称 \",\" modifyDate \":\"修改时间 \",\" publicDate \":\"发布时间 \",\" status \":\"状态(0为无效/删除，1为有效,2为草稿,3,回收站)\",\" auditStatus \":\" 审核状态(0：未通过，1：审核中，2：审核通过)\",\" reportStatus \":\" 举报状态(3：举报审核未通过，即无非法现象，2：举报审核通过，1 : 未被举报，0：已被举报)\"}";
    }
}