package com.ginkgocap.ywxt.knowledge.id;

import java.io.Serializable;

/**
 * Created by gintong on 2016/7/29.
 */
public class CloudConfig implements Serializable {

    public CloudConfig(int seq,String ip)
    {
        this.id = seq;
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;
    private String ip;
}
