package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import com.ginkgocap.ywxt.knowledge.utils.KnowledgeConstant;
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

}