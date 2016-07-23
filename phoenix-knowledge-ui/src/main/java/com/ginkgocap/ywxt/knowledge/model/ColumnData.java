package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;

/**
 * @Description: 知识栏目传输对象
 * @Author: zhangzhen
 * @CreateDate: 2014-10-17
 * @Version: [v1.0]
 */

public class ColumnData implements Serializable {

	private static final long serialVersionUID = -4990228487665207288L;

	private long id;// 栏目id
	private short type; //类型
	private long userId;// 用户id
	private String sortId;// 排序位置
	private long parentId;// 父级id
	private String columnname;// 栏目名称
	private List<ColumnData> listColumn = new ArrayList<ColumnData>();// 所属栏目

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getSortId() {
		return sortId;
	}

	public void setSortId(String sortId) {
		this.sortId = sortId;
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public String getColumnname() {
		return columnname;
	}

	public void setColumnname(String columnname) {
		this.columnname = columnname;
	}

	public List<ColumnData> getListColumn() {
		return listColumn;
	}

	public void setListColumn(List<ColumnData> listColumn) {
		this.listColumn = listColumn;
	}

	public short getType() {
		return type;
	}

	public void setType(short type) {
		this.type = type;
	}

	/**
	 * @author zhangzhen
	 * 如果数据为空返回null
	 * 
	 * 指导使用方法
	 * JSONObject j = JSONObject.fromObject(requestJson);
	 * String jsonData = j.getString("Entity");
	 * */
	public static ColumnData getByJsonString(String jsonEntity) {
		if(jsonEntity.equals("{}")) {
			return null; //无数据判断
		}
		return JSON.parseObject(jsonEntity, ColumnData.class);
	}
	
	/**
	 * @author zhangzhen
	 * 如果数据为空返回null
	 * 
	 * 指导使用方法
	 * JSONObject j = JSONObject.fromObject(requestJson);
	 * Object jsonData = j.get("Entity");
	 * */
	public static ColumnData getByJsonObject(Object jsonEntity) {
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
	public static List<ColumnData> getListByJsonString(String object) {
		return JSON.parseArray(object, ColumnData.class);
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
	public static List<ColumnData> getListByJsonObject(Object object) {
		return getListByJsonString(object.toString());
	}
}
