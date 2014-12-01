package com.ginkgocap.ywxt.knowledge.entity;

import java.io.Serializable;

public class KnowledgeStatics implements Serializable {
    private Long knowledgeId;

    private Long commentcount;

    private Long sharecount;

    private Long collectioncount;

    private Long clickcount;

    private Short source;

    private Short type;

    private static final long serialVersionUID = 1L;

    public Long getKnowledgeId() {
        return knowledgeId;
    }

    public void setKnowledgeId(Long knowledgeId) {
        this.knowledgeId = knowledgeId;
    }

    public Long getCommentcount() {
        return commentcount;
    }

    public void setCommentcount(Long commentcount) {
        this.commentcount = commentcount;
    }

    public Long getSharecount() {
        return sharecount;
    }

    public void setSharecount(Long sharecount) {
        this.sharecount = sharecount;
    }

    public Long getCollectioncount() {
        return collectioncount;
    }

    public void setCollectioncount(Long collectioncount) {
        this.collectioncount = collectioncount;
    }

    public Long getClickcount() {
        return clickcount;
    }

    public void setClickcount(Long clickcount) {
        this.clickcount = clickcount;
    }

    public Short getSource() {
        return source;
    }

    public void setSource(Short source) {
        this.source = source;
    }

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }
}