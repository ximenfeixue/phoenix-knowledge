package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.entity.KnowledgeConnectInfo;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeConnectInfoMapper;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeConnectInfoService;
import com.ginkgocap.ywxt.knowledge.util.Constants;

@Service("knowledgeConnectInfoService")
public class KnowledgeConnectInfoServiceImpl implements
		KnowledgeConnectInfoService {

	@Resource
	private KnowledgeConnectInfoMapper knowledgeConnectInfoMapper;

	@Override
	public Map<String, Object> insertKnowledgeConnectInfo(String knowledgeasso) {

		Map<String, Object> result = new HashMap<String, Object>();
		JSONObject j = JSONObject.fromObject(knowledgeasso);
		String strr = j.get(Constants.KnowledgeConnectType.demand.c())
				.toString();
		String tag = getTag(strr);
		String conn = getConn(strr);
		int count = insertJsonAraay(conn, tag);
		if (count < 0) {
			result.put(Constants.errormessage,
					Constants.ErrorMessage.addasso.c());
		}
		result.put("", count);
		return result;
	}

	public String jsonString(String str) {

		JSONObject j = JSONObject.fromObject(str);
		String strr = j.get("r").toString();
		return strr;
	}

	public int insertJsonAraay(String str, String tag) {

		JSONArray json = JSONArray.fromObject(str); // 首先把字符串转成
		int count = 0;
		if (json.size() > 0) {
			for (int i = 0; i < json.size(); i++) {
				JSONObject job = json.getJSONObject(i); // 遍历 jsonarray
				KnowledgeConnectInfo knowledgeconnect = new KnowledgeConnectInfo();
				knowledgeconnect.setConntype(Integer.parseInt((String) job
						.get("type")));
				knowledgeconnect.setConnid(Long.parseLong((String) job
						.get("id")));
				if (Integer.parseInt(job.get("type") + "") == Constants.KnowledgeConnectType.contacts
						.v()
						|| Integer.parseInt(job.get("type") + "") == Constants.KnowledgeConnectType.demand
								.v()) {

					knowledgeconnect.setConnname(job.get("title") + "");
				} else {
					knowledgeconnect.setConnname(job.get("name") + "");
				}
				knowledgeconnect.setOwnerid(Long.parseLong(job.get("ownerid")
						+ ""));
				knowledgeconnect.setOwner(job.get("ownername") + "");
				if (Integer.parseInt(job.get("type") + "") == Constants.KnowledgeConnectType.contacts
						.v()) {
					knowledgeconnect.setRequirementtype(job
							.get("requirementtype") + "");
					knowledgeconnect.setCareer(job.get("career") + "");
				}
				if (Integer.parseInt(job.get("type") + "") == Constants.KnowledgeConnectType.organization
						.v()) {
					knowledgeconnect.setCompany(job.get("company") + "");
				}
				if (Integer.parseInt(job.get("type") + "") == Constants.KnowledgeConnectType.knowledge
						.v()) {
					knowledgeconnect.setCompany(job.get("address") + "");
					knowledgeconnect.setHy(job.get("hy") + "");
				}
				if (Integer.parseInt(job.get("type") + "") == Constants.KnowledgeConnectType.demand
						.v()) {
					knowledgeconnect.setColumntype(Integer.parseInt(job
							.get("columntype") + ""));
					knowledgeconnect.setColumnpath(job.get("columnpath") + "");
				}
				count = knowledgeConnectInfoMapper.insert(knowledgeconnect);

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
		JSONObject j = JSONObject.fromObject(str);
		j = JSONObject.fromObject(str.substring(1, str.length() - 1));
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
		j = JSONObject.fromObject(str);
		String strr = j.get("conn").toString();
		return strr;
	}
}
