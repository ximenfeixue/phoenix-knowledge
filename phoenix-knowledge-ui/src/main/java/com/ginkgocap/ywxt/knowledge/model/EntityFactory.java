package com.ginkgocap.ywxt.knowledge.model;

import com.ginkgocap.ywxt.knowledge.utils.Utils;
import com.ginkgocap.ywxt.user.model.User;
import com.ginkgocap.ywxt.user.service.UserService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
* <p>Title: EntityFactory.java<／p>
* <p>Description: 实体类创建工厂，可以根据json解析出各种实体类<／p>

* @author xuxinjian
* @date 2014-11-11
* @version 1.0
 */

public class EntityFactory {
	
	
	@Autowired//用户详情接口
	static UserService userService;
	
	private final static int JOINT_RESOURCES_TYPE_ALL_PLATFORM = 3;
	
	private final static int JOINT_RESOURCES_TYPE_ALL_PEOPLE = 2;
	
	//关联相关常量
	//1 需求 2 人脉 3 全平台普通用户 4 组织(全平台组织用户) 5 客户 6 知识
	public final static int ASSO_REQUIREMENT = 1;
	public final static int ASSO_PEOPLE_OFFLINE = 2;
	public final static int ASSO_PEOPLE_ONLINE = 3;
	public final static int ASSO_ORGANIZATION_ONLINE = 4;
	public final static int ASSO_ORGANIZATION_OFFLINE = 5;
	public final static int ASSO_KNOWLEDGE = 6;
	
	
	/**
	 * @description 创建一个KnowledgeStatics实体类对象
	 * @return
	 *
	public static KnowledgeStatics createKnowledgeStaticsFromJson(JSONObject obj){
		try{
			KnowledgeStatics self = new KnowledgeStatics();
			self.setKnowledgeId(CommonUtil.optLongFromJSONObject(obj, "knowledgeId"));
					self.setCommentcount(CommonUtil.optLongFromJSONObject(obj, "commentcount"));
							self.setSharecount(CommonUtil.optLongFromJSONObject(obj, "sharecount"));
									self.setCollectioncount(CommonUtil.optLongFromJSONObject(obj, "collectioncount"));
			self.setClickcount(CommonUtil.optLongFromJSONObject(obj, "clickcount"));
			//self.setTitle(obj.optString("title"));
			self.setType((short)obj.optInt("type"));
			self.setSource((short)obj.optInt("source"));
			return self;
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	 * @description 创建一个Column实体类对象
	 * @return
	 *
	public static Column createColumnFromJson(JSONObject obj){
		try{
			Column self = new Column();
			self.setId(CommonUtil.optLongFromJSONObject(obj, "id"));
					self.setColumnname(obj.optString("columnname"));
			self.setUserId(CommonUtil.optLongFromJSONObject(obj, "userId"));
			self.setParentId(CommonUtil.optLongFromJSONObject(obj, "parentId"));
			
			Date dateTemp = DateUtil.convertStringToDateTimeForChina(obj.optString("createtime"));
			self.setCreatetime(dateTemp);
			
			self.setPathName(obj.optString("pathName"));
			self.setColumnLevelPath(obj.optString("columnLevelPath"));
			self.setDelStatus((byte) obj.optInt("delStatus"));
			
			dateTemp = DateUtil.convertStringToDateTimeForChina(obj.optString("updateTime"));
			self.setUpdateTime(dateTemp);
			
			self.setSubscribeCount(CommonUtil.optLongFromJSONObject(obj, "subscribeCount"));
					self.setType((short) obj.optInt("type"));
			return self;
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	 * @description 创建一个UserCategory list 实体类对象
	 * @return
	 *
	public static List<UserCategory> createListUserCategoryFromJson(JSONArray array){
		try{
			List<UserCategory> self = new ArrayList<UserCategory>();
			for(int i = 0; i < array.size(); i++){
				JSONObject obj = array.optJSONObject(i);
				if(obj != null){
					UserCategory userCategory = EntityFactory.createUserCategoryFromJson(obj);
					if(userCategory != null){
						self.add(userCategory);
					}
				}
			}
			return self;
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	 * @description 创建一个UserCategory 实体类对象
	 * @return
	 *
	public static UserCategory createUserCategoryFromJson(JSONObject obj){
		try{
			UserCategory self = new UserCategory();
			self.setId(CommonUtil.optLongFromJSONObject(obj, "id"));
			self.setUserId(CommonUtil.optLongFromJSONObject(obj, "userId"));
			self.setCategoryname(obj.optString("categoryname"));
			self.setSortid(obj.optString("sortid"));
			
			Date dateTemp = DateUtil.convertStringToDateTimeForChina(obj.optString("createtime"));
			self.setCreatetime(dateTemp);
			
			self.setPathName(obj.optString("pathName"));
			self.setParentId(CommonUtil.optLongFromJSONObject(obj, "parentId"));
					self.setCategoryType((short) obj.optInt("categoryType"));
			return self;
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	 * @description 权限管理器对象，创建符合平台层需求的字符串
	 * @return {"dule":false,"xiaoles":[17,14449],"zhongles":[0,10357,14358],"dales":[14360,-1]})
	 *
	public static String genSelectIds(List<Connections> listHightPermission, 
			List<Connections> listMiddlePermission,
			List<Connections> listLowPermission){
		
		JSONObject obj = new JSONObject();
		try{
			boolean isDule = true;
			if(listHightPermission != null && listHightPermission.size() > 0){
				isDule = false;
				JSONArray array = new JSONArray();
				for(int i = 0; i < listHightPermission.size(); i++){
					array.add(getPermissionMap(listHightPermission.get(i)));
				}
				obj.put("dales", array);
			} else {
				obj.put("dales", new JSONArray());
			}

			if(listMiddlePermission != null && listMiddlePermission.size() > 0){
				isDule = false;
				JSONArray array = new JSONArray();
				for(int i = 0; i < listMiddlePermission.size(); i++){
					array.add(getPermissionMap(listMiddlePermission.get(i)));
				}
				obj.put("zhongles", array);
			} else {
				obj.put("zhongles", new JSONArray());
			}
			
			if(listLowPermission != null && listLowPermission.size() > 0){
				isDule = false;
				JSONArray array = new JSONArray();
				for(int i = 0; i < listLowPermission.size(); i++){
					array.add(getPermissionMap(listLowPermission.get(i)));
				}
				obj.put("xiaoles", array);
			} else {
				obj.put("xiaoles", new JSONArray());
			}
			
			obj.put("dule", isDule);
			
			
		}catch(Exception e){
			
			System.out.println(e);
			System.out.println(e.getMessage());
			System.out.println(e.getLocalizedMessage());
			
			obj.put("dule", true);
		}
		return obj.toString();
	}
	
	/**
	 * @description 生成关联组件对应的json
	 * @return { 
				r:[{tag:xx, conn:[{需求}]}], // 关联的需求分为几个类目，每个类目包含多个需求 
				p:[{tag:xx, conn:[{人}]}], // 关联的人分为几个类目，每个类目包含多个人 
				o:[{tag:xx, conn:[{组织}]}], // 关联的组织分为几个类目，每个类目包含多个组织 
				k:[{tag:xx, conn:[{知识}]}], // 关联的知识分为几个类目，每个类目包含多个知识 
				} 
	 */
	public static String genAsso(List<ConnectionsNode> listRelatedConnectionsNode, 
			List<ConnectionsNode> listRelatedOrganizationNode,
			List<KnowledgeNode> listRelatedKnowledgeNode,
			List<AffairNode> listRelatedAffairNode){
		
		JSONObject obj = new JSONObject();
		try{
			//相关的人
			if(listRelatedConnectionsNode != null && listRelatedConnectionsNode.size() > 0){
				JSONArray array = new JSONArray();//关联的人组列表
				for(int i = 0; i < listRelatedConnectionsNode.size(); i++){
					ConnectionsNode node = listRelatedConnectionsNode.get(i);
					JSONObject objNode = new JSONObject();
					objNode.put("tag", node.getMemo());
					
					List<Connections> cons = node.getListConnections();
					if(cons != null && cons.size() > 0){//一组关联内部的人列表
						JSONArray arrayCon = new JSONArray();//关联的人列表
						for(int j = 0; j < cons.size(); j++){
							//填“人”的属性？？
							Connections con = cons.get(j);
							JSONObject objCon = new JSONObject();
							objCon.put("id", con.getId());
							
							
							objCon.put("name", con.getName());
							objCon.put("ownername", con.getOwnerName());
							Long ownerID = con.getOwnerId();
							//if(ownerID = null)
								objCon.put("ownerid", "" + ownerID);
								objCon.put("userId", ownerID+"");
							if(con.getType() == Connections.TYPE_PERSON){
								if(con.getJtContactMini() != null){
									objCon.put("company", con.getJtContactMini().getCompany());
									objCon.put("career", con.getJtContactMini().getCareer());
									if(con.getJtContactMini().getIsOnline()){
										//在线用户
										objCon.put("type", ASSO_PEOPLE_ONLINE);
									}else{
										objCon.put("type", ASSO_PEOPLE_OFFLINE);
									}
								}
							}
							arrayCon.add(objCon);
						}
						objNode.put("conn", arrayCon);
					}
					
					array.add(objNode);
				}
				obj.put("p", array);
			}
			
			//相关的组织
			if(listRelatedOrganizationNode != null && listRelatedOrganizationNode.size() > 0){
				JSONArray array = new JSONArray();//关联的组织组列表
				for(int i = 0; i < listRelatedOrganizationNode.size(); i++){
					ConnectionsNode node = listRelatedOrganizationNode.get(i);
					JSONObject objNode = new JSONObject();
					objNode.put("tag", node.getMemo());
					
					List<Connections> cons = node.getListConnections();
					if(cons != null && cons.size() > 0){//一组关联内部的人列表
						JSONArray arrayCon = new JSONArray();//关联的组织列表
						for(int j = 0; j < cons.size(); j++){
							//填“组织”的属性？？
							Connections con = cons.get(j);
							JSONObject objCon = new JSONObject();
							objCon.put("id", con.getId());
							//objCon.put("type", con.getType());
							objCon.put("name", con.getName());
							objCon.put("ownername", con.getOwnerName());
							objCon.put("ownerid", "" + con.getOwnerId());
							if(con.getType() == Connections.TYPE_ORGANIZATION){
								if(con.getOrganizationMini() != null){
									objCon.put("address", con.getOrganizationMini().getAddress());//此处带修改
									objCon.put("hy", con.getOrganizationMini().getTrade());
									
									if(con.getOrganizationMini().getIsOnline()){
										//在线用户
										objCon.put("type", ASSO_ORGANIZATION_ONLINE);
									}else{
										objCon.put("type", ASSO_ORGANIZATION_OFFLINE);
									}
								}
							}
							arrayCon.add(objCon);
						}
						objNode.put("conn", arrayCon);
					}
					
					array.add(objNode);
				}
				obj.put("o", array);
			}

			//相关的知识
			if(listRelatedKnowledgeNode != null && listRelatedKnowledgeNode.size() > 0){
				JSONArray array = new JSONArray();//关联的人组列表
				for(int i = 0; i < listRelatedKnowledgeNode.size(); i++){
					KnowledgeNode node = listRelatedKnowledgeNode.get(i);
					JSONObject objNode = new JSONObject();
					objNode.put("tag", node.getMemo());
					
					List<KnowledgeMini2> km = node.getListKnowledgeMini2();
					if(km != null && km.size() > 0){//一组关联知识内部的人列表
						JSONArray arrayCon = new JSONArray();//关联的知识列表
						for(int j = 0; j < km.size(); j++){
							//填“知识”的属性？？
							KnowledgeMini2 con = km.get(j);
							JSONObject objCon = new JSONObject();
							objCon.put("id", con.getId());
							objCon.put("type", "" + ASSO_KNOWLEDGE);
							objCon.put("name", con.getTitle());
							objCon.put("ownername", con.getOwnerName());
							objCon.put("ownerid", "" + con.getOwnerId());
							objCon.put("columnpath", "" + con.getColumnpath());
							objCon.put("columntype", "" + con.getType());
							objCon.put("title", con.getTitle());
							arrayCon.add(objCon);
						}
						objNode.put("conn", arrayCon);
					}
					
					array.add(objNode);
				}
				obj.put("k", array);
			}
			
			//相关的需求
			if(listRelatedAffairNode != null && listRelatedAffairNode.size() > 0){
				JSONArray array = new JSONArray();//关联的人组列表
				for(int i = 0; i < listRelatedAffairNode.size(); i++){
					AffairNode node = listRelatedAffairNode.get(i);
					JSONObject objNode = new JSONObject();
					objNode.put("tag", node.getMemo());
					
					List<AffairMini> am = node.getListAffairMini();
					if(am != null && am.size() > 0){//一组关联知识内部的人列表
						JSONArray arrayCon = new JSONArray();//关联的知识列表
						for(int j = 0; j < am.size(); j++){
							//填“需求”的属性？？
							AffairMini con = am.get(j);
							JSONObject objCon = new JSONObject();
							objCon.put("id", con.getId());
							objCon.put("type", ""+ASSO_REQUIREMENT);
							objCon.put("title", con.getTitle());
							objCon.put("ownername", con.getOwnerName());
							objCon.put("ownerid", "" + con.getOwnerId());
							objCon.put("requirementType", "" + con.getReserve());
							arrayCon.add(objCon);
						}
						objNode.put("conn", arrayCon);
					}
					
					array.add(objNode);
				}
				obj.put("r", array);
			}

			
		}catch(Exception e){
			e.printStackTrace();
		}
		return obj.toString();
	}
	
	/** 剥离 json 并组装成 ConnectionsNode  2014-11-4
	public static ConnectionsNode getConnectionsNodeByPeopleJson(JSONObject obj) {
		if(obj.toString().equals("null")) {return null;}
		ConnectionsNode cn = new ConnectionsNode();
		List<Connections> cs = new ArrayList<Connections>();
		JSONArray tempList = obj.getJSONArray("details");
		@SuppressWarnings("unchecked") /**将json形式的数据map集合转换为List<Map>
		List<Map<String,Object>> list = (List<Map<String, Object>>) JSONArray.toCollection(tempList, Map.class);
		// 临时长度记录
		int tempSize = list.size();
		Connections c = null;
		for (int i = 0; i < tempSize; i++) {
			Map<String,Object> m = list.get(i);
			Object tempId = m.get("id");
			Object tempType = m.get("type");
			Object tempName = m.get("name");
			// 脏数据排除机制
			if(isNullOrEmpty(tempId) || isNullOrEmpty(tempType) || isNullOrEmpty(tempName))
				break;
			
			String id = tempId.toString();
			// 2 人脉 3 全平台普通用户
			int type = Integer.parseInt(tempType.toString());
			
			c = new Connections();
			c.setId(Long.parseLong(id));
			c.setType(Connections.TYPE_PERSON);
			JTContactMini mini = new  JTContactMini();
			mini.setName(tempName.toString());
			mini.setId(id);
			List<MobilePhone> mps = new ArrayList<MobilePhone>();
			MobilePhone mp = new MobilePhone();
			mp.setName("电话");
			//获取用户详情
			if (type == JOINT_RESOURCES_TYPE_ALL_PLATFORM) {// 全平台普通用户
				User detailUser = userService.selectByPrimaryKey(c.getId());
				if(null != detailUser) {
				mini.setIsOffline(true);
				mini.setImage(detailUser.getPicPath());
				mp.setMobile(detailUser.getMobile());
				}
			}
			if(type == JOINT_RESOURCES_TYPE_ALL_PEOPLE) {//人脉
//				PeopleTemp pt = peopleMongoService.selectByPrimary(id);
//				if(null != pt) {
//				mini.setImage(pt.getPortrait());
//				mini.setIsOffline(true);
//				List<PeopleContactComm> pc = pt.getContactMobileList();
//				if(null != pc && pc.size() > 0) {
//					mp.setMobile(pc.get(0).getContent());
//					}
//				}
			}
			mps.add(mp);
			mini.setListMobilePhone(mps);
			c.setJtContactMini(mini);
			cs.add(c);
		}
		cn.setMemo(obj.getString("clmname"));
		cn.setListConnections(cs);
		return cn;
	}
	
	// 剥离json 并组装成AffairNode对象 2014-11-5
	public static AffairNode getAffairNodeByjson(JSONObject obj) {
		if(obj.toString().equals("null")) {return null;}
		AffairNode an = new AffairNode();
		List<AffairMini> ams = new ArrayList<AffairMini>();
		JSONArray tempList = obj.getJSONArray("details");
		@SuppressWarnings("unchecked") // 将json形式的数据map集合转换为List<Map>
		List<Map<String,Object>> list = (List<Map<String, Object>>) JSONArray.toCollection(tempList, Map.class);
		//临时长度记录
		int tempSize = list.size();
		AffairMini am = null;
		for (int i = 0; i < tempSize; i++) {
			Map<String,Object> m = list.get(i);
			
			Object tempId = m.get("id");
			Object tempTitle= m.get("title");
			Object tempOwnerid = m.get("ownerid");
			Object tempOwnername = m.get("ownername");
			// 脏数据排除机制
			if(isNullOrEmpty(tempId) || isNullOrEmpty(tempTitle) || isNullOrEmpty(tempOwnerid) || isNullOrEmpty(tempOwnername))
				break;
			
			am = new AffairMini();
			am.setType(1);// 目前查询的全是需求
			am.setTitle(m.get("title").toString());
			String id = m.get("id").toString();
			am.setId(Integer.parseInt(id));
			String ownerid = m.get("ownerid").toString();
			String ownername = m.get("ownername").toString();
			User user = userService.selectByPrimaryKey(Long.parseLong(id));
			if( user != null) {
			Connections connections = new Connections();
			connections.setId(Long.parseLong(ownerid));
			int utype = user.getType();
			int temp = Connections.TYPE_PERSON;
			if(utype != 1)
				temp = Connections.TYPE_ORGANIZATION;
			connections.setType(temp);
			if(utype == 1) {
				JTContactMini mini = new  JTContactMini();
				mini.setIsOnline(true);
				mini.setName(ownername);
				connections.setJtContactMini(mini);
			} else {
				OrganizationMini om = new OrganizationMini();
				om.setFullName(ownername);
				om.setIsOffline(true);
				connections.setOrganizationMini(om);
			}
			am.setConnections(connections);
			}
			ams.add(am);
		}
		an.setMemo(obj.getString("clmname"));
		an.setListAffairMini(ams);
		return an;
	}*/
	
	/** 剥离 json 并组装成 ConnectionsNode  2014-11-4 */
	public static ConnectionsNode getConnectionsNodeByOrganizationJson(JSONObject obj) {
		if(obj.toString().equals("null")) {return null;}
		ConnectionsNode cn = new ConnectionsNode();
		List<Connections> cs = new ArrayList<Connections>();
		JSONArray tempList = obj.getJSONArray("details");
		@SuppressWarnings("unchecked") /**将json形式的数据map集合转换为List<Map>*/
		List<Map<String,Object>> list = (List<Map<String, Object>>) JSONArray.toCollection(tempList, Map.class);
		/** 临时长度记录 */
		int tempSize = list.size();
		Connections c = null;
		for (int i = 0; i < tempSize; i++) {
			Map<String,Object> m = list.get(i);
			
			Object tempId = m.get("id");
			Object tempHy= m.get("hy");
			Object tempName = m.get("name");
			Object tempType = m.get("type");
			/** 脏数据排除机制 */
			if(isNullOrEmpty(tempId) || isNullOrEmpty(tempHy) || isNullOrEmpty(tempName))
				break;
			
			c = new Connections();
			String id = m.get("id").toString();
			c.setId(Long.parseLong(id));
			c.setType(Connections.TYPE_ORGANIZATION);
			OrganizationMini om = new OrganizationMini();
			om.setTrade(m.get("hy").toString());
			om.setFullName(m.get("name").toString());
			User organizationUser = userService.selectByPrimaryKey(c.getId());
			if(null != organizationUser) {
			List<MobilePhone> mps = new ArrayList<MobilePhone>();
			MobilePhone mp = new MobilePhone();
			
			if(Integer.parseInt(tempType.toString())==5) {
				om.setIsOffline(true);
			} else {
				om.setIsOnline(true);
			}
			
			mp.setName("电话");
			mp.setMobile(organizationUser.getMobile());
			mps.add(mp);
			om.setListMobilePhone(mps);
			om.setLogo(organizationUser.getPicPath());
			}
			cs.add(c);
		}
		cn.setMemo(obj.getString("clmname"));
		cn.setListConnections(cs);
		return cn;
	}
	
	// 剥离 json 并组装成 knowledgeNode  2014-11-4
	public static KnowledgeNode getknowledgeNodeByjson(JSONObject obj) {
		if(obj.toString().equals("null")) {return null;}
		KnowledgeNode kn = new KnowledgeNode();
		List<KnowledgeMini2> km2s = new ArrayList<KnowledgeMini2>();
		JSONArray tempList = obj.getJSONArray("details");
		@SuppressWarnings("unchecked") /**将json形式的数据map集合转换为List<Map>*/
		List<Map<String,Object>> list = (List<Map<String, Object>>) JSONArray.toCollection(tempList, Map.class);
		/** 临时长度记录 */
		int tempSize = list.size();
		KnowledgeMini2 km2 = null;
		for (int i = 0; i < tempSize; i++) {
			Map<String,Object> m = list.get(i);
			
			Object tempId = m.get("id");
			Object tempTitle= m.get("title");
			Object tempOwnerid = m.get("ownerid");
			Object tempOwnername = m.get("ownername");
			/** 脏数据排除机制 */
			if(isNullOrEmpty(tempId) || isNullOrEmpty(tempTitle) || isNullOrEmpty(tempOwnerid) || isNullOrEmpty(tempOwnername))
				break;
			
			km2 = new KnowledgeMini2();
			km2.setTitle(m.get("title").toString());
			String id = m.get("id").toString();
			km2.setId(Long.parseLong(id));
			Connections connections = new Connections();
			String ownerid = m.get("ownerid").toString();
			connections.setId(Long.parseLong(ownerid));
			User organizationUser = userService.selectByPrimaryKey(Long.parseLong(tempOwnerid.toString()));
			
			if(null != organizationUser) {
			/** 判断用户属性 */
			int usertype = organizationUser.getType();
			String ownername = m.get("ownername").toString();
			int temp = Connections.TYPE_PERSON;
			if(usertype != 1)
				temp = Connections.TYPE_ORGANIZATION;
			connections.setType(temp);
			if(usertype == Connections.TYPE_ORGANIZATION) {
				JTContactMini mini = new  JTContactMini();
				mini.setIsOnline(true);
				mini.setName(ownername);
				connections.setJtContactMini(mini);
			} else {
				OrganizationMini om = new OrganizationMini();
				om.setFullName(ownername);
				om.setIsOnline(true);
				connections.setOrganizationMini(om);
				}
			}
			//km2.setConnections(connections);
			km2s.add(km2);
		}
		kn.setMemo(obj.getString("clmname"));
		kn.setListKnowledgeMini2(km2s);
		return kn;
	}
	
	 /**
     * 判断对象是否为null或空
     * return IOException
     */
	public static boolean isNullOrEmpty(Object obj){
        return Utils.isNullOrEmpty(obj);
    }
	
	//制作为新权限格式修订 2015-2-2 by zhangzhen
	public static Map<String, Object> getPermissionMap(Connections conn) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		long id = conn.getId();
		
		map.put("id", id);
		String name = "";
		if (id == 0) {
			name = "金桐脑";
		} else if (id == -1) {
			name = "全平台";
		}else {
			
			JTContactMini jm = conn.getJtContactMini();
			
			if (jm != null)
				name = jm.getName();
			
			if(name == null || "".equals(name)) {
				
				OrganizationMini om = conn.getOrganizationMini();
				
				if(om != null)
					name = om.getFullName();
			}
			
		}
		map.put("name", name);
		return map;
	}
}
