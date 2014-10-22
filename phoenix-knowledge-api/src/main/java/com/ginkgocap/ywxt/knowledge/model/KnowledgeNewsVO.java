package com.ginkgocap.ywxt.knowledge.model;

public class KnowledgeNewsVO {
	private String title;
	private String content;
	private String pic;
	private String taskId;
	private String cpath;
	private String tags;
	private String catalogueIds;
	private Long columnid;
	private String essence;
	private String selectedIds;
	private Integer submittype;
	private String shareMessage;
	private String columnType;

	
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

	public Integer getSubmittype() {
		return submittype;
	}

	public void setSubmittype(Integer submittype) {
		this.submittype = submittype;
	}

	public Long getColumnid() {
		return columnid;
	}

	public void setColumnid(Long columnid) {
		this.columnid = columnid;
	}

}
