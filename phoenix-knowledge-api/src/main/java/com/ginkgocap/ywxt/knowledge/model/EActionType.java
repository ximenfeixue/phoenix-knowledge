package com.ginkgocap.ywxt.knowledge.model;

/**
 * Created by gintong on 2016/7/9.
 */
public enum EActionType {
    EAddDynamic((short)1),
    EUpdate((short)2),
    EDelete((short)3),
    EDeleteKnowledgeTag((short)7),
    EDeleteKnowledgeDirectory((short)8);

    short value;
    private EActionType(short value)
    {
        this.value = value;
    }

    public short getValue()
    {
        return value;
    }
}
