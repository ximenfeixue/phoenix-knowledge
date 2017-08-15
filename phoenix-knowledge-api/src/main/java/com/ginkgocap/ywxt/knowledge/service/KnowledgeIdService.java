package com.ginkgocap.ywxt.knowledge.service;


/**
 * @Title: 知识公共服务
 * @Description: TODO
 * @date 2016年1月13日 下午2:42:28
 * @version V1.0.0
 */
public interface KnowledgeIdService {
	
	/**
	 * 生成并获取主键
	 * @date 2016年1月13日 下午2:42:57
	 * @return
	 */
	public Long getUniqueSequenceId();

	public Long getUniqueSequenceId(String prefix);
}
