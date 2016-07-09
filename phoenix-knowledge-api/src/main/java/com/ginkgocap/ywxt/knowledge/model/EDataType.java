package com.ginkgocap.ywxt.knowledge.model;

/**
 * Created by gintong on 2016/7/9.
 */
public enum EDataType {
    ETag((short)1),
    EDirectory((short)2),
    EAssociate((short)3),
    EPermission((short)4);

    private short value;
    private EDataType(short value)
    {
        this.value = value;
    }

    public short getValue()
    {
        return value;
    }
}
