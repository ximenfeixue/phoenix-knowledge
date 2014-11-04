package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class UserPermissionMongo implements Serializable {
	
	private static final long serialVersionUID = 5092135711686623071L;
	
	private String id;
	
	private List<Long> receiveUserId;
	
	private String receiveName;
	
	private String sendUserName;

	private Long knowledgeId;

    private String title;
    
    private String desc;
    
    private String tags;
    
    private String picPath;
    
    private Long sendUserId;

    private String mento; //留言
    
    private String createtime; 

    private Short columnType; 

    private Long columnId;

    public String getReceiveName() {
 		return receiveName;
 	}

 	public void setReceiveName(String receiveName) {
 		this.receiveName = receiveName;
 	}
    
    public List<Long> getReceiveUserId() {
		return receiveUserId;
	}

	public void setReceiveUserId(List<Long> receiveUserId) {
		this.receiveUserId = receiveUserId;
	}

	public Long getKnowledgeId() {
		return knowledgeId;
	}

	public void setKnowledgeId(Long knowledgeId) {
		this.knowledgeId = knowledgeId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	public Long getSendUserId() {
		return sendUserId;
	}

	public void setSendUserId(Long sendUserId) {
		this.sendUserId = sendUserId;
	}

	public String getMento() {
		return mento;
	}

	public void setMento(String mento) {
		this.mento = mento;
	}

	public Short getColumnType() {
		return columnType;
	}

	public void setColumnType(Short columnType) {
		this.columnType = columnType;
	}

	public Long getColumnId() {
		return columnId;
	}

	public void setColumnId(Long columnId) {
		this.columnId = columnId;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getSendUserName() {
		return sendUserName;
	}

	public void setSendUserName(String sendUserName) {
		this.sendUserName = sendUserName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}