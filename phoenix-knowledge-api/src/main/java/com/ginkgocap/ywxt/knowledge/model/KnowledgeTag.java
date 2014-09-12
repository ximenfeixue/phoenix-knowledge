package com.ginkgocap.ywxt.knowledge.model;


/**
 * 知识javaBean （知识标签中间表）
 * 
 * @author Administrator
 * 
 */
public class KnowledgeTag {

	// id
	private long id;

	// 知识ID
	private long knowledge_id;

	// 目录ID
	private long column_id;

	// 标签ID
	private long tag_id;

	// 类型(0:栏目，1：知识)
	private String type;

	// 用户ID
	private long user_id;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getKnowledge_id() {
		return knowledge_id;
	}

	public void setKnowledge_id(long knowledge_id) {
		this.knowledge_id = knowledge_id;
	}

	public long getColumn_id() {
		return column_id;
	}

	public void setColumn_id(long column_id) {
		this.column_id = column_id;
	}

	public long getTag_id() {
		return tag_id;
	}

	public void setTag_id(long tag_id) {
		this.tag_id = tag_id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}
}