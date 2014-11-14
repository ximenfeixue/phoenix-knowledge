package com.ginkgocap.ywxt.knowledge.model;

import java.util.List;

import com.ginkgocap.ywxt.user.model.User;

public class KnowledgeShareVO {
	private List<Long> receiveList;
	
	private KnowledgeNewsVO vo;
	
	private User user;

	
	public List<Long> getReceiveList() {
		return receiveList;
	}

	public void setReceiveList(List<Long> receiveList) {
		this.receiveList = receiveList;
	}

	public KnowledgeNewsVO getVo() {
		return vo;
	}

	public void setVo(KnowledgeNewsVO vo) {
		this.vo = vo;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
}
