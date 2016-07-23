/**
 * Copyright (c) 2011 银杏资本.
 * All Rights Reserved. 保留所有权利.
 */
package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ginkgocap.ywxt.utils.CommonUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.alibaba.fastjson.JSON;

public class Connections implements Serializable {

	private static final long serialVersionUID = 6814797207320979841L;

	private long id;//分别为JTContact、机构id
	
	public final static int TYPE_PERSON = 1;
	public final static int TYPE_ORGANIZATION = 2;
	
	private int type;//1-个人、2-机构,参考 TYPE_***
	
	private String sourceFrom;//第二行显示的文字，根据不同的关系对象， 存放不同的内容，目前存放的是关系来源
	
	private JTContactMini jtContactMini;
	
	private OrganizationMini organizationMini;
	
	
	
	public static Connections createDemo(int i ,boolean f){
		Connections self = new Connections();
		self.setId(i);
		if(f)
		self.setType(TYPE_PERSON);
		if(!f)
			self.setType(TYPE_ORGANIZATION);
		
		self.setSourceFrom("金桐开发");
		//JTContactMini
		JTContactMini jm = JTContactMini.createDemo();
		//organizationMini
		OrganizationMini om = OrganizationMini.createDemo();
		
		//填充
		if(f)
		self.setJtContactMini(jm);
		if(!f)
		self.setOrganizationMini(om);
		return self;
	}
	
	//从asso里创建人脉对象专用
	public static Connections createSimpleFromAssoJson(JSONObject obj){
		Connections self = new Connections();
		self.id = CommonUtil.optLongFromJSONObject(obj, "id");
		self.type = obj.optInt("type");
		
		//2015-2-2 zhangzhen start
		
		if(self.type == TYPE_PERSON){
			//人脉
			self.type = Connections.TYPE_PERSON;
			self.jtContactMini = new JTContactMini();
			self.jtContactMini.setName(obj.getJSONObject("jtContactMini").optString("name"));
			self.jtContactMini.setId(obj.optString("id"));
		}
		
		//2015-2-2 zhangzhen end
		
		else if(self.type == EntityFactory.ASSO_PEOPLE_OFFLINE) {
			
			if(self.id == 0) {
				self.type = Connections.TYPE_ORGANIZATION;
				self.organizationMini = new OrganizationMini();
				self.organizationMini.setFullName("金桐脑");
				self.organizationMini.setShortName("金桐脑");
				self.organizationMini.setId("0");
			} else if (self.id == -1) {
				self.type = Connections.TYPE_ORGANIZATION;
				self.organizationMini = new OrganizationMini();
				self.organizationMini.setFullName("全平台");
				self.organizationMini.setShortName("全平台");
				self.organizationMini.setId("-1");
			} else {
				//人脉
//				self.type = Connections.TYPE_PERSON;
				self.jtContactMini = new JTContactMini();
				
				if(obj.has("organizationMini")) {
					
					JSONObject om = obj.optJSONObject("organizationMini");
					
					if(om != null)
					self.jtContactMini.setName(om.optString("fullName"));
				
				} else {
					self.jtContactMini.setName(obj.optString("name"));
				}
				self.jtContactMini.setId(obj.optString("id"));
				self.jtContactMini.setIsOffline(true);
				self.jtContactMini.setIsOnline(false);
			}
			
		}
		else if(self.type == EntityFactory.ASSO_PEOPLE_ONLINE){
			//用户
//			self.type = Connections.TYPE_PERSON;
			self.jtContactMini = new JTContactMini();
			self.jtContactMini.setName(obj.optString("name"));
			self.jtContactMini.setId(obj.optString("id"));
			self.jtContactMini.setIsOffline(false);
			self.jtContactMini.setIsOnline(true);
		
		} else if(self.type == EntityFactory.ASSO_ORGANIZATION_ONLINE){
			//组织、客户
			
			self.type = Connections.TYPE_ORGANIZATION;
			self.organizationMini = new OrganizationMini();
			self.organizationMini.setFullName(obj.optString("name"));
			self.organizationMini.setShortName(obj.optString("name"));
			self.organizationMini.setId(obj.optString("id"));
			self.organizationMini.setIsOnline(true);
			
		} else if(self.type == EntityFactory.ASSO_ORGANIZATION_OFFLINE){
			//组织、客户
			
			self.type = Connections.TYPE_ORGANIZATION;
			self.organizationMini = new OrganizationMini();
			self.organizationMini.setFullName(obj.optString("name"));
			self.organizationMini.setShortName(obj.optString("name"));
			self.organizationMini.setId(obj.optString("id"));
			self.organizationMini.setIsOffline(true);
			
		} else{
			
			self.type = Connections.TYPE_ORGANIZATION;
			self.organizationMini = new OrganizationMini();
			self.organizationMini.setFullName(obj.optString("name"));
			self.organizationMini.setShortName(obj.optString("name"));
			self.organizationMini.setId(obj.optString("id"));
			if(self.type == EntityFactory.ASSO_ORGANIZATION_ONLINE){
				//组织
				self.organizationMini.setIsOffline(false);
				self.organizationMini.setIsOnline(true);
			}else{
				//客户
				self.organizationMini.setIsOffline(true);
				self.organizationMini.setIsOnline(false);
			}
		}
		
		return self;
	}
	
	public static Connections createSimpleJson(JSONObject obj){
			Connections self = new Connections();
			self.id = CommonUtil.optLongFromJSONObject(obj, "id");
			self.type = Connections.TYPE_PERSON;
			self.jtContactMini = new JTContactMini();
			self.jtContactMini.setName(obj.optString("name"));
			self.jtContactMini.setmName(self.jtContactMini.getName());
			self.jtContactMini.setId(obj.optString("id"));
			self.jtContactMini.setIsOnline(true);
			return self;
	}
	
	
	//从asso里创建人脉对象专用
	public static List<Connections> createListSimpleFromAssoJson(JSONArray array){
		try{
			List<Connections> self = new ArrayList<Connections>();
			
			for(int i = 0; i < array.size(); i++){
				JSONObject obj = array.optJSONObject(i);
				if(obj != null){
					Connections c = createSimpleFromAssoJson(obj);
					if(c != null){
						self.add(c);
					}
				}
			}
			return self;
		}catch(Exception e){
			return null;
		}
	}
	
	//从普通人脉里创建人脉对象，主要用于权限管理器
	public static Connections createSimpleFromJson(JSONObject obj){
		Connections self = new Connections();
		self.id = CommonUtil.optLongFromJSONObject(obj, "id");
		self.type = obj.optInt("type");
		if(self.type == TYPE_PERSON){
			//个人、人脉
			self.jtContactMini = new JTContactMini();
			self.jtContactMini.setName(obj.optString("name"));
		}else{
			//组织、客户
			self.organizationMini = new OrganizationMini();
			self.organizationMini.setFullName(obj.optString("name"));
			self.organizationMini.setShortName(obj.optString("name"));
		}
		return self;
	}
	
	//从普通人脉里创建人脉对象，主要用于权限管理器
	public static List<Connections> createListSimpleFromJson(JSONArray array){
		try{
			List<Connections> self = new ArrayList<Connections>();
			for(int i = 0; i < array.size(); i++){
				JSONObject obj = array.optJSONObject(i);
				if(obj != null){
					Connections c = getByJsonString(obj.toString());
					if(c != null){
						self.add(c);
					}
				}
			}
			return self;
		}catch(Exception e){
			return null;
		}
	}
	
	public static List<Connections> createListSimpleFromJsonByPerson(JSONArray array){
		try{
			List<Connections> self = new ArrayList<Connections>();
			for(int i = 0; i < array.size(); i++){
				JSONObject obj = array.optJSONObject(i);
				if(obj != null){
					Connections c = createSimpleJson(obj);
					if(c != null){
						self.add(c);
					}
				}
			}
			return self;
		}catch(Exception e){
			return null;
		}
	}
	
	
	public JTContactMini getJtContactMini() {
		return jtContactMini;
	}

	public void setJtContactMini(JTContactMini jtContactMini) {
		this.jtContactMini = jtContactMini;
	}

	public OrganizationMini getOrganizationMini() {
		return organizationMini;
	}

	public void setOrganizationMini(OrganizationMini organizationMini) {
		this.organizationMini = organizationMini;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getSourceFrom() {
		return sourceFrom;
	}

	public void setSourceFrom(String sourceFrom) {
		this.sourceFrom = sourceFrom;
	}

	public long getId() {
		if(id > 0)
			return id;
		try{
		if(type == TYPE_PERSON){
			if(this.jtContactMini != null){
				return Long.parseLong(this.jtContactMini.getId());
			}
		}else {
			if(this.organizationMini != null){
				return Long.parseLong(this.organizationMini.getId());
			}
		}
		}catch(Exception e){
			
		}
		return 0;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	//获取关系名称
	public String getName(){
		if(type==0) {
			return "";
		}
		if(type == TYPE_PERSON){
			if(this.jtContactMini != null){
				return this.jtContactMini.getName();
			}
		}else {
			if(this.organizationMini != null){
				return this.organizationMini.getFullName();
			}
		}
		return "";
	}
	
	//获取关系拥有者id
	public Long getOwnerId(){
		if(type == 0) {
			return new Long(0);
		}
		if(type == TYPE_PERSON){
			if(this.jtContactMini != null){
				return this.jtContactMini.getOwnerid();
			}
		}else {
			if(this.organizationMini != null){
				return this.organizationMini.getOwnerid();
			}
		}
		return new Long(0);
	}
	
	//获取关系拥有者名称
	public String getOwnerName(){
		if(type == 0) {
			return "";
		}
		
		if(type == TYPE_PERSON){
			if(this.jtContactMini != null){
				return this.jtContactMini.getOwnername();
			}
		}else {
			if(this.organizationMini != null){
				return this.organizationMini.getOwnername();
			}
		}
		return "";
	}

	/**
	 * @author zhangzhen
	 * 如果数据为空返回null
	 * 
	 * 指导使用方法
	 * JSONObject j = JSONObject.fromObject(requestJson);
	 * String jsonData = j.getString("Entity");
	 * */
	public static Connections getByJsonString(String jsonEntity) {
		if(jsonEntity.equals("{}")) {
			return null; //无数据判断
		}
		return JSON.parseObject(jsonEntity, Connections.class);
	}
	
	/**
	 * @author zhangzhen
	 * 如果数据为空返回null
	 * 
	 * 指导使用方法
	 * JSONObject j = JSONObject.fromObject(requestJson);
	 * Object jsonData = j.get("Entity");
	 * */
	public static Connections getByJsonObject(Object jsonEntity) {
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
	public static List<Connections> getListByJsonString(String object) {
		return JSON.parseArray(object, Connections.class);
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
	public static List<Connections> getListByJsonObject(Object object) {
		return getListByJsonString(object.toString());
	}
	
	
	
}

