package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;
import java.util.Date;

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

	public long getOid() {
		return oid;
	}

	public void setOid(long oid) {
		this.oid = oid;
	}

}