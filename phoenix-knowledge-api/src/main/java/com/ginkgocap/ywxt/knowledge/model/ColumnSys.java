package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;

import javax.persistence.*;

import java.sql.Timestamp;
import java.util.Date;


/**
 * The persistent class for the tb_knowledge_column_system database table.
 * 
 */
@Entity
@Table(name="tb_knowledge_column_system")
@NamedQuery(name="ColumnSystem.findAll", query="SELECT c FROM ColumnSystem c")
public class ColumnSys implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Column(name="column_level_path")
	private String columnLevelPath;

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