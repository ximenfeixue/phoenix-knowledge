package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;
import java.util.Date;

public class KnowledgeCollection implements Serializable {

	/**
	 * 知识收藏
	 */
	private static final long serialVersionUID = 7827738358308956910L;

	private long id;
	private long knowledge_id; // '知识id',
	private long column_id; // '栏目id',
	private Date timestamp;// '收藏时间',
	private String knowledgeType; // '知识类型（默认0：其他,1：资讯，2：投融工具，3：行业，4：经典案例，5：图书报告，6：资产管理，7：宏观，8：观点，9：判例，10，法律法规，11：文章）',
	private String source; // '来源(1：自己，2：好友，3：金桐脑，4：全平台，5：组织)',
	private long category_id; // '目录id(左侧目录.id)',

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getKnowledge_id() {
		return knowledge_id;
	}

	public void setKnowledge_id(long knowledge_id) {
		this.knowledge_id = knowledge_id;
	}

	public long getColumn_id() {
		return column_id;
	}

	public void setColumn_id(long column_id) {
		this.column_id = column_id;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getKnowledgeType() {
		return knowledgeType;
	}

	public void setKnowledgeType(String knowledgeType) {
		this.knowledgeType = knowledgeType;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public long getCategory_id() {
		return category_id;
	}

	public void setCategory_id(long category_id) {
		this.category_id = category_id;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
