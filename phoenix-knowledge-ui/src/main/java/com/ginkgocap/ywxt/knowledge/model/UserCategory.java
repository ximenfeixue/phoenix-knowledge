package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;

public class UserCategory implements Serializable {
	
	private static final long serialVersionUID = -9062835517314960963L;

	private Long id;

    private Long userId;

    private String categoryname;

    private String sortid;

    private Date createtime;

    private String pathName;

    private Long parentId;

    private Short categoryType;
    
    private List<UserCategory> listUserCategory;
    
    private Integer level;

    public UserCategory() {
    	super();
    }
    
    public UserCategory(com.ginkgocap.ywxt.knowledge.entity.UserCategory userCategory) {
    	
    	this.id = userCategory.getId();
    	this.userId = userCategory.getUserId();
    	this.categoryname = userCategory.getCategoryname();
    	this.sortid = userCategory.getSortid();
    	this.createtime = userCategory.getCreatetime();
    	this.pathName = userCategory.getPathName();
    	this.parentId = userCategory.getParentId();
    	this.categoryType = userCategory.getCategoryType();
    	
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public String getSortid() {
        return sortid;
    }

    public void setSortid(String sortid) {
        this.sortid = sortid;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Short getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(Short categoryType) {
        this.categoryType = categoryType;
    }

	public List<UserCategory> getListUserCategory() {
		return listUserCategory;
	}

	public void setListUserCategory(List<UserCategory> listUserCategory) {
		this.listUserCategory = listUserCategory;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
  
	/**
	 * @author zhangzhen
	 * 如果数据为空返回null
	 * 
	 * 指导使用方法
	 * JSONObject j = JSONObject.fromObject(requestJson);
	 * String jsonData = j.getString("Entity");
	 * */
	public static UserCategory getByJsonString(String jsonEntity) {
		if(jsonEntity.equals("{}")) {
			return null; //无数据判断
		}
		return JSON.parseObject(jsonEntity, UserCategory.class);
	}
	
	/**
	 * @author zhangzhen
	 * 如果数据为空返回null
	 * 
	 * 指导使用方法
	 * JSONObject j = JSONObject.fromObject(requestJson);
	 * Object jsonData = j.get("Entity");
	 * */
	public static UserCategory getByJsonObject(Object jsonEntity) {
		return getByJsonString(jsonEntity.toString());
	}
	
	/**
	 * @author zhangzhen
	 * 如果没有数据，返回空数组
	 * 
	 * 指导使用方法
	 * JSONObject j = JSONObject.fromObject(requestJson);
	 * String jsonData = j.getString("Entity");
	 * */
	public static List<UserCategory> getListByJsonString(String object) {
		return JSON.parseArray(object, UserCategory.class);
	}
	
	/**
	 * @author zhangzhen
	 * @CreateTime 2014-11-11
	 * 如果没有数据，返回空数组
	 * 
	 * 指导使用方法 
	 * JSONObject j = JSONObject.fromObject(requestJson);
	 * Object jsonData = j.get("EntityList");
	 * */
	public static List<UserCategory> getListByJsonObject(Object object) {
		return getListByJsonString(object.toString());
	}
}
