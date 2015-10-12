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
	public int insertUserPermission(List<String> permList, long knowledgeid,
			long send_uid, int type, String shareMessage, short column_type,
			long column_id) {

		List<UserPermission> list = new ArrayList<UserPermission>();
		UserPermission userPermission = null;
		for (String perm : permList) {
			// 2:1,2,3,4
			String[] perInfo = perm.split(":");
			if (perInfo != null && perInfo.length > 0) {
				String perType = perInfo[0];
				String perUser = perInfo[1];
				if (perInfo != null && perInfo.length > 0) {
					String[] userList = perUser.split(",");
					for (String userId : userList) {
						userPermission = new UserPermission();
						userPermission.setReceiveUserId(Long.parseLong(userId));
						userPermission.setColumnId(column_id);
						userPermission.setColumnType(column_type);
						userPermission.setCreatetime(new Date());
						userPermission.setKnowledgeId(knowledgeid);
						userPermission.setMento(shareMessage);
						userPermission.setType(Integer.parseInt(perType));
						userPermission.setSendUserId(send_uid);
						list.add(userPermission);
					}
				}
			}
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

	/* (non-Javadoc)
	 * @see com.ginkgocap.ywxt.knowledge.dao.userpermission.UserPermissionDAO#isZhongLeForMe(java.lang.Long, java.lang.Long, int)
	 * Administrator
	 */
	@Override
	public boolean isZhongLeForMe(Long knowledgeid, Long receiveUserId, int type) {
		UserPermissionExample example = new UserPermissionExample();
		Criteria criteria = example.createCriteria();
		criteria.andKnowledgeIdEqualTo(knowledgeid);
		criteria.andReceiveUserIdEqualTo(receiveUserId);
		criteria.andTypeEqualTo(type);
		int count = userPermissionMapper.countByExample(example);
		if(count > 0){
			return true;
		}else{
			return false;
		}
	}
}
