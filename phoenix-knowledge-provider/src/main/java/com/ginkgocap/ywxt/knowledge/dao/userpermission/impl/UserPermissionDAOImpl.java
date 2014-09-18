package com.ginkgocap.ywxt.knowledge.dao.userpermission.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

import com.ginkgocap.ywxt.knowledge.dao.userpermission.UserPermissionDAO;
import com.ginkgocap.ywxt.knowledge.entity.UserPermission;
import com.ginkgocap.ywxt.knowledge.mapper.UserPermissionValueMapper;
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

	@Resource
	private UserPermissionValueMapper userPermissionValueMapper;

	@Override
	public List<Long> selectByreceive_user_id(long receive_user_id,
			long send_userid) {

		Map<String, Object> map = new HashMap<String, Object>();
		if (receive_user_id > 0) {
			map.put("receive_user_id", receive_user_id);
		}
		if (send_userid > 0) {
			map.put("send_user_id", send_userid);
		}
		List<Long> list = (List<Long>) getSqlMapClientTemplate().queryForList(
				"tb_user_permission.selectByreceive_user_id", map);
		return list;
	}

	@Override
	public int insertUserPermission(long[] receive_uid, long knowledgeid,
			long send_uid, int type, String mento, short column_id) {

		List<UserPermission> list = new ArrayList<UserPermission>();
		UserPermission userPermission = null;
		if (receive_uid != null && receive_uid.length > 0) {
			for (int i = 0; i < receive_uid.length; i++) {
				userPermission = new UserPermission();
				userPermission.setReceiveUserId(receive_uid[i]);
				userPermission.setColumnId((short) column_id);
				userPermission.setCreatetime(new Date());
				userPermission.setKnowledgeId(knowledgeid);
				userPermission.setMento(mento);
				userPermission.setType(type);
				userPermission.setSendUserId(send_uid);
				list.add(userPermission);
			}
		} else {
			userPermission = new UserPermission();
			userPermission.setColumnId((short) column_id);
			userPermission.setCreatetime(new Date());
			userPermission.setKnowledgeId(knowledgeid);
			userPermission.setMento(mento);
			userPermission.setType(type);
			userPermission.setSendUserId(send_uid);
			list.add(userPermission);
		}
		return userPermissionValueMapper.batchInsert(list);
	}

	@Override
	public int deleteUserPermission(long[] knowledgeids, long userid) {

		return userPermissionValueMapper.delete(knowledgeids, userid);
	}

	@Override
	public List<Long> selectByParams(Long receive_user_id, Long column_id,
			Long type) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("receive_user_id", receive_user_id);
		map.put("column_id", column_id);
		map.put("type", type);
		List<Long> list = (List<Long>) getSqlMapClientTemplate().queryForList(
				"tb_user_permission.selectByParams", map);
		return list;
	}
}
