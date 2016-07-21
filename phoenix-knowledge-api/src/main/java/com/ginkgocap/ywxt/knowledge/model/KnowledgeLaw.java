package com.ginkgocap.ywxt.knowledge.model;

import com.ginkgocap.ywxt.knowledge.utils.KnowledgeConstant;
import com.ginkgocap.ywxt.user.model.User;

/**
 * 知识javaBean （法律法规）
 * 
 * @author caihe
 * 
 */
public class KnowledgeLaw extends Knowledge 
{
	private static final long serialVersionUID = 1L;
	// 老知识ID
	private long oid;

	// 法律法规-发文单位
	private String postUnit;

	// 法律法规-文号
	private String titanic;

	// 法律法规-发布日期
	private String submitTime;

	// 法律法规-执行日期
	private String performTime;

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

	public long getOid() {
		return oid;
	}

	public void setOid(long oid) {
		this.oid = oid;
	}

	

	@Override
	public <T> Knowledge setValue(KnowledgeNewsVO vo, User user) {
		this.setColumnid(String.valueOf(vo.getColumnid()));
		this.setUid(user.getId());
		this.setUname(user.getName());
		this.setTags(vo.getTags());
		this.setId(vo.getkId());
		this.setTitle(vo.getTitle());
		this.setCid(user.getId());
		this.setCname(user.getName());
		this.setSource(vo.getSource());
		this.setS_addr("");
		this.setCpathid(vo.getColumnPath());
		this.setPic(vo.getPic());
		this.setDesc(vo.getDesc());
		this.setContent(vo.getContent());
		this.setEssence(Integer.parseInt(vo.getEssence()));
		this.setCreatetime(vo.getCreatetime());
		this.setStatus(vo.getSelectedIds().equals(KnowledgeConstant.Ids.EPlatform.v()) ? KnowledgeConstant.Status.checking
				.v() : KnowledgeConstant.Status.checked.v());
		this.setReport_status(KnowledgeConstant.ReportStatus.unreport.v());
		this.setIsh(KnowledgeConstant.HighLight.unlight.v());
		this.setHcontent("");
		this.setPerformTime(String.valueOf(vo.getPerformTime()));
		this.setPostUnit(vo.getPostUnit());
		this.setTitanic(vo.getTitanic());
		this.setSubmitTime(String.valueOf(vo.getSubmitTime()));
		this.setAsso(vo.getAsso());
		this.setSelectedIds(vo.getSelectedIds());
		this.setTaskid(vo.getTaskId());
		this.setKnowledgeMainId(vo.getKnowledgeMainId());
		this.setFileType(vo.getFileType());
		return this;
	}

	@Override
	public <T> Knowledge setDraftValue(KnowledgeNewsVO vo, User user) {
		this.setColumnid(String.valueOf(vo.getColumnid()));
		this.setUid(user.getId());
		this.setUname(user.getName());
		this.setTags(vo.getTags());
		this.setId(vo.getkId());
		this.setTitle(vo.getTitle());
		this.setCid(user.getId());
		this.setCname(user.getName());
		this.setSource(vo.getSource());
		this.setS_addr("");
		this.setCpathid(vo.getColumnPath());
		this.setPic(vo.getPic());
		this.setDesc(vo.getDesc());
		this.setContent(vo.getContent());
		this.setEssence(Integer.parseInt(vo.getEssence()));
		this.setCreatetime(vo.getCreatetime());
		this.setStatus(KnowledgeConstant.Status.draft.v());
		this.setReport_status(KnowledgeConstant.ReportStatus.unreport.v());
		this.setIsh(KnowledgeConstant.HighLight.unlight.v());
		this.setHcontent("");
		this.setOid(vo.getOid());
		this.setPerformTime(String.valueOf(vo.getPerformTime()));
		this.setPostUnit(vo.getPostUnit());
		this.setTitanic(vo.getTitanic());
		this.setSubmitTime(String.valueOf(vo.getSubmitTime()));
		this.setAsso(vo.getAsso());
		this.setSelectedIds(vo.getSelectedIds());
		this.setTaskid(vo.getTaskId());
		this.setKnowledgeMainId(vo.getKnowledgeMainId());
		return this;
	}
}