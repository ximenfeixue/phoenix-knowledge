package com.ginkgocap.ywxt.knowledge.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @Title: 知识基础信息（MySQL保存，一般保存基础信息，提供给列表查询）
 * @author 周仕奇
 * @date 2016年1月11日 下午3:38:53
 * @version V1.0.0
 */
@Entity
@Table(name = "tb_knowledge_base", catalog = "phoenix_knowledge_new")
public class KnowledgeBase implements Serializable {

	private static final long serialVersionUID = 9167004345845581253L;
	
	public static final int CONTENT_DESC_LENGTH = 50;
	
	/**主键*/
	private long id;

    /**类型，0为系统创建，1为用户创建*/
    private short type;

    /**栏目主键*/
    private short columnId;

    /**旧知识id*/
    private long knowledgeId;

	/**标题*/
	private String title;

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
	private long createDate;
	
	/**修改人ID*/
	private long modifyUserId;
	
	/**修改人名称*/
	private String modifyUserName;
	
	/**修改时间*/
	private long modifyDate;
	
	/**发布时间*/
	private long publicDate;
	
	/**状态（0为无效/删除，1为有效，2为草稿，3,：回收站）*/
	private short status;
	
	/**审核状态（0：未通过，1：审核中，2：审核通过）*/
	private short auditStatus;
	
	/**举报状态（3：举报审核未通过，即无非法现象，2：举报审核通过，1:未被举报，0：已被举报）*/
	private short reportStatus;

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

    @Column(name = "knowledge_id")
    public long getKnowledgeId() {
        return knowledgeId;
    }

    public void setKnowledgeId(long knowledgeId) {
        this.knowledgeId = knowledgeId;
    }

    @Column(name = "column_id")
    public short getColumnId() {
        return columnId;
    }

    public void setColumnId(short columnId) {
        this.columnId = columnId;
    }

    @Column(name = "type")
    public short getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
    }

	@Column(name = "title")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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
	public long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(long createDate) {
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
	public long getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(long modifyDate) {
		this.modifyDate = modifyDate;
	}

	@Column(name = "public_date")
	public long getPublicDate() {
		return publicDate;
	}

	public void setPublicDate(long publicDate) {
		this.publicDate = publicDate;
	}

	@Column(name = "status")
	public short getStatus() {
		return status;
	}

	public void setStatus(short status) {
		this.status = status;
	}

	@Column(name = "audit_status")
	public short getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(short auditStatus) {
		this.auditStatus = auditStatus;
	}

	@Column(name = "report_status")
	public short getReportStatus() {
		return reportStatus;
	}

	public void setReportStatus(short reportStatus) {
		this.reportStatus = reportStatus;
	}
}