package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Transient;



/**
 *   
 * <p>知识栏目</p>  
 * <p>于2014-8-20 由 边智伟 创建 </p>
 * @author  <p>当前负责人 guangyuan</p>     
 * @since <p>1.2.1-SNAPSHOT</p> 
 *
 */
public class KnowledgeColumn implements Serializable {
    
    private static final long serialVersionUID = -5440480908931909931L;
    
    private Long id;//分类id
    private String columnName;//分类名称
    private Long parentColumnId;//父分类id 在设值时数字后边一定要加上l
    private Long createUserId;//用户id
    private Integer level;//
    private String columnLevelPath;//层级路径
   // private String pathName;//层级路径对应的文字名称
    private Date createTime;//创建时间
    private Date updateTime;//更新时间
    private Integer delStatus=0; //删除状态    0-正常  1-已删除
    private Integer subscribeCount=0; //订阅数量
    
    @Transient
    private List<KnowledgeColumn> list;
    @Transient 
    private String tag;//分类标签，分号分割
//    private Object subscribers;  //订阅人列表
    
    @Transient 
    private Integer kcType;
    
    public KnowledgeColumn() {
    }
    
    public List<KnowledgeColumn> getList() {
        return list;
    }

    public void setList(List<KnowledgeColumn> list) {
        this.list = list;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getColumnName() {
        return columnName;
    }
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
    public Long getParentColumnId() {
        return parentColumnId;
    }
    public void setParentColumnId(Long parentColumnId) {
        this.parentColumnId = parentColumnId;
    }
    public Long getCreateUserId() {
        return createUserId;
    }
    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }
    public String getTag() {
        return tag;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }
    public Integer getLevel() {
        return level;
    }
    public void setLevel(Integer level) {
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
    public Integer getDelStatus() {
        return delStatus;
    }
    public void setDelStatus(Integer delStatus) {
        this.delStatus = delStatus;
    }
    public Integer getSubscribeCount() {
        return subscribeCount;
    }
    public void setSubscribeCount(Integer subscribeCount) {
        this.subscribeCount = subscribeCount;
    }



    public Integer getKcType() {
        return kcType;
    }

    public void setKcType(Integer kcType) {
        this.kcType = kcType;
    }
    
}
