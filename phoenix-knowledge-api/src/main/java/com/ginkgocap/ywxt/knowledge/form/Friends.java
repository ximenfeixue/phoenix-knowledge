package com.ginkgocap.ywxt.knowledge.form;

import java.io.Serializable;

public class Friends implements Serializable{
	private static final long serialVersionUID = 1L;

	private long userId;
	
	private int status;//状态，删除为-1

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
}
