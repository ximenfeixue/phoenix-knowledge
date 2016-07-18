package com.ginkgocap.ywxt.knowledge.model.mobile;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gintong on 2016/7/9.
 */
public class DataSync implements Serializable
{
	private static final long serialVersionUID = -6781604493056034407L;

	//the unique
    private long id;

    //resourceId
    private long resId;

    //execute userId
    private long userId;

    //1. add  2. update, 3, delete
    private short action;

    //1. tag, 2. directory, 3. asso, 4. permission, 5,dynamic
    private short idType;

    //Request content
    private String content;

    public long getResId() {
        return resId;
    }

    public void setResId(long resId) {
        this.resId = resId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public short getAction() {
        return action;
    }

    public void setAction(short action) {
        this.action = action;
    }

    public short getIdType() {
        return idType;
    }

    public void setIdType(short idType) {
        this.idType = idType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
