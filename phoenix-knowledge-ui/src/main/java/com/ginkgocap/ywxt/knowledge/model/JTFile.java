/**
 * Copyright (c) 2011 银杏资本.
 * All Rights Reserved. 保留所有权利.
 */
package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;

public class JTFile implements Serializable,Comparable {

	private static final long serialVersionUID = 1833486332854505693L;
	
	public final static String TYPE_IMAGE = "3";//图片类型
	public final static String TYPE_OTHER = "4";//图片类型
	
	private String id;
	
	private String fileName;//文件名
	
	private long fileSize;//文件大小
	
	private String url;//文件地址
	
	private String suffixName;//jpg,png,amr,pdf等
	
	private String type;//0-video,1-audio,2-file,3-image,4-other
	
	private String moduleType;//0:需求、1：业务需求、2：公司客户、3：公司项目、4：会员、5：名片 、6 公司名片 、7资讯、8客户、9人脉分享 、10机构

	private String taskId;//附件索引
	
	private String reserved1;
	
	private String reserved2;
	
	private String reserved3;
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSuffixName() {
		return suffixName;
	}

	public void setSuffixName(String suffixName) {
		this.suffixName = suffixName;
	}

	public String getType() {
		if(StringUtils.isBlank(type)){
			//如果文件类型为空， 根据后缀判断
			if(!StringUtils.isBlank(suffixName)){
				if( (suffixName.equalsIgnoreCase("jpg")) ||
						(suffixName.equalsIgnoreCase("png")) ||
						(suffixName.equalsIgnoreCase("gif")) ||
						(suffixName.equalsIgnoreCase("jpeg")) ){
							type = TYPE_IMAGE;
						}
			}
		}
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public String getModuleType() {
		return moduleType;
	}

	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getReserved1() {
		return reserved1;
	}

	public void setReserved1(String reserved1) {
		this.reserved1 = reserved1;
	}

	public String getReserved2() {
		return reserved2;
	}

	public void setReserved2(String reserved2) {
		this.reserved2 = reserved2;
	}

	public String getReserved3() {
		return reserved3;
	}

	public void setReserved3(String reserved3) {
		this.reserved3 = reserved3;
	}

	/**
	 * @author zhangzhen
	 * 如果数据为空返回null
	 * 
	 * 指导使用方法
	 * JSONObject j = JSONObject.fromObject(requestJson);
	 * String jsonData = j.getString("Entity");
	 * */
	public static JTFile getByJsonString(String jsonEntity) {
		if(jsonEntity.equals("{}")) {
			return null; //无数据判断
		}
		return JSON.parseObject(jsonEntity, JTFile.class);
	}
	
	/**
	 * @author zhangzhen
	 * 如果数据为空返回null
	 * 
	 * 指导使用方法
	 * JSONObject j = JSONObject.fromObject(requestJson);
	 * Object jsonData = j.get("Entity");
	 * */
	public static JTFile getByJsonObject(Object jsonEntity) {
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
	public static List<JTFile> getListByJsonString(String object) {
		try{
			List<JTFile> ret = JSON.parseArray(object, JTFile.class);
			return ret;
		}catch(Exception e){
			return null;
		}
		
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
	public static List<JTFile> getListByJsonObject(Object object) {
		return getListByJsonString(object.toString());
	}

	@Override
	public int compareTo(Object o) {
		if (null == o)
			return 1;
		JTFile jTFile = (JTFile) o;
		if (this.id != null || jTFile.getId() != null) {
			if (this.id.equals(jTFile.getId())) {
				return 0;
			}
		}
		return 1;
	}
}