package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;

public class KnowledgeNewsVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long kId; // 知识ID
	private String title; // 知识标题
	private String content; // 知识内容
	private String pic; // 知识封面图片
	private String taskId; // 知识附件
	private String tags; // 知识
	private String source; // 知识来源
	private String catalogueIds; // 知识所在目录 (目录数组) 格式(263,266,267,268)
	private String columnid; // 知识所属栏目
	private String essence; // 知识加精状态( 是否加精（0-不加 1-加）)
	private String selectedIds; // 知识所属权限 格式(
	private String shareMessage; // 分享感言
	private String columnType; // 知识所属类型(类型（1：资讯，2：投融工具，3：行业，4：经典案例，5：图书报告，6：资产管理，7：宏观，8：观点，9：判例，10，法律法规，11：文章）)
	private String columnPath; // 知识所在的栏目路径
	private String columnName; // 知识所属栏目类型名(用于草稿箱查询)
	// 关联格式（r:人脉,p:组织,o:知识,k:需求）
	// asso:{"r":[{"tag":"33","conn":[{"type":1,"id":2111,"title":"d d d d","ownerId":1,"ownerName":"的的的的的的的"}]}],
	// "p":[{"tag":"111","conn":[{"type":2,"id":"141527672992500079","name":"五小六","ownerId":1,"ownerName":null,"caree":null,"company":null}]}],
	// "o":[{"tag":"22","conn":[{"type":5,"id":618,"name":"我的测试客户","ownerId":null,"ownerName":null,"address":null,"hy":","},{"type":5,"id":617,"name":" 中国平安","ownerId":null,"ownerName":null,"address":null,"hy":",保险公司,"}]}],
	// "k":[]}
	private String asso; // 知识关联
	// 草稿箱用
	private String knowledgeid;

	// 老知识id
	private long oid;
	// 价格
	private float price;

	// 法律法规-发文单位
	private String postUnit;

	// 法律法规-文号
	private String titanic;

	// 法律法规-发布日期
	private String submitTime;

	// 法律法规-执行日期
	private String performTime;

	// 描述
	private String desc;

	// 知识存入知识目录表中的 状态（0：不生效，1：生效）
	private String status;

	// 知识创建时间
	private String createtime;

	private String synonyms; // 同义词

	private Long knowledgeMainId;// 代表草稿箱中存储了真正知识的ID

	// 状态（1为草稿，2：待审核 3：审核中 4：审核通过 5：未通过 6：回收站,7永久删除)
	private int knowledgestatus;

	// 引用
	private String refrenceData;
	// 图册
	private String imageBookData;
	// 是否需要更新mongo
	private boolean needUpdate;

	private String hideDesc;

	private String fileType;

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getHideDesc() {
		return hideDesc;
	}

	public void setHideDesc(String hideDesc) {
		this.hideDesc = hideDesc;
	}

	public String getRefrenceData() {
		return refrenceData;
	}

	/**
	 * @return the needUpdate
	 */
	public boolean isNeedUpdate() {
		return needUpdate;
	}

	/**
	 * @param needUpdate
	 *            the needUpdate to set
	 */
	public void setNeedUpdate(boolean needUpdate) {
		this.needUpdate = needUpdate;
	}

	public void setRefrenceData(String refrenceData) {
		this.refrenceData = refrenceData;
	}

	public String getImageBookData() {
		return imageBookData;
	}

	public void setImageBookData(String imageBookData) {
		this.imageBookData = imageBookData;
	}

	public int getKnowledgestatus() {
		return knowledgestatus;
	}

	public void setKnowledgestatus(int knowledgestatus) {
		this.knowledgestatus = knowledgestatus;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPostUnit() {
		return postUnit;
	}

	public void setPostUnit(String postUnit) {
		this.postUnit = postUnit;
	}

	public String getTitanic() {
		return titanic;
	}

	public void setTitanic(String titanic) {
		this.titanic = titanic;
	}

	public String getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(String submitTime) {
		this.submitTime = submitTime;
	}

	public String getPerformTime() {
		return performTime;
	}

	public void setPerformTime(String performTime) {
		this.performTime = performTime;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getkId() {
		return kId;
	}

	public void setkId(Long kId) {
		this.kId = kId;
	}

	public String getColumnPath() {
		return columnPath;
	}

	public void setColumnPath(String columnPath) {
		this.columnPath = columnPath;
	}

	public String getColumnType() {
		return columnType;
	}

	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}

	public String getShareMessage() {
		return shareMessage;
	}

	public void setShareMessage(String shareMessage) {
		this.shareMessage = shareMessage;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getCatalogueIds() {
		return catalogueIds;
	}

	public void setCatalogueIds(String catalogueIds) {
		this.catalogueIds = catalogueIds;
	}

	public String getEssence() {
		return essence;
	}

	public void setEssence(String essence) {
		this.essence = essence;
	}

	public String getSelectedIds() {
		return selectedIds;
	}

	public void setSelectedIds(String selectedIds) {
		this.selectedIds = selectedIds;
	}

	public String getColumnid() {
		return columnid;
	}

	public void setColumnid(String columnid) {
		this.columnid = columnid;
	}

	public long getOid() {
		return oid;
	}

	public void setOid(long oid) {
		this.oid = oid;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getAsso() {
		return asso;
	}

	public void setAsso(String asso) {
		this.asso = asso;
	}

	public String getKnowledgeid() {
		return knowledgeid;
	}

	public void setKnowledgeid(String knowledgeid) {
		this.knowledgeid = knowledgeid;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getSynonyms() {
		return synonyms;
	}

	public void setSynonyms(String synonyms) {
		this.synonyms = synonyms;
	}

	public Long getKnowledgeMainId() {
		return knowledgeMainId;
	}

	public void setKnowledgeMainId(Long knowledgeMainId) {
		this.knowledgeMainId = knowledgeMainId;
	}

}
