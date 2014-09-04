package com.ginkgocap.ywxt.knowledge.model;

import java.util.Date;

public class Knowledge {

	// id
	private long id;

	// 知识ID，兼容原来老数据
	private long knowledgeid;

	// phoenix_user.tb_user.id，创建用户Id
	private long userId;

	// 用户名
	private String username;

	// 创建人
	private long create_user_id;

	// 创建人名称
	private String create_username;

	// 知识类型（默认0：其他,1：资讯，2：投融工具，3：行业，4：经典案例，5：图书报告，6：资产管理，7：宏观，8：观点，9：判例，10，法律法规，11：文章）
	private int knowledgetype;

	// 知识作者，默认为当前登录用户的name
	private String knowledgeauthor;

	// 知识来源
	private String knowledgesource;

	// 知识内容前50个字符
	private String knowledgedesc;

	// 知识标题
	private String knowledgetitle;

	// phoenix_knowledge.tb_column.columnid，栏目ID
	private long columnid;

	// 知识所在栏目路径(格式为：资讯/金融/货币)
	private String know_path;

	// 是否加精 0:否 1:是
	private String essence;

	// 封面id(对应附件表.id)
	private String pictureTaskId;

	// 可见范围，默认0：为全平台可见，1：为自己可见，2：好友可见
	private String visible;

	// 知识发布时间
	private Date pubdate;

	// 最后修改时间
	private Date modifytime;

	// 状态（0为无效/删除，1为有效，2为草稿，3,：回收站）
	private String status;

	// 举报状态(3：举报审核未通过，即无非法现象，2：举报审核通过，1:未被举报，0：已被举报)
	private String report_status;

	// 审核状态(0：未通过，1：审核中，2：审核通过)
	private String check_status;

	// 知识点击次数
	private long clicknum;

	// 评论数
	private long commentNum;

	// 分享数
	private long shareNum;

	// 收藏数
	private long collectionNum;

	// 创建时间
	private Date createtime;

	// 与知识附件表关联ID
	private String attatchmentTaskId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getKnowledgeid() {
		return knowledgeid;
	}

	public void setKnowledgeid(long knowledgeid) {
		this.knowledgeid = knowledgeid;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public long getCreate_user_id() {
		return create_user_id;
	}

	public void setCreate_user_id(long create_user_id) {
		this.create_user_id = create_user_id;
	}

	public String getCreate_username() {
		return create_username;
	}

	public void setCreate_username(String create_username) {
		this.create_username = create_username;
	}

	public int getKnowledgetype() {
		return knowledgetype;
	}

	public void setKnowledgetype(int knowledgetype) {
		this.knowledgetype = knowledgetype;
	}

	public String getKnowledgeauthor() {
		return knowledgeauthor;
	}

	public void setKnowledgeauthor(String knowledgeauthor) {
		this.knowledgeauthor = knowledgeauthor;
	}

	public String getKnowledgesource() {
		return knowledgesource;
	}

	public void setKnowledgesource(String knowledgesource) {
		this.knowledgesource = knowledgesource;
	}

	public String getKnowledgedesc() {
		return knowledgedesc;
	}

	public void setKnowledgedesc(String knowledgedesc) {
		this.knowledgedesc = knowledgedesc;
	}

	public String getKnowledgetitle() {
		return knowledgetitle;
	}

	public void setKnowledgetitle(String knowledgetitle) {
		this.knowledgetitle = knowledgetitle;
	}

	public long getColumnid() {
		return columnid;
	}

	public void setColumnid(long columnid) {
		this.columnid = columnid;
	}

	public String getKnow_path() {
		return know_path;
	}

	public void setKnow_path(String know_path) {
		this.know_path = know_path;
	}

	public String getEssence() {
		return essence;
	}

	public void setEssence(String essence) {
		this.essence = essence;
	}

	public String getPictureTaskId() {
		return pictureTaskId;
	}

	public void setPictureTaskId(String pictureTaskId) {
		this.pictureTaskId = pictureTaskId;
	}

	public String getVisible() {
		return visible;
	}

	public void setVisible(String visible) {
		this.visible = visible;
	}

	public Date getPubdate() {
		return pubdate;
	}

	public void setPubdate(Date pubdate) {
		this.pubdate = pubdate;
	}

	public Date getModifytime() {
		return modifytime;
	}

	public void setModifytime(Date modifytime) {
		this.modifytime = modifytime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReport_status() {
		return report_status;
	}

	public void setReport_status(String report_status) {
		this.report_status = report_status;
	}

	public String getCheck_status() {
		return check_status;
	}

	public void setCheck_status(String check_status) {
		this.check_status = check_status;
	}

	public long getClicknum() {
		return clicknum;
	}

	public void setClicknum(long clicknum) {
		this.clicknum = clicknum;
	}

	public long getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(long commentNum) {
		this.commentNum = commentNum;
	}

	public long getShareNum() {
		return shareNum;
	}

	public void setShareNum(long shareNum) {
		this.shareNum = shareNum;
	}

	public long getCollectionNum() {
		return collectionNum;
	}

	public void setCollectionNum(long collectionNum) {
		this.collectionNum = collectionNum;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getAttatchmentTaskId() {
		return attatchmentTaskId;
	}

	public void setAttatchmentTaskId(String attatchmentTaskId) {
		this.attatchmentTaskId = attatchmentTaskId;
	}

}