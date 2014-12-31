package com.ginkgocap.ywxt.knowledge.model;

/**
 * @Title: ConnKnowledgeVO.java
 * @Package com.ginkgocap.ywxt.knowledge.model
 * @Description:
 * @author haiyan
 * @date 2014-12-31 下午1:53:15
 */
public class ConnKnowledgeVO extends ConnectionBaseVO {

	private static final long serialVersionUID = 1L;

	/** 栏目路径 **/
	private String columnpath;

	/** 栏目类型 **/
	private Integer columntype;

	/**
	 * @return the columnpath
	 */
	public String getColumnpath() {
		return columnpath;
	}

	/**
	 * @param columnpath
	 *            the columnpath to set
	 */
	public void setColumnpath(String columnpath) {
		this.columnpath = columnpath;
	}

	/**
	 * @return the columntype
	 */
	public Integer getColumntype() {
		return columntype;
	}

	/**
	 * @param columntype
	 *            the columntype to set
	 */
	public void setColumntype(Integer columntype) {
		this.columntype = columntype;
	}

}
