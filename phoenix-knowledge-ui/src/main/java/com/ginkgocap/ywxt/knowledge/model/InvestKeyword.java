package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSON;

/**
 * 投融资意向关键字对象
 * @author qinguochao
 *
 */
public class InvestKeyword  implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3444602790804845429L;

	private MoneyType moneyType; // CNY,USD等，配置登录里返回的数组里选择
	
	private String moneyRange; // "资金范围，配置登录里返回的数组里选择"
	
	private List<InvestType> listInvestType; // 投融资类型
	
	private List<Trade> listTrade; //行业类型
	
	private Area area;
	
	
	public String getMoneyRange() {
		return moneyRange;
	}
	public void setMoneyRange(String moneyRange) {
		this.moneyRange = moneyRange;
	}
	public List<InvestType> getListInvestType() {
		return listInvestType;
	}
	public void setListInvestType(List<InvestType> listInvestType) {
		this.listInvestType = listInvestType;
	}
	public List<Trade> getListTrade() {
		return listTrade;
	}
	public void setListTrade(List<Trade> listTrade) {
		this.listTrade = listTrade;
	}
	public Area getArea() {
		return area;
	}
	public void setArea(Area area) {
		this.area = area;
	}
	public MoneyType getMoneyType() {
		return moneyType;
	}
	public void setMoneyType(MoneyType moneyType) {
		this.moneyType = moneyType;
	}

	/***getXXX() and setXXX()***/
	/**
	 * @author zhangzhen
	 * 如果数据为空返回null
	 * 
	 * 指导使用方法
	 * JSONObject j = JSONObject.fromObject(requestJson);
	 * String jsonData = j.getString("Entity");
	 * */
	public static InvestKeyword getByJsonString(String jsonEntity) {
		if(jsonEntity.equals("{}")) {
			return null; //无数据判断
		}
		return JSON.parseObject(jsonEntity, InvestKeyword.class);
	}
	
	/**
	 * @author zhangzhen
	 * 如果数据为空返回null
	 * 
	 * 指导使用方法
	 * JSONObject j = JSONObject.fromObject(requestJson);
	 * Object jsonData = j.get("Entity");
	 * */
	public static InvestKeyword getByJsonObject(Object jsonEntity) {
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
	public static List<InvestKeyword> getListByJsonString(String object) {
		return JSON.parseArray(object, InvestKeyword.class);
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
	public static List<InvestKeyword> getListByJsonObject(Object object) {
		return getListByJsonString(object.toString());
	}
	
}
