package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.dao.userpermission.UserPermissionDAO;
import com.ginkgocap.ywxt.knowledge.entity.UserPermission;
import com.ginkgocap.ywxt.knowledge.mapper.UserPermissionValueMapper;
import com.ginkgocap.ywxt.knowledge.model.UserPermissionMongo;
import com.ginkgocap.ywxt.knowledge.service.UserPermissionService;
import com.ginkgocap.ywxt.user.model.User;
import com.ginkgocap.ywxt.user.service.UserService;
import com.ginkgocap.ywxt.util.PageUtil;

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
			User user=userService.selectByPrimaryKey(receive_uid[j]);
			if(user!=null && user.getId()>0){
				receiveList.add(receive_uid[j]);
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

	@Override
	public Map<String, Object> getMyShare(Long userId, int start, int pageSize) {
		List<UserPermissionMongo> lt=null;
		PageUtil page =null;
		Criteria c=Criteria.where("sendUserId").is(userId);
		Query query = new Query(c);
		long count = mongoTemplate.count(query, UserPermissionMongo.class);
		page= new PageUtil((int) count, start, pageSize);
		
		query = new Query(c);
		
		query.sort().on("createtime", Order.DESCENDING);
		if(pageSize>0){
			query.skip(page.getPageStartRow());
			query.limit(pageSize);
		}
		lt = mongoTemplate.find(query, UserPermissionMongo.class);
		if(lt==null){
			lt=new ArrayList<UserPermissionMongo>();
		}
		Map<String,Object> returnMap=new HashMap<String,Object>();
		returnMap.put("pageUtil", page);
		returnMap.put("list", lt);
		
		return returnMap;
	}

	@Override
	public Map<String, Object> getShareme(Long userId, int start, int pageSize) {
		List<UserPermissionMongo> lt=null;
		PageUtil page =null;
		Criteria c=Criteria.where("receiveUserId").is(userId);
		Query query = new Query(c);
		long count = mongoTemplate.count(query, UserPermissionMongo.class);
		page= new PageUtil((int) count, start, pageSize);
		
		query = new Query(c);
		
		query.sort().on("createtime", Order.DESCENDING);
		if(pageSize>0){
			query.skip(page.getPageStartRow());
			query.limit(pageSize);
		}
		lt = mongoTemplate.find(query, UserPermissionMongo.class);
		if(lt==null){
			lt=new ArrayList<UserPermissionMongo>();
		}
		Map<String,Object> returnMap=new HashMap<String,Object>();
		returnMap.put("pageUtil", page);
		returnMap.put("list", lt);
		
		return returnMap;
	}

}
