package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;
import java.util.Date;

import com.ginkgocap.ywxt.user.model.User;

/**
 * 知识javaBean （资讯，资产管理,宏观，观点，文章）
 * 
 * @author Administrator
 * 
 */
public class Knowledge implements Serializable {

	private static final long serialVersionUID = 1L;

	// id
	private long id;

	// 标题
	private String title;

	// 作者
	private long uid;

	// 作者名称
	private String uname;

	// 创建人id
	private long cid;

	// 创建人名称
	private String cname;

	// 来源
	private String source;

	// 来源地址
	private String s_addr;

	// 栏目路徑
	private String cpathid;

	// 封面地址
	private String pic;

	// 描述
	private String desc;

	// 原内容
	private String content;

	private String hcontent;

	// 是否加精
	private int essence;

	// 创建时间
	private String createtime;

	// 最后修改时间
	private String modifytime;

	// 状态（1为草稿，2：待审核 3：审核中 4：审核通过 5：未通过 6：回收站)
	private int status;

	// 举报状态(1:举报，0：未举报)
	private int report_status;

	// 附件ID
	private String taskid;

	// 标签
	private String tags;

	// 栏目
	private String columnid;

	// 关联
	private String asso;

	private String selectedIds;

	private String columnType;

	// 投融工具、行业、经典案例用到的隐藏DIV，用于编辑、阅读器使用，之前的desc仅用于知识查询列表展现
	private String hideDesc;

	// 法律法规-发布日期
	private String submitTime;

	// 法律法规-执行日期
	private String performTime;

	// 法律法规-发文单位
	private String postUnit;

	// 法律法规-文号
	private String titanic;

	private String synonyms; // 同义词

	private String fileType;

	// 转换状态
	private int tranStatus;

	public int getTranStatus() {
		return tranStatus;
	}

	public void setTranStatus(int tranStatus) {
		this.tranStatus = tranStatus;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getSynonyms() {
		return synonyms;
	}

	public void setSynonyms(String synonyms) {
		this.synonyms = synonyms;
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

	public String getHideDesc() {
		return hideDesc;
	}

	public void setHideDesc(String hideDesc) {
		this.hideDesc = hideDesc;
	}

	// 系统时间（大数据推送带过来的数据）
	private String sysTime;

	private Long knowledgeMainId;// 代表草稿箱中存储了真正知识的ID

	/**
	 * @return the columnType
	 */
	public String getColumnType() {
		return columnType;
	}

	/**
	 * @param columnType
	 *            the columnType to set
	 */
	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}

	public String getSysTime() {
		return sysTime;
	}

	public void setSysTime(String sysTime) {
		this.sysTime = sysTime;
	}

	public String getSelectedIds() {
		return selectedIds;
	}

	public void setSelectedIds(String selectedIds) {
		this.selectedIds = selectedIds;
	}

	public String getAsso() {
		return asso;
	}

	public void setAsso(String asso) {
		this.asso = asso;
	}

	public String getColumnid() {
		return columnid;
	}

	public void setColumnid(String columnid) {
		this.columnid = columnid;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	// 高亮状态（0-未加 1-已加）
	private int ish;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public long getCid() {
		return cid;
	}

	public void setCid(long cid) {
		this.cid = cid;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getS_addr() {
		return s_addr;
	}

	public void setS_addr(String s_addr) {
		this.s_addr = s_addr;
	}

	public String getCpathid() {
		return cpathid;
	}

	public void setCpathid(String cpathid) {
		this.cpathid = cpathid;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getHcontent() {
		return hcontent;
	}

	public void setHcontent(String hcontent) {
		this.hcontent = hcontent;
	}

	public int getEssence() {
		return essence;
	}

	public void setEssence(int essence) {
		this.essence = essence;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getModifytime() {
		return modifytime;
	}

	public void setModifytime(String modifytime) {
		this.modifytime = modifytime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getReport_status() {
		return report_status;
	}

	public void setReport_status(int report_status) {
		this.report_status = report_status;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public int getIsh() {
		return ish;
	}

	public void setIsh(int ish) {
		this.ish = ish;
	}

	public <T> Knowledge setValue(KnowledgeNewsVO vo, User user) {
		return null;
	}

	public <T> Knowledge setDraftValue(KnowledgeNewsVO vo, User user) {
		return null;
	}

	public Long getKnowledgeMainId() {
		return knowledgeMainId;
	}

	public void setKnowledgeMainId(Long knowledgeMainId) {
		this.knowledgeMainId = knowledgeMainId;
	}
}