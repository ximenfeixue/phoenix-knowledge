package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;
import java.util.Date;

public class ColumnVO implements Serializable {
	
    private Long id;

    private String columnname;

    private Long userId;

    private Long parentId;

    private Date createtime;

    private String pathName;

    private String columnLevelPath;

    private Byte delStatus;

    private Date updateTime;

    private Long subscribeCount;

    private Short type;
    
    private Boolean ishas;
    
    private int state;

    private static final long serialVersionUID = 1L;
    
    public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getColumnname() {
        return columnname;
    }

    public void setColumnname(String columnname) {
        this.columnname = columnname;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    public String getColumnLevelPath() {
        return columnLevelPath;
    }

    public void setColumnLevelPath(String columnLevelPath) {
        this.columnLevelPath = columnLevelPath;
    }

    public Byte getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(Byte delStatus) {
        this.delStatus = delStatus;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getSubscribeCount() {
        return subscribeCount;
    }

    public void setSubscribeCount(Long subscribeCount) {
        this.subscribeCount = subscribeCount;
    }

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }
    
    public Boolean getIshas() {
        return ishas;
    }

    public void setIshas(Boolean ishas) {
        this.ishas = ishas;
    }
}