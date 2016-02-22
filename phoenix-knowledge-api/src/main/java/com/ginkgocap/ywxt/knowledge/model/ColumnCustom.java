package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.math.BigInteger;


/**
 * The persistent class for the tb_knowledge_column_customer database table.
 * 
 */
@Entity
@Table(name="tb_knowledge_column_customer")
@NamedQuery(name="ColumnCustomer.findAll", query="SELECT c FROM ColumnCustomer c")
public class ColumnCustom implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private String id;

	@Column(name="column_name")
	private String columnName;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_time")
	private Date createTime;

	@Column(name="order_num")
	private int orderNum;

	@Column(name="path_name")
	private String pathName;
	
	private BigInteger cid;

	private BigInteger pcid;

	@Column(name="top_col_type")
	private int topColType;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="update_time")
	private Date updateTime;

	@Column(name="user_id")
	private BigInteger userId;

	@Column(name="user_or_system")
	private short userOrSystem;

	@Column(name="view_status")
	private short viewStatus;

	public ColumnCustom() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getColumnName() {
		return this.columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getOrderNum() {
		return this.orderNum;
	}

	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}

	public String getPathName() {
		return this.pathName;
	}

	public void setPathName(String pathName) {
		this.pathName = pathName;
	}
	
	public BigInteger getCid() {
		return this.cid;
	}

	public void setCid(BigInteger cid) {
		this.cid = cid;
	}

	public BigInteger getPcid() {
		return this.pcid;
	}

	public void setPcid(BigInteger pid) {
		this.pcid = pid;
	}

	public int getTopColType() {
		return this.topColType;
	}

	public void setTopColType(int topColType) {
		this.topColType = topColType;
	}

	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public BigInteger getUserId() {
		return this.userId;
	}

	public void setUserId(BigInteger userId) {
		this.userId = userId;
	}

	public short getUserOrSystem() {
		return this.userOrSystem;
	}

	public void setUserOrSystem(short userOrSystem) {
		this.userOrSystem = userOrSystem;
	}

	public short getViewStatus() {
		return this.viewStatus;
	}

	public void setViewStatus(short viewStatus) {
		this.viewStatus = viewStatus;
	}

}