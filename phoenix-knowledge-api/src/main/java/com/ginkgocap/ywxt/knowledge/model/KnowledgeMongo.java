package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;

/**
 * @Title: 知识详细信息（mongoDB保存，保存知识的全部信息，提供给编辑、详细信息查看界面查询）
 * @author 周仕奇
 * @date 2016年1月11日 下午3:38:53
 * @version V1.0.0
 */
public class KnowledgeMongo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4155861553088421781L;
	
	/**主键*/
	private long id;
	
	/**标题*/
	private String title;
	
	/**类型，0为系统创建，1为用户创建*/
	private String type;
	
	/**栏目主键*/
	private long columnId;
	
	/**描述*/
	private String content;
	
	/**描述简略，一般存储描述的前50个字*/
	private String contentDesc;
	
	/**附件ID*/
	private long attachmentId;
	
	/**图片ID*/
	private long pictureId;
	
	/**作者*/
	private String author;
	
	/**创建人ID*/
	private long createUserId;
	
	/**创建人名称*/
	private String createUserName;
	
	/**创建时间*/
	private String createDate;
	
	/**修改人ID*/
	private long modifyUserId;
	
	/**修改人名称*/
	private String modifyUserName;
	
	/**修改时间*/
	private String modifyDate;
	
	/**发布时间*/
	private String publicDate;
	
	/**状态（0为无效/删除，1为有效，2为草稿，3,：回收站）*/
	private String status;
	
	/**审核状态（0：未通过，1：审核中，2：审核通过）*/
	private String auditStatus;
	
	/**举报状态（3：举报审核未通过，即无非法现象，2：举报审核通过，1:未被举报，0：已被举报）*/
	private String reportStatus;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getColumnId() {
		return columnId;
	}

	public void setColumnId(long columnId) {
		this.columnId = columnId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContentDesc() {
		return contentDesc;
	}

	public void setContentDesc(String contentDesc) {
		this.contentDesc = contentDesc;
	}

	public long getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(long attachmentId) {
		this.attachmentId = attachmentId;
	}

	public long getPictureId() {
		return pictureId;
	}

	public void setPictureId(long pictureId) {
		this.pictureId = pictureId;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public long getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(long createUserId) {
		this.createUserId = createUserId;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public long getModifyUserId() {
		return modifyUserId;
	}

	public void setModifyUserId(long modifyUserId) {
		this.modifyUserId = modifyUserId;
	}

	public String getModifyUserName() {
		return modifyUserName;
	}

	public void setModifyUserName(String modifyUserName) {
		this.modifyUserName = modifyUserName;
	}

	public String getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getPublicDate() {
		return publicDate;
	}

	public void setPublicDate(String publicDate) {
		this.publicDate = publicDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}

	public String getReportStatus() {
		return reportStatus;
	}

	public void setReportStatus(String reportStatus) {
		this.reportStatus = reportStatus;
	}
	
	
}