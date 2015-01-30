package com.ginkgocap.ywxt.knowledge.model;

/**
 * @Title: ConnPeopleVO.java
 * @Package com.ginkgocap.ywxt.knowledge.model
 * @Description:
 * @author haiyan
 * @date 2014-12-31 下午1:49:24
 */
public class ConnPeopleVO extends ConnectionBaseVO {

	private static final long serialVersionUID = 1L;

	/** 职业 **/
	private String career;
	/** 公司 **/
	private String company;

	/**
	 * @return the career
	 */
	public String getCareer() {
		return career;
	}

	/**
	 * @param career
	 *            the career to set
	 */
	public void setCareer(String career) {
		this.career = career;
	}

	/**
	 * @return the company
	 */
	public String getCompany() {
		return company;
	}

	/**
	 * @param company
	 *            the company to set
	 */
	public void setCompany(String company) {
		this.company = company;
	}

}
