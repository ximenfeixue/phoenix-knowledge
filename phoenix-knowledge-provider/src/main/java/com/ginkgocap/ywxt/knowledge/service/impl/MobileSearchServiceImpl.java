package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;

import com.ginkgocap.ywxt.knowledge.service.MobileSearchService;
import com.ginkgocap.ywxt.knowledge.util.HTTPUtil;

public class MobileSearchServiceImpl implements MobileSearchService {

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

}
