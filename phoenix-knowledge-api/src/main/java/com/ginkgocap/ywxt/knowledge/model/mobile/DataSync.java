package com.ginkgocap.ywxt.knowledge.model.mobile;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gintong on 2016/7/9.
 */
public class DataSync<T> implements Serializable
{
	private static final long serialVersionUID = -6781604493056034407L;

    //the unique
    private long id;

    //Request content
    private T data;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public DataSync(long id, T data) {
        this.id = id;
        this.data = data;
    }
}
