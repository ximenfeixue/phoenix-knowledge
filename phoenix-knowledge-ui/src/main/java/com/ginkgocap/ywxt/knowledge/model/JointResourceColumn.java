package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSON;

public class JointResourceColumn implements Serializable {

	private static final long serialVersionUID = -533214181302805496L;

	private int type;//对接资源的类型:1-需求;2-人脉;3-全平台普通用户;4-组织(全平台组织用户);5-客户;6-知识
	
	private String column;//栏目名称
	
	private String columnType;//栏目类型
	
	private long[] listItemId;//栏目中的项目id列表
	
	private int[] listItemType;//栏目id集合
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public long[] getListItemId() {
		return listItemId;
	}

	public void setListItemId(long[] listItemId) {
		this.listItemId = listItemId;
	}

	public String getColumnType() {
		return columnType;
	}

	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}

	public int[] getListItemType() {
		return listItemType;
	}

	public void setListItemType(int[] listItemType) {
		this.listItemType = listItemType;
	}

	/**
	 * @author zhangzhen
	 * 如果数据为空返回null
	 * 
	 * 指导使用方法
	 * JSONObject j = JSONObject.fromObject(requestJson);
	 * String jsonData = j.getString("Entity");
	 * 将jsonData参数传入本方法
	 * */
	public static JointResourceColumn getJointResourceColumnByJsonString(String jsonEntity) {
		if(jsonEntity.equals("{}")) {
			return null; //无数据判断
		}
		return JSON.parseObject(jsonEntity, JointResourceColumn.class);
	}
	/**
	 * @author zhangzhen
	 * 如果数据为空返回null
	 * 
	 * 指导使用方法
	 * JSONObject j = JSONObject.fromObject(requestJson);
	 * Object jsonData = j.get("Entity");
	 * 将jsonData参数传入本方法
	 * */
	public static JointResourceColumn getJointResourceColumnByJsonObject(Object jsonEntity) {
		return getJointResourceColumnByJsonString(jsonEntity.toString());
	}
	
	/**
	 * @author zhangzhen
	 * 如果没有数据，返回空数组
	 * 
	 * 指导使用方法
	 * JSONObject j = JSONObject.fromObject(requestJson);
	 * String jsonData = j.getString("Entity");
	 * 将jsonData参数传入本方法
	 * */
	public static List<JointResourceColumn> getJointResourceColumnListByJsonString(String object) {
		return JSON.parseArray(object, JointResourceColumn.class);
	}
	
	/**
	 * @author zhangzhen
	 * @CreateTime 2014-11-11
	 * 如果没有数据，返回空数组
	 * 
	 * 指导使用方法 
	 * JSONObject j = JSONObject.fromObject(requestJson);
	 * Object jsonData = j.get("EntityList");
	 * 将jsonData参数传入本方法;
	 * */
	public static List<JointResourceColumn> getJointResourceColumnListByJsonObject(Object object) {
		return getJointResourceColumnListByJsonString(object.toString());
	}
}
