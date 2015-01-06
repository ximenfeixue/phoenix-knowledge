package com.ginkgocap.ywxt.knowledge.entity;

import java.io.Serializable;

public class ConnectionInfo implements Serializable {
    private Long id;

    private Long knowledgeid;

    private String tag;

    private Integer conntype;

    private Long connid;

    private String connname;

    private Long ownerid;

    private String owner;

    private String requirementtype;

    private String career;

    private String company;

    private String address;

    private String hy;

    private String columnpath;

    private Integer columntype;

    private String url;

    private String groupname;

    private String picpath;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getKnowledgeid() {
        return knowledgeid;
    }

    public void setKnowledgeid(Long knowledgeid) {
        this.knowledgeid = knowledgeid;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Integer getConntype() {
        return conntype;
    }

    public void setConntype(Integer conntype) {
        this.conntype = conntype;
    }

    public Long getConnid() {
        return connid;
    }

    public void setConnid(Long connid) {
        this.connid = connid;
    }

    public String getConnname() {
        return connname;
    }

    public void setConnname(String connname) {
        this.connname = connname;
    }

    public Long getOwnerid() {
        return ownerid;
    }

    public void setOwnerid(Long ownerid) {
        this.ownerid = ownerid;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getRequirementtype() {
        return requirementtype;
    }

    public void setRequirementtype(String requirementtype) {
        this.requirementtype = requirementtype;
    }

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHy() {
        return hy;
    }

    public void setHy(String hy) {
        this.hy = hy;
    }

    public String getColumnpath() {
        return columnpath;
    }

    public void setColumnpath(String columnpath) {
        this.columnpath = columnpath;
    }

    public Integer getColumntype() {
        return columntype;
    }

    public void setColumntype(Integer columntype) {
        this.columntype = columntype;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getPicpath() {
        return picpath;
    }

    public void setPicpath(String picpath) {
        this.picpath = picpath;
    }
}