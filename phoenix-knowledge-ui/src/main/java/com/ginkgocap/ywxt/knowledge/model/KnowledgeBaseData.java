package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSON;

/**
 * @Description: 知识临时转换对象
 * @Author: zhangzhen
 * @CreateDate: 2014-10-29
 * @Version: [v1.0]
 */

public class KnowledgeBaseData implements Serializable {

	private static final long serialVersionUID = 6319694587482465106L;

	private long user_id;

	private int essence;

	private int column_type;

	private String tag;

	private long column_id;

	private String pic_path;

	private long knowledge_id;

	private String createtime;

	private String author;

	private String title;

	private String source;

	private String path;
	
	private String c_desc;
	
	private String url;
	
	private int type;
	
	private String columnpath;
	
	private long shareMeId;
	
	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}

	public int getEssence() {
		return essence;
	}

	public void setEssence(int essence) {
		this.essence = essence;
	}

	public int getColumn_type() {
		return column_type;
	}

	public void setColumn_type(int column_type) {
		this.column_type = column_type;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public long getColumn_id() {
		return column_id;
	}

	public void setColumn_id(long column_id) {
		this.column_id = column_id;
	}

	public String getPic_path() {
		return pic_path;
	}

	public void setPic_path(String pic_path) {
		this.pic_path = pic_path;
	}

	public long getKnowledge_id() {
		return knowledge_id;
	}

	public void setKnowledge_id(long knowledge_id) {
		this.knowledge_id = knowledge_id;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getC_desc() {
		return c_desc;
	}

	public void setC_desc(String c_desc) {
		this.c_desc = c_desc;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getColumnpath() {
		return columnpath;
	}

	public void setColumnpath(String columnpath) {
		this.columnpath = columnpath;
	}
	
	public long getShareMeId() {
		return shareMeId;
	}

	public void setShareMeId(long shareMeId) {
		this.shareMeId = shareMeId;
	}

	/**
	 * @author zhangzhen
	 * 如果数据为空返回null
	 * 
	 * 指导使用方法
	 * JSONObject j = JSONObject.fromObject(requestJson);
	 * String jsonData = j.getString("Entity");
	 * */
	public static KnowledgeBaseData getByJsonString(String jsonEntity) {
		if(jsonEntity.equals("{}")) {
			return null; //无数据判断
		}
		return JSON.parseObject(jsonEntity, KnowledgeBaseData.class);
	}
	
	/**
	 * @author zhangzhen
	 * 如果数据为空返回null
	 * 
	 * 指导使用方法
	 * JSONObject j = JSONObject.fromObject(requestJson);
	 * Object jsonData = j.get("Entity");
	 * */
	public static KnowledgeBaseData getByJsonObject(Object jsonEntity) {
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
	public static List<KnowledgeBaseData> getListByJsonString(String object) {
		return JSON.parseArray(object, KnowledgeBaseData.class);
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
	public static List<KnowledgeBaseData> getListByJsonObject(Object object) {
		return getListByJsonString(object.toString());
	}
}
