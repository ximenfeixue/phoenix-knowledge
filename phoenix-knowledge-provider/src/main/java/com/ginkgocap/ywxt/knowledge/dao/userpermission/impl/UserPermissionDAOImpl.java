package com.ginkgocap.ywxt.knowledge.dao.userpermission.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.ginkgocap.ywxt.knowledge.dao.userpermission.UserPermissionDAO;
import com.ginkgocap.ywxt.knowledge.entity.UserPermission;
import com.ginkgocap.ywxt.knowledge.entity.UserPermissionExample;
import com.ginkgocap.ywxt.knowledge.entity.UserPermissionExample.Criteria;
import com.ginkgocap.ywxt.knowledge.mapper.UserPermissionMapper;
import com.ginkgocap.ywxt.knowledge.mapper.UserPermissionValueMapper;

@Component("userpermissionDAO")
public class UserPermissionDAOImpl implements UserPermissionDAO {

	@Resource
	private UserPermissionValueMapper userPermissionValueMapper;

	@Resource
	private UserPermissionMapper userPermissionMapper;

	@Override
	public List<Long> selectByreceive_user_id(long receive_user_id,
			long send_userid) {

		List<Long> list = new ArrayList<Long>();
		List<Map<String, Object>> maplist = userPermissionValueMapper
				.selectByreceive_user_id(receive_user_id, send_userid);
		if (maplist != null && maplist.size() > 0) {
			for (int i = 0; i < maplist.size(); i++) {
				Map<String, Object> map = maplist.get(i);
				list.add((Long) map.get("knowledge_id"));
			}
		}
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

		List<Long> list = new ArrayList<Long>();
		List<Map<String, Object>> maplist = null;
		// List<Map<String, Object>> maplist = userPermissionValueMapper
		// .selectByParams(receive_user_id, column_id, type);
		if (maplist != null && maplist.size() > 0) {
			for (int i = 0; i < maplist.size(); i++) {
				Map<String, Object> map = maplist.get(i);
				list.add((Long) map.get("knowledge_id"));
			}
		}
		return list;
	}

	@Override
	public int deleteUserPermission(long knowledgeid, long userid) {
		UserPermissionExample example = new UserPermissionExample();
		Criteria criteria = example.createCriteria();
		criteria.andKnowledgeIdEqualTo(knowledgeid);
		criteria.andSendUserIdEqualTo(userid);
		return userPermissionMapper.deleteByExample(example);
	}
}
