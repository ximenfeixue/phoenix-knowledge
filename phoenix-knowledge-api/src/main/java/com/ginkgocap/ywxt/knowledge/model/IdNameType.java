package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;

/**
 * Created by gintong on 2016/7/7.
 */
public class IdNameType extends IdName implements Serializable
{
	private static final long serialVersionUID = -8513921341380189475L;

	public IdNameType(long id, String name,long typeId) {
        super(id, name);
        this.typeId = typeId;
    }

    public long getTypeId() {
        return typeId;
    }

    public void setTypeId(long typeId) {
        this.typeId = typeId;
    }

    private long typeId;
}
