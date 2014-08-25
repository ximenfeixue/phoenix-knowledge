package com.ginkgocap.ywxt.knowledge.model;

import java.util.Date;

public class Knowledge {
	
	//id
    private Long id;

    //知识ID，兼容原来老数据
    private Long knowledgeid;
    
    //phoenix_user.tb_user.id，创建用户Id
    private Long userId;
    
    //知识类型，默认0：其他,1：资讯，2：投融工具，3：行业，4：经典案例，5：图书报告，6：资产管理，7：宏观，8：观点，9：判例，10，法律法规
    private Short knowledgetype;
    
    //知识作者，默认为当前登录用户的name
    private String knowledgeauthor;
    
    //知识来源
    private String knowledgesource;
    
    //知识内容前50个字符
    private String knowledgedesc;
    
    //知识标题
    private String knowledgetitle;
    
    //自定义标签，使用逗号分割
    private String tag;

    //phoenix_knowledge.tb_column.columnid，栏目ID
    private Long columnid;
    
    //phoenix_knowledge.tb_category.id，保存目录id
    private Long categoryid;
    
    //是否加精 0:否 1:是
    private String essence;
    
    //知识图片表Id
    private String pictureTaskId;
    
    //可见范围，默认0：为全平台可见，1：为自己可见，2：好友可见
    private String visible;
    
    //phoenix_knowledge.tb_category.sortId，目录顺序
    private String sortid;
    
    //知识发布时间
    private Date pubdate;
    
    //最后修改时间
    private Date modifytime;
    
    //是否标志为回收站知识0:否  1:是
    private String recyclebin;
    
    //0为有效，1为失效
    private String state;
    
    //知识点击次数
    private Long clicknum;
    
    //与知识附件表关联ID
    private String attatchmentTaskId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getKnowledgeid() {
        return knowledgeid;
    }

    public void setKnowledgeid(Long knowledgeid) {
        this.knowledgeid = knowledgeid;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Short getKnowledgetype() {
        return knowledgetype;
    }

    public void setKnowledgetype(Short knowledgetype) {
        this.knowledgetype = knowledgetype;
    }

    public String getKnowledgeauthor() {
        return knowledgeauthor;
    }

    public void setKnowledgeauthor(String knowledgeauthor) {
        this.knowledgeauthor = knowledgeauthor == null ? null : knowledgeauthor.trim();
    }

    public String getKnowledgesource() {
        return knowledgesource;
    }

    public void setKnowledgesource(String knowledgesource) {
        this.knowledgesource = knowledgesource == null ? null : knowledgesource.trim();
    }

    public String getKnowledgedesc() {
        return knowledgedesc;
    }

    public void setKnowledgedesc(String knowledgedesc) {
        this.knowledgedesc = knowledgedesc == null ? null : knowledgedesc.trim();
    }

    public String getKnowledgetitle() {
        return knowledgetitle;
    }

    public void setKnowledgetitle(String knowledgetitle) {
        this.knowledgetitle = knowledgetitle == null ? null : knowledgetitle.trim();
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag == null ? null : tag.trim();
    }

    public Long getColumnid() {
        return columnid;
    }

    public void setColumnid(Long columnid) {
        this.columnid = columnid;
    }

    public Long getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(Long categoryid) {
        this.categoryid = categoryid;
    }

    public String getEssence() {
        return essence;
    }

    public void setEssence(String essence) {
        this.essence = essence == null ? null : essence.trim();
    }

    public String getPictureTaskId() {
        return pictureTaskId;
    }

    public void setPictureTaskId(String pictureTaskId) {
        this.pictureTaskId = pictureTaskId == null ? null : pictureTaskId.trim();
    }

    public String getVisible() {
        return visible;
    }

    public void setVisible(String visible) {
        this.visible = visible == null ? null : visible.trim();
    }

    public String getSortid() {
        return sortid;
    }

    public void setSortid(String sortid) {
        this.sortid = sortid == null ? null : sortid.trim();
    }

    public Date getPubdate() {
        return pubdate;
    }

    public void setPubdate(Date pubdate) {
        this.pubdate = pubdate;
    }

    public Date getModifytime() {
        return modifytime;
    }

    public void setModifytime(Date modifytime) {
        this.modifytime = modifytime;
    }

    public String getRecyclebin() {
        return recyclebin;
    }

    public void setRecyclebin(String recyclebin) {
        this.recyclebin = recyclebin == null ? null : recyclebin.trim();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    public Long getClicknum() {
        return clicknum;
    }

    public void setClicknum(Long clicknum) {
        this.clicknum = clicknum;
    }

    public String getAttatchmentTaskId() {
        return attatchmentTaskId;
    }

    public void setAttatchmentTaskId(String attatchmentTaskId) {
        this.attatchmentTaskId = attatchmentTaskId == null ? null : attatchmentTaskId.trim();
    }
}