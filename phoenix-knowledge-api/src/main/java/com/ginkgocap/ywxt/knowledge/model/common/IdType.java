package com.ginkgocap.ywxt.knowledge.model.common;

import java.io.Serializable;

/**
 * Created by dev on 16-9-14.
 */
public class IdType implements Serializable
{
	private static final long serialVersionUID = 6447603915975611883L;

	private long id;
    private int type;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public IdType(long id, int type)
    {
        this.id = id;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
