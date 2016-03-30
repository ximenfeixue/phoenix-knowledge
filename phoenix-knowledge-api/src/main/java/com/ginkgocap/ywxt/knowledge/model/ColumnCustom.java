package com.ginkgocap.ywxt.knowledge.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/**
 * The persistent class for the tb_knowledge_column_customer database table.
 * 
 */
@Entity
@Table(name="tb_knowledge_column_customer")
//@NamedQuery(name="ColumnCustomer.findAll", query="SELECT c FROM ColumnCustomer c")
public class ColumnCustom implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Column(name="column_name")
	private String columnName;
	
	@Column(name="column_level_path")
	private String columnLevelPath;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_time")
	private Date createTime;

	@Column(name="order_num")
	private int orderNum;

	@Column(name="path_name")
	private String pathName;
	
	private Long cid;

	private Long pcid;

	@Column(name="top_col_type")
	private int topColType;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="update_time")
	private Date updateTime;

	@Column(name="user_id")
	private Long userId;

	@Column(name="user_or_system")
	private short userOrSystem;

	@Column(name="view_status")
	private short viewStatus;
	
	@Column(name="del_status")
	private short delStatus;

	public ColumnCustom() {
	}

    @Id
    @GeneratedValue(generator = "id")
    @GenericGenerator(name = "id", strategy = "com.ginkgocap.ywxt.framework.dal.dao.id.util.TimeIdGenerator", parameters = { @org.hibernate.annotations.Parameter(name = "sequence", value = "tb_knowledge_column_customer") })
    @Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
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
	
	public Long getCid() {
		return this.cid;
	}

	public void setCid(Long cid) {
		this.cid = cid;
	}

	public Long getPcid() {
		return this.pcid;
	}

	public void setPcid(Long pid) {
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

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
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
	
	public short getDelStatus() {
		return this.delStatus;
	}

	public void setDelStatus(short delStatus) {
		this.delStatus = delStatus;
	}
	
	public String getColumnLevelPath() {
		return this.columnLevelPath;
	}

	public void setColumnLevelPath(String columnLevelPath) {
		this.columnLevelPath = columnLevelPath;
	}

}