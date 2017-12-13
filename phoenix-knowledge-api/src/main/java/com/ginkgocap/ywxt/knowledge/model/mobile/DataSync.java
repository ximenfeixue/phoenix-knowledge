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

    private long time;

    private int resType = 0;

    //Request content
    private T data;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getResType() {
        return resType;
    }

    public void setResType(int resType) {
        this.resType = resType;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public DataSync() {    }

    public DataSync(long id, T data) {
        this.id = id;
        this.resType = 0;
        this.time = System.currentTimeMillis();
        this.data = data;
    }

    public DataSync(long id, int resType, T data) {
        this.id = id;
        this.resType = resType;
        this.time = System.currentTimeMillis();
        this.data = data;
    }

    public static enum EResType {
        EMsgNotify(1),
        EPerm(2),
        EIdTypeUid(3),
        EDynamic(4),
        EResMsg(5);

        private int value;

        private EResType(int value) {
            this.value = value;
        }

        public int value() {
            return this.value;
        }
    }
}
