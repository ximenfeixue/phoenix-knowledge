package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;

/**
 * Created by Chen Peifeng on 2016/4/15.
 */
public class KnowledgeReport implements Serializable {

    /**
     * 主键
     */
    private Long id;

    /**
     * 举报的知识id
     */
    private long knowledgeId;

    /**
     * 举报的栏目id
     */
    private short columnId;

    /**
     * 举报时间
     */
    private long createTime;

    /**
     * 举报描述
     */
    private String content;
    /**
     * 举报原因
     */
    private String reason;
    /**
     * 举报联系方式
     */
    private long contact;
    /**
     * 举报人id
     */
    private long userId;
    /**
     * 举报人用户名
     */
    private String userName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getContact() {
        return contact;
    }

    public void setContact(long contact) {
        this.contact = contact;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getKnowledgeId() {
        return knowledgeId;
    }

    public void setKnowledgeId(long knowledgeId) {
        this.knowledgeId = knowledgeId;
    }

    public short getColumnId() {
        return columnId;
    }

    public void setColumnId(short columnId) {
        this.columnId = columnId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

}
