package com.ginkgocap.ywxt.knowledge.service;

import java.util.Map;

public interface KnowledgeReportService {

	/**
	 * 添加举报信息
	 * @param kid 知识id	
	 * @param type	举报类型
	 * @param desc 描述信息
	 * @param userid 用户id
	 * @return
	 */
	Map<String, Object> addReport(long kid, String type, String desc, long userid);
}
