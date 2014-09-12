package com.ginkgocap.ywxt.knowledge.dao.userpermission.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

import com.ginkgocap.ywxt.knowledge.dao.userpermission.UserPermissionDAO;
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

}
