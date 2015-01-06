package com.ginkgocap.ywxt.knowledge.service;

import java.util.Map;


/**
 *   
 * <p>关联业务接口</p>  
 * <p>于2015-1-6 由 bianzhiwei 创建 </p>
 * @author  <p>当前负责人 bianzhiwei</p>   
 *
 */
public interface ConnectInfoService {

	/**
	 * findConnectInfo <p>(查询关联)</p>      
	 * @param kid 知识id
	 * @param connType 关联类型 （为null时查询所有类型）
	 * @param page 当前页
	 * @param size 每页大小
	 * @return
	 */
	public Map<String,Object> findConnectInfo(Long kid,Integer connType,int page, int size);

}
