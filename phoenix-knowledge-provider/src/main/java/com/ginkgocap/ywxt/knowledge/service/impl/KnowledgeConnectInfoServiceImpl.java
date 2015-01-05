package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.entity.KnowledgeConnectInfo;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeConnectInfoExample;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeConnectInfoExample.Criteria;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeConnectInfoMapper;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeConnectInfoService;
import com.ginkgocap.ywxt.knowledge.util.Constants;

@Service("knowledgeConnectInfoService")
public class KnowledgeConnectInfoServiceImpl implements
		KnowledgeConnectInfoService {

	@Resource
	private KnowledgeConnectInfoMapper knowledgeConnectInfoMapper;

	@Override
	public Map<String, Object> insertKnowledgeConnectInfo(String knowledgeasso,
			Long knowledgeId) {

		//删除该知识的关联信息
		deleteKnowledgeConnectInfo(knowledgeId);
		
		Map<String, Object> result = new HashMap<String, Object>();
		JSONObject j = JSONObject.fromObject(knowledgeasso);
		String jsonstr = "";
		String tag = "";
		String conn = "";
		int count = 0;

		jsonstr = j.get(Constants.KnowledgeConnectType.people.c()).toString();
		if (!StringUtils.equals(jsonstr, "[]")) {
			tag = getTag(jsonstr);
			conn = getConn(jsonstr);
			count = insertJsonAraay(conn, tag, knowledgeId);
			if (count < 0) {
				result.put(Constants.errormessage,
						Constants.ErrorMessage.addasso.c());
			}
			return result;
		}

		jsonstr = j.get(Constants.KnowledgeConnectType.organization.c())
				.toString();
		if (!StringUtils.equals(jsonstr, "[]")) {
			tag = getTag(jsonstr);
			conn = getConn(jsonstr);
			count = insertJsonAraay(conn, tag, knowledgeId);
			if (count < 0) {
				result.put(Constants.errormessage,
						Constants.ErrorMessage.addasso.c());
			}
			return result;
		}

		jsonstr = j.get(Constants.KnowledgeConnectType.knowledge.c())
				.toString();
		if (!StringUtils.equals(jsonstr, "[]")) {
			tag = getTag(jsonstr);
			conn = getConn(jsonstr);
			count = insertJsonAraay(conn, tag, knowledgeId);
			if (count < 0) {
				result.put(Constants.errormessage,
						Constants.ErrorMessage.addasso.c());
			}
			return result;
		}

		jsonstr = j.get(Constants.KnowledgeConnectType.event.c()).toString();
		if (!StringUtils.equals(jsonstr, "[]")) {
			tag = getTag(jsonstr);
			conn = getConn(jsonstr);
			count = insertJsonAraay(conn, tag, knowledgeId);
			if (count < 0) {
				result.put(Constants.errormessage,
						Constants.ErrorMessage.addasso.c());
			}
			return result;
		}
		result.put(Constants.status, Constants.ResultType.success.v());
		return result;
	}

	public int insertJsonAraay(String str, String tag, Long knowledgeId) {

		JSONArray json = JSONArray.fromObject(str); // 首先把字符串转成
		int count = 0;
		KnowledgeConnectInfo knowledgeconnect = null;
		if (json.size() > 0) {
			for (int i = 0; i < json.size(); i++) {
				JSONObject job = json.getJSONObject(i); // 遍历 jsonarray
				knowledgeconnect = new KnowledgeConnectInfo();
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
				count = knowledgeConnectInfoMapper
						.insertSelective(knowledgeconnect);

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
		KnowledgeConnectInfoExample example = new KnowledgeConnectInfoExample();
		Criteria criteria = example.createCriteria();
		criteria.andKnowledgeidEqualTo(knowledgeid);
		knowledgeConnectInfoMapper.deleteByExample(example);
	}
}
