package com.ginkgocap.ywxt.knowledge.model;

import java.util.Date;

public class Knowledge {

	// id
	private long id;

	// 知识ID，兼容原来老数据
	private long knowledgeid;

	// phoenix_user.tb_user.id，创建用户Id
	private long userId;

	// 知识类型，默认0：其他,1：资讯，2：投融工具，3：行业，4：经典案例，5：图书报告，6：资产管理，7：宏观，8：观点，9：判例，10，法律法规
	private int knowledgetype;

	// 知识作者，默认为当前登录用户的name
	private String knowledgeauthor;

	// 知识来源
	private String knowledgesource;

	// 知识内容前50个字符
	private String knowledgedesc;

	// 知识标题
	private String knowledgetitle;

	// 自定义标签，使用逗号分割
	private String tag;

	// phoenix_knowledge.tb_column.columnid，栏目ID
	private long columnid;

	// phoenix_knowledge.tb_category.id，保存目录id
	private long categoryid;

	// 是否加精 0:否 1:是
	private String essence;

	// 知识图片表Id
	private String pictureTaskId;

	// 可见范围，默认0：为全平台可见，1：为自己可见，2：好友可见
	private String visible;

	// phoenix_knowledge.tb_category.sortId，目录顺序
	private String sortid;

	// 知识发布时间
	private Date pubdate;

	// 最后修改时间
	private Date modifytime;

	// 是否标志为回收站知识0:否 1:是
	private String recyclebin;

	// 0为有效，1为失效
	private String state;

	// 知识点击次数
	private long clicknum;

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

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public long getColumnid() {
		return columnid;
	}

	public void setColumnid(long columnid) {
		this.columnid = columnid;
	}

	public long getCategoryid() {
		return categoryid;
	}

	public void setCategoryid(long categoryid) {
		this.categoryid = categoryid;
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

	public String getSortid() {
		return sortid;
	}

	public void setSortid(String sortid) {
		this.sortid = sortid;
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

	public String getRecyclebin() {
		return recyclebin;
	}

	public void setRecyclebin(String recyclebin) {
		this.recyclebin = recyclebin;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public long getClicknum() {
		return clicknum;
	}

	public void setClicknum(long clicknum) {
		this.clicknum = clicknum;
	}

	public String getAttatchmentTaskId() {
		return attatchmentTaskId;
	}

	public void setAttatchmentTaskId(String attatchmentTaskId) {
		this.attatchmentTaskId = attatchmentTaskId;
	}
}