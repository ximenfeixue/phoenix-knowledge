package com.ginkgocap.ywxt.knowledge.service.userpermission.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.dao.userpermission.UserPermissionDAO;
import com.ginkgocap.ywxt.knowledge.service.userpermission.UserPermissionService;

@Service("userpermissionService")
public class UserPermissionServiceImpl implements UserPermissionService {

	@Autowired
	private UserPermissionDAO userPermissionDAO;

	@Override
	public List<Long> selectByreceive_user_id(long receive_user_id,
			long send_userid) {

		return userPermissionDAO.selectByreceive_user_id(receive_user_id,
				send_userid);
	}

	@Override
	public int insertUserPermission(long[] receive_uid, long knowledgeid,
			long send_uid, int type, String mento, short column_id) {

		return userPermissionDAO.insertUserPermission(receive_uid, knowledgeid,
				send_uid, type, mento, column_id);
	}

	@Override
	public int deleteUserPermission(long[] knowledgeids, long userid) {

		return userPermissionDAO.deleteUserPermission(knowledgeids, userid);
	}

	@Override
	public List<Long> selectByParams(Long receive_user_id, Long column_id,
			Long type) {
		return userPermissionDAO.selectByParams(receive_user_id, column_id,
				type);
	}

}
