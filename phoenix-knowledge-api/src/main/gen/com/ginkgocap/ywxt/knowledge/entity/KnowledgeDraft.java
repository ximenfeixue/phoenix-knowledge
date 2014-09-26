package com.ginkgocap.ywxt.knowledge.entity;

import java.io.Serializable;
import java.util.Date;

public class KnowledgeDraft implements Serializable {
    private Long knowledgeId;

    private String draftname;

    private Integer drafttype;

    private Date createtime;

    private Long userid;

    private static final long serialVersionUID = 1L;

    public Long getKnowledgeId() {
        return knowledgeId;
    }

    public void setKnowledgeId(Long knowledgeId) {
        this.knowledgeId = knowledgeId;
    }

    public String getDraftname() {
        return draftname;
    }

    public void setDraftname(String draftname) {
        this.draftname = draftname;
    }

    public Integer getDrafttype() {
        return drafttype;
    }

    public void setDrafttype(Integer drafttype) {
        this.drafttype = drafttype;
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
}