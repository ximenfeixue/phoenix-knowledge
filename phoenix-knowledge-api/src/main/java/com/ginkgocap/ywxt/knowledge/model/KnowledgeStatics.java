package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;
import java.util.Date;

public class KnowledgeStatics implements Serializable {

	/**
	 * 知识统计javaBean
	 */
	private static final long serialVersionUID = 9210031377998416429L;

	/** 知识Id */
	private long knowledgeid;

	/** 评论数 */
	private long commentCount;

	// 分享数
	private long shareCount;

	// 收藏数
	private long collectionCount;

	// 点击数
	private long clickCount;

	public long getKnowledgeid() {
		return knowledgeid;
	}

	public void setKnowledgeid(long knowledgeid) {
		this.knowledgeid = knowledgeid;
	}

	public long getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(long commentCount) {
		this.commentCount = commentCount;
	}

	public long getShareCount() {
		return shareCount;
	}

	public void setShareCount(long shareCount) {
		this.shareCount = shareCount;
	}

	public long getCollectionCount() {
		return collectionCount;
	}

	public void setCollectionCount(long collectionCount) {
		this.collectionCount = collectionCount;
	}

	public long getClickCount() {
		return clickCount;
	}

	public void setClickCount(long clickCount) {
		this.clickCount = clickCount;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
