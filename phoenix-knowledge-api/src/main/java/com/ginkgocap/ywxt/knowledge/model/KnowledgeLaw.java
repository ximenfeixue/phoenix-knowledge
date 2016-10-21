package com.ginkgocap.ywxt.knowledge.model;

import com.ginkgocap.ywxt.knowledge.utils.KnowledgeConstant;
import com.ginkgocap.ywxt.user.model.User;

/**
 * 知识javaBean （法律法规）
 * 
 * @author caihe
 * 
 */
public class KnowledgeLaw extends Knowledge 
{
	private static final long serialVersionUID = 1L;
	// 老知识ID
	private long oid;

	/*
	// 法律法规-发文单位
	private String postUnit;

	// 法律法规-文号
	private String titanic;

	// 法律法规-发布日期
	private String submitTime;

	// 法律法规-执行日期
	private String performTime;

	public String getPostUnit() {
		return postUnit;
	}

	public void setPostUnit(String postUnit) {
		this.postUnit = postUnit;
	}

	public String getTitanic() {
		return titanic;
	}

	public void setTitanic(String titanic) {
		this.titanic = titanic;
	}

	public String getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(String submitTime) {
		this.submitTime = submitTime;
	}

	public String getPerformTime() {
		return performTime;
	}

	public void setPerformTime(String performTime) {
		this.performTime = performTime;
	}*/

	public long getOid() {
		return oid;
	}

	public void setOid(long oid) {
		this.oid = oid;
	}

}