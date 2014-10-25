package com.ginkgocap.ywxt.knowledge.model;

public class KnowledgeCollectionVO {
	/** 知识ID **/
	private long kId;
	/** 目录ID集合 **/
	private String categoryIds;
	/** 标签 **/
	private String tags;
	/** 收藏评论 **/
	private String comment;
	/** 栏目类型 **/
	private String columType;
	/**知识对象**/
	private Knowledge knowledge;
	

	
	public Knowledge getKnowledge() {
		return knowledge;
	}

	public void setKnowledge(Knowledge knowledge) {
		this.knowledge = knowledge;
	}

	public String getColumType() {
		return columType;
	}

	public void setColumType(String columType) {
		this.columType = columType;
	}

	public long getkId() {
		return kId;
	}

	public void setkId(long kId) {
		this.kId = kId;
	}

	public String getCategoryIds() {
		return categoryIds;
	}

	public void setCategoryIds(String categoryIds) {
		this.categoryIds = categoryIds;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
