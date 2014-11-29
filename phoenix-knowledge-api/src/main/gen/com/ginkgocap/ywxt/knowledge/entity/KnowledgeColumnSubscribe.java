package com.ginkgocap.ywxt.knowledge.entity;

import java.io.Serializable;
import java.util.Date;

public class KnowledgeColumnSubscribe implements Serializable {
    private Long id;

    private Long userId;

    private Long columnId;

    private Date subDate;

    private Short columnType;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getColumnId() {
        return columnId;
    }

    public void setColumnId(Long columnId) {
        this.columnId = columnId;
    }

    public Date getSubDate() {
        return subDate;
    }

    public void setSubDate(Date subDate) {
        this.subDate = subDate;
    }

    public Short getColumnType() {
        return columnType;
    }

    public void setColumnType(Short columnType) {
        this.columnType = columnType;
    }
}