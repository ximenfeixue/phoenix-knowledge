package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;

/**
 * Created by gintong on 2016/7/11.
 */
public class KnowledgeStatics implements Serializable {
    private long knowledgeId;

    private long commentcount;

    private long sharecount;

    private long collectioncount;

    private long clickcount;

    private String title;

    private Short source;

    private short type;

    private static final long serialVersionUID = 1L;

    public long getKnowledgeId() {
        return knowledgeId;
    }

    public void setKnowledgeId(long knowledgeId) {
        this.knowledgeId = knowledgeId;
    }

    public long getCommentcount() {
        return commentcount;
    }

    public void setCommentcount(long commentcount) {
        this.commentcount = commentcount;
    }

    public long getSharecount() {
        return sharecount;
    }

    public void setSharecount(long sharecount) {
        this.sharecount = sharecount;
    }

    public long getCollectioncount() {
        return collectioncount;
    }

    public void setCollectioncount(long collectioncount) {
        this.collectioncount = collectioncount;
    }

    public long getClickcount() {
        return clickcount;
    }

    public void setClickcount(long clickcount) {
        this.clickcount = clickcount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Short getSource() {
        return source;
    }

    public void setSource(Short source) {
        this.source = source;
    }

    public short getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
    }
}
