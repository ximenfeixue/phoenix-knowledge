package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;
import java.util.List;


public class Category implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9210031377998416429L;
	 
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public long getParentId() {
		return parentId;
	}
	public void setParentId(long parentId) {
		this.parentId = parentId;
	}
	public String getSortId() {
		return sortId;
	}
	public void setSortId(String sortId) {
		this.sortId = sortId;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getSubtime() {
		return subtime;
	}
	public void setSubtime(String subtime) {
		this.subtime = subtime;
	}
	public String getModtime() {
		return modtime;
	}
	public void setModtime(String modtime) {
		this.modtime = modtime;
	}
	public List<Category> getChildList() {
		return childList;
	}
	public void setChildList(List<Category> childList) {
		this.childList = childList;
	}



		/** 主键 */
	private long id;
	/** phoenix_user.tb_user.id */
	private long uid;
	/** 分类名称 */
	private String categoryName;
	/** 父类ID */
	private long parentId;
	/** 排序ID，九位一级 如000000001000000001,为一级分类下的第一个分类 */
	private String sortId;
	/** 分类状态 0:正常   1:删除 */
	private String state;
	/** 分类创建时间 */
	private String subtime;
	/** 最后修改时间 */
	private String modtime;
	/** 子分类列表 */
	private List<Category> childList;
}
