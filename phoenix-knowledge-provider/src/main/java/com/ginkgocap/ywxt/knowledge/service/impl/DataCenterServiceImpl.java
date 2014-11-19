package com.ginkgocap.ywxt.knowledge.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.service.DataCenterService;
import com.ginkgocap.ywxt.knowledge.util.Constants;
import com.ginkgocap.ywxt.knowledge.util.HTTPUrlConfig;
import com.ginkgocap.ywxt.knowledge.util.HTTPUtil;

@Service("dataCenterService")
public class DataCenterServiceImpl implements DataCenterService {
	private static final Logger logger = LoggerFactory
			.getLogger(DataCenterServiceImpl.class);

	@Resource
	private HTTPUrlConfig httpUrlConfig;

	@Override
	public Map<String, Object> getCaseDataFromDataCenter(String path) {
		logger.info("进入转换经典案例请求");
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			if (StringUtils.isBlank(path)) {
				result.put(Constants.status, Constants.ResultType.fail.v());
				result.put(Constants.errormessage,
						Constants.ErrorMessage.paramNotBlank.c());
				return result;
			}

			Map<String, String> params = new HashMap<String, String>();
			params.put("path", path);

			String str = HTTPUtil.post(httpUrlConfig.getParseUrl() + "pdf/",
					params);
			if (StringUtils.isBlank(str)) {
				logger.error("转换错误,转换返回值为空!");
				result.put(Constants.status, Constants.ResultType.fail.v());
				result.put(Constants.errormessage,
						Constants.ErrorMessage.parseError.c());
				return result;
			}
			ObjectMapper mapper = new ObjectMapper();
			result = mapper.readValue(str, Map.class);
			if (StringUtils.isBlank(result.get("responseData") + "")) {
				result.put(Constants.status, Constants.ResultType.fail.v());
				result.put(Constants.errormessage,
						Constants.ErrorMessage.parseError.c());
				return result;
			}
			result.put(Constants.status, Constants.ResultType.success.v());
		} catch (Exception e) {
			logger.error("搜转换经典案例失败{}", e.toString());
			e.printStackTrace();
		}
		logger.info("转换经典案例成功,返回值:{}", path);
		return result;
	}

	@Override
	public Map<String, Object> noticeDataCenterWhileColumnChange(long columnId) {
		logger.info("进入添加栏目通知数据中心请求");
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, String> params = new HashMap<String, String>();
		params.put("columnId", columnId + "");

		String str = HTTPUtil.post(httpUrlConfig.getPushUrl(), params);
		if (StringUtils.isBlank(str)) {
			logger.error("通知失败!栏目ID:{}",columnId);
			result.put(Constants.status, Constants.ResultType.fail.v());
			result.put(Constants.errormessage,
					Constants.ErrorMessage.parseError.c());
			return result;
		}
		try {
			ObjectMapper mapper = new ObjectMapper();
			result = mapper.readValue(str, Map.class);
		}  catch (Exception e) {
			e.printStackTrace();
			logger.error("通知失败!栏目ID:{}",columnId);
		}
		
		result.put(Constants.status, Constants.ResultType.success.v());
		
		return result;
	}

}
