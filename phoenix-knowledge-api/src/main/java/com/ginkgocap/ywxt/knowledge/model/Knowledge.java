package com.ginkgocap.ywxt.knowledge.model;

import java.util.Date;

/**
 * 知识javaBean （资讯，资产管理,宏观，观点，文章）
 * 
 * @author Administrator
 * 
 */
public class Knowledge {

	// id
	private long id;

	// 拥有者id
	private long owner_id;

	// 创建人(作者id)
	private long create_user_id;

	// 来源（最初创建者）
	private String knowledgesource;

	// 来源地址
	private String source_url;

	// 知识所在栏目路径id
	private String know_path;

	// 描述
	private String knowledgeDesc;

	// 标题
	private String knowledgetitle;

	// 是否加精
	private boolean essence;

	// 封面地址id
	private String pictureTaskId;

	// 最后修改时间
	private Date modifytime;

	// 审核状态(0：待审核，1：审核中，2：审核通过，-1：未通过)
	private String check_status;

	// 状态（1为草稿，2：待审核 3：审核中 4：审核通过 5：未通过 6：回收站)
	private short status;

	// 举报状态(3：举报审核未通过，即无非法现象，2：举报审核通过，1:未被举报，0：已被举报)
	private int report_status;

	// 创建时间
	private Date createtime;

	// 分类表
	private String table_name;

	public String getTable_name() {
		return table_name;
	}

	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getOwner_id() {
		return owner_id;
	}

	public void setOwner_id(long owner_id) {
		this.owner_id = owner_id;
	}

	public long getCreate_user_id() {
		return create_user_id;
	}

	public void setCreate_user_id(long create_user_id) {
		this.create_user_id = create_user_id;
	}

	public String getKnowledgesource() {
		return knowledgesource;
	}

	public void setKnowledgesource(String knowledgesource) {
		this.knowledgesource = knowledgesource;
	}

	public String getSource_url() {
		return source_url;
	}

	public void setSource_url(String source_url) {
		this.source_url = source_url;
	}

	public String getKnow_path() {
		return know_path;
	}

	public void setKnow_path(String know_path) {
		this.know_path = know_path;
	}

	public String getKnowledgeDesc() {
		return knowledgeDesc;
	}

	public void setKnowledgeDesc(String knowledgeDesc) {
		this.knowledgeDesc = knowledgeDesc;
	}

	public String getKnowledgetitle() {
		return knowledgetitle;
	}

	public void setKnowledgetitle(String knowledgetitle) {
		this.knowledgetitle = knowledgetitle;
	}

	public boolean isEssence() {
		return essence;
	}

	public void setEssence(boolean essence) {
		this.essence = essence;
	}

	public String getPictureTaskId() {
		return pictureTaskId;
	}

	public void setPictureTaskId(String pictureTaskId) {
		this.pictureTaskId = pictureTaskId;
	}

	public Date getModifytime() {
		return modifytime;
	}

	public void setModifytime(Date modifytime) {
		this.modifytime = modifytime;
	}

	public String getCheck_status() {
		return check_status;
	}

	public void setCheck_status(String check_status) {
		this.check_status = check_status;
	}

	public short getStatus() {
		return status;
	}

	public void setStatus(short status) {
		this.status = status;
	}

	public int getReport_status() {
		return report_status;
	}

	public void setReport_status(int report_status) {
		this.report_status = report_status;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

}