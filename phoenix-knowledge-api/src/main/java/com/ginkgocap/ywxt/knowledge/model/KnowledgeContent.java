package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;

/**
 * <p>
 * 知识内容
 * </p>
 * 
 */
public class KnowledgeContent implements Serializable {
	private static final long serialVersionUID = -5440480908931909931L;
	private long id;// 内容id
	private long knowledgeid;// 知识ID
	private String content;// 知识内容
	private int page_count;// 页码总数，默认为1，可以有多页的需要填写
	private String status;// 1为有效，0为无效
	private String htmlcontent;// html内容

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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getPage_count() {
		return page_count;
	}

	public void setPage_count(int page_count) {
		this.page_count = page_count;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getHtmlcontent() {
		return htmlcontent;
	}

	public void setHtmlcontent(String htmlcontent) {
		this.htmlcontent = htmlcontent;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
