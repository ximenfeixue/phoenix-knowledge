package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;

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
	private int status;

	/** 排序ID，九位一级 如000000001000000001,为一级分类下的第一个分类 */
	private String sortId;

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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
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
