package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;

/**
 * Created by Admin on 2016/4/15.
 */
public class KnowledgeCollect implements Serializable {

    private static final long serialVersionUID = 3224229209951867532L;
    /**
     * 主键
     */
    private long id;
    /**
     * 需求服务类型
     */
    private long knowledgeId;
    /**
     * 关系用户id
     */
    private long ownerId;
    /**
     * 需求服务标题
     */
    private String knowledgeTitle;
    /**
     * 需求服务类型id
     */
    private short columnId;

    /**
     * 创建时间
     */
    private long createTime;
    /**
     * 拥有者类型（0创建者，1收藏者）
     */
    private short type;

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public long getKnowledgeId() {
        return knowledgeId;
    }
    public void setKnowledgeId(long knowledgeId) {
        this.knowledgeId = knowledgeId;
    }
    public String getKnowledgeTitle() {
        return knowledgeTitle;
    }
    public void setKnowledgeTitle(String knowledgeTitle) {
        this.knowledgeTitle = knowledgeTitle;
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
    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
    public short getType() {
        return type;
    }
    public void setType(short type) {
        this.type = type;
    }
    public long getOwnerId() {
        return ownerId;
    }
    public void setOwnerId(long userId) {
        this.ownerId = userId;
    }
}
