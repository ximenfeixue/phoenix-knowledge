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
import com.ginkgocap.ywxt.knowledge.entity.UserPermissionExample;
import com.ginkgocap.ywxt.knowledge.mapper.UserPermissionMapper;
import com.ginkgocap.ywxt.knowledge.mapper.UserPermissionValueMapper;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNewsVO;
import com.ginkgocap.ywxt.knowledge.model.UserPermissionMongo;
import com.ginkgocap.ywxt.knowledge.service.UserPermissionService;
import com.ginkgocap.ywxt.user.model.User;
import com.ginkgocap.ywxt.user.service.UserService;
import com.ginkgocap.ywxt.util.PageUtil;
import com.ginkgocap.ywxt.utils.DateUtils;

@Service("userpermissionService")
public class UserPermissionServiceImpl implements UserPermissionService {

	@Autowired
	private UserPermissionDAO userPermissionDAO;
	@Autowired
	private UserPermissionValueMapper userPermissionValueMapper;
	@Autowired
	private UserPermissionMapper userPermissionMapper;
	@Resource
	private MongoTemplate mongoTemplate;
	@Resource
	private UserService userService;

	private final static String split = ",";

	@Override
	public List<Long> selectByreceive_user_id(long receive_user_id,
			long send_userid) {

		return userPermissionDAO.selectByreceive_user_id(receive_user_id,
				send_userid);
	}

	@Override
	public int insertUserPermission(List<String> permList, long knowledgeid,
			long send_uid, String shareMessage, short column_type,
			long column_id) {

		List<UserPermission> list = new ArrayList<UserPermission>();
		UserPermission userPermission = null;
		for (String perm : permList) {
			// 2:1,2,3,4
			String[] perInfo = perm.split(":");
			if (perInfo != null && perInfo.length == 2) {
				String perType = perInfo[0];
				String perUser = perInfo[1].substring(1,
						perInfo[1].length() - 1);
				if (perInfo != null && perInfo.length > 0) {
					String[] userList = perUser.split(",");
					for (String userId : userList) {
						userPermission = new UserPermission();
						userPermission.setReceiveUserId(Long.parseLong(userId
								.trim()));
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
	public int insertUserPermission(List<String> permList, long knowledgeid,
			long send_uid, int type, String shareMessage, short column_type,
			long column_id, String title, String desc, String picPath,
			String tags) {
		// 用户ID集合
		List<Long> receiveList = new ArrayList<Long>();
		// 用户权限集合
		List<UserPermission> list = new ArrayList<UserPermission>();
		UserPermission userPerm = null;
		for (String perm : permList) {
			// 2:1,2,3,4
			String[] perInfo = perm.split(":");
			if (perInfo != null && perInfo.length > 0) {
				String perType = perInfo[0];
				String perUser = perInfo[1];
				if (perInfo != null && perInfo.length > 0) {
					String[] userList = perUser.split(split);
					for (String userId : userList) {
						userPerm = new UserPermission();
						userPerm.setReceiveUserId(Long.parseLong(userId));
						userPerm.setColumnId(column_id);
						userPerm.setColumnType(column_type);
						userPerm.setCreatetime(new Date());
						userPerm.setKnowledgeId(knowledgeid);
						userPerm.setMento(shareMessage);
						userPerm.setType(Integer.parseInt(perType));
						userPerm.setSendUserId(send_uid);
						list.add(userPerm);
						receiveList.add(Long.parseLong(userId));
					}
				}
			}
		}
		int v = userPermissionValueMapper.batchInsert(list);

		UserPermissionMongo userPermission = new UserPermissionMongo();
		StringBuffer sb = new StringBuffer();

		for (Long uid : receiveList) {
			User user = userService.selectByPrimaryKey(uid);
			if (user != null) {
				sb.append(user.getName());
				sb.append(split);
			}
		}
		if (sb.length() > 0) {
			sb = sb.deleteCharAt(sb.length() - 1);
		}
		User user = userService.selectByPrimaryKey(send_uid);
		userPermission.setSendUserName(user.getName());
		userPermission.setReceiveUserId(receiveList);
		userPermission.setReceiveName(sb.toString());
		userPermission.setColumnId(column_id);
		userPermission.setColumnType(column_type);
		userPermission.setColumnId(column_id);
		userPermission.setCreatetime(DateUtils.dateToString(new Date(),
				"yyyy-MM-dd HH:mm:ss"));
		userPermission.setKnowledgeId(knowledgeid);
		userPermission.setMento(shareMessage);
		userPermission.setSendUserId(send_uid);
		userPermission.setTitle(title);
		userPermission.setDesc(desc);
		userPermission.setPicPath(picPath);
		userPermission.setTags(tags);
		mongoTemplate.insert(userPermission);

		return v;
	}

	@Override
	public int deleteUserPermission(long[] knowledgeids, long userid) {

		return userPermissionValueMapper.delete(knowledgeids, userid);
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

		UserPermissionExample example = new UserPermissionExample();
		com.ginkgocap.ywxt.knowledge.entity.UserPermissionExample.Criteria criteria = example
				.createCriteria();
		criteria.andKnowledgeIdEqualTo(knowledgeid);
		criteria.andSendUserIdEqualTo(userid);
		return userPermissionMapper.deleteByExample(example);
	}

	@Override
	public Map<String, Object> getMyShare(Long userId, int start, int pageSize) {
		List<UserPermissionMongo> lt = null;
		PageUtil page = null;
		Criteria c = Criteria.where("sendUserId").is(userId);
		Query query = new Query(c);
		long count = mongoTemplate.count(query, UserPermissionMongo.class);
		page = new PageUtil((int) count, start - 1, pageSize);

		query = new Query(c);

		query.sort().on("createtime", Order.DESCENDING);
		if (pageSize > 0) {
			query.skip((start - 1) * pageSize);
			query.limit(pageSize);
		}
		lt = mongoTemplate.find(query, UserPermissionMongo.class);
		if (lt == null) {
			lt = new ArrayList<UserPermissionMongo>();
		}
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("pageUtil", page);
		returnMap.put("list", lt);

		return returnMap;
	}

	@Override
	public Map<String, Object> getShareme(Long userId, int start, int pageSize) {
		List<UserPermissionMongo> lt = null;
		PageUtil page = null;
		Criteria c = Criteria.where("receiveUserId").is(userId);
		Query query = new Query(c);
		long count = mongoTemplate.count(query, UserPermissionMongo.class);
		page = new PageUtil((int) count, start, pageSize);

		query = new Query(c);

		query.sort().on("createtime", Order.DESCENDING);
		if (pageSize > 0) {
			query.skip((start - 1) * pageSize);
			query.limit(pageSize);
		}
		lt = mongoTemplate.find(query, UserPermissionMongo.class);
		if (lt == null) {
			lt = new ArrayList<UserPermissionMongo>();
		}
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("pageUtil", page);
		returnMap.put("list", lt);

		return returnMap;
	}

	@Override
	public List<UserPermission> selectUserPermission(long knowledgeid,
			long userid) {

		UserPermissionExample example = new UserPermissionExample();
		com.ginkgocap.ywxt.knowledge.entity.UserPermissionExample.Criteria criteria = example
				.createCriteria();
		criteria.andKnowledgeIdEqualTo(knowledgeid);
		criteria.andSendUserIdEqualTo(userid);
		return userPermissionMapper.selectByExample(example);
	}

	@Override
	public int deleteUserPermission(long knowledgeid) {

		UserPermissionExample example = new UserPermissionExample();
		com.ginkgocap.ywxt.knowledge.entity.UserPermissionExample.Criteria criteria = example
				.createCriteria();
		criteria.andKnowledgeIdEqualTo(knowledgeid);
		return userPermissionMapper.deleteByExample(example);
	}

	@Override
	public void insertUserShare(List<String> permList, long kId,
			KnowledgeNewsVO vo, User user) {

		// 用户ID集合
		List<Long> receiveList = new ArrayList<Long>();
		// 用户权限集合
		for (String perm : permList) {
			// 2:1,2,3,4
			String[] perInfo = perm.split(":");
			if (perInfo != null && perInfo.length == 2) {
				String perType = perInfo[0];
				String perUser = perInfo[1].substring(1,
						perInfo[1].length() - 1);
				if (perInfo != null && perInfo.length > 0
						&& Integer.parseInt(perType) == 2) {
					String[] userList = perUser.split(split);
					for (String userId : userList) {
						if (Integer.parseInt(userId.trim()) == -1) {
							receiveList.add(Long.parseLong(userId));
							insertUserPermissionMongo(receiveList,
									vo.getTitle(), vo.getShareMessage(),
									vo.getPic(), vo.getTags(), user.getId(),
									vo.getShareMessage(),
									Short.parseShort(vo.getColumnType()),
									Long.parseLong(vo.getColumnid()), kId);
						}
					}

				}
			}
		}
	}

	@Override
	public boolean insertUserPermissionMongo(List<Long> receiveList,
			String title, String desc, String picPath, String tags,
			long send_uid, String mento, short column_type, long column_id,
			long knowledgeid) {
		UserPermissionMongo userPermission = new UserPermissionMongo();
		StringBuffer sb = new StringBuffer();

		for (Long uid : receiveList) {
			User user = userService.selectByPrimaryKey(uid);
			if (user != null) {
				sb.append(user.getName());
				sb.append(split);
			}
		}
		if (sb.length() > 0) {
			sb = sb.deleteCharAt(sb.length() - 1);
		}
		User user = userService.selectByPrimaryKey(send_uid);
		userPermission.setSendUserName(user.getName());
		userPermission.setReceiveUserId(receiveList);
		userPermission.setReceiveName(sb.toString());
		userPermission.setColumnId(column_id);
		userPermission.setColumnType(column_type);
		userPermission.setColumnId(column_id);
		userPermission.setCreatetime(DateUtils.dateToString(new Date(),
				"yyyy-MM-dd HH:mm:ss"));
		userPermission.setKnowledgeId(knowledgeid);
		userPermission.setMento(mento);
		userPermission.setSendUserId(send_uid);
		userPermission.setTitle(title);
		userPermission.setDesc(desc);
		userPermission.setPicPath(picPath);
		userPermission.setTags(tags);
		mongoTemplate.insert(userPermission);
		return false;
	}

}
