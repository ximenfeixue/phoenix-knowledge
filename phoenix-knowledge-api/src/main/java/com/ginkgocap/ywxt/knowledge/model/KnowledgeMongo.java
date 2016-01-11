package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;

/**
 * @Title: ֪ʶ��ϸ��Ϣ��mongoDB���棬����֪ʶ��ȫ����Ϣ���ṩ���༭����ϸ��Ϣ�鿴�����ѯ��
 * @author ������
 * @date 2016��1��11�� ����3:38:53
 * @version V1.0.0
 */
public class KnowledgeMongo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4155861553088421781L;
	
	/**����*/
	private long id;
	
	/**����*/
	private String title;
	
	/**���ͣ�0Ϊϵͳ������1Ϊ�û�����*/
	private String type;
	
	/**��Ŀ����*/
	private long columnId;
	
	/**����*/
	private String content;
	
	/**�������ԣ�һ��洢������ǰ50����*/
	private String contentDesc;
	
	/**����ID*/
	private long attachmentId;
	
	/**ͼƬID*/
	private long pictureId;
	
	/**����*/
	private String author;
	
	/**������ID*/
	private long createUserId;
	
	/**����������*/
	private String createUserName;
	
	/**����ʱ��*/
	private String createDate;
	
	/**�޸���ID*/
	private long modifyUserId;
	
	/**�޸�������*/
	private String modifyUserName;
	
	/**�޸�ʱ��*/
	private String modifyDate;
	
	/**����ʱ��*/
	private String publicDate;
	
	/**״̬��0Ϊ��Ч/ɾ����1Ϊ��Ч��2Ϊ�ݸ壬3,������վ��*/
	private String status;
	
	/**���״̬��0��δͨ����1������У�2�����ͨ����*/
	private String auditStatus;
	
	/**�ٱ�״̬��3���ٱ����δͨ�������޷Ƿ�����2���ٱ����ͨ����1:δ���ٱ���0���ѱ��ٱ���*/
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