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
	private String show_content;// 显示内容，大数据分析处理过的数据
	private int page_count;// 页码总数，默认为1，可以有多页的需要填写
	private String page_head;// 页头html数据，包含css
	private String page_bottom;// 页尾html数据，包含css
	private int state;// 1为有效，0为无效

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

	public String getShow_content() {
		return show_content;
	}

	public void setShow_content(String show_content) {
		this.show_content = show_content;
	}

	public int getPage_count() {
		return page_count;
	}

	public void setPage_count(int page_count) {
		this.page_count = page_count;
	}

	public String getPage_head() {
		return page_head;
	}

	public void setPage_head(String page_head) {
		this.page_head = page_head;
	}

	public String getPage_bottom() {
		return page_bottom;
	}

	public void setPage_bottom(String page_bottom) {
		this.page_bottom = page_bottom;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
