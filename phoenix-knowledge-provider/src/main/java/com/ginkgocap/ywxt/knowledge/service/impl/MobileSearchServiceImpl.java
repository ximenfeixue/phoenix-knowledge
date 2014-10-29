package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.mapper.MobileKnowledgeMapper;
import com.ginkgocap.ywxt.knowledge.service.MobileSearchService;
import com.ginkgocap.ywxt.knowledge.util.HTTPUtil;
import com.ginkgocap.ywxt.util.PageUtil;

@Service("mobileSearchService")
public class MobileSearchServiceImpl implements MobileSearchService {
	
	private static final Logger logger = LoggerFactory
			.getLogger(KnowledgeCollectionServiceImpl.class);
	
	@Resource
	private MobileKnowledgeMapper mobileKnowledgeMapper;

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
	
	@Override
	public Map<String, Object> selectKnowledgeByTagsAndkeywords(Long userid,
			String keywords, String scope, String tag, int page, int size) {
		logger.info(
				"com.ginkgocap.ywxt.knowledge.service.impl.MobileSearchService.selectKnowledgeByTagsAndkeywords:{},",
				userid);
		logger.info(
				"com.ginkgocap.ywxt.knowledge.service.impl.MobileSearchService.selectKnowledgeByTagsAndkeywords:{},",
				keywords);
		logger.info(
				"com.ginkgocap.ywxt.knowledge.service.impl.MobileSearchService.selectKnowledgeByTagsAndkeywords:{},",
				scope);
		logger.info(
				"com.ginkgocap.ywxt.knowledge.service.impl.MobileSearchService.selectKnowledgeByTagsAndkeywords:{},",
				tag);
		logger.info(
				"com.ginkgocap.ywxt.knowledge.service.impl.MobileSearchService.selectKnowledgeByTagsAndkeywords:{},",
				page);
		logger.info(
				"com.ginkgocap.ywxt.knowledge.service.impl.MobileSearchService.selectKnowledgeByTagsAndkeywords:{},",
				size);
		int start = (page - 1) * size;
		int count = mobileKnowledgeMapper
				.selectCountKnowledgeByTagsAndKeyWords(userid, tag, keywords);
		List<?> kcl = mobileKnowledgeMapper.selectKnowledgeByTagsAndKeyWords(
				userid, tag, keywords, start, size);
		PageUtil p = new PageUtil(count, page, size);
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("page", p);
		m.put("list", kcl);
		return m;
	}
}
