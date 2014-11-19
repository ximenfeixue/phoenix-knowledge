package com.ginkgocap.ywxt.knowledge.service;

import java.util.Map;

public interface DataCenterService {
	
	/**
	 * 获取解析后的文件信息
	 * @param path 文件路径
	 * @param id 知识ID
	 * @return
	 */
	Map<String,Object> getCaseDataFromDataCenter(String path);
}
