package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;
import java.util.Date;

public class KnowledgeRCategory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9210031377998416429L;

	/** 主键 */
	private long id;

	/** 知识Id(tb_knowledge.id) */
	private long knowledgeid;

	/** 目录id */
	private long categoryid;

	/** 状态(1生效，0失效) */
	private String status;

	/** 排序ID，九位一级 如000000001000000001,为一级分类下的第一个分类 */
	private String sortId;

	private long userid;

	// 标题
	private String title;

	// 作者名称
	private String author;

	// 栏目路径
	private String path;

	// 分享者
	private String share_author;

	// 发表时间
	private Date createtime;

	// 标签
	private String tag;

	// 简介
	private String know_desc;

	// 栏目id
	private long column_id;

	// 封面图片地址
	private String pic_path;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getShare_author() {
		return share_author;
	}

	public void setShare_author(String share_author) {
		this.share_author = share_author;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getKnow_desc() {
		return know_desc;
	}

	public void setKnow_desc(String know_desc) {
		this.know_desc = know_desc;
	}

	public long getColumn_id() {
		return column_id;
	}

	public void setColumn_id(long column_id) {
		this.column_id = column_id;
	}

	public String getPic_path() {
		return pic_path;
	}

	public void setPic_path(String pic_path) {
		this.pic_path = pic_path;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getKnowledgeid() {
		return knowledgeid;
	}

	public void setKnowledgeid(long knowledgeid) {
		this.knowledgeid = knowledgeid;
	}

	public long getCategoryid() {
		return categoryid;
	}

	public void setCategoryid(long categoryid) {
		this.categoryid = categoryid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSortId() {
		return sortId;
	}

	public void setSortId(String sortId) {
		this.sortId = sortId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
