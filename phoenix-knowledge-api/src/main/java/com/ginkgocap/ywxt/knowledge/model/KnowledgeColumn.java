package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;
import java.util.Date;

/**
 *   
 * <p>知识栏目</p>  
 * <p>于2014-8-20 由 边智伟 创建 </p>
 * @author  <p>当前负责人 边智伟</p>     
 * @since <p>1.2.1-SNAPSHOT</p> 
 *
 */
public class KnowledgeColumn implements Serializable {
    private static final long serialVersionUID = -5440480908931909931L;
    private long id;//分类id
    private String columnName;//分类名称
    private int parentColumnId;//父分类id
    private int createUserId;//用户id
    private String tag;//分类标签，分号分割
    private int level;//
    private String columnLevelPath;//分类层级路径
    private Date createTime;//创建时间
    private Date updateTime;//更新时间

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public int getParentColumnId() {
        return parentColumnId;
    }

    public void setParentColumnId(int parentColumnId) {
        this.parentColumnId = parentColumnId;
    }

    public int getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(int createUserId) {
        this.createUserId = createUserId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getColumnLevelPath() {
        return columnLevelPath;
    }

    public void setColumnLevelPath(String columnLevelPath) {
        this.columnLevelPath = columnLevelPath;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

}
