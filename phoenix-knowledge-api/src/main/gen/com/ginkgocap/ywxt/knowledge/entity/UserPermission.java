package com.ginkgocap.ywxt.knowledge.entity;

import java.io.Serializable;
import java.util.Date;

public class UserPermission implements Serializable {
    private Long receiveUserId;

    private Long knowledgeId;

    private Long sendUserId;

    private Integer type;

    private String mento;

    private Date createtime;

    private Short columnType;

    private Long columnId;

    private static final long serialVersionUID = 1L;

    public Long getReceiveUserId() {
        return receiveUserId;
    }

    public void setReceiveUserId(Long receiveUserId) {
        this.receiveUserId = receiveUserId;
    }

    public Long getKnowledgeId() {
        return knowledgeId;
    }

    public void setKnowledgeId(Long knowledgeId) {
        this.knowledgeId = knowledgeId;
    }

    public Long getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(Long sendUserId) {
        this.sendUserId = sendUserId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getMento() {
        return mento;
    }

    public void setMento(String mento) {
        this.mento = mento;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Short getColumnType() {
        return columnType;
    }

    public void setColumnType(Short columnType) {
        this.columnType = columnType;
    }

    public Long getColumnId() {
        return columnId;
    }

    public void setColumnId(Long columnId) {
        this.columnId = columnId;
    }
}