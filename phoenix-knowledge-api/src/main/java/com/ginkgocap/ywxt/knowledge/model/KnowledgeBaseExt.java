package com.ginkgocap.ywxt.knowledge.model;

/**
 * Created by oem on 6/7/17.
 */
public class KnowledgeBaseExt extends KnowledgeBase {
    /**类型，一级栏目*/
    private String typeName;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void clone(KnowledgeBase base) {
        this.setId(base.getId());
        this.setKnowledgeId(base.getKnowledgeId());
        this.setType(base.getType());
        this.setColumnId(base.getColumnId());
        this.setTitle(base.getTitle());
        this.setContent(base.getContent());
        this.setContentDesc(base.getContentDesc());
        this.setCreateUserId(base.getCreateUserId());
        this.setCreateUserName(base.getCreateUserName());
        this.setCreateDate(base.getCreateDate());

        this.setPrivated(base.getPrivated());
        this.setEssence(base.getEssence());
        this.setCollected(base.getCollected());
        this.setCoverPic(base.getCoverPic());
        this.setCpath(base.getCpath());
        this.setModifyUserId(base.getModifyUserId());
        this.setModifyUserName(base.getModifyUserName());
        this.setIsOld(base.getIsOld());
        this.setPublicDate(base.getPublicDate());
        this.setReadCount(base.getReadCount());
        this.setSource(base.getSource());
        this.setReportStatus(base.getReportStatus());
        this.setStatus(base.getStatus());
        this.setUserStar(base.getUserStar());
    }
}
