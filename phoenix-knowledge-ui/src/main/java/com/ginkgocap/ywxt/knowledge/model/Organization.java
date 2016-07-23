/**
 * Copyright (c) 2011 银杏资本.
 * All Rights Reserved. 保留所有权利.
 */
package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSON;


public class Organization implements Serializable {
	
	private static final long serialVersionUID = 4326262039031458964L;

	private int id;// 机构id
	
	private String logo;//机构logo图片url1
	
	private String fullName;//机构全称
	
	private String shortName;//客户简称
	private String area; // 所属地区
	private String trade;// 行业
	private String tradeId;// 行业id
	private int guestType;//客户类型，0-一般客户，1-合作客户,2-核心客户
	
	private boolean isOnline;//是否线上机构
	private boolean isOffline;//是否线下机构
	
	private int friendState;//0-好友；1-非好友；2-等待对方验证；3-对方请求我为好友待我通过
	
	private int joinState;//0-已加入机构、1-无关系、2-已申请加入机构，待审批、3-机构已邀请加入，待我审批
	
	private String paperDocumentNumber; // 纸文档编号
	
	private String fromDes; //人脉来源描述， 如XX引荐、我主要请求、对方请求等
	
	
	private String brief; //机构简介
	
	private InvestKeyword  outInvestKeyword;//投资意向关键字
	
	private InvestKeyword inInvestKeyword;//融资意向关键字
	
	private List<JTFile> listJTFile; //附件文件对象 JTFile
	private List<RequirementMini>listRequirementMini;
	private List<RequirementMini> listMatchRequirementMini; // 匹配需求
	private List<ConnectionsMini> listMatchConnectionsMini; // 匹配关系
	private List<KnowledgeMini> listMatchKnowledgeMini;// 匹配知识
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public int getGuestType() {
		return guestType;
	}

	public void setGuestType(int guestType) {
		this.guestType = guestType;
	}

	public int getFriendState() {
		return friendState;
	}

	public void setFriendState(int friendState) {
		this.friendState = friendState;
	}

	public int getJoinState() {
		return joinState;
	}

	public void setJoinState(int joinState) {
		this.joinState = joinState;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public boolean getIsOffline() {
		return isOffline;
	}

	public void setIsOffline(boolean isOffline) {
		this.isOffline = isOffline;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getFromDes() {
		return fromDes;
	}

	public void setFromDes(String fromDes) {
		this.fromDes = fromDes;
	}

	public boolean getIsOnline() {
		return isOnline;
	}

	public void setIsOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}

	public InvestKeyword getOutInvestKeyword() {
		return outInvestKeyword;
	}

	public void setOutInvestKeyword(InvestKeyword outInvestKeyword) {
		this.outInvestKeyword = outInvestKeyword;
	}

	public InvestKeyword getInInvestKeyword() {
		return inInvestKeyword;
	}

	public void setInInvestKeyword(InvestKeyword inInvestKeyword) {
		this.inInvestKeyword = inInvestKeyword;
	}

	public List<JTFile> getListJTFile() {
		return listJTFile;
	}

	public void setListJTFile(List<JTFile> listJTFile) {
		this.listJTFile = listJTFile;
	}

	public List<RequirementMini> getListRequirementMini() {
		return listRequirementMini;
	}

	public void setListRequirementMini(List<RequirementMini> listRequirementMini) {
		this.listRequirementMini = listRequirementMini;
	}

	public String getBrief() {
		return brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	public List<RequirementMini> getListMatchRequirementMini() {
		return listMatchRequirementMini;
	}

	public void setListMatchRequirementMini(
			List<RequirementMini> listMatchRequirementMini) {
		this.listMatchRequirementMini = listMatchRequirementMini;
	}

	public List<ConnectionsMini> getListMatchConnectionsMini() {
		return listMatchConnectionsMini;
	}

	public void setListMatchConnectionsMini(
			List<ConnectionsMini> listMatchConnectionsMini) {
		this.listMatchConnectionsMini = listMatchConnectionsMini;
	}

	public List<KnowledgeMini> getListMatchKnowledgeMini() {
		return listMatchKnowledgeMini;
	}

	public void setListMatchKnowledgeMini(List<KnowledgeMini> listMatchKnowledgeMini) {
		this.listMatchKnowledgeMini = listMatchKnowledgeMini;
	}

	public String getPaperDocumentNumber() {
		return paperDocumentNumber;
	}

	public void setPaperDocumentNumber(String paperDocumentNumber) {
		this.paperDocumentNumber = paperDocumentNumber;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getTrade() {
		return trade;
	}

	public void setTrade(String trade) {
		this.trade = trade;
	}

	public String getTradeId() {
		return tradeId;
	}

	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}

	/**
	 * @author zhangzhen
	 * 如果数据为空返回null
	 * 
	 * 指导使用方法
	 * JSONObject j = JSONObject.fromObject(requestJson);
	 * String jsonData = j.getString("Entity");
	 * */
	public static Organization getByJsonString(String jsonEntity) {
		if(jsonEntity.equals("{}")) {
			return null; //无数据判断
		}
		return JSON.parseObject(jsonEntity, Organization.class);
	}
	
	/**
	 * @author zhangzhen
	 * 如果数据为空返回null
	 * 
	 * 指导使用方法
	 * JSONObject j = JSONObject.fromObject(requestJson);
	 * Object jsonData = j.get("Entity");
	 * */
	public static Organization getByJsonObject(Object jsonEntity) {
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
	public static List<Organization> getListByJsonString(String object) {
		return JSON.parseArray(object, Organization.class);
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
	public static List<Organization> getListByJsonObject(Object object) {
		return getListByJsonString(object.toString());
	}
}
