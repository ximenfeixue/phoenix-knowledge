package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeNewsVO;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNews;
import com.gintong.rocketmq.api.DefaultMessageService;
import com.gintong.rocketmq.api.enums.TopicType;
import com.gintong.rocketmq.api.model.RocketSendResult;
import com.gintong.rocketmq.api.utils.FlagTypeUtils;

@Service
public class BaseServiceImpl {

	private static final Logger logger = LoggerFactory.getLogger(BaseServiceImpl.class);

	@Autowired(required = true)
	private DefaultMessageService defaultMessageService;

	/**
	 * 大数据通知接口
	 * 
	 */
	public void noticeDataCenter(String noticeType, Object bean, long userId, String uName) {
		logger.info("通知大数据，发送请求 请求用户{}", userId);
		RocketSendResult result = null;
		try {
			if (StringUtils.isNotBlank(noticeType)) {
				String flagType = "";
				if (StringUtils.equals("upd", noticeType)) {
					flagType = FlagTypeUtils.updateKnowledgeFlag();
				} else if (StringUtils.equals("add", noticeType)) {
					flagType = FlagTypeUtils.createKnowledgeFlag();
				} else if (StringUtils.equals("del", noticeType)) {
					flagType = FlagTypeUtils.deleteKnowledgeFlag();
				}
				result = defaultMessageService.sendMessage(TopicType.KNOWLEDGE_TOPIC, flagType, beanToJson(setMapVO(bean, userId, uName)));
				logger.info("返回参数{}", result.getSendResult());
			} else {
				defaultMessageService.sendMessage(TopicType.KNOWLEDGE_TOPIC, beanToJson(setMapVO(bean, userId, uName)));
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("发送失败  返回参数{}", result.getSendResult());
		}
	}

	public static String beanToJson(Object bean) {
		JSONObject json = JSONObject.fromObject(bean);
		String selectedIds = json.getString("selectedIds").replace("\\", "");
		json.remove("selectedIds");
		json.put("selectedIds", selectedIds);
		logger.info(json.toString());
		return json.toString();
	}

	/**
	 * Method: beanToJsonForInit <br>
	 * Description: 用于知识老数据的初始化到MQ <br>
	 * Creator: xutianlong@gingtong.com <br>
	 * Date: 2016/1/21 10:50
	 */
	public static String beanToJsonForInit(Object bean) {
		JSONObject json = JSONObject.fromObject(bean);
		String selectedIds ="";
		if(json.getString("selectedIds") != null){
			selectedIds = json.getString("selectedIds").replace("\\", "");
		}else {
			selectedIds = "{\"dales\":[{\"id\":\"-1\",\"name\":\"全平台\"}],\"dule\":false,\"xiaoles\":[],\"zhongles\":[]}";
		}

		json.remove("selectedIds");
		json.put("selectedIds", selectedIds);
		logger.info(json.toString());
		return json.toString();
	}

	public static Map<String, Object> setMapVO(Object bean, long userId, String uName) {
		Map<String, Object> map = new HashMap<String, Object>();
		KnowledgeNewsVO vo = (KnowledgeNewsVO) bean;
		map.put("kid", vo.getkId());
		map.put("cid", userId);
		map.put("cname", uName);
		map.put("title", vo.getTitle());
		map.put("cpathid", vo.getColumnPath());
		map.put("pic", vo.getPic());
		map.put("selectedIds", vo.getSelectedIds());
		map.put("status", vo.getStatus());
		map.put("tags", vo.getTags());
		map.put("columnid", vo.getColumnid());
		map.put("columnType", vo.getColumnType());
		map.put("content", vo.getContent());
		map.put("desc", vo.getDesc());
		map.put("createtime", vo.getCreatetime());
		return map;
	}

	/**
	 * Method: setMapVO(Object bean) <br>
	 * Description: 用于知识老数据的初始化到MQ <br>
	 * Creator: xutianlong@gingtong.com <br>
	 * Date: 2016/1/21 10:52
	 */
	public static Map<String, Object> setMapVO(Object bean) {
		Map<String, Object> map = new HashMap<String, Object>();
		KnowledgeNewsVO vo = (KnowledgeNewsVO) bean;
		KnowledgeNews k = (KnowledgeNews)bean;
		map.put("kid", vo.getkId());
		map.put("cid", k.getCid());
		map.put("cname", k.getCname());
		map.put("title", vo.getTitle());
		map.put("cpathid", vo.getColumnPath());
		map.put("pic", vo.getPic());
		map.put("selectedIds", vo.getSelectedIds());
		map.put("status", vo.getStatus());
		map.put("tags", vo.getTags());
		map.put("columnid", vo.getColumnid());
		map.put("columnType", vo.getColumnType());
		map.put("content", vo.getContent());
		map.put("desc", vo.getDesc());
		map.put("createtime", vo.getCreatetime());
		return map;
	}

	public static void main(String[] args) {
		// KnowledgeNewsVO vo = new KnowledgeNewsVO();
		//
		// vo.setSelectedIds("{\"dales\":[{\"id\":\"-1\",\"name\":\"全平台\"}],\"dule\":false,\"xiaoles\":[],\"zhongles\":[]}");
		// vo.setTitle("111");
		// vo.setColumnid("1");
		// vo.setContent("ss");
		//
		// User user = new User();
		// user.setId(36);
		// user.setName("sss");
		// vo.setColumnType("1");
		// vo.setAsso("{\"r\":[],\"p\":[],\"o\":[],\"k\":[]}");
		// System.out.println(beanToJson(setMapVO(vo, 36l, "不知道")));
		String str = "{\"dales\":[{\"id\":\"-1\",\"name\":\"全平台\"}],\"dule\":false,\"xiaoles\":[],\"zhongles\":[]}";
		System.out.println(str.replace("\\", ""));
	}

}
