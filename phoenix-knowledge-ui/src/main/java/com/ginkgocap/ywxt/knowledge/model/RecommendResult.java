package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSON;

public class RecommendResult extends SearchResult implements Serializable {

	private static final long serialVersionUID = 3799748746576697718L;

	private String knowledgeType; // 知识类型

	private String tagsScores;// 对接的人

	private String tags;// 标签
	
	private String desc;// 知识简介

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getKnowledgeType() {
		return knowledgeType;
	}

	public void setKnowledgeType(String knowledgeType) {
		this.knowledgeType = knowledgeType;
	}

	public String getTagsScores() {
		return tagsScores;
	}

	public void setTagsScores(String tagsScores) {
		this.tagsScores = tagsScores;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

}
