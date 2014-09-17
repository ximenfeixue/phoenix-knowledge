package com.ginkgocap.ywxt.knowledge.service;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.model.ColumnKnowledge;
import com.ginkgocap.ywxt.knowledge.service.userpermission.UserPermissionService;

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
	public void selectcolumnknowledge() throws Exception {

		ColumnKnowledge columnKnowledge = new ColumnKnowledge();
		columnKnowledge.setColumn_id(1);
		columnKnowledge.setKnowledge_id(1);
		columnKnowledge.setUser_id(111);
		columnKnowledge.setType(1);

		List<Long> list = userPermissionService.selectByreceive_user_id(2, 0);

		for (Long l : list) {
			System.out.println(l);
		}
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
	public void selectByParams()  {
	    userPermissionService.selectByParams(1l, 1l, 1l);
	}
}
