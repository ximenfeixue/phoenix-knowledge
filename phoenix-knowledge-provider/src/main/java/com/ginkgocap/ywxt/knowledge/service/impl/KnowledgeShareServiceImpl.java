package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.dao.share.KnowledgeShareDao;
import com.ginkgocap.ywxt.knowledge.form.Friends; 
import com.ginkgocap.ywxt.knowledge.model.KnowledgeShare; 
import com.ginkgocap.ywxt.knowledge.service.KnowledgeShareService;
import com.ginkgocap.ywxt.user.model.User;
import com.ginkgocap.ywxt.user.service.FriendsRelationService;
import com.ginkgocap.ywxt.user.service.UserService;
import com.ginkgocap.ywxt.util.PageUtil;

@Service("knowledgeShareService")
public class KnowledgeShareServiceImpl implements KnowledgeShareService {

	@Autowired
	private KnowledgeShareDao knowledgeShareDao;
	@Autowired
	private FriendsRelationService friendsRelationService;
 
	@Autowired
	private UserService userService;
	
	private Logger logger = LoggerFactory.getLogger(KnowledgeShareServiceImpl.class) ;
	
	@Override
	public KnowledgeShare save(long userId, long knowledgeId, String receiverId, String receiverName) {
        return null;
	
	}

	@Override
	public Map<String, Object> findMyShare(long userId, int currentPage, int pageSize, String title) {
		Map<String,Object> map = new HashMap<String,Object>();
		int count = knowledgeShareDao.findMyShareCount(userId, title);
		PageUtil page = new PageUtil(count, currentPage, pageSize);
		List<KnowledgeShare> list = knowledgeShareDao.findMyShare(userId, page.getPageStartRow()-1, pageSize, title);
		list = this.setKnowledgeShareInfo(list);
		map.put("page", page);
		map.put("list", list);
		return map;
	}

	@Override
	public Map<String, Object> findShareMe(long userId, int currentPage, int pageSize, String title) {
		Map<String,Object> map = new HashMap<String,Object>();
		int count = knowledgeShareDao.findShareMeCount(userId, title);
		PageUtil page = new PageUtil(count, currentPage, pageSize);
		List<KnowledgeShare> list = knowledgeShareDao.findShareMe(userId, page.getPageStartRow()-1, pageSize, title);
		list = this.setKnowledgeShareInfo(list);
		map.put("page", page);
		map.put("list", list);
		return map;
	}
	/**
	 * 给分享的知识赋值
	 * @param list
	 * @return List<KnowledgeShare>
	 */
	private List<KnowledgeShare> setKnowledgeShareInfo(List<KnowledgeShare> list){
        return list;}

	@Override
	public void deleteShareInfoByKnowledgeId(long knowledgeId) {
		knowledgeShareDao.deleteShareInfoByKnowledgeId(knowledgeId);
	}

	@Override
	public KnowledgeShare findMyShareOne(long userId, long knowledgeId) {
		return knowledgeShareDao.findMyShareOne(userId, knowledgeId);
	}

	@Override
	public KnowledgeShare findShareMeOne(long userId, long knowledgeId) {
		return knowledgeShareDao.findShareMeOne(userId, knowledgeId);
	}

	@Override
	public void updateTitle(long userId, long knowledgeId, String title) {
		knowledgeShareDao.updateMyShareTitle(userId, knowledgeId, title);
		knowledgeShareDao.updateShareMeTitle(userId, knowledgeId, title);
	}


	@Override
	public void deleteMyShare(long userId, long knowledgeId) {
		knowledgeShareDao.deleteMyShare(userId, knowledgeId);
	}

	@Override
	public void deleteShareMe(long userId, long knowledgeId) {
		knowledgeShareDao.deleteShareMe(userId, knowledgeId);
	}
}
