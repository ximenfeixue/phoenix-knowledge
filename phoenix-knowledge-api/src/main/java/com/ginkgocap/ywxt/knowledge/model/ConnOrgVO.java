package com.ginkgocap.ywxt.knowledge.model;

/**
 * @Title: ConnOrgVO.java
 * @Package com.ginkgocap.ywxt.knowledge.model
 * @Description:
 * @author haiyan
 * @date 2014-12-31 下午1:54:35
 */
public class ConnOrgVO extends ConnectionBaseVO {

	private static final long serialVersionUID = 1L;

	/** 地址 **/
	private String address;

	/** 行业 **/
	private String hy;

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the hy
	 */
	public String getHy() {
		return hy;
	}

	/**
	 * @param hy
	 *            the hy to set
	 */
	public void setHy(String hy) {
		this.hy = hy;
	}

}
