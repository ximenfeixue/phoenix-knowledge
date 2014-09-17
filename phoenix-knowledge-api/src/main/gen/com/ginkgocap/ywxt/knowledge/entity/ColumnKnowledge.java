package com.ginkgocap.ywxt.knowledge.entity;

import java.io.Serializable;

public class ColumnKnowledge implements Serializable {
    private Long columnId;

    private Long knowledgeId;

    private Long userId;

    private Short type;

    private static final long serialVersionUID = 1L;

    public Long getColumnId() {
        return columnId;
    }

    public void setColumnId(Long columnId) {
        this.columnId = columnId;
    }

    public Long getKnowledgeId() {
        return knowledgeId;
    }

    public void setKnowledgeId(Long knowledgeId) {
        this.knowledgeId = knowledgeId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }
}