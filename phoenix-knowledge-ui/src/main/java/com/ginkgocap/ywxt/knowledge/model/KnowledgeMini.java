package com.ginkgocap.ywxt.knowledge.model;

/**
 * Created by gintong on 2016/7/6.
 */
import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSON;

public class KnowledgeMini implements Serializable {
    private static final long serialVersionUID = -1952086533379737984L;

    private long id;//相关知识列表，知识id

    private short type;

    private int columnType;

    private String url;//知识对应url

    private String title;//知识标题

    private long time;//发布时间

    private long userId;

    private String userName;

    private String sharedId;

    private String sharedName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public short getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
    }

    public int getColumnType() {
        return columnType;
    }

    public void setColumnType(int columnType) {
        this.columnType = columnType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSharedId() {
        return sharedId;
    }

    public void setSharedId(String sharedId) {
        this.sharedId = sharedId;
    }

    public String getSharedName() {
        return sharedName;
    }

    public void setSharedName(String sharedName) {
        this.sharedName = sharedName;
    }
}
