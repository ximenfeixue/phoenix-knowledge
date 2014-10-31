package com.ginkgocap.ywxt.knowledge.entity;

import java.io.Serializable;
import java.util.Date;

public class KnowledgeRecycle implements Serializable {
    private Long knowledgeId;

    private String title;

    private String type;

    private Date createtime;

    private Long userid;

    private Long catetoryid;

    private static final long serialVersionUID = 1L;

    public Long getKnowledgeId() {
        return knowledgeId;
    }

    public void setKnowledgeId(Long knowledgeId) {
        this.knowledgeId = knowledgeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Long getCatetoryid() {
        return catetoryid;
    }

    public void setCatetoryid(Long catetoryid) {
        this.catetoryid = catetoryid;
    }
}