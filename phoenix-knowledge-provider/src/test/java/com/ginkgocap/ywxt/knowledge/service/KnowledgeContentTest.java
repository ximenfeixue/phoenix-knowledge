package com.ginkgocap.ywxt.knowledge.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.user.model.User;

/**
 * <p>
 * 知识目录操作测试接口
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

public class KnowledgeContentTest extends TestBase {
	
	@Autowired
	private KnowledgeCommentService knowledgeCommentService;
	
	@Test
	public void pushTest(){
		
		User user =new User();
		user.setId(36);
		user.setName("name");
		knowledgeCommentService.findCommentList(10978, 0, 1, 20, user);
	}
	

}
