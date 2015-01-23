package com.ginkgocap.ywxt.knowledge.service;

import org.junit.Test;

import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.ywxt.knowledge.base.TestBase;

/**   
 * <p>TODO（关联）</p>  
 * <p>于2015-1-6 由 bianzhiwei 创建 </p>
 * @author  <p>当前负责人 bianzhiwei</p>   
 *
 */

public class ConnectInfoTest extends TestBase {
	@Autowired
	private ConnectInfoService connectInfoService;

	@Test
	public void findConnectInfo() throws Exception {
	    connectInfoService.findConnectInfo(1460792l, null,"", 1, 10);
	}
}
