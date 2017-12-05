package com.ginkgocap.ywxt.knowledge.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @Title: 知识基础信息（MySQL保存，一般保存基础信息，提供给列表查询）
 * @date 2016年1月11日 下午3:38:53
 * @version V1.0.0
 */
@Entity
@Table(name = "tb_knowledge_base", catalog = "phoenix_knowledge_new")
public class KnowledgeBase implements Serializable {

	private static final long serialVersionUID = 9167004345845581253L;
	/**主键*/
	private long id;

    /**类型，一级栏目*/
    private short type;

    /**栏目主键*/
    private int columnId;

    /**旧知识id*/
    private long knowledgeId;

	/**标题*/
	private String title;

	/**描述*/
	private String content;

	//来源
	private String source;

	/**描述简略，一般存储描述的前50个字*/
	private String contentDesc;

	//是否加精, 0否，1是
	private short essence;

    /** 标签信息 **/
    private String tags;

	/**封面图片地址*/
	private String coverPic;

	/**column Path*/
	private String cpath;

	private String taskId;

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
	
	//状态（0为无效/删除，1为草稿，2：待审核 3：审核中 4：审核通过 5：未通过 6：回收站)
	private short status;

	/**举报状态（3：举报审核未通过，即无非法现象，2：举报审核通过，1:未被举报，0：已被举报）*/
	private short reportStatus;

    /** 是否是旧数据 1:是, 0:否**/
    private short isOld;

    /** 星标(1:是,0:否(默认)) **/
    private short userStar;

	//是否私密，1是，0否
	private short privated = 0;

	/** 阅读数量  **/
	private int readCount;

	//是否收藏，1是，0否
	private short collected = 0;

	//分享id
	private long shareId;

	@Id
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
    public int getColumnId() {
        return columnId;
    }

    public void setColumnId(int columnId) {
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

	@Column(name = "source")
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@Column(name = "content_desc")
	public String getContentDesc() {
		return contentDesc;
	}

	public void setContentDesc(String contentDesc) {
		this.contentDesc = contentDesc;
	}

	@Column(name = "essence")
	public short getEssence() {
		return essence;
	}

	public void setEssence(short essence) {
		this.essence = essence;
	}

	@Column(name = "tags")
    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

	/*
	@Column(name = "directorys")
	public String getDirectorys() {
		return directorys;
	}

	public void setDirectorys(String directorys) {
		this.directorys = directorys;
	}*/

	@Column(name = "coverPic")
	public String getCoverPic() {
		return coverPic;
	}

	public void setCoverPic(String coverPic) {
		this.coverPic = coverPic;
	}

	@Column(name = "cpath")
	public String getCpath() {
		return cpath;
	}

	public void setCpath(String cpath) {
		this.cpath = cpath;
	}

	@Column(name = "taskId")
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
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

	@Column(name = "report_status")
	public short getReportStatus() {
		return reportStatus;
	}

	public void setReportStatus(short reportStatus) {
		this.reportStatus = reportStatus;
	}

    @Column(name = "isOld")
    public short getIsOld() {
        return isOld;
    }

    public void setIsOld(short isOld) {
        this.isOld = isOld;
    }

    @Column(name = "userStar")
    public short getUserStar() {
        return userStar;
    }

    public void setUserStar(short userStar) {
        this.userStar = userStar;
    }

	@Column(name = "privated")
	public short getPrivated() {
		return privated;
	}

	public void setPrivated(short privated) {
		this.privated = privated;
	}

    @Transient
	public int getReadCount() {return readCount;}

	public void setReadCount(int readCount) {this.readCount = readCount;}

	@Transient
	public short getCollected() {
		return collected;
	}

	public void setCollected(short collected) {
		this.collected = collected;
	}

	@Transient
	public long getShareId() {
		return shareId;
	}

	public void setShareId(long shareId) {
		this.shareId = shareId;
	}
}