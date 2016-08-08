package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;
import java.util.List;

import com.ginkgocap.ywxt.knowledge.model.common.IdName;
import org.springframework.data.annotation.Transient;

import com.ginkgocap.ywxt.knowledge.form.Friends;

public class KnowledgeShare implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private long id;
	private long userId;//分享人
	@Transient
	private String userName;
	private long knowledgeId;//对应的知识id

	private List<IdName> receivers;//接收人Id,名称
	private String ctime;//分享时间
	private String title;
	private List<Friends> friends;
	
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
	public long getKnowledgeId() {
		return knowledgeId;
	}
	public void setKnowledgeId(long knowledgeId) {
		this.knowledgeId = knowledgeId;
	}
	public List<IdName> getReceivers() {
		return receivers;
	}

	public void setReceivers(List<IdName> receivers) {
		this.receivers = receivers;
	}

	public String getCtime() {
		return ctime;
	}
	public void setCtime(String ctime) {
		this.ctime = ctime;
	}
 
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<Friends> getFriends() {
		return friends;
	}
	public void setFriends(List<Friends> friends) {
		this.friends = friends;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}
