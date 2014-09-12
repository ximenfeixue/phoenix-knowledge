package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * <p>
 * 知识栏目关系表
 * </p>
 * 
 */
public class ColumnKnowledge implements Serializable {
	private static final long serialVersionUID = -5440480908931909931L;
	private long column_id;// 栏目ID
	private long knowledge_id;// 知识ID
	private int user_id;// 用户id
	private int type;// 类型

	public long getColumn_id() {
		return column_id;
	}

	public void setColumn_id(long column_id) {
		this.column_id = column_id;
	}

	public long getKnowledge_id() {
		return knowledge_id;
	}

	public void setKnowledge_id(long knowledge_id) {
		this.knowledge_id = knowledge_id;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
