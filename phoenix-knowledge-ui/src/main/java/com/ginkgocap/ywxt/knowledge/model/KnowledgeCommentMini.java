package com.ginkgocap.ywxt.knowledge.model;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.ginkgocap.ywxt.knowledge.utils.DateFormater;

public class KnowledgeCommentMini {
	
	private Long id;

	private Long knowledgeId;
	
	private String content;
	
	private Date createtime;
	
	private Boolean status;
	
	private Long parentId;
	
	private Long count;
	
	private ConnectionsMini connectionsMini;
	
	private List<KnowledgeCommentMini> listKnowledgeComment;
	
	public KnowledgeCommentMini(KnowledgeCommentVO knowledgeCommentVO) {
		
		this.id = knowledgeCommentVO.getId(); 
		this.knowledgeId = knowledgeCommentVO.getKnowledgeId(); 
		this.content = knowledgeCommentVO.getContent(); 
		this.createtime = DateFormater.ConvertDate(knowledgeCommentVO.getCreatetime()); 
		this.status = knowledgeCommentVO.getStatus(); 
		this.parentId = knowledgeCommentVO.getParentid(); 
		this.count = knowledgeCommentVO.getCount(); 

		ConnectionsMini connectionsMini = new ConnectionsMini(); 
	//	connectionsMini.setImage("http://file.gintong.com" + knowledgeCommentVO.getPic()); 
	//	connectionsMini.setName(knowledgeCommentVO); 
		this.connectionsMini = connectionsMini;
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getKnowledgeId() {
		return knowledgeId;
	}

	public void setKnowledgeId(Long knowledgeId) {
		this.knowledgeId = knowledgeId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCretetime(Date createtime) {
		this.createtime = createtime;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public List<KnowledgeCommentMini> getListKnowledgeComment() {
		return listKnowledgeComment;
	}

	public void setListKnowledgeComment(List<KnowledgeCommentMini> listKnowledgeComment) {
		this.listKnowledgeComment = listKnowledgeComment;
	}

	public ConnectionsMini getConnectionsMini() {
		return connectionsMini;
	}

	public void setConnectionsMini(ConnectionsMini connectionsMini) {
		this.connectionsMini = connectionsMini;
	}
	
	/**
	 * @author zhangzhen
	 * 如果数据为空返回null
	 * 
	 * 指导使用方法
	 * JSONObject j = JSONObject.fromObject(requestJson);
	 * String jsonData = j.getString("Entity");
	 * */
	public static KnowledgeCommentMini getByJsonString(String jsonEntity) {
		if(jsonEntity.equals("{}")) {
			return null; //无数据判断
		}
		return JSON.parseObject(jsonEntity, KnowledgeCommentMini.class);
	}
	
	/**
	 * @author zhangzhen
	 * 如果数据为空返回null
	 * 
	 * 指导使用方法
	 * JSONObject j = JSONObject.fromObject(requestJson);
	 * Object jsonData = j.get("Entity");
	 * */
	public static KnowledgeCommentMini getByJsonObject(Object jsonEntity) {
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
	public static List<KnowledgeCommentMini> getListByJsonString(String object) {
		return JSON.parseArray(object, KnowledgeCommentMini.class);
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
	public static List<KnowledgeCommentMini> getListByJsonObject(Object object) {
		return getListByJsonString(object.toString());
	}
}
