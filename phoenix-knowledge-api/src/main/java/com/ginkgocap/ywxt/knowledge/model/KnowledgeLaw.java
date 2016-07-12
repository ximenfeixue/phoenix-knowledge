package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;

/**
 * Created by gintong on 2016/7/12.
 */
public class KnowledgeLaw extends KnowledgeDetail implements Serializable
{
    // 法律法规-发文单位
    private String postUnit;
    // 法律法规-文号
    private String titanic;

    // 法律法规-发布日期
    private long publishTime;

    // 法律法规-执行日期
    private long performTime;

    private String fileType;

    // 转换状态
    private int tranStatus;

    // WEB端和APP分开
    private int web;

    public String getTitanic() {
        return titanic;
    }

    public void setTitanic(String titanic) {
        this.titanic = titanic;
    }

    public String getPostUnit() {
        return postUnit;
    }

    public void setPostUnit(String postUnit) {
        this.postUnit = postUnit;
    }

    public long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(long publishTime) {
        this.publishTime = publishTime;
    }

    public long getPerformTime() {
        return performTime;
    }

    public void setPerformTime(long performTime) {
        this.performTime = performTime;
    }

    public int getTranStatus() {
        return tranStatus;
    }

    public void setTranStatus(int tranStatus) {
        this.tranStatus = tranStatus;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
