package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.service.DataCenterService;
import com.ginkgocap.ywxt.knowledge.util.Constants;
import com.ginkgocap.ywxt.knowledge.util.HTTPUtil;

@Service("dataCenterService")
public class DataCenterServiceImpl implements DataCenterService {
	private static final Logger logger = LoggerFactory
			.getLogger(DataCenterServiceImpl.class);

	@Override
	public Map<String, Object> getCaseDataFromDataCenter(String path, long id) {
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

			String str = HTTPUtil.post("", params);
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
		} catch (Exception e) {
			logger.error("搜转换经典案例失败{}", e.toString());
			e.printStackTrace();
		}
		logger.info("转换经典案例成功,返回值:{}", path);
		return result;
	}

}
