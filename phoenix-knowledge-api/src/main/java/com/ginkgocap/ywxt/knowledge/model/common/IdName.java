package com.ginkgocap.ywxt.knowledge.model.common;

import java.io.Serializable;

/**
 * Created by gintong on 2016/7/7.
 */
public class IdName implements Serializable {

	private static final long serialVersionUID = -4719512970628605310L;
	
	public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IdName(long id, String name)
    {
        this.id = id;
        this.name = name;
    }

    private long id;
    private String name;
}
