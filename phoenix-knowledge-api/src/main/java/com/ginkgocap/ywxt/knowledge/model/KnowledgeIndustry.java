package com.ginkgocap.ywxt.knowledge.model;


import com.ginkgocap.ywxt.knowledge.utils.KnowledgeConstant;
import org.apache.commons.lang3.StringUtils;

import com.ginkgocap.ywxt.user.model.User;

/**
 * 知识javaBean （行业）
 * 
 * @author caihe
 * 
 */
public class KnowledgeIndustry extends Knowledge 
{
	private static final long serialVersionUID = -7620510485620837623L;
	// 老知识ID
	private long oid;

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
		this.setSource("");
		this.setS_addr("");
		this.setCpathid(vo.getColumnPath());
		this.setPic(vo.getPic());
		this.setDesc(vo.getDesc());
		this.setContent(vo.getContent());
		this.setEssence(Integer.parseInt(StringUtils.isBlank(vo.getEssence()) ? "0"	: vo.getEssence()));
		this.setCreatetime(vo.getCreatetime());
		this.setStatus(vo.getSelectedIds().equals(KnowledgeConstant.Ids.EPlatform.v()) ? KnowledgeConstant.Status.checking
				.v() : KnowledgeConstant.Status.checked.v());
		this.setReport_status(KnowledgeConstant.ReportStatus.unreport.v());
		this.setIsh(KnowledgeConstant.HighLight.unlight.v());
		this.setHcontent("");
		
		this.setOid(vo.getOid());
		this.setTaskid(vo.getTaskId());
		this.setSelectedIds(vo.getSelectedIds());
		this.setAsso(vo.getAsso());
		this.setKnowledgeMainId(vo.getKnowledgeMainId());
		this.setHideDesc(vo.getHideDesc());
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
		this.setSource("");
		this.setS_addr("");
		this.setCpathid(vo.getColumnPath());
		this.setPic(vo.getPic());
		this.setDesc(vo.getDesc());
		this.setContent(vo.getContent());
		this.setEssence(Integer.parseInt(StringUtils.isBlank(vo.getEssence()) ? "0"
				: vo.getEssence()));
		this.setCreatetime(vo.getCreatetime());
		this.setStatus(KnowledgeConstant.Status.draft.v());
		this.setReport_status(KnowledgeConstant.ReportStatus.unreport.v());
		this.setIsh(KnowledgeConstant.HighLight.unlight.v());
		this.setHcontent("");
		this.setOid(vo.getOid());
		
		this.setSelectedIds(vo.getSelectedIds());
		this.setAsso(vo.getAsso());
		this.setKnowledgeMainId(vo.getKnowledgeMainId());
		this.setHideDesc(vo.getHideDesc());
		return this;
	}
}