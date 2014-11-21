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
	
	/**
	 * 添加栏目通知数据中心
	 * @param columnId 栏目ID
	 * @return
	 */
	Map<String,Object> noticeDataCenterWhileColumnChange(long columnId);
	/**
	 * 
	 * 导出知识成文件
	 * @param knowledgeIds 知识ids
	 * @param group 导出内容 1 所选文章 2 所选附件 3 所选文章及附件
	 * @param uid 用户id
	 * @param nfsHome nginx 挂在目录
	 * @param taskId 任务id
	 * @return
	 * liubang
	 */
	Map<String,Object> getExportFile(long uid,String knowledgeIds,String group,String nfsHome,String taskId);
	/**
	 * 获取文件进度
	 * @param taskId 任务id
	 * @return
	 * liubang
	 */
	Map<String, Object> processView(String taskId);
	/** 知识修改通知数据中心
	 * @param kId 知识ID
	 * @param oper 操作(add del upd) 
	 * @param type 知识类型(11种类型)
	 * @return
	 */
	Map<String,Object> noticeDataCenterWhileKnowledgeChange(long kId,String oper,String type);
	
	
}
