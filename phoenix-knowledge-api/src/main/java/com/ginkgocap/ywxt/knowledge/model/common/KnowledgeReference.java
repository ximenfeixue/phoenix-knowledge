package com.ginkgocap.ywxt.knowledge.model.common;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @Title: 知识来源信息
 * @date 2016年1月11日 下午3:38:53
 * @version V1.0.0
 */
@Entity
@Table(name = "tb_knowledge_reference", catalog = "phoenix_knowledge_new")
public class KnowledgeReference implements Serializable {

	/** */
	private static final long serialVersionUID = 4445285733282369227L;
	
	/**主键*/
	private long id;
	
	/**知识主键*/
	private long knowledgeId;
	
	/**引用资料文章名称*/
	private String articleName;
	
	/**引用网址*/
	private String url;
	
	/**应用网址名称*/
	private String websiteName;
	
	/**标示本条资料是否有效，1：为有效，0：为无效*/
	private short status;
	
	/**引用时间*/
	private long refDate;
	
	/**创建时间*/
	private long createDate;
	
	/**修改时间*/
	private long modifyDate;

	@Id
	@Column(name = "id", unique = true, nullable = false)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "knowledge_id")
	public long getKnowledgeId() {
		return knowledgeId;
	}

	public void setKnowledgeId(long knowledgeId) {
		this.knowledgeId = knowledgeId;
	}

	@Column(name = "article_name")
	public String getArticleName() {
		return articleName;
	}

	public void setArticleName(String articleName) {
		this.articleName = articleName;
	}

	@Column(name = "url")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Column(name = "website_name")
	public String getWebsiteName() {
		return websiteName;
	}

	public void setWebsiteName(String websiteName) {
		this.websiteName = websiteName;
	}

	@Column(name = "status")
	public short getStatus() {
		return status;
	}

	public void setStatus(short status) {
		this.status = status;
	}

	@Column(name = "ref_date")
	public long getRefDate() {
		return refDate;
	}

	public void setRefDate(long refDate) {
		this.refDate = refDate;
	}

	@Column(name = "create_date")
	public long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}

	@Column(name = "modify_date")
	public long getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(long modifyDate) {
		this.modifyDate = modifyDate;
	}
}