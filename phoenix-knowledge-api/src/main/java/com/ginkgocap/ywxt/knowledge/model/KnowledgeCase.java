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
}