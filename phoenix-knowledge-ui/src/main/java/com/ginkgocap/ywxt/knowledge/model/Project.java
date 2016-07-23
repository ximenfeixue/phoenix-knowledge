package com.ginkgocap.ywxt.knowledge.model;


import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSON;


public class Project implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;//项目id,发布时为空

	private Integer userid;//发布人id，发布时为空
	
	private String name;//发布人姓名，发布时为空
	
	private String userAvatar;//需求发布人头像
	
	private String publishTime;//发布时间，发布时为空
	
	private String type;//0-投资，1-融资
	
	private String title;//标题
	
//	private String keyword;//关键字
	
	private String content;//简短描述下内容
	
	private JTContactMini principal;//负责人
	
	private JTContactMini maintainer;//维护人
	
	private String deadline;//完成期限
	
	private Integer progress;//当前进度，百分比，整型
	
	private Integer exeMode;//执行方式, 推介-1，承做-2，推介和承做-3",
	
	private Integer publishRange;//0-仅自己可见，1-对所有人公开，2-对listConnections里的人可见",
	
	private String taskId;//附件索引
	
	private List<JTContactMini> listJoinJTContactMini;//项目成员
	
	private List<ConnectionsMini> listConnectionsMini;//相关机构与联系人（相关关系）
	
//	private List<Connections> listMemberConnections;//项目成员
	
	private InvestKeyword investKeyword;
	
	private List<JTFile> listJTFile;
	
    private List<AffairMini> listRelatedRequirementMini;//关联需求
	private List<AffairMini> listRelatedTaskMini;//相关任务
	private List<RequirementMini> listMatchRequirementMini;//匹配需求
	private List<ConnectionsMini> listMatchConnectionsMini;//匹配关系
	private List<KnowledgeMini> listMatchKnowledgeMini;//匹配知识

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public JTContactMini getMaintainer() {
		return maintainer;
	}

	public void setMaintainer(JTContactMini maintainer) {
		this.maintainer = maintainer;
	}

	public Integer getExeMode() {
		return exeMode;
	}

	public void setExeMode(Integer exeMode) {
		this.exeMode = exeMode;
	}

	public Integer getPublishRange() {
		return publishRange;
	}

	public void setPublishRange(Integer publishRange) {
		this.publishRange = publishRange;
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

	public List<AffairMini> getListRelatedRequirementMini() {
		return listRelatedRequirementMini;
	}

	public void setListRelatedRequirementMini(
			List<AffairMini> listRelatedRequirementMini) {
		this.listRelatedRequirementMini = listRelatedRequirementMini;
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
	public static Project getByJsonString(String jsonEntity) {
		if(jsonEntity.equals("{}")) {
			return null; //无数据判断
		}
		return JSON.parseObject(jsonEntity, Project.class);
	}
	
	/**
	 * @author zhangzhen
	 * 如果数据为空返回null
	 * 
	 * 指导使用方法
	 * JSONObject j = JSONObject.fromObject(requestJson);
	 * Object jsonData = j.get("Entity");
	 * */
	public static Project getByJsonObject(Object jsonEntity) {
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
	public static List<Project> getListByJsonString(String object) {
		return JSON.parseArray(object, Project.class);
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
	public static List<Project> getListByJsonObject(Object object) {
		return getListByJsonString(object.toString());
	}

}
