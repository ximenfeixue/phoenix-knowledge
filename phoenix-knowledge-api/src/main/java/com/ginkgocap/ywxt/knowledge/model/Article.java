package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;

public class Article  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7827738358308956910L;

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getArticleTitle() {
		return articleTitle;
	}
	public void setArticleTitle(String articleTitle) {
		this.articleTitle = articleTitle;
	}
	public String getArticleContent() {
		return articleContent;
	}
	public void setArticleContent(String articleContent) {
		this.articleContent = articleContent;
	}
	public long getCategoryid() {
		return categoryid;
	}
	public void setCategoryid(long categoryid) {
		this.categoryid = categoryid;
	}
	public String getSortId() {
		return sortId;
	}
	public void setSortId(String sortId) {
		this.sortId = sortId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getPubdate() {
		return pubdate;
	}
	public void setPubdate(String pubdate) {
		this.pubdate = pubdate;
	}
	public String getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
	public String getEssence() {
		return essence;
	}
	public void setEssence(String essence) {
		this.essence = essence;
	}
	public String getRecycleBin() {
		return recycleBin;
	}
	public void setRecycleBin(String recycleBin) {
		this.recycleBin = recycleBin;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public long getClickNum() {
		return clickNum;
	}
	public void setClickNum(long clickNum) {
		this.clickNum = clickNum;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getIsNew() {
		return isNew;
	}
	public void setIsNew(String isNew) {
		this.isNew = isNew;
	}
	/** 主键 */
	private long id;
	/** phoenix_user.tb_user.id */
	private long uid;
	/** 文章作者，默认为当前登录用户的name */
	private String author;
	/** 文章来源 */
	private String source;
	/** 文章标题 */
	private String articleTitle;
	/** 文章内容 */
	private String articleContent;
	/** phoenix_knowledge.tb_category.id */
	private long categoryid;
	/** phoenix_knowledge.tb_category.sortId */
	private String sortId;
	/** phoenix_knowledge.tb_category.categoryName */
	private String categoryName;
	/** 文章发布时间	 */
	private String pubdate;
	/** 最后修改时间 */
	private String modifyTime;
	/** 是否加精 0:否 1:是 */
	private String essence;
	/** 是否标志为回收站文章0:否  1:是 */
	private String recycleBin;
	/** 文章状态 */
	private String state;
	/** 文章点击次数 */
	private long clickNum;
	/** 与文章附件表关联 */
	private String taskId;
	/** 是否是最新的文章 在发布于三天之内的和最新修改于三天之内的为最新文章  */
	private String isNew;
}
