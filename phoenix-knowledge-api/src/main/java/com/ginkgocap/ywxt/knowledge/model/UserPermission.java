package com.ginkgocap.ywxt.knowledge.model;

import java.util.Date;

public class UserPermission {

	//接收者
	private long receive_user_id;

	//知识ID
	private long knowledge_id;
	
	//发起者ID
	private long send_user_id;
	
	//类型（1-收藏，2-分享，3-可见性，4-全平台可见）
	private int type;
	
	//分享留言
	private String mento;
	
	//创建时间
	private Date createtime;
	
	//知识所属类目ID，共十一种顶级类目之一
	private long column_id;

	public long getReceive_user_id() {
		return receive_user_id;
	}

	public void setReceive_user_id(long receive_user_id) {
		this.receive_user_id = receive_user_id;
	}

	public long getKnowledge_id() {
		return knowledge_id;
	}

	public void setKnowledge_id(long knowledge_id) {
		this.knowledge_id = knowledge_id;
	}

	public long getSend_user_id() {
		return send_user_id;
	}

	public void setSend_user_id(long send_user_id) {
		this.send_user_id = send_user_id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getMento() {
		return mento;
	}

	public void setMento(String mento) {
		this.mento = mento;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public long getColumn_id() {
		return column_id;
	}

	public void setColumn_id(long column_id) {
		this.column_id = column_id;
	}
}
