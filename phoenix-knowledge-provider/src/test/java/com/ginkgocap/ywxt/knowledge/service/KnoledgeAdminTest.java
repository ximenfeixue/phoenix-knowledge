package com.ginkgocap.ywxt.knowledge.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.ywxt.knowledge.admin.service.KnowledgeAdminService;
import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNewsVO;
import com.ginkgocap.ywxt.user.model.User;

/**
 * <p>
 * 用户权限操作测试接口
 * </p>
 * <p>
 * 于2014-8-19 由 bianzhiwei 创建
 * </p>
 * 
 * @author <p>
 *         当前负责人 bianzhiwei
 *         </p>
 * @since <p>
 *        1.2.1-SNAPSHO
 *        </p>
 */

public class KnoledgeAdminTest extends TestBase {
	@Autowired
	private KnowledgeAdminService knowledgeAdminService;

	@Test
	public void selectByParams() {
		
		KnowledgeNewsVO vo = new KnowledgeNewsVO();
		vo.setColumnid("172");
		vo.setTitle("测试");
		vo.setContent("ssssssssssssssssssssssssssss");
		vo.setColumnType("1");
		
		User user = new User();
		user.setId(0);
		user.setName("name");
		knowledgeAdminService.addNews(vo, user);
	}
	
	
}
