package com.ginkgocap.ywxt.knowledge.service.impl;

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
import com.ginkgocap.ywxt.knowledge.service.KnowledgeConnectInfoService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeHomeService;
import com.ginkgocap.ywxt.knowledge.util.Constants;
import com.ginkgocap.ywxt.people.domain.modelnew.PeopleName;
import com.ginkgocap.ywxt.people.domain.modelnew.PeopleSimple;
import com.ginkgocap.ywxt.people.domain.modelnew.PeopleTemp;
import com.ginkgocap.ywxt.people.service.PeopleMongoService;
import com.ginkgocap.ywxt.personalcustomer.service.PersonalCustomerService;

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

	@Override
	public Map<String, Object> insertKnowledgeConnectInfo(String knowledgeasso,
			Long knowledgeId, Long userid) {

		// 删除该知识的关联信息
		deleteKnowledgeConnectInfo(knowledgeId);

		Map<String, Object> result = new HashMap<String, Object>();
		JSONObject j = JSONObject.fromObject(knowledgeasso);
		String jsonstr = "";
		String tag = "";
		String conn = "";
		int count = 0;

		String assotype[] = { Constants.KnowledgeConnectType.event.c(),
				Constants.KnowledgeConnectType.people.c(),
				Constants.KnowledgeConnectType.organization.c(),
				Constants.KnowledgeConnectType.knowledge.c() };
		for (int i = 0; i < assotype.length; i++) {

			jsonstr = j.get(assotype[i]).toString();

			if (!StringUtils.equals(jsonstr, "[]")
					&& !StringUtils.equals(jsonstr, "-1")) {
				tag = getTag(jsonstr);
				conn = getConn(jsonstr);
				count = insertJsonAraay(conn, tag, knowledgeId);
				if (count < 0) {
					result.put(Constants.errormessage,
							Constants.ErrorMessage.addasso.c());
					return result;
				}
			} else {
				// 选人脉全部
				ConnectionInfo knowledgeconnect = null;
				if (StringUtils.equals(assotype[i], "p")) {

					List<PeopleSimple> peoples = peopleMongoService
							.getPeopleSimplelist(0, null, null, 1, 10, userid,
									1);
					if (peoples != null && peoples.size() > 0) {
						for (PeopleSimple peopleSimple : peoples) {
							knowledgeconnect = new ConnectionInfo();
							knowledgeconnect
									.setConntype(Constants.KnowledgeConnectType.people
											.v());
							knowledgeconnect.setKnowledgeid(knowledgeId);
							knowledgeconnect.setConnid(Long
									.parseLong(peopleSimple.getId()));
							knowledgeconnect.setUrl("/people/"
									+ peopleSimple.getId());
							knowledgeconnect.setOwnerid(userid);
							PeopleTemp peolpletemp = peopleMongoService
									.selectByPrimary(peopleSimple.getId());
							if (peolpletemp != null) {
								knowledgeconnect.setPicpath(peolpletemp
										.getPortrait());
								List<PeopleName> list = peolpletemp
										.getPeopleNameList();
								if (list != null && list.size() > 0) {
									for (PeopleName peopleName : list) {
										if (Integer.parseInt(peopleName
												.getTypeTag().getCode()) == 1) {
											knowledgeconnect
													.setConnname(peopleName
															.getName());
										}
									}
								}
							}
							connectionInfoMapper
									.insertSelective(knowledgeconnect);
						}
					}
				}
				if (StringUtils.equals(assotype[i], "r")) {

					// List<PeopleSimple> peoples = peopleMongoService
					// .getPeopleSimplelist(0, null, null, 1, 10, userid,
					// 1);
					// if (peoples != null && peoples.size() > 0) {
					//
					// }
				}
				if (StringUtils.equals(assotype[i], "o")) {

//					List<Map<String, Object>> lt = personalCustomerService
//							.list(userid, null, null, null, null, 0, 0, 10);
//
//					if (lt != null && lt.size() > 0) {
//
//						for (int k = 0; k < lt.size(); k++) {
//							Map<String, Object> map = lt.get(k);
//							if (map != null) {
//								knowledgeconnect = new ConnectionInfo();
//								knowledgeconnect
//										.setConntype(Constants.KnowledgeConnectType.organization
//												.v());
//								knowledgeconnect.setKnowledgeid(knowledgeId);
//								knowledgeconnect.setConnid(Long.parseLong(map
//										.get("id") + ""));
//								knowledgeconnect.setConnname(map.get("name")
//										+ "");
//								knowledgeconnect.setOwnerid(userid);
//								connectionInfoMapper
//										.insertSelective(knowledgeconnect);
//							}
//						}
//					}

				}
				if (StringUtils.equals(assotype[i], "k")) {

					// Map<String, Object> m = knowledgeHomeService
					// .selectAllKnowledgeCategoryByParam(tid, lid, state,
					// sortId, userid, keyword, page, size);
				}
			}
		}
		result.put(Constants.status, Constants.ResultType.success.v());
		return result;
	}

	public int insertJsonAraay(String str, String tag, Long knowledgeId) {

		JSONArray json = JSONArray.fromObject(str); // 首先把字符串转成
		int count = 0;
		ConnectionInfo knowledgeconnect = null;
		if (json.size() > 0) {
			for (int i = 0; i < json.size(); i++) {
				JSONObject job = json.getJSONObject(i); // 遍历 jsonarray
				knowledgeconnect = new ConnectionInfo();
				knowledgeconnect.setKnowledgeid(knowledgeId);
				knowledgeconnect.setTag(tag);
				knowledgeconnect.setConntype(Integer.parseInt(job.get("type")
						+ ""));
				knowledgeconnect.setConnid(Long.parseLong(job.get("id") + ""));
				if (Integer.parseInt(job.get("type") + "") == Constants.KnowledgeConnectType.event
						.v()
						|| Integer.parseInt(job.get("type") + "") == Constants.KnowledgeConnectType.knowledge
								.v()) {

					knowledgeconnect.setConnname(job.get("title") + "");
				} else {
					knowledgeconnect.setConnname(job.get("name") + "");
				}
				String ownerid = job.get("ownerid") + "";
				knowledgeconnect.setOwnerid(Long.parseLong(StringUtils.equals(
						ownerid, "null") ? "-2" : ownerid));
				knowledgeconnect.setOwner(job.get("ownername") + "");
				if (Integer.parseInt(job.get("type") + "") == Constants.KnowledgeConnectType.event
						.v()) {
					knowledgeconnect.setRequirementtype(job
							.get("requirementtype") + "");
					knowledgeconnect.setCareer(job.get("career") + "");
					knowledgeconnect.setUrl("/requirement/detail/"
							+ job.get("requirementtype") + "" + "/"
							+ job.get("id"));
				}
				if (Integer.parseInt(job.get("type") + "") == Constants.KnowledgeConnectType.people
						.v()) {
					knowledgeconnect.setCompany(job.get("company") + "");
					knowledgeconnect.setUrl("/people/" + job.get("id"));
				}
				if (Integer.parseInt(job.get("type") + "") == Constants.KnowledgeConnectType.organization
						.v()) {
					knowledgeconnect.setCompany(job.get("address") + "");
					knowledgeconnect.setHy(job.get("hy") + "");
				}
				if (Integer.parseInt(job.get("type") + "") == Constants.KnowledgeConnectType.knowledge
						.v()) {
					knowledgeconnect.setColumntype(Integer.parseInt(job
							.get("columntype") + ""));
					knowledgeconnect.setColumnpath(job.get("columnpath") + "");
					knowledgeconnect.setUrl("/knowledge/reader?type="
							+ job.get("columntype") + "&kid=" + job.get("id"));

				}

				if (Integer.parseInt(job.get("type") + "") == Constants.KnowledgeConnectType.people
						.v()
						|| Integer.parseInt(job.get("type") + "") == Constants.KnowledgeConnectType.organization
								.v()) {

					PeopleTemp peolpletemp = peopleMongoService
							.selectByPrimary(job.get("id") + "");
					if (peolpletemp != null) {

						knowledgeconnect.setPicpath(peolpletemp.getPortrait());
					}
				}
				count = connectionInfoMapper.insertSelective(knowledgeconnect);
			}
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
		JSONObject j = JSONObject
				.fromObject(str.substring(1, str.length() - 1));
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
		JSONObject j = JSONObject
				.fromObject(str.substring(1, str.length() - 1));
		String strr = j.get("conn").toString();
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
