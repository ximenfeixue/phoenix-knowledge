package com.ginkgocap.ywxt.knowledge.model;

import com.ginkgocap.ywxt.knowledge.utils.KnowledgeConstant;
import com.ginkgocap.ywxt.user.model.User;

/**
 * 知识javaBean （资产管理）
 * 
 * @author caihe
 * 
 */
public class KnowledgeAsset extends Knowledge {

	private static final long serialVersionUID = 1L;

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
		this.setAsso(vo.getAsso());
		this.setSelectedIds(vo.getSelectedIds());
		this.setTaskid(vo.getTaskId());
		this.setKnowledgeMainId(vo.getKnowledgeMainId());
		return this;
	}
}