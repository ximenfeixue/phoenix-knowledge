package com.ginkgocap.ywxt.knowledge.model;

/**
 * Created by wang fei on 2018/1/24.
 */
public class ContributeData {

    private Long id;

    private short columnType;

    private Long organId;

    private Byte privated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public short getColumnType() {
        return columnType;
    }

    public void setColumnType(short columnType) {
        this.columnType = columnType;
    }

    public Long getOrganId() {
        return organId;
    }

    public void setOrganId(Long organId) {
        this.organId = organId;
    }

    public Byte getPrivated() {
        return privated;
    }

    public void setPrivated(Byte privated) {
        this.privated = privated;
    }
}
