package com.ginkgocap.ywxt.knowledge.dao.userpermission.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

import com.ginkgocap.ywxt.knowledge.dao.userpermission.UserPermissionDAO;
import com.ginkgocap.ywxt.knowledge.model.UserPermission;
import com.ibatis.sqlmap.client.SqlMapClient;

@Component("userpermissionDAO")
public class UserPermissionDAOImpl extends SqlMapClientDaoSupport implements
		UserPermissionDAO {

	@Autowired
	SqlMapClient sqlMapClient;

	@PostConstruct
	public void initSqlMapClient() {
		super.setSqlMapClient(sqlMapClient);
	}

	@Override
	public List<Long> selectByreceive_user_id(long receive_user_id) {

		Map<String, Object> map = new HashMap<String, Object>();
		if (receive_user_id > 0) {
			map.put("receive_user_id", receive_user_id);
		}
		List<Long> list = (List<Long>) getSqlMapClientTemplate().queryForList(
				"tb_user_permission.selectByreceive_user_id", receive_user_id);
		return list;
	}

	@Override
	public void insertUserPermission(long[] receive_uid, long knowledgeid,
			long send_uid, int type, String mento, long column_id) {

		List<UserPermission> list = new ArrayList<UserPermission>();
		UserPermission userPermission = null;
		if (receive_uid != null && receive_uid.length > 0) {
			for (int i = 0; i < receive_uid.length; i++) {
				userPermission = new UserPermission();
				userPermission.setReceive_user_id(receive_uid[i]);
				userPermission.setColumn_id(column_id);
				userPermission.setCreatetime(new Date());
				userPermission.setKnowledge_id(knowledgeid);
				userPermission.setMento(mento);
				userPermission.setType(type);
				userPermission.setSend_user_id(send_uid);
				list.add(userPermission);
			}
		} else {
			userPermission = new UserPermission();
			userPermission.setColumn_id(column_id);
			userPermission.setCreatetime(new Date());
			userPermission.setKnowledge_id(knowledgeid);
			userPermission.setMento(mento);
			userPermission.setType(type);
			userPermission.setSend_user_id(send_uid);
			list.add(userPermission);
		}
		getSqlMapClientTemplate()
				.insert("tb_user_permission.batchInsert", list);
	}
}
