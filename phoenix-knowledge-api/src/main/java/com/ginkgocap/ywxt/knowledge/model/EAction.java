package com.ginkgocap.ywxt.knowledge.model;

/**
 * Created by oem on 3/3/17.
 */
public enum  EAction {
    EAdd(1),
    EDelete(2),
    EUpdate(3);

    private int value;
    private EAction(int value) {
        this.value = value;
    }

    public int value() { return value; }
}
