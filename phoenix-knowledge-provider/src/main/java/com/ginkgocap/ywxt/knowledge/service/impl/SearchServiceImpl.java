package com.ginkgocap.ywxt.knowledge.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.service.SearchService;
import com.ginkgocap.ywxt.knowledge.util.Constants;
import com.ginkgocap.ywxt.knowledge.util.HTTPUtil;

@Service("searchService")
public class SearchServiceImpl implements SearchService {
	private static final Logger logger = LoggerFactory
			.getLogger(SearchServiceImpl.class);

	@Override
	public Map<String, Object> getUserTag(Long userid, String type) {
		logger.info("进入搜索首页热门标签请求,用户ID：{},type:{}", userid, type);
		Map<String, String> params = new HashMap<String, String>();
		Map<String, Object> result = new HashMap<String, Object>();
		params.put("user_id", userid + "");
		params.put("type", type);
		String str = HTTPUtil.post("user/tags/search.json", params);

		String tags = null;
		try {
			if (!StringUtils.isBlank(str)) {
//				ObjectMapper mapper = new ObjectMapper();
				String restr = str.replace("[", "").replace("]", "");
				result.put("tags", restr);
//				JsonNode node = mapper.readTree(str);
//				String status = node.path("status").asText();
//
//				if (status.equals("1")) {
//					tags = node.path("tags").asText();
//				}

			}
		} catch (Exception e) {
			logger.error("搜索首页热门标签请求查询失败,{}", e.toString());
			e.printStackTrace();
		}

		result.put(Constants.status, Constants.ResultType.success.v());
//		result.put("tags", tags);
		logger.info("搜索首页热门标签请求查询成功,返回值:{}", tags);
		return result;
	}

	/*
	 * userid - 用户id (默认不填就表示不走权限,查全部的数据) keyword - 关键词(默认为空,查全部数据) tag -
	 * 标签参数,根据关键词查的同时
	 * ,可以加标签一起查,之间默认关系是and。注意：如果此处传值,keyword为空,则和keyword有值,只查tags一样的效果,跟qf参数相关。
	 * scope - 搜索范围(不填默认全部，0-全部,1-资讯...) pno - 请求页数(1) psize - 请求个数(20) qf -
	 * 根据哪些列查(title,tags,content ,多个之间用逗号隔开,不传默认为title) type - 我收藏的-1, 分享给我的-2,
	 * 我创建的-3, 全部-4(默认查全部,前提是必须有用户) sort -
	 * 排序支持多个如：sort=createTime-ASC0&sort=name
	 * -DESC,解析：排序字段-升降序（-ASC升序，-DESC降序（默认为降序
	 * ,即sort=createTime等价于sort=createTime-
	 * DESC）），默认根据关键词打分,能排序的字段名和mongod表的字段名一样.
	 * 
	 * @see
	 * com.ginkgocap.ywxt.knowledge.service.SearchService#searchByKeywords(java
	 * .lang.Long, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String)
	 */

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Map<String, Object> searchByKeywords(Long userid, String keywords,
			String scope, String pno, String psize) {
		logger.info("进入搜索关键词请求,用户ID：{},关键词:{}", userid, keywords);
		Map<String, String> params = new HashMap<String, String>();
		params.put("userid", userid + "");
		params.put("keywords", keywords);
		params.put("scope", scope);
		params.put("qf", "title,tags,content");
		params.put("pno", pno);
		params.put("psize", psize);

		String str = HTTPUtil.post("/knowledge/keyword/search.json", params);

		ObjectMapper mapper = new ObjectMapper();
		Map result = null;
		try {
			result = mapper.readValue(str, Map.class);
		} catch (Exception e) {
			logger.error("搜索搜索关键词请求查询失败,{}", e.toString());
			e.printStackTrace();
		}
		logger.info("搜索搜索关键词请求查询成功,返回值:{}", str);
		return result;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Map<String, Object> searchTags(Long userid, String keywords,
			String scope, String pno, String psize) {
		logger.info("进入搜索标签请求,用户ID：{},标签:{}", userid, keywords);
		Map<String, String> params = new HashMap<String, String>();
		params.put("userid", userid + "");
		params.put("tag", keywords);
		params.put("scope", scope);
		params.put("pno", pno);
		params.put("psize", psize);

		String str = HTTPUtil.post("/knowledge/keyword/search.json", params);

		ObjectMapper mapper = new ObjectMapper();
		Map result = null;
		try {
			result = mapper.readValue(str, Map.class);
		} catch (Exception e) {
			logger.error("搜索标签请求查询失败,{}", e.toString());
			e.printStackTrace();
		}
		logger.info("搜索标签请求查询成功,返回值:{}", str);
		return result;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Map<String, Object> shareToJinTN(Long userid, Long kid, String type) {
		logger.info("进入分享到金桐脑请求,用户ID：{},知识ID:{}", userid, kid);
		Map<String, String> params = new HashMap<String, String>();
		params.put("user_id", userid + "");
		params.put("kid", kid + "");
		params.put("type", type);
		String str = HTTPUtil.post("knowledge/put.json", params);

		ObjectMapper mapper = new ObjectMapper();
		Map result = null;
		try {
			result = mapper.readValue(str, Map.class);
		} catch (Exception e) {
			logger.error("分享到金桐脑请求失败,{}", e.toString());
			e.printStackTrace();
		}
		logger.info("分享到金桐脑请求成功,返回值:{}", str);
		return result;
	}
}
