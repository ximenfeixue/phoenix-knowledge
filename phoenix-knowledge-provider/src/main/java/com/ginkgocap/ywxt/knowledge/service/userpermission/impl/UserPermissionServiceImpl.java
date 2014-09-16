package com.ginkgocap.ywxt.knowledge.service.userpermission.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.dao.userpermission.UserPermissionDAO;
import com.ginkgocap.ywxt.knowledge.model.ColumnKnowledge;
import com.ginkgocap.ywxt.knowledge.service.userpermission.UserPermissionService;

@Service("userpermissionService")
public class UserPermissionServiceImpl implements UserPermissionService {

	@Autowired
	private UserPermissionDAO userPermissionDAO;

	@Override
	public int deleteColumnKnowledge(long[] knowledgeids, long columnid) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Long> selectByreceive_user_id(long receive_user_id) {

		return userPermissionDAO.selectByreceive_user_id(receive_user_id);
	}

	@Override
	public void insertUserPermission(long[] receive_uid, long knowledgeid,
			long send_uid, int type, String mento, long column_id) {

		userPermissionDAO.insertUserPermission(receive_uid, knowledgeid,
				send_uid, type, mento, column_id);
	}

}
