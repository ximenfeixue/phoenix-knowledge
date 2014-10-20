package com.ginkgocap.ywxt.knowledge.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.service.SearchService;
import com.ginkgocap.ywxt.knowledge.util.Constants;
import com.ginkgocap.ywxt.knowledge.util.HTTPUtil;

@Service("searchService")
public class SearchServiceImpl implements SearchService {

	@Override
	public Map<String, Object> getUserTag(Long userid, String type) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("user_id", userid + "");
		params.put("type", type);
		String str = HTTPUtil.post("user/tags/search.json", params);

		String tags = null;
		try {
			if (!StringUtils.isBlank(str)) {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode node = mapper.readTree(str);
				String status = node.path("status").asText();

				if (status.equals("1")) {
					tags = node.path("tags").asText();
				}

			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Map<String, Object> result = new HashMap<String, Object>();
		result.put(Constants.status, Constants.ResultType.success.v());
		result.put("tags", tags);

		return result;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Map<String, Object> searchByKeywords(Long userid, String keywords,
			String scope, String pno, String psize) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("user_id", userid + "");
		params.put("keywords", keywords);
		params.put("scope", scope);
		params.put("pno", pno);
		params.put("psize", psize);

		String str = HTTPUtil.post("knowledge/keyword/search.json", params);

		ObjectMapper mapper = new ObjectMapper();
		Map result = null;
		try {
			result = mapper.readValue(str, Map.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Map<String, Object> searchTags(Long userid, String keywords,
			String scope, String pno, String psize) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("user_id", userid + "");
		params.put("keywords", keywords);
		params.put("scope", scope);
		params.put("pno", pno);
		params.put("psize", psize);

		String str = HTTPUtil.post("knowledge/tag/search.json", params);

		ObjectMapper mapper = new ObjectMapper();
		Map result = null;
		try {
			result = mapper.readValue(str, Map.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Map<String, Object> shareToJinTN(Long userid, Long kid, String type) {
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
			e.printStackTrace();
		}
		return result;
	}
}
