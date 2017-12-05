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
     * 知识Id
     */
    private long knowledgeId;
    /**
     * 创建人id
     */
    private long ownerId;
    /**
     * 知识标题
     */
    private String knowledgeTitle;
    /**
     * 知识栏目Id
     */
    private int columnId;

    /**
     * 创建时间
     */
    private long createTime;
    /**
     * 知识栏目类型
     */
    private short type;

    //来源(1：自己，2：好友，3：金桐脑，4：全平台，5：组织)
    private short source;

    //是否私密，1是，0否
    private short privated;

    //分享id
    private long shareId;

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
    public int getColumnId() {
        return columnId;
    }
    public void setColumnId(int columnId) {
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

    public short getSource() {
        return source;
    }

    public void setSource(short source) {
        this.source = source;
    }

    public short getPrivated() {
        return privated;
    }

    public void setPrivated(short privated) {
        this.privated = privated;
    }

    public long getShareId() {
        return shareId;
    }

    public void setShareId(long shareId) {
        this.shareId = shareId;
    }
}
