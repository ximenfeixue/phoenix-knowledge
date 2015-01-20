package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ginkgocap.ywxt.knowledge.dao.userpermission.UserPermissionDAO;
import com.ginkgocap.ywxt.knowledge.entity.UserPermission;
import com.ginkgocap.ywxt.knowledge.entity.UserPermissionExample;
import com.ginkgocap.ywxt.knowledge.mapper.UserPermissionMapper;
import com.ginkgocap.ywxt.knowledge.mapper.UserPermissionValueMapper;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNewsVO;
import com.ginkgocap.ywxt.knowledge.model.UserPermissionMongo;
import com.ginkgocap.ywxt.knowledge.service.UserPermissionService;
import com.ginkgocap.ywxt.knowledge.util.Constants;
import com.ginkgocap.ywxt.knowledge.util.HtmlToText;
import com.ginkgocap.ywxt.user.model.User;
import com.ginkgocap.ywxt.user.service.FriendsRelationService;
import com.ginkgocap.ywxt.user.service.UserService;
import com.ginkgocap.ywxt.util.MakePrimaryKey;
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
	@Resource
	private FriendsRelationService friendsRelationService;

	private final static String split = ",";

	@Override
	public List<Long> selectByreceive_user_id(long receive_user_id,
			long send_userid) {

		return userPermissionDAO.selectByreceive_user_id(receive_user_id,
				send_userid);
	}

	@Override
	@Transactional
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
						if (StringUtils.isNotBlank(userId)) {
							userPermission = new UserPermission();
							userPermission.setReceiveUserId(Long
									.parseLong(userId.trim()));
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
	@Transactional
	public int deleteUserPermission(long knowledgeid, long userid) {

		UserPermissionExample example = new UserPermissionExample();
		com.ginkgocap.ywxt.knowledge.entity.UserPermissionExample.Criteria criteria = example
				.createCriteria();
		criteria.andKnowledgeIdEqualTo(knowledgeid);
		criteria.andSendUserIdEqualTo(userid);
		return userPermissionMapper.deleteByExample(example);
	}

	@Override
	public Map<String, Object> getMyShare(Long userId, String title, int start,
			int pageSize) {
		List<UserPermissionMongo> lt = null;
		PageUtil page = null;
		Criteria c = Criteria.where("sendUserId").is(userId);
		if (!"".equals(title)) {
			Pattern pattern = Pattern.compile("^.*" + title + ".*$");
			c.and("title").regex(pattern);
		}
		Query query = new Query(c);
		long count = mongoTemplate.count(query, UserPermissionMongo.class);

		query = new Query(c);

		query.sort().on("createtime", Order.DESCENDING);
		if (pageSize > 0) {
			page = new PageUtil((int) count, start - 1, pageSize);
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
	public Map<String, Object> getShareme(Long userId, String title, int start,
			int pageSize) {
		List<UserPermissionMongo> lt = null;
		PageUtil page = null;
		Criteria c = Criteria.where("receiveUserId").is(userId);
		if (!"".equals(title)) {
			Pattern pattern = Pattern.compile("^.*" + title + ".*$");
			c.and("title").regex(pattern);
		}
		Query query = new Query(c);
		long count = mongoTemplate.count(query, UserPermissionMongo.class);

		query = new Query(c);

		query.sort().on("createtime", Order.DESCENDING);
		if (pageSize > 0) {
			page = new PageUtil((int) count, start, pageSize);
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
						if (StringUtils.isNotBlank(userId)) {

							receiveList.add(Long.parseLong(userId.trim()));
						}
					}
					insertUserPermissionMongo(receiveList, vo, user);
				}
			}
		}
	}

	@Override
	public boolean insertUserPermissionMongo(List<Long> receiveList,
			KnowledgeNewsVO vo, User user) {
		Criteria c = Criteria.where("sendUserId").is(user.getId());
		c.and("knowledgeId").is(vo.getkId());
		Query query = new Query(c);
		mongoTemplate.remove(query, UserPermission.class);
		UserPermissionMongo userPermission = new UserPermissionMongo();
		StringBuffer sb = new StringBuffer();

		for (Long uid : receiveList) {
			User shareUser = userService.selectByPrimaryKey(uid);
			if (shareUser != null) {
				sb.append(shareUser.getName());
				sb.append(split);
			}
		}
		if (sb.length() > 0) {
			sb = sb.deleteCharAt(sb.length() - 1);
		}
		String id = MakePrimaryKey.getPrimaryKey();
		userPermission.setId(id);
		userPermission.setSendUserName(user.getName());
		userPermission.setReceiveUserId(receiveList);
		userPermission.setReceiveName(sb.toString());
		userPermission.setColumnId(Long.parseLong(vo.getColumnid()));
		userPermission.setColumnType(Short.parseShort(vo.getColumnType()));
		userPermission.setColumnId(Long.parseLong(vo.getColumnid()));
		userPermission.setCreatetime(DateUtils.dateToString(new Date(),
				"yyyy-MM-dd HH:mm:ss"));
		userPermission.setKnowledgeId(vo.getkId());
		userPermission.setMento(vo.getShareMessage());
		userPermission.setSendUserId(user.getId());
		userPermission.setTitle(vo.getTitle());
		String columnType = vo.getColumnType();
		String desc = vo.getDesc();
		// 判断如果是投融工具 行业 案例 则将简介插入，否则正文中截取90个字符后插入到我的分享的简介中
		if (columnType.equals(Constants.Type.Investment.v() + "")
				|| columnType.equals(Constants.Type.Industry.v() + "")
				|| columnType.equals(Constants.Type.Case.v() + "")) {
			if (desc != null && desc.length() > 0) {
				desc = HtmlToText.html2Text(desc);
				if (StringUtils.isNotBlank(desc)) {
					desc = desc.length() > 50 ? desc.substring(0, 50) + "..."
							: desc;
				} else {
					desc = "";
				}
				userPermission.setDesc(desc);
			} else {
				userPermission.setDesc("");
			}
		} else {
			String content = vo.getContent();
			if (content == null) {
				content = "";
			} else {
				content = HtmlToText.html2Text(content);
				if (StringUtils.isNotBlank(content)) {
					content = content.length() > 50 ? content.substring(0, 50)
							+ "..." : content;
				} else {
					content = "";
				}
			}
			userPermission.setDesc(content);
		}
		if (vo != null && vo.getPic() != null && !"".equals(vo.getPic())) {
			userPermission.setPicPath(vo.getPic());
		} else {
			userPermission.setPicPath("");
		}
		userPermission.setTags(vo.getTags());
		mongoTemplate.insert(userPermission);
		return false;
	}

	@Override
	public boolean deleteMyShare(String ids) {
		String id[] = ids.split(",");
		for (int i = 0; i < id.length; i++) {
			Criteria c = Criteria.where("id").is(id[i]);
			Query query = new Query(c);
			mongoTemplate.findAndRemove(query, UserPermissionMongo.class);
		}
		return true;
	}

	@Override
	public boolean deleteShareMe(String ids, Long userId) {
		String id[] = ids.split(",");
		User user = userService.selectByPrimaryKey(userId);
		String name = user.getName();
		for (int i = 0; i < id.length; i++) {
			Criteria c = Criteria.where("id").is(id[i]);
			Query query = new Query(c);
			UserPermissionMongo upm = mongoTemplate.findOne(query,
					UserPermissionMongo.class);
			List<Long> recivedId = upm.getReceiveUserId();
			recivedId.remove(userId);
			upm.setReceiveUserId(recivedId);
			String recivedName = upm.getReceiveName();
			if (recivedName.indexOf(name) == 0) {
				recivedName = recivedName.replace(name + ",", "");
			} else {
				recivedName = recivedName.replace("," + name, "");
			}
			upm.setReceiveName(recivedName);
			mongoTemplate.save(upm);
		}

		return false;
	}

	@Override
	public boolean checkUserSource(List<String> permList) {
		// 用户权限集合
		for (String perm : permList) {
			// 2:1,2,3,4
			String[] perInfo = perm.split(":");
			if (perInfo != null && perInfo.length == 2) {
				String perType = perInfo[0];
				String perUser = perInfo[1].substring(1,
						perInfo[1].length() - 1);
				if (perInfo != null
						&& perInfo.length > 0
						&& (Integer.parseInt(perType) == 2 || Integer
								.parseInt(perType) == 3)) {
					String[] userList = perUser.split(split);
					for (String userId : userList) {
						if (StringUtils.isNotBlank(userId)) {
							if (Integer.parseInt(userId.trim()) == -1
									|| Integer.parseInt(userId.trim()) == 0) {
								return true;
							}
						}
					}

				}
			}
		}
		return false;
	}

	@Override
	public boolean isPublicForMe(User user, Long targetId) {
		if (user == null) {
			return false;
		} else {
			List<UserPermissionMongo> lt = null;
			PageUtil page = null;
			Criteria c = Criteria.where("receiveUserId").is(-1)
					.and("knowledgeId").is(targetId);
			Query query = new Query(c);
			long count = mongoTemplate.count(query, UserPermissionMongo.class);
			if (count > 0) {
				return true;
			} else {
				c = new Criteria();
				c = Criteria.where("receiveUserId").is(user.getId())
						.and("knowledgeId").is(targetId);
				Query query1 = new Query(c);
				count = mongoTemplate.count(query1, UserPermissionMongo.class);
				if (count > 0) {
					return true;
				} else {
					return false;
				}
			}
		}
	}

	@Override
	public List<String> userPermissionAll(String selectedIds, User user) {
		Map<Integer, Object> map = new HashMap<Integer, Object>();
		List<String> perList = null;
		List<User> list = null;
		JSONObject j = JSONObject.fromObject(selectedIds);
		String dales = j.get(Constants.PermissionType.dales.c()).toString();
		perList = new ArrayList<String>();
		if (StringUtils.equals(dales, "-9")) {
			list = friendsRelationService.findAllFriends(user.getId(), 0l, "",
					"", 1l, 99999);
			if (list != null && list.size() > 0) {
				for (User user2 : list) {
					perList.add(user2.getId() + "");
				}
			}
		}
		map.put(Constants.PermissionType.dales.v(), perList);
		perList = null;

		String zhongles = j.get(Constants.PermissionType.zhongles.c())
				.toString();
		perList = new ArrayList<String>();
		if (StringUtils.equals(zhongles, "-9")) {
			list = friendsRelationService.findAllFriends(user.getId(), 0, "",
					"", 1, 99999);
			if (list != null && list.size() > 0) {
				for (User user2 : list) {
					perList.add(user2.getId() + "");
				}
			}
		}
		map.put(Constants.PermissionType.zhongles.v(), perList);
		perList = null;

		String xiaoles = j.get(Constants.PermissionType.xiaoles.c()).toString();
		perList = new ArrayList<String>();
		if (StringUtils.equals(xiaoles, "-9")) {
			list = friendsRelationService.findAllFriends(user.getId(), 0, "",
					"", 1l, 99999);
			if (list != null && list.size() > 0) {
				for (User user2 : list) {
					perList.add(user2.getId() + "");
				}
			}
		}
		map.put(Constants.PermissionType.xiaoles.v(), perList);
		perList = null;

		List<String> permList = new ArrayList<String>();
		Set<Integer> set = map.keySet();
		Iterator<Integer> iterator = set.iterator();

		StringBuffer sb = new StringBuffer();
		while (iterator.hasNext()) {
			Integer key = iterator.next();
			sb = new StringBuffer();
			sb.append(key);
			sb.append(":");
			sb.append(map.get(key));
			permList.add(sb.toString());
			sb = null;
		}

		return permList;
	}

}
