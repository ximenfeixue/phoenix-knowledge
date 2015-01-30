package com.ginkgocap.ywxt.knowledge.model;

/**
 * @Title: ConnRequireVO.java
 * @Package com.ginkgocap.ywxt.knowledge.model
 * @Description:
 * @author haiyan
 * @date 2014-12-31 下午1:51:19
 */
public class ConnRequireVO extends ConnectionBaseVO {

	private static final long serialVersionUID = 1L;

	/** 需求类型 **/
	private String requirementtype;

	/**
	 * @return the requirementtype
	 */
	public String getRequirementtype() {
		return requirementtype;
	}

	/**
	 * @param requirementtype
	 *            the requirementtype to set
	 */
	public void setRequirementtype(String requirementtype) {
		this.requirementtype = requirementtype;
	}
}
