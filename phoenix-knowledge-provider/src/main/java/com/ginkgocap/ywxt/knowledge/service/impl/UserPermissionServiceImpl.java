package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.dao.userpermission.UserPermissionDAO;
import com.ginkgocap.ywxt.knowledge.entity.UserPermission;
import com.ginkgocap.ywxt.knowledge.mapper.UserPermissionValueMapper;
import com.ginkgocap.ywxt.knowledge.model.UserPermissionMongo;
import com.ginkgocap.ywxt.knowledge.service.UserPermissionService;
import com.ginkgocap.ywxt.user.model.User;
import com.ginkgocap.ywxt.user.service.UserService;

@Service("userpermissionService")
public class UserPermissionServiceImpl implements UserPermissionService {

	@Autowired
	private UserPermissionDAO userPermissionDAO;
	@Autowired
	private UserPermissionValueMapper userPermissionValueMapper;
	@Resource
    private MongoTemplate mongoTemplate;
	@Resource
	private UserService userService;
	
	
	@Override
	public List<Long> selectByreceive_user_id(long receive_user_id,
			long send_userid) {

		return userPermissionDAO.selectByreceive_user_id(receive_user_id,
				send_userid);
	}

	@Override
	public int insertUserPermission(long[] receive_uid, long knowledgeid,
			long send_uid, int type, String mento,  short column_type,long column_id,String title,String desc,String picPath,String tags) {

		int i= userPermissionDAO.insertUserPermission(receive_uid, knowledgeid,
				send_uid, type, mento,   column_type,column_id);
		
		UserPermissionMongo userPermission = new UserPermissionMongo();
		StringBuffer sb=new StringBuffer();
		List<Long> receiveList=new ArrayList<Long>();
		for (int j = 0; j < receive_uid.length; j++) {
			receiveList.add(receive_uid[i]);
			User user=userService.selectByPrimaryKey(receive_uid[i]);
			if(user!=null && user.getId()>0){
				if(j>0){
					sb.append(","+user.getName());
				}else{
					sb.append(user.getName());
				}
			}
		}
		userPermission.setReceiveUserId(receiveList);
		userPermission.setReceiveName(sb.toString());
		userPermission.setColumnId(column_id);
		userPermission.setColumnType(column_type);
		userPermission.setColumnId(column_id);
		userPermission.setCreatetime(new Date());
		userPermission.setKnowledgeId(knowledgeid);
		userPermission.setMento(mento);
		userPermission.setSendUserId(send_uid);
		userPermission.setTitle(title);
		userPermission.setDesc(desc);
		userPermission.setPicPath(picPath);
		userPermission.setTags(tags);
		mongoTemplate.insert(userPermission);
		
		return i;
	}

	@Override
	public int deleteUserPermission(long[] knowledgeids, long userid) {

		return userPermissionDAO.deleteUserPermission(knowledgeids, userid);
	}

	@Override
	public List<Long> selectByParams(Long receive_user_id, Long column_id,
			Long type) {
		if (type == -1) {
			return userPermissionValueMapper.selectByParamsSingle(
					receive_user_id, column_id);
		} else {
			return userPermissionDAO.selectByParams(receive_user_id, column_id,
					type);
		}
	}

	@Override
	public int deleteUserPermission(long knowledgeid, long userid) {

		return userPermissionDAO.deleteUserPermission(knowledgeid, userid);
	}

}
