package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.ginkgocap.ywxt.knowledge.util.Constants;
import com.ginkgocap.ywxt.user.model.User;

/**
 * 知识javaBean （投融工具）
 * 
 * @author caihe
 * 
 */
public class KnowledgeInvestment extends Knowledge implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6269965672863016722L;
	// 老知识ID
	private long oid;
	
	//同义词
	private String synonyms;
	//引用
	private String refrenceData;
	//图册
	private String imageBookData;
	
	public String getRefrenceData() {
		return refrenceData;
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
		this.setDesc(vo.getDesc());
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
		
		this.setAsso(vo.getAsso());
		this.setSelectedIds(vo.getSelectedIds());
		this.setSynonyms(vo.getSynonyms());
		this.setKnowledgeMainId(vo.getKnowledgeMainId());
		this.setRefrenceData(vo.getRefrenceData());
		this.setImageBookData(vo.getImageBookData());
		this.setHideDesc(vo.getHideDesc());
		this.setFileType(vo.getFileType());
		this.setTaskid(vo.getTaskId());
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
		this.setDesc(vo.getDesc());
		this.setContent(vo.getContent());
		this.setEssence(Integer.parseInt(StringUtils.isBlank(vo.getEssence()) ? "0"
				: vo.getEssence()));
		this.setCreatetime(vo.getCreatetime());
		this.setStatus(Constants.Status.draft.v());
		this.setReport_status(Constants.ReportStatus.unreport.v());
		this.setIsh(Constants.HighLight.unlight.v());
		this.setHcontent("");
		this.setOid(vo.getOid());

		this.setAsso(vo.getAsso());
		this.setSelectedIds(vo.getSelectedIds());
		this.setSynonyms(vo.getSynonyms());
		this.setKnowledgeMainId(vo.getKnowledgeMainId());
		this.setRefrenceData(vo.getRefrenceData());
		this.setImageBookData(vo.getImageBookData());
		this.setHideDesc(vo.getHideDesc());
		return this;
	}

	public String getSynonyms() {
		return synonyms;
	}

	public void setSynonyms(String synonyms) {
		this.synonyms = synonyms;
	}
	
}