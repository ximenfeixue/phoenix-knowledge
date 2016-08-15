package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;

/**
 * Created by gintong on 2016/8/15.
 */
public class KnowledgeBaseSync implements Serializable
{
	private static final long serialVersionUID = 803307540389772055L;

	//Knowledge Id
    private long id;

    //是否私密，1是，0否
    private short privated;

    //1, add, 2, update, 3, delete
    private short action;

    public KnowledgeBaseSync(long id,short privated,short action)
    {
        this.id = id;
        this.privated = privated;
        this.action = action;
    }
    public short getPrivated() {
        return privated;
    }

    public void setPrivated(short privated) {
        this.privated = privated;
    }

    public short getAction() {
        return action;
    }

    public void setAction(short action) {
        this.action = action;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
