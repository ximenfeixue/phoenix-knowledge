package com.ginkgocap.ywxt.knowledge.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
	public Map<String, Object> getUserTag(long userid, String type) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("user_id", userid + "");
		params.put("type", type);
		HTTPUtil httpUtils = new HTTPUtil();
		String str = httpUtils.post("user/tags/search.json", params);

		String tags = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode node = mapper.readTree(str);
			String status = node.path("status").asText();
			if (status.equals("1")) {
				tags = node.path("tags").asText();
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

	@Override
	public Map<String, Object> searchByKeywords(long userid, String keywords,
			String scope, String pno, String pszie) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> searchTags(long userid, String keywords,
			String scope, String pno, String pszie) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> shareToJinTN(long userid, long kid, String type) {
		// TODO Auto-generated method stub
		return null;
	}
}
