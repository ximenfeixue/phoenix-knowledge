/**
 * Copyright (c) 2011 银杏资本.
 * All Rights Reserved. 保留所有权利.
 */
package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;


/** @Description: 封装已经察看过的新关系消息   
 * @Author:       qinguochao  
 * @CreateDate:  2014-5-23   
 * @Version:      [v1.0]
 */
public class ProcessedNewconnection implements Serializable {

	 
	private static final long serialVersionUID = 6698864427100853984L;

	private int id;//id
	
	private Integer userId; // 用户id
	
	private String noticeId; // 新关系消息编号
	
	private Date ctime; // 创建时间
	
	private String creater ; // 创建人
	
	private Date utime; // 更新时间
	
	private String updater; // 更新人
	
	private String remake; // 备注

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}

	public Date getCtime() {
		return ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public Date getUtime() {
		return utime;
	}

	public void setUtime(Date utime) {
		this.utime = utime;
	}

	public String getUpdater() {
		return updater;
	}

	public void setUpdater(String updater) {
		this.updater = updater;
	}

	public String getRemake() {
		return remake;
	}

	public void setRemake(String remake) {
		this.remake = remake;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	/**
	 * @author zhangzhen
	 * 如果数据为空返回null
	 * 
	 * 指导使用方法
	 * JSONObject j = JSONObject.fromObject(requestJson);
	 * String jsonData = j.getString("Entity");
	 * */
	public static ProcessedNewconnection getByJsonString(String jsonEntity) {
		if(jsonEntity.equals("{}")) {
			return null; //无数据判断
		}
		return JSON.parseObject(jsonEntity, ProcessedNewconnection.class);
	}
	
	/**
	 * @author zhangzhen
	 * 如果数据为空返回null
	 * 
	 * 指导使用方法
	 * JSONObject j = JSONObject.fromObject(requestJson);
	 * Object jsonData = j.get("Entity");
	 * */
	public static ProcessedNewconnection getByJsonObject(Object jsonEntity) {
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
	public static List<ProcessedNewconnection> getListByJsonString(String object) {
		return JSON.parseArray(object, ProcessedNewconnection.class);
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
	public static List<ProcessedNewconnection> getListByJsonObject(Object object) {
		return getListByJsonString(object.toString());
	}
}
