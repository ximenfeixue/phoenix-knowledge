package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.entity.ConnectionInfo;
import com.ginkgocap.ywxt.knowledge.entity.ConnectionInfoExample;
import com.ginkgocap.ywxt.knowledge.entity.ConnectionInfoExample.Criteria;
import com.ginkgocap.ywxt.knowledge.mapper.ConnectionInfoMapper;
import com.ginkgocap.ywxt.knowledge.mapper.ConnectionInfoValueMapper;
import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeConnectInfoService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeHomeService;
import com.ginkgocap.ywxt.knowledge.util.Constants;
import com.ginkgocap.ywxt.people.domain.modelnew.PeopleName;
import com.ginkgocap.ywxt.people.domain.modelnew.PeopleSimple;
import com.ginkgocap.ywxt.people.domain.modelnew.PeopleTemp;
import com.ginkgocap.ywxt.people.service.PeopleMongoService;
import com.ginkgocap.ywxt.personalcustomer.service.PersonalCustomerService;
import com.ginkgocap.ywxt.requirement.model.Requirement;
import com.ginkgocap.ywxt.requirement.service.RequirementService;

@Service("knowledgeConnectInfoService")
public class KnowledgeConnectInfoServiceImpl implements
		KnowledgeConnectInfoService {

	@Resource
	private ConnectionInfoMapper connectionInfoMapper;

	@Resource
	private PeopleMongoService peopleMongoService;

	@Resource
	private KnowledgeHomeService knowledgeHomeService;

	@Resource
	private PersonalCustomerService personalCustomerService;

	@Resource
	private ConnectionInfoValueMapper connectionInfoValueMapper;

	@Resource
	private RequirementService requirementService;

	@Override
	public Map<String, Object> insertKnowledgeConnectInfo(String knowledgeasso,
			Long knowledgeId, Long userid) {

		// 删除该知识的关联信息

		deleteKnowledgeConnectInfo(knowledgeId);

		Map<String, Object> result = new HashMap<String, Object>();
		try{
		
			JSONObject j = JSONObject.fromObject(knowledgeasso);
			String jsonstr = "";
			String tag = "";
			String conn = "";
			int count = 0;

			List<ConnectionInfo> conneclist = null;
			String assotype[] = { Constants.KnowledgeConnectType.event.c(),
					Constants.KnowledgeConnectType.people.c(),
					Constants.KnowledgeConnectType.organization.c(),
					Constants.KnowledgeConnectType.knowledge.c() };
			for (int i = 0; i < assotype.length; i++) {

				if(StringUtils.equals(j.get(assotype[i]).toString(), "[]")){
					 continue;
				}
				jsonstr = j.get(assotype[i]).toString();
				tag = getTagAll(jsonstr.toString());
				conn = getConnAll(jsonstr.toString());
				if (!StringUtils.equals(conn, "[]")&& !StringUtils.equals(conn, "-9")) {
					JSONArray json = JSONArray.fromObject(jsonstr); // 首先把字符串转成
					for (int t = 0; t < json.size(); t++) {
						JSONObject job = json.getJSONObject(t); // 遍历 jsonarray
						tag = getTag(job.toString());
						conn = getConn(job.toString());
						count = insertJsonAraay(conn, tag, knowledgeId);
						if (count < 0) {
							result.put(Constants.errormessage,Constants.ErrorMessage.addasso.c());
							return result;
						}
					}
				} else if ( StringUtils.equals(conn, "-9")){
					// 选人脉全部
					ConnectionInfo knowledgeconnect = null;
					if (StringUtils.equals(assotype[i], "p")) {
						conneclist = new ArrayList<ConnectionInfo>();
						List<PeopleSimple> peoples = peopleMongoService.getPeopleSimplelist(0, null, null, 1, 100000,userid, 1);
						if (peoples != null && peoples.size() > 0) {
							for (PeopleSimple peopleSimple : peoples) {
								knowledgeconnect = new ConnectionInfo();
								knowledgeconnect.setConntype(Constants.KnowledgeConnectType.people.v());
								knowledgeconnect.setKnowledgeid(knowledgeId);
								knowledgeconnect.setTag(tag);
								knowledgeconnect.setConnid(Long.parseLong(peopleSimple.getId()));
								knowledgeconnect.setUrl("/people/"	+ peopleSimple.getId()+ "/");
								knowledgeconnect.setOwnerid(userid);
								PeopleTemp peolpletemp = peopleMongoService.selectByPrimary(peopleSimple.getId());
								if (peolpletemp != null) {
									knowledgeconnect.setPicpath(peolpletemp.getPortrait());
									List<PeopleName> list = peolpletemp.getPeopleNameList();
									if (list != null && list.size() > 0) {
										for (PeopleName peopleName : list) {
											String pername = peopleName.getTypeTag().getCode() == null ? "-1" : peopleName.getTypeTag().getCode();
											if (Integer.parseInt(pername) == 1) {
												knowledgeconnect.setConnname(peopleName.getName());
											}
										}
									}
								}
								// connectionInfoMapper.insertSelective(knowledgeconnect);
								conneclist.add(knowledgeconnect);
							}
							connectionInfoValueMapper.insertConnectionInfo(conneclist);

						}
					}
					if (StringUtils.equals(assotype[i], "r")) {
						conneclist = new ArrayList<ConnectionInfo>();
						Map<String, Object> map = requirementService.selectMy(userid, 0, 100000, -1, "");
						List<Requirement> results = (List<Requirement>) map.get("results");
						if (results != null && results.size() > 0) {
							for (Requirement requirement : results) {
								knowledgeconnect = new ConnectionInfo();
								knowledgeconnect.setConntype(Constants.KnowledgeConnectType.event.v());
								knowledgeconnect.setKnowledgeid(knowledgeId);
								knowledgeconnect.setConnid(requirement.getId());
								knowledgeconnect.setTag(tag);
								knowledgeconnect.setConnname(requirement.getTitle());
								knowledgeconnect.setOwnerid(requirement.getFbrId());
								knowledgeconnect.setOwner(requirement.getFbr());
								knowledgeconnect.setRequirementtype(requirement.getRequirementType() + "");
								knowledgeconnect.setUrl("/requirement/detail/"+ requirement.getRequirementType() + ""+ "/" + requirement.getId()+ "/");
								conneclist.add(knowledgeconnect);
							}
							connectionInfoValueMapper.insertConnectionInfo(conneclist);
						}

					}
					if (StringUtils.equals(assotype[i], "o")) {

						conneclist = new ArrayList<ConnectionInfo>();
						List<Map<String, Object>> lt = personalCustomerService.list(userid, "", "", "", "", 0, 1, 100000);

						if (lt != null && lt.size() > 0) {

							for (int k = 0; k < lt.size(); k++) {
								Map<String, Object> map = lt.get(k);
								if (map != null) {
									knowledgeconnect = new ConnectionInfo();
									knowledgeconnect.setConntype(Constants.KnowledgeConnectType.organization.v());
									knowledgeconnect.setKnowledgeid(knowledgeId);
									knowledgeconnect.setConnid(Long.parseLong(map.get("id") + ""));
									knowledgeconnect.setConnname(map.get("name")+ "");
									knowledgeconnect.setOwnerid(userid);
									knowledgeconnect.setTag(tag);
									PeopleTemp peolpletemp = peopleMongoService.selectByPrimary(map.get("id") + "");
									if (peolpletemp != null) {
										knowledgeconnect.setPicpath(peolpletemp.getPortrait());
									}
									conneclist.add(knowledgeconnect);
									// connectionInfoMapper
									// .insertSelective(knowledgeconnect);
								}
							}
							connectionInfoValueMapper.insertConnectionInfo(conneclist);
						}

					}
					if (StringUtils.equals(assotype[i], "k")) {

						conneclist = new ArrayList<ConnectionInfo>();
						Map<String, Object> map = knowledgeHomeService.selectAllKnowledgeCategoryByParam("", "", 0, "",userid, "", 1, 100000);

						List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("list");

						if (list != null && list.size() > 0) {
							for (int m = 0; m < list.size(); m++) {
								Map<String, Object> mapk = list.get(m);
								if (mapk != null) {
									knowledgeconnect = new ConnectionInfo();
									knowledgeconnect.setConntype(Constants.KnowledgeConnectType.knowledge.v());
									knowledgeconnect.setKnowledgeid(knowledgeId);
									knowledgeconnect.setConnid(Long.parseLong(mapk.get("knowledge_id") + ""));
									knowledgeconnect.setConnname(mapk.get("title")+ "");
									knowledgeconnect.setOwnerid(userid);
									knowledgeconnect.setTag(tag);
									knowledgeconnect.setColumnpath(mapk.get("path")+ "");
									knowledgeconnect.setColumntype(Integer.parseInt(mapk.get("column_type") + ""));
									knowledgeconnect.setUrl("/knowledge/reader?type="+ mapk.get("column_type") + ""+ "&kid="+ mapk.get("knowledge_id") + "");
									conneclist.add(knowledgeconnect);
								}
							}
							connectionInfoValueMapper.insertConnectionInfo(conneclist);
						}

					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		result.put(Constants.status, Constants.ResultType.success.v());
		return result;
	}

	public int insertJsonAraay(String str, String tag, Long knowledgeId) {

		List<ConnectionInfo> conlist = new ArrayList<ConnectionInfo>();
		JSONArray json = JSONArray.fromObject(str); // 首先把字符串转成
		int count = 0;
		ConnectionInfo knowledgeconnect = null;
		if (json.size() > 0) {
			for (int i = 0; i < json.size(); i++) {
				JSONObject job = json.getJSONObject(i); // 遍历 jsonarray
				knowledgeconnect = new ConnectionInfo();
				knowledgeconnect.setKnowledgeid(knowledgeId);
				knowledgeconnect.setTag(tag);
				String type = StringUtils.equals(job.get("type") + "", "null") ? "-2" : job.get("type")+ "";
				knowledgeconnect.setConntype(Integer.parseInt(type));
				String id = StringUtils.equals(job.get("id") + "", "null") ? "-2" : job.get("id")+ "";
				knowledgeconnect.setConnid(Long.parseLong(id));
				if (Integer.parseInt(type) == Constants.KnowledgeConnectType.event.v()
						|| Integer.parseInt(type) == Constants.KnowledgeConnectType.knowledge.v()) {

					knowledgeconnect.setConnname(job.get("title") + "");
				} else {
					knowledgeconnect.setConnname(job.get("name") + "");
				}
				String ownerid = job.get("ownerid") + "";
				knowledgeconnect.setOwnerid(Long.parseLong(StringUtils.equals(ownerid, "null") ? "-2" : ownerid));
				knowledgeconnect.setOwner(job.get("ownername") + "");
				if (Integer.parseInt(type) == Constants.KnowledgeConnectType.event.v()) {
					knowledgeconnect.setRequirementtype(job.get("requirementtype") + "");
					knowledgeconnect.setCareer(job.get("career") + "");
					knowledgeconnect.setUrl("/requirement/detail/"+ job.get("requirementtype") + "" + "/"+ job.get("id")+ "/");
				}
				if (Integer.parseInt(type) == Constants.KnowledgeConnectType.people.v()) {
					knowledgeconnect.setCompany(job.get("company") + "");
					knowledgeconnect.setUrl("/people/" + job.get("id")+ "/");
				}
				if (Integer.parseInt(type) == Constants.KnowledgeConnectType.organization.v()) {
					knowledgeconnect.setCompany(job.get("address") + "");
					knowledgeconnect.setHy(job.get("hy") + "");
				}
				if (Integer.parseInt(type) == Constants.KnowledgeConnectType.knowledge.v()) {
					knowledgeconnect.setColumntype(Integer.parseInt(job.get("column_type") + ""));
					knowledgeconnect.setColumnpath(job.get("columnpath") + "");
					knowledgeconnect.setUrl("/knowledge/reader?type="+ job.get("columntype") + "&kid=" + job.get("id"));
					knowledgeconnect.setPicpath(job.get("pic_path") + "");
				}

				if (Integer.parseInt(type) == Constants.KnowledgeConnectType.people.v()
						|| Integer.parseInt(type) == Constants.KnowledgeConnectType.organization.v()) {

					PeopleTemp peolpletemp = peopleMongoService.selectByPrimary(id);
					if (peolpletemp != null) {

						knowledgeconnect.setPicpath(peolpletemp.getPortrait());
					}
				}
				conlist.add(knowledgeconnect);
				// count =
				// connectionInfoMapper.insertSelective(knowledgeconnect);
			}
			connectionInfoValueMapper.insertConnectionInfo(conlist);
		}
		return count;
	}

	/**
	 * 获取关联tag值
	 * 
	 * @param str
	 * @return
	 */
	public String getTag(String str) {
		JSONObject j = JSONObject.fromObject(str);
		String strr = j.get("tag").toString();
		return strr;
	}
	
	public String getTagAll(String str) {
		JSONObject j = JSONObject.fromObject(str.substring(1, str.length()-1));
		String strr = j.get("tag").toString();
		return strr;
	}

	/**
	 * 获取关联conn值
	 * 
	 * @param str
	 * @return
	 */
	public String getConn(String str) {
		JSONObject j = JSONObject.fromObject(str);
		String strr = "";
		if( j.get("conn") != null ){
			
			strr = j.get("conn").toString();
		}
		return strr;
	}
	
	public String getConnAll(String str) {
		JSONObject j = JSONObject.fromObject(str.substring(1, str.length()-1));
		String strr = "";
		if( j.get("conn") != null ){
			
			strr = j.get("conn").toString();
		}
		return strr;
	}

	@Override
	public void deleteKnowledgeConnectInfo(Long knowledgeid) {
		ConnectionInfoExample example = new ConnectionInfoExample();
		Criteria criteria = example.createCriteria();
		criteria.andKnowledgeidEqualTo(knowledgeid);
		connectionInfoMapper.deleteByExample(example);
	}
}
