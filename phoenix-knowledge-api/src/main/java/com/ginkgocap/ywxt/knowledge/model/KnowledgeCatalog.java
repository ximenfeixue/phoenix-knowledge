package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;

/** 
 * <p>知识文章目录</p>  
 * <p>于2014-8-19 由 bianzhiwei 创建 </p>
 * @author  <p>当前负责人 bianzhiwei</p>     
 * @since <p>1.2.1-SNAPSHOT</p> 
 */
public class KnowledgeCatalog implements Serializable {
    
    private static final long serialVersionUID = 3051558353151190781L;

    private long id;//主键(自增)
    private long knowledgeId;//知识id
    private String catalogName;//目录名称
    private long pid;//上级目录id
    private int level;//目录级别
    private int knowledgeType;//默认0：为自定义,1：资讯，2：投融工具，3：行业，4：经典案例，5：图书报告，6：资产管理，7：宏观，8：观点，9：判例，10，法律法规
    private String levelPath;//目录层级路径

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getKnowledgeId() {
        return knowledgeId;
    }

    public void setKnowledgeId(long knowledgeId) {
        this.knowledgeId = knowledgeId;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getKnowledgeType() {
        return knowledgeType;
    }

    public void setKnowledgeType(int knowledgeType) {
        this.knowledgeType = knowledgeType;
    }

    public String getLevelPath() {
        return levelPath;
    }

    public void setLevelPath(String levelPath) {
        this.levelPath = levelPath;
    }


}
