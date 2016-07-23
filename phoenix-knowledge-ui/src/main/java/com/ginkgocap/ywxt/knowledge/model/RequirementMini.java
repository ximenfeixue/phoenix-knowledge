/**
 * Copyright (c) 2011 银杏资本.
 * All Rights Reserved. 保留所有权利.
 */
package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSON;

public class RequirementMini implements Serializable {
	
	private static final long serialVersionUID = -1447708106872783526L;

	private int id;//需求id
	
	private String typeName;//需求类型名称，如我要投资，我要融资
	
	private String title;//需求标题
	
	private String content;//需求内容
	
	private String Time;// 发布时间
	
	private Connections connections;

	public Connections getConnections() {
		return connections;
	}

	public void setConnections(Connections connections) {
		this.connections = connections;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
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

	public String getTime() {
		return Time;
	}

	public void setTime(String time) {
		Time = time;
	}
	
	/**
	 * @author zhangzhen
	 * 如果数据为空返回null
	 * 
	 * 指导使用方法
	 * JSONObject j = JSONObject.fromObject(requestJson);
	 * String jsonData = j.getString("Entity");
	 * */
	public static RequirementMini getByJsonString(String jsonEntity) {
		if(jsonEntity.equals("{}")) {
			return null; //无数据判断
		}
		return JSON.parseObject(jsonEntity, RequirementMini.class);
	}
	
	/**
	 * @author zhangzhen
	 * 如果数据为空返回null
	 * 
	 * 指导使用方法
	 * JSONObject j = JSONObject.fromObject(requestJson);
	 * Object jsonData = j.get("Entity");
	 * */
	public static RequirementMini getByJsonObject(Object jsonEntity) {
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
	public static List<RequirementMini> getListByJsonString(String object) {
		return JSON.parseArray(object, RequirementMini.class);
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
	public static List<RequirementMini> getListByJsonObject(Object object) {
		return getListByJsonString(object.toString());
	}
}