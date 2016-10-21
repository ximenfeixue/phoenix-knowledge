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
}