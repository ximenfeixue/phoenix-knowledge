package com.ginkgocap.ywxt.knowledge.service;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.service.UserPermissionService;

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

public class UserPermissionTest extends TestBase {
	@Autowired
	private UserPermissionService userPermissionService;

	@Test
	public void insertUserPermission() throws Exception {

		long[] receive_uid = { 1, 2 };
		long knowledgeid = 1;
		long send_uid = 1;
		int type = 1;
		String mento = "12";
		short column_id = 1;
		Date createtime=new Date();

//		int count = userPermissionService.insertUserPermission(receive_uid,
//				knowledgeid, send_uid, type, mento, column_id);

	//	System.out.println("count : " + count);
	}

	@Test
	public void deleteuserPermission() throws Exception {

		long[] knowledgeids = { 1, 8 };
		long knowledgeid = 1;
		long userid = 1;
		int type = 1;
		String mento = "测试";
		long column_id = 1;

		System.out.println(userPermissionService.deleteUserPermission(
				knowledgeids, userid));
	}

	@Test
	public void selectByParams() {
		userPermissionService.selectByParams(1l, 1l, 1l);
	}
}
