/**
 * Copyright (c) 2011 银杏资本.
 * All Rights Reserved. 保留所有权利.
 */
package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.ginkgocap.ywxt.knowledge.model.mobile.JTFile;

public class BusinessRequirement implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1657386315607789949L;

	private Integer id;//
	
	private Integer userid;//需求发布人id，发布时为空
	
	private String name;//需求发布人姓名，发布时为空
	
	private String userAvatar;//需求发布人头像
	
	private String publishTime;//需求发布时间，发布时为空
	
	private String title;//任务标题
	
	private String type;//0-投资，1-融资
	
	private String content;//简短描述下内容
	
	private JTContactMini principal;//负责人
	
	private String deadline;//完成期限
	
	private Integer progress; //当前进度，百分比，整型
	
	private List<ConnectionsMini> listConnectionsMini;
	
	private InvestKeyword investKeyword;
	
	private List<JTFile> listJTFile;
	
	private String taskId;//附件索引
	
	private List<AffairMini> listRelatedTaskMini;//相关任务
	private List<RequirementMini> listMatchRequirementMini;
	private List<ConnectionsMini> listMatchConnectionsMini;
	private List<KnowledgeMini> listMatchKnowledgeMini;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public JTContactMini getPrincipal() {
		return principal;
	}

	public void setPrincipal(JTContactMini principal) {
		this.principal = principal;
	}

	public String getDeadline() {
		return deadline;
	}

	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}

	public Integer getProgress() {
		return progress;
	}

	public void setProgress(Integer progress) {
		this.progress = progress;
	}

	public List<JTFile> getListJTFile() {
		return listJTFile;
	}

	public void setListJTFile(List<JTFile> listJTFile) {
		this.listJTFile = listJTFile;
	}

	public List<RequirementMini> getListMatchRequirementMini() {
		return listMatchRequirementMini;
	}

	public void setListMatchRequirementMini(
			List<RequirementMini> listMatchRequirementMini) {
		this.listMatchRequirementMini = listMatchRequirementMini;
	}

	public List<ConnectionsMini> getListMatchConnectionsMini() {
		return listMatchConnectionsMini;
	}

	public void setListMatchConnectionsMini(
			List<ConnectionsMini> listMatchConnectionsMini) {
		this.listMatchConnectionsMini = listMatchConnectionsMini;
	}

	public List<KnowledgeMini> getListMatchKnowledgeMini() {
		return listMatchKnowledgeMini;
	}

	public void setListMatchKnowledgeMini(List<KnowledgeMini> listMatchKnowledgeMini) {
		this.listMatchKnowledgeMini = listMatchKnowledgeMini;
	}

	public List<ConnectionsMini> getListConnectionsMini() {
		return listConnectionsMini;
	}

	public void setListConnectionsMini(List<ConnectionsMini> listConnectionsMini) {
		this.listConnectionsMini = listConnectionsMini;
	}

	public InvestKeyword getInvestKeyword() {
		return investKeyword;
	}

	public void setInvestKeyword(InvestKeyword investKeyword) {
		this.investKeyword = investKeyword;
	}

	public List<AffairMini> getListRelatedTaskMini() {
		return listRelatedTaskMini;
	}

	public void setListRelatedTaskMini(List<AffairMini> listRelatedTaskMini) {
		this.listRelatedTaskMini = listRelatedTaskMini;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getUserAvatar() {
		return userAvatar;
	}

	public void setUserAvatar(String userAvatar) {
		this.userAvatar = userAvatar;
	}
	
	/**
	 * @author zhangzhen
	 * 如果数据为空返回null
	 * 
	 * 指导使用方法
	 * JSONObject j = JSONObject.fromObject(requestJson);
	 * String jsonData = j.getString("Entity");
	 * */
	public static BusinessRequirement getByJsonString(String jsonEntity) {
		if(jsonEntity.equals("{}")) {
			return null; //无数据判断
		}
		return JSON.parseObject(jsonEntity, BusinessRequirement.class);
	}
	
	/**
	 * @author zhangzhen
	 * 如果数据为空返回null
	 * 
	 * 指导使用方法
	 * JSONObject j = JSONObject.fromObject(requestJson);
	 * Object jsonData = j.get("Entity");
	 * */
	public static BusinessRequirement getByJsonObject(Object jsonEntity) {
		return getByJsonString(jsonEntity.toString());
	}
	
	/**
	 * @author zhangzhen
	 * 如果没有数据，返回空数组
	 * 
	 * 指导使用方法
	 * JSONObject j = JSONObject.fromObject(requestJson);
	 * String jsonData = j.getString("Entity");
	 * */
	public static List<BusinessRequirement> getListByJsonString(String object) {
		return JSON.parseArray(object, BusinessRequirement.class);
	}
	
	/**
	 * @author zhangzhen
	 * @CreateTime 2014-11-11
	 * 如果没有数据，返回空数组
	 * 
	 * 指导使用方法 
	 * JSONObject j = JSONObject.fromObject(requestJson);
	 * Object jsonData = j.get("EntityList");
	 * */
	public static List<BusinessRequirement> getListByJsonObject(Object object) {
		return getListByJsonString(object.toString());
	}
}
