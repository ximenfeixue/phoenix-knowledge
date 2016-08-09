package com.ginkgocap.ywxt.knowledge.model;

import org.apache.commons.lang3.StringUtils;

import com.ginkgocap.ywxt.knowledge.utils.KnowledgeConstant;
import com.ginkgocap.ywxt.user.model.User;

/**
 * 知识javaBean （经典案例）
 * 
 * @author Administrator
 * 
 */
public class KnowledgeCase extends Knowledge {

	private static final long serialVersionUID = -5784533000140594006L;

	// 老知识ID
	private long oid;

	// 价格
	private float price;

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
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
		this.setSource("");
		this.setS_addr("");
		this.setCpathid(vo.getColumnPath());
		this.setPic(vo.getPic());
		this.setDesc(vo.getDesc());
		this.setEssence(Integer.parseInt(StringUtils.isBlank(vo.getEssence()) ? "0"
				: vo.getEssence()));
		this.setCreatetime(vo.getCreatetime());
		this.setStatus(vo.getSelectedIds().equals(
				KnowledgeConstant.Ids.EPlatform.v()) ? KnowledgeConstant.Status.checking
				.v() : KnowledgeConstant.Status.checked.v());
		this.setReport_status(KnowledgeConstant.ReportStatus.unreport.v());
		this.setIsh(KnowledgeConstant.HighLight.unlight.v());
		this.setHcontent("");

		this.setOid(vo.getOid());
		this.setPrice(vo.getPrice());

		this.setAsso(vo.getAsso());

		this.setTaskid(vo.getTaskId());
		this.setSelectedIds(vo.getSelectedIds());
		this.setKnowledgeMainId(vo.getKnowledgeMainId());
		this.setContent(vo.getContent());
		this.setTranStatus(0);
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
		this.setPrice(vo.getPrice());

		this.setTaskid(vo.getTaskId());
		this.setSelectedIds(vo.getSelectedIds());
		this.setAsso(vo.getAsso());
		this.setKnowledgeMainId(vo.getKnowledgeMainId());
		this.setTranStatus(0);
		this.setHideDesc(vo.getHideDesc());
		return this;
	}
}