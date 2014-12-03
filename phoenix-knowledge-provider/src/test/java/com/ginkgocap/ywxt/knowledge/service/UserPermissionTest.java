package com.ginkgocap.ywxt.knowledge.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.model.UserPermissionMongo;

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
	@Test
	public void getMyshare() {
		Map map=userPermissionService.getMyShare(14359l, "", 1, 100);
		
		List<UserPermissionMongo> list=(List<UserPermissionMongo>) map.get("list");
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			UserPermissionMongo userPermissionMongo = (UserPermissionMongo) iterator
					.next();
			System.out.println(userPermissionMongo);
		}
	}
	
	@Test
	public void deleteUserPermissionMongo() throws Exception {
		List<String> list=new ArrayList<String>();
		list.add("545844fb8e729e16bc3118c8");
		list.add("54572b94a01fd17cad114dd2");
		userPermissionService.deleteMyShare(list);
	}
	
}