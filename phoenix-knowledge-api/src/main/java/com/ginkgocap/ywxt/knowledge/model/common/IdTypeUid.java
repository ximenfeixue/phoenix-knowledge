package com.ginkgocap.ywxt.knowledge.model.common;

/**
 * Created by oem on 6/2/17.
 */
public class IdTypeUid extends IdType {
    private long uid;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public IdTypeUid(final long id, int type, long uid) {
        this.setId(id);
        this.setType(type);
        this.uid = uid;
    }
}
