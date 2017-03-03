package com.ginkgocap.ywxt.knowledge.model.common;

/**
 * Created by oem on 3/3/17.
 */
public enum EModuleType {
    ETag(1, "tagList"),
    EDirectory(2, "directorys"),
    EAssociate(3, "assoList"),
    EPermission(4, "permission");

    private int value;
    private String keyWord;

    private EModuleType(int value, String keyWord) {
        this.value = value;
        this.keyWord = keyWord;
    }

    public int value() {return value; }

    public String keyWord() { return keyWord; }
}
