package com.ginkgocap.ywxt.knowledge.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;


/**
 * The persistent class for the tb_knowledge_column_system database table.
 * 
 */
@Entity
@Table(name="tb_knowledge_column_system")
//@NamedQuery(name="ColumnSystem.findAll", query="SELECT c FROM ColumnSystem c")
public class ColumnSys implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Column(name="column_level_path")
	private String columnLevelPath;
	
	/**系统栏目编码，此编码一般使用字母进行编码，例如“新闻”，则用“new”来编码。在mongoDb中，不同类型的知识使用不同的表来存储，表名则是columnCode值*/
	private String columnCode;

	private String columnName;

	private Timestamp createtime;

	@Column(name="del_status")
	private byte delStatus;

	@Column(name="parent_id")
	private Long parentId;

	@Column(name="path_name")
	private String pathName;

	@Column(name="subscribe_count")
	private Long subscribeCount;

	private short type;

	@Column(name="update_time")
	private Timestamp updateTime;

	@Column(name="user_id")
	private Long userId;

	public ColumnSys() {
	}

    @Id
    @GeneratedValue(generator = "id")
    @GenericGenerator(name = "id", strategy = "com.ginkgocap.ywxt.framework.dal.dao.id.util.TimeIdGenerator", parameters = { @org.hibernate.annotations.Parameter(name = "sequence", value = "tb_knowledge_column_system") })
    @Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getColumnLevelPath() {
		return this.columnLevelPath;
	}

	public void setColumnLevelPath(String columnLevelPath) {
		this.columnLevelPath = columnLevelPath;
	}

	public String getColumnCode() {
		return this.columnCode;
	}

	public void setColumnCode(String columnCode) {
		this.columnCode = columnCode;
	}

	public String getColumnName() {
		return this.columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public Timestamp getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}

	public byte getDelStatus() {
		return this.delStatus;
	}

	public void setDelStatus(byte delStatus) {
		this.delStatus = delStatus;
	}

	public Long getParentId() {
		return this.parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getPathName() {
		return this.pathName;
	}

	public void setPathName(String pathName) {
		this.pathName = pathName;
	}

	public Long getSubscribeCount() {
		return this.subscribeCount;
	}

	public void setSubscribeCount(Long subscribeCount) {
		this.subscribeCount = subscribeCount;
	}

	public short getType() {
		return this.type;
	}

	public void setType(short type) {
		this.type = type;
	}

	public Timestamp getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}