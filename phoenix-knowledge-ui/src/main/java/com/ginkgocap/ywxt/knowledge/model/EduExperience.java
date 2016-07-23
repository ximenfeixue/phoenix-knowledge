package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSON;

public class EduExperience implements Serializable {

	private static final long serialVersionUID = -7861631052299253000L;
	
	private int id; //对象ID
	
	private String startTime; //起始时间，如2002-07-01
	
	private String endTime; //结束时间
	
	private String school; //学校
	
	private String major; //专业
	
	private int background; //学历
	
	private String backgroundOffline; //学历,线下人脉，学历用户自己填写
	
	private boolean isOverseas; //是否海外经历
	
	private String description; //描述
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public boolean getIsOverseas() {
		return isOverseas;
	}

	public void setIsOverseas(boolean isOverseas) {
		this.isOverseas = isOverseas;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getBackground() {
		return background;
	}

	public void setBackground(int background) {
		this.background = background;
	}

	public String getBackgroundOffline() {
		return backgroundOffline;
	}

	public void setBackgroundOffline(String backgroundOffline) {
		this.backgroundOffline = backgroundOffline;
	}

	/**
	 * @author zhangzhen
	 * 如果数据为空返回null
	 * 
	 * 指导使用方法
	 * JSONObject j = JSONObject.fromObject(requestJson);
	 * String jsonData = j.getString("Entity");
	 * */
	public static EduExperience getByJsonString(String jsonEntity) {
		if(jsonEntity.equals("{}")) {
			return null; //无数据判断
		}
		return JSON.parseObject(jsonEntity, EduExperience.class);
	}
	
	/**
	 * @author zhangzhen
	 * 如果数据为空返回null
	 * 
	 * 指导使用方法
	 * JSONObject j = JSONObject.fromObject(requestJson);
	 * Object jsonData = j.get("Entity");
	 * */
	public static EduExperience getByJsonObject(Object jsonEntity) {
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
	public static List<EduExperience> getListByJsonString(String object) {
		return JSON.parseArray(object, EduExperience.class);
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
	public static List<EduExperience> getListByJsonObject(Object object) {
		return getListByJsonString(object.toString());
	}
	
}
