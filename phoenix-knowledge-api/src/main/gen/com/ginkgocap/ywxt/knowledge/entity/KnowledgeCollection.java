package com.ginkgocap.ywxt.knowledge.entity;

import java.io.Serializable;
import java.util.Date;

public class KnowledgeCollection implements Serializable {
    private Long id;

    private Long knowledgeId;

    private Date createtime;

    private Integer source;

    private Long categoryId;

    private String collectionTags;

    private String collectionComment;

    private Long userid;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getKnowledgeId() {
        return knowledgeId;
    }

    public void setKnowledgeId(Long knowledgeId) {
        this.knowledgeId = knowledgeId;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCollectionTags() {
        return collectionTags;
    }

    public void setCollectionTags(String collectionTags) {
        this.collectionTags = collectionTags;
    }

    public String getCollectionComment() {
        return collectionComment;
    }

    public void setCollectionComment(String collectionComment) {
        this.collectionComment = collectionComment;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }
}