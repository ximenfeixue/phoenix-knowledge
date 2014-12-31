package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;

/**
 * @Title: ConnectionBaseVO.java
 * @Package com.ginkgocap.ywxt.knowledge.model
 * @Description:
 * @author haiyan
 * @date 2014-12-31 下午1:38:45
 */
public class ConnectionBaseVO implements Serializable{
	private static final long serialVersionUID = 1L;

	/**主键ID**/
	private Long id;
	/**关联类型(1-需求 2-人 3-组织 4-知识)**/
	private Integer conntype;
	/**关联数据ID**/
	private Long connid;
	/**关联数据名称**/
	private String connname;
	/**拥有者ID**/
	private Long ownerid;
	/**拥有者名称**/
	private String owner;
	/**跳转链接url**/
	private String url;
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the conntype
	 */
	public Integer getConntype() {
		return conntype;
	}
	/**
	 * @param conntype the conntype to set
	 */
	public void setConntype(Integer conntype) {
		this.conntype = conntype;
	}
	/**
	 * @return the connid
	 */
	public Long getConnid() {
		return connid;
	}
	/**
	 * @param connid the connid to set
	 */
	public void setConnid(Long connid) {
		this.connid = connid;
	}
	/**
	 * @return the connname
	 */
	public String getConnname() {
		return connname;
	}
	/**
	 * @param connname the connname to set
	 */
	public void setConnname(String connname) {
		this.connname = connname;
	}
	/**
	 * @return the ownerid
	 */
	public Long getOwnerid() {
		return ownerid;
	}
	/**
	 * @param ownerid the ownerid to set
	 */
	public void setOwnerid(Long ownerid) {
		this.ownerid = ownerid;
	}
	/**
	 * @return the owner
	 */
	public String getOwner() {
		return owner;
	}
	/**
	 * @param owner the owner to set
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	
}
