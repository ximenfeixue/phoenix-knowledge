package com.ginkgocap.ywxt.knowledge.model;

public class KnowledgeBaseVO {

	private String columType;
	/** 知识对象 **/
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

}
