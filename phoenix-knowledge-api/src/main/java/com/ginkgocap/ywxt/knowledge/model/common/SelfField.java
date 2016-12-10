package com.ginkgocap.ywxt.knowledge.model.common;

import java.io.Serializable;

/**
 * Created by oem on 12/5/16.
 */
public class SelfField implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String name;
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public SelfField() {}

    public SelfField(String name,String value)
    {
        this.name = name;
        this.value = value;
    }
}
