package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;

import com.ginkgocap.ywxt.knowledge.entity.UserCategory;

/**
 * 用户目录左树
 * <p>
 * 于2014-9-16 由 bianzhiwei 创建
 * </p>
 * 
 * @author <p>
 *         当前负责人 bianzhiwei
 *         </p>
 * 
 */
public class AdminUserCategory extends UserCategory implements Serializable {

	private static final long serialVersionUID = 9210031377998416429L;

	private int usetype; // 删除标识位(1：默认、0：删除)

	private String createName; // 创建人

	private String desc; // 描述

	public int getUsetype() {
		return usetype;
	}

	public void setUsetype(int usetype) {
		this.usetype = usetype;
	}

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
