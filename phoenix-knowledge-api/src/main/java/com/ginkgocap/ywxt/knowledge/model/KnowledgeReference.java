package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * @Title: ֪ʶ��Դ��Ϣ
 * @author ������
 * @date 2016��1��11�� ����3:38:53
 * @version V1.0.0
 */
@Entity
@Table(name = "tb_knowledge_reference", catalog = "phoenix_knowledge")
public class KnowledgeReference implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4445285733282369227L;
	
	/**����*/
	private long id;
	
	/**֪ʶ����*/
	private long knowledgeId;
	
	/**����������������*/
	private String articleName;
	
	/**������ַ*/
	private String url;
	
	/**Ӧ����ַ����*/
	private String websiteName;
	
	/**��ʾ���������Ƿ���Ч��1��Ϊ��Ч��0��Ϊ��Ч*/
	private String status;
	
	/**����ʱ��*/
	private String refDate;
	
	/**����ʱ��*/
	private String createDate;
	
	/**�޸�ʱ��*/
	private String modifyDate;

	@Id
	@GeneratedValue(generator = "id")
	@GenericGenerator(name = "id", strategy = "com.ginkgocap.ywxt.framework.dal.dao.id.util.TimeIdGenerator", parameters = { @Parameter(name = "sequence", value = "t_knowledge_reference_label") })
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
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "ref_date")
	public String getRefDate() {
		return refDate;
	}

	public void setRefDate(String refDate) {
		this.refDate = refDate;
	}

	@Column(name = "create_date")
	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	@Column(name = "modify_date")
	public String getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}
	
}