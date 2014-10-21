package com.ginkgocap.ywxt.knowledge.entity;

import java.io.Serializable;
import java.util.Date;

public class KnowledgeCollection implements Serializable {
    private Long id;

    private Long knowledgeId;

    private Long columnId;

    private Date timestamp;

    private String knowledgetype;

    private String source;

    private Long categoryId;

    private String collectionTags;

    private String collectionComment;

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

    public Long getColumnId() {
        return columnId;
    }

    public void setColumnId(Long columnId) {
        this.columnId = columnId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getKnowledgetype() {
        return knowledgetype;
    }

    public void setKnowledgetype(String knowledgetype) {
        this.knowledgetype = knowledgetype;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
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
}