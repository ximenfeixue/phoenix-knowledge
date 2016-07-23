/**
 * Copyright (c) 2011 银杏资本.
 * All Rights Reserved. 保留所有权利.
 */
package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSON;

public class Task implements Serializable {

	private static final long serialVersionUID = 8779195815674858510L;
	
	private Integer id;//
	
	private int userid;//需求发布人id，发布时为空
	
	private String name;//需求发布人姓名，发布时为空
	
	private String userAvatar;//需求发布人头像
	
	private String publishTime;//需求发布时间，发布时为空
	
	private String title;//任务标题
	
	private String content;//简短描述下内容
	
	private JTContactMini principal;//负责人
	
	private String deadline;//完成期限
	
	private String jobTitle;//个人用户存放person对象，公司职位",
	
	private String company;//所在公司	
	
	private String image;//用户头像
	
	private Integer progress; //当前进度，百分比，整型
	
	private String taskId;//附件索引

//	private List<JTContact> listJTContact;
	
	private List<JTContactMini> listJoinJTContactMini;
	
	private List<ConnectionsMini> listConnectionsMini;

	private InvestKeyword investKeyword;
	
	private List<JTFile> listJTFile;
	
//	private List<BusinessRequirement> listBusinessRequirement;
	
	private List<AffairMini> listRelatedBusinessRequirementMini;//关联的业务需求
	
	private List<AffairMini> listRelatedProjectMini;//关联的项目
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
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

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Integer getProgress() {
		return progress;
	}

	public void setProgress(Integer progress) {
		this.progress = progress;
	}

	public InvestKeyword getInvestKeyword() {
		return investKeyword;
	}

	public void setInvestKeyword(InvestKeyword investKeyword) {
		this.investKeyword = investKeyword;
	}

	public List<JTFile> getListJTFile() {
		return listJTFile;
	}

	public void setListJTFile(List<JTFile> listJTFile) {
		this.listJTFile = listJTFile;
	}

	public List<JTContactMini> getListJoinJTContactMini() {
		return listJoinJTContactMini;
	}

	public void setListJoinJTContactMini(List<JTContactMini> listJoinJTContactMini) {
		this.listJoinJTContactMini = listJoinJTContactMini;
	}

	public List<ConnectionsMini> getListConnectionsMini() {
		return listConnectionsMini;
	}

	public void setListConnectionsMini(List<ConnectionsMini> listConnectionsMini) {
		this.listConnectionsMini = listConnectionsMini;
	}

	public List<AffairMini> getListRelatedBusinessRequirementMini() {
		return listRelatedBusinessRequirementMini;
	}

	public void setListRelatedBusinessRequirementMini(
			List<AffairMini> listRelatedBusinessRequirementMini) {
		this.listRelatedBusinessRequirementMini = listRelatedBusinessRequirementMini;
	}

	public List<AffairMini> getListRelatedProjectMini() {
		return listRelatedProjectMini;
	}

	public void setListRelatedProjectMini(List<AffairMini> listRelatedProjectMini) {
		this.listRelatedProjectMini = listRelatedProjectMini;
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
	public static Task getByJsonString(String jsonEntity) {
		if(jsonEntity.equals("{}")) {
			return null; //无数据判断
		}
		return JSON.parseObject(jsonEntity, Task.class);
	}
	
	/**
	 * @author zhangzhen
	 * 如果数据为空返回null
	 * 
	 * 指导使用方法
	 * JSONObject j = JSONObject.fromObject(requestJson);
	 * Object jsonData = j.get("Entity");
	 * */
	public static Task getByJsonObject(Object jsonEntity) {
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
	public static List<Task> getListByJsonString(String object) {
		return JSON.parseArray(object, Task.class);
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
	public static List<Task> getListByJsonObject(Object object) {
		return getListByJsonString(object.toString());
	}
}
