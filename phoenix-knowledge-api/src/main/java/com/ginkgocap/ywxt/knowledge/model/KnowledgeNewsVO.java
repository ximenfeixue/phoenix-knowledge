package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;

public class KnowledgeNewsVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long kId;
	private String title;
	private String content;
	private String pic;
	private String taskId;
	private String cpath;
	private String tags;
	private String catalogueIds;
	private String columnid;
	private String essence;
	private String selectedIds;
	private String shareMessage;
	private String columnType;
	private String columnPath;
	private String columnName;

	// 老知识id
	private long oid;
	// 价格
	private float price;

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getkId() {
		return kId;
	}

	public void setkId(Long kId) {
		this.kId = kId;
	}

	public String getColumnPath() {
		return columnPath;
	}

	public void setColumnPath(String columnPath) {
		this.columnPath = columnPath;
	}

	public String getColumnType() {
		return columnType;
	}

	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}

	public String getShareMessage() {
		return shareMessage;
	}

	public void setShareMessage(String shareMessage) {
		this.shareMessage = shareMessage;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getCpath() {
		return cpath;
	}

	public void setCpath(String cpath) {
		this.cpath = cpath;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getCatalogueIds() {
		return catalogueIds;
	}

	public void setCatalogueIds(String catalogueIds) {
		this.catalogueIds = catalogueIds;
	}

	public String getEssence() {
		return essence;
	}

	public void setEssence(String essence) {
		this.essence = essence;
	}

	public String getSelectedIds() {
		return selectedIds;
	}

	public void setSelectedIds(String selectedIds) {
		this.selectedIds = selectedIds;
	}

	public String getColumnid() {
		return columnid;
	}

	public void setColumnid(String columnid) {
		this.columnid = columnid;
	}

	public long getOid() {
		return oid;
	}

	public void setOid(long oid) {
		this.oid = oid;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

}
