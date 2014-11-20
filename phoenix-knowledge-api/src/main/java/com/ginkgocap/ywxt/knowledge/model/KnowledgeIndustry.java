package com.ginkgocap.ywxt.knowledge.model;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.ginkgocap.ywxt.knowledge.util.Constants;
import com.ginkgocap.ywxt.knowledge.util.HtmlToText;
import com.ginkgocap.ywxt.user.model.User;

/**
 * 知识javaBean （行业）
 * 
 * @author caihe
 * 
 */
public class KnowledgeIndustry extends Knowledge {

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
		this.setColumnid(vo.getColumnid() + "");
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
		this.setDesc(HtmlToText.htmlTotest(vo.getDesc()));
		this.setContent(vo.getContent());
		this.setEssence(Integer.parseInt(StringUtils.isBlank(vo.getEssence()) ? "0"
				: vo.getEssence()));
		this.setCreatetime(vo.getCreatetime());
		this.setStatus(vo.getSelectedIds().equals(Constants.Ids.platform.v()) ? Constants.Status.checking
				.v() : Constants.Status.checked.v());
		this.setReport_status(Constants.ReportStatus.unreport.v());
		this.setIsh(Constants.HighLight.unlight.v());
		this.setHcontent("");
		
		this.setOid(vo.getOid());
		this.setDesc(vo.getDesc());
		
		this.setSelectedIds(vo.getSelectedIds());
		this.setAsso(vo.getAsso());
		this.setKnowledgeMainId(vo.getKnowledgeMainId());
		return this;
	}

	@Override
	public <T> Knowledge setDraftValue(KnowledgeNewsVO vo, User user) {
		this.setColumnid(vo.getColumnid() + "");
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
		this.setDesc(HtmlToText.htmlTotest(vo.getDesc()));
		this.setContent(vo.getContent());
		this.setEssence(Integer.parseInt(StringUtils.isBlank(vo.getEssence()) ? "0"
				: vo.getEssence()));
		this.setCreatetime(vo.getCreatetime());
		this.setStatus(Constants.Status.draft.v());
		this.setReport_status(Constants.ReportStatus.unreport.v());
		this.setIsh(Constants.HighLight.unlight.v());
		this.setHcontent("");
		this.setOid(vo.getOid());
		this.setDesc(vo.getDesc());
		
		this.setSelectedIds(vo.getSelectedIds());
		this.setAsso(vo.getAsso());
		this.setKnowledgeMainId(vo.getKnowledgeMainId());
		return this;
	}
	
}