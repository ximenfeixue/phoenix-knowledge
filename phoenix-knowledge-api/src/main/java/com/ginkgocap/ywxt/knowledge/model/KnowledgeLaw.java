package com.ginkgocap.ywxt.knowledge.model;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.ginkgocap.ywxt.knowledge.util.Constants;
import com.ginkgocap.ywxt.user.model.User;

/**
 * 知识javaBean （法律法规）
 * 
 * @author caihe
 * 
 */
public class KnowledgeLaw extends Knowledge {

	// 老知识ID
	private long oid;

	public long getOid() {
		return oid;
	}

	public void setOid(long oid) {
		this.oid = oid;
	}

	private static final long serialVersionUID = 1L;

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
		this.setDesc(vo.getContent().length() > 50 ? vo.getContent().substring(
				0, 50) : vo.getContent());
		this.setContent(vo.getContent());
		this.setEssence(Integer.parseInt(StringUtils.isBlank(vo.getEssence()) ? "0"
				: vo.getEssence()));
		this.setCreatetime(new Date());
		this.setStatus(vo.getSelectedIds().equals(Constants.Ids.platform.v()) ? Constants.Status.checking
				.v() : Constants.Status.checked.v());
		this.setReport_status(Constants.ReportStatus.unreport.v());
		this.setIsh(Constants.HighLight.unlight.v());
		this.setHcontent("");
		
		return this;
	}

}