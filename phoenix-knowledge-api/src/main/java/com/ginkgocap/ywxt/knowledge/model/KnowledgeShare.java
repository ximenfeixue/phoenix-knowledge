package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Transient;

import com.ginkgocap.ywxt.knowledge.form.Friends;

/**
 * 知识分享
 * @author liuyang
 *
 */
public class KnowledgeShare implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String id;
	private long userId;//分享人
	@Transient
	private String userName;
	private long knowledgeId;//对应的知识id
	private List<Long> receiverId;//接收人
	private List<String> receiverName;//接收人名称
	private String ctime;//分享时间
	private String title;
	private List<Friends> friends;
	@Transient
	private Article article;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
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
	public List<Long> getReceiverId() {
		return receiverId;
	}
	public void setReceiverId(List<Long> receiverId) {
		this.receiverId = receiverId;
	}
	public List<String> getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(List<String> receiverName) {
		this.receiverName = receiverName;
	}
	public String getCtime() {
		return ctime;
	}
	public void setCtime(String ctime) {
		this.ctime = ctime;
	}
	public Article getArticle() {
		return article;
	}
	public void setArticle(Article article) {
		this.article = article;
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
