/**
 * Copyright (c) 2011 银杏资本.
 * All Rights Reserved. 保留所有权利.
 */
package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSON;

public class JTMember implements Serializable {

	private static final long serialVersionUID = 4437254356672916767L;

	private int id;// id

	private long uid; // uid

	private String nick;// 昵称

	private String username;// 登录用户名

	private String image; // 用户头像

	private String mobile;// 手机号码,如果用户是邮箱注册的，该值可能为null

	private String email;// 邮箱, 如果用户是手机号码注册的， 改制可能为null

	private boolean mobileAuth;// 手机号码是否已验证

	private boolean emailAuth;// 邮箱是否已验证

	private int role;// 0-普通用户，1-机构业务员

	private int userType;// 1-个人用户，2-机构用户

	private Integer userStatus;// 用户状态 0:邮箱未验证 1:信息未完善 2已完善个人信息

	private Integer homePageVisible;// 容许非好友浏览我的主页 1 对好友可见(不容许) 2所有人可见(容许)

	private Integer evaluateVisible;// 容许非好友对我评价 1 对好友可见(不容许) 2所有人可见(容许)

	private String qqNikeName;

	private String sinaWeiboNikeName;

	private String qqlogin;// qq登陆
	private String sinalogin;// 新浪登陆

	public String getQqlogin() {
		return qqlogin;
	}

	public void setQqlogin(String qqlogin) {
		this.qqlogin = qqlogin;
	}

	public String getSinalogin() {
		return sinalogin;
	}

	public void setSinalogin(String sinalogin) {
		this.sinalogin = sinalogin;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isEmailAuth() {
		return emailAuth;
	}

	public void setEmailAuth(boolean emailAuth) {
		this.emailAuth = emailAuth;
	}

	public boolean isMobileAuth() {
		return mobileAuth;
	}

	public void setMobileAuth(boolean mobileAuth) {
		this.mobileAuth = mobileAuth;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * @author zhangzhen 如果数据为空返回null
	 * 
	 *         指导使用方法 JSONObject j = JSONObject.fromObject(requestJson); String
	 *         jsonData = j.getString("Entity");
	 * */
	public static JTMember getByJsonString(String jsonEntity) {
		if (jsonEntity.equals("{}")) {
			return null; // 无数据判断
		}
		return JSON.parseObject(jsonEntity, JTMember.class);
	}

	/**
	 * @author zhangzhen 如果数据为空返回null
	 * 
	 *         指导使用方法 JSONObject j = JSONObject.fromObject(requestJson); Object
	 *         jsonData = j.get("Entity");
	 * */
	public static JTMember getByJsonObject(Object jsonEntity) {
		return getByJsonString(jsonEntity.toString());
	}

	/**
	 * @author zhangzhen 如果没有数据，返回空数组
	 * 
	 *         指导使用方法 JSONObject j = JSONObject.fromObject(requestJson); String
	 *         jsonData = j.getString("Entity");
	 * */
	public static List<JTMember> getListByJsonString(String object) {
		return JSON.parseArray(object, JTMember.class);
	}

	/**
	 * @author zhangzhen
	 * @CreateTime 2014-11-11 如果没有数据，返回空数组
	 * 
	 *             指导使用方法 JSONObject j = JSONObject.fromObject(requestJson);
	 *             Object jsonData = j.get("EntityList");
	 * */
	public static List<JTMember> getListByJsonObject(Object object) {
		return getListByJsonString(object.toString());
	}

	public Integer getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(Integer userStatus) {
		this.userStatus = userStatus;
	}

	public Integer getHomePageVisible() {
		return homePageVisible;
	}

	public void setHomePageVisible(Integer homePageVisible) {
		this.homePageVisible = homePageVisible;
	}

	public Integer getEvaluateVisible() {
		return evaluateVisible;
	}

	public void setEvaluateVisible(Integer evaluateVisible) {
		this.evaluateVisible = evaluateVisible;
	}

	public String getQqNikeName() {
		return qqNikeName;
	}

	public void setQqNikeName(String qqNikeName) {
		this.qqNikeName = qqNikeName;
	}

	public String getSinaWeiboNikeName() {
		return sinaWeiboNikeName;
	}

	public void setSinaWeiboNikeName(String sinaWeiboNikeName) {
		this.sinaWeiboNikeName = sinaWeiboNikeName;
	}

}
