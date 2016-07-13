package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;

/**
 * Created by Chen Peifeng on 2016/4/7.
 */
public class KnowledgeComment implements Serializable{

    private static final long serialVersionUID = -6024880400982992427L;

    /** 评论id **/
    private long id;
    /** 知识id **/
    private long knowledgeId;
    /** 栏目id **/
    private int columnId;

    /** 评论者id **/
    private long ownerId;

    /** 评论者 **/
    private String ownerName;

    /** 评论时间 **/
    private long createTime;

    /** 评论内容 **/
    private String content;

    /** 是否可见 0 不可见， 1 可见**/
    private Integer visible;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

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

    public int getColumnId() {
        return columnId;
    }

    public void setColumnId(int columnId) {
        this.columnId = columnId;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public Integer getVisible() {
        return visible;
    }

    public void setVisible(Integer visible) {
        this.visible = visible;
    }
}
