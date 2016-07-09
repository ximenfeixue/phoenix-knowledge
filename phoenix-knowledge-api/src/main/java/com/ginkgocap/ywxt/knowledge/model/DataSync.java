package com.ginkgocap.ywxt.knowledge.model;

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

    //1. add  2. update, 3, delete
    private short action;

    //1. tag, 2. directory, 3. asso, 4. permission
    private short idType;

    private List<KnowledgeUnique> ids;

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

    public List<KnowledgeUnique> getIds() {
        return ids;
    }

    public void setIds(List<KnowledgeUnique> ids) {
        this.ids = ids;
    }
}
