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
import com.ginkgocap.ywxt.knowledge.service.KnowledgeConnectInfoService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeHomeService;
import com.ginkgocap.ywxt.knowledge.util.Constants;
import com.ginkgocap.ywxt.people.domain.modelnew.PeopleTemp;
import com.ginkgocap.ywxt.people.service.PeopleMongoService;
import com.ginkgocap.ywxt.personalcustomer.service.PersonalCustomerService;
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
						if( StringUtils.equals(conn, "-9")){
							//全部
							allAsso(tag,assotype[i],knowledgeId,userid);
						}else{
							
							count = insertJsonAraay(conn, tag, knowledgeId);
						}
						if (count < 0) {
							result.put(Constants.errormessage,Constants.ErrorMessage.addasso.c());
							return result;
						}
					}
				} else if ( StringUtils.equals(conn, "-9")){
					//全部
					allAsso(tag,assotype[i],knowledgeId,userid);
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
					if(StringUtils.equals(job.get("ptype") + "", "rm")){
						
						knowledgeconnect.setUrl("/people/" + job.get("id")+ "/");
					}
					if(StringUtils.equals(job.get("ptype") + "", "hy")){
						knowledgeconnect.setUrl("/member/view/?id=" + job.get("id"));
					}
				}
				if (Integer.parseInt(type) == Constants.KnowledgeConnectType.knowledge.v()) {
					if( job.get("columntype") != null){
						
						knowledgeconnect.setColumntype(Integer.parseInt(job.get("columntype") + ""));
						knowledgeconnect.setUrl("/knowledge/reader?type="+ job.get("columntype") + "&kid=" + job.get("id"));
					}else{
						
						knowledgeconnect.setColumntype(Integer.parseInt(job.get("column_type") + ""));
						knowledgeconnect.setUrl("/knowledge/reader?type="+ job.get("column_type") + "&kid=" + job.get("id"));
					}
					knowledgeconnect.setColumnpath(job.get("columnpath") + "");
					
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
	
	public void allAsso(String tag,String assotype,Long knowledgeId,Long userid){

		// 选人脉全部
		ConnectionInfo knowledgeconnect = null;
		if (StringUtils.equals(assotype, "p")) {
			knowledgeconnect=new ConnectionInfo();
			knowledgeconnect.setAllasso(-1);
			knowledgeconnect.setConntype(Constants.KnowledgeConnectType.people.v());
			knowledgeconnect.setKnowledgeid(knowledgeId);
			connectionInfoMapper.insertSelective(knowledgeconnect);
			
		}
		if (StringUtils.equals(assotype, "r")) {
			knowledgeconnect=new ConnectionInfo();
			knowledgeconnect.setAllasso(-1);
			knowledgeconnect.setConntype(Constants.KnowledgeConnectType.event.v());
			knowledgeconnect.setKnowledgeid(knowledgeId);
			connectionInfoMapper.insertSelective(knowledgeconnect);
		}
		if (StringUtils.equals(assotype, "o")) {
			knowledgeconnect=new ConnectionInfo();
			knowledgeconnect.setAllasso(-1);
			knowledgeconnect.setConntype(Constants.KnowledgeConnectType.organization.v());
			knowledgeconnect.setKnowledgeid(knowledgeId);
			connectionInfoMapper.insertSelective(knowledgeconnect);
		}
		if (StringUtils.equals(assotype, "k")) {
			knowledgeconnect=new ConnectionInfo();
			knowledgeconnect.setAllasso(-1);
			knowledgeconnect.setConntype(Constants.KnowledgeConnectType.knowledge.v());
			knowledgeconnect.setKnowledgeid(knowledgeId);
			connectionInfoMapper.insertSelective(knowledgeconnect);
		}
	
	
	}
}
