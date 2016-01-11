package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * @Title: ֪ʶ������Ϣ��MySQL���棬һ�㱣�������Ϣ���ṩ���б��ѯ��
 * @author ������
 * @date 2016��1��11�� ����3:38:53
 * @version V1.0.0
 */
@Entity
@Table(name = "tb_knowledge_base", catalog = "phoenix_knowledge")
public class KnowledgeBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9167004345845581253L;
	
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

	@Id
	@GeneratedValue(generator = "id")
	@GenericGenerator(name = "id", strategy = "com.ginkgocap.ywxt.framework.dal.dao.id.util.TimeIdGenerator", parameters = { @Parameter(name = "sequence", value = "t_knowledge_label") })
	@Column(name = "id", unique = true, nullable = false)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "title")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "column_id")
	public long getColumnId() {
		return columnId;
	}

	public void setColumnId(long columnId) {
		this.columnId = columnId;
	}

	@Column(name = "content")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "content_desc")
	public String getContentDesc() {
		return contentDesc;
	}

	public void setContentDesc(String contentDesc) {
		this.contentDesc = contentDesc;
	}

	@Column(name = "attachment_id")
	public long getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(long attachmentId) {
		this.attachmentId = attachmentId;
	}

	@Column(name = "picture_id")
	public long getPictureId() {
		return pictureId;
	}

	public void setPictureId(long pictureId) {
		this.pictureId = pictureId;
	}

	@Column(name = "author")
	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	@Column(name = "create_user_id")
	public long getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(long createUserId) {
		this.createUserId = createUserId;
	}

	@Column(name = "create_user_name")
	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	@Column(name = "create_date")
	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	@Column(name = "modify_user_id")
	public long getModifyUserId() {
		return modifyUserId;
	}

	public void setModifyUserId(long modifyUserId) {
		this.modifyUserId = modifyUserId;
	}

	@Column(name = "modify_user_name")
	public String getModifyUserName() {
		return modifyUserName;
	}

	public void setModifyUserName(String modifyUserName) {
		this.modifyUserName = modifyUserName;
	}

	@Column(name = "modify_date")
	public String getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}

	@Column(name = "public_date")
	public String getPublicDate() {
		return publicDate;
	}

	public void setPublicDate(String publicDate) {
		this.publicDate = publicDate;
	}

	@Column(name = "status")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "audit_status")
	public String getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}

	@Column(name = "report_status")
	public String getReportStatus() {
		return reportStatus;
	}

	public void setReportStatus(String reportStatus) {
		this.reportStatus = reportStatus;
	}
	
	
}