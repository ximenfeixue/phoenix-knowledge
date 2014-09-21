package com.ginkgocap.ywxt.knowledge.service.knowledge.impl;

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
import com.ginkgocap.ywxt.knowledge.model.Article;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeShare;
import com.ginkgocap.ywxt.knowledge.service.knowledge.ArticleService;
import com.ginkgocap.ywxt.knowledge.service.knowledge.KnowledgeShareService;
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
	private ArticleService articleService;
	@Autowired
	private UserService userService;
	
	private Logger logger = LoggerFactory.getLogger(KnowledgeShareServiceImpl.class) ;
	
	@Override
	public KnowledgeShare save(long userId, long knowledgeId, String receiverId, String receiverName) {
		KnowledgeShare knowledgeShare = new KnowledgeShare();
		knowledgeShare.setKnowledgeId(knowledgeId);
		knowledgeShare.setUserId(userId);
		knowledgeShare.setTitle(articleService.selectByPrimaryKey(knowledgeId).getArticleTitle());
		List<Long> recId = new ArrayList<Long>();//接收人id 
		List<String> recName = new ArrayList<String>();//接受人name
		List<Friends> friendsList = new ArrayList<Friends>();
		logger.info("[ KnowledgeShareServiceImpl ] get receiverId is " + receiverId);
		logger.info("[ KnowledgeShareServiceImpl ] get receiverName is " + receiverName);
		if(StringUtils.isNotBlank(receiverId) && StringUtils.isNotBlank(receiverName)){//选择出来的好友
			String ids[] =StringUtils.split(receiverId, ",");
			String names[] = StringUtils.split(receiverName, ",");
			if(ids.length==names.length){
				for(int i=0; i<ids.length;i++){
					recId.add(Long.valueOf(ids[i]));
					recName.add(names[i]);
					Friends friends = new Friends();
					friends.setUserId(Long.valueOf(ids[i]));
					friends.setStatus(1);
					friendsList.add(friends);
				}
			}else{
				logger.debug("[ KnowledgeShareServiceImpl ] get receiverId length not equals receiverName length");
			}
			
		}else{//全部好友
			List<User> friends = friendsRelationService.findAllFriendsByUserId(userId);
			if(friends!=null && friends.size()>0){
				for(User friend : friends){
					recId.add(friend.getId());
					recName.add(friend.getName());
					Friends f = new Friends();
					f.setUserId(friend.getId());
					f.setStatus(1);
					friendsList.add(f);
				}
			}else{
				logger.debug("[ KnowledgeShareServiceImpl ] get friends is null");
			}
		}
		knowledgeShare.setFriends(friendsList);
		knowledgeShare.setReceiverId(recId);
		knowledgeShare.setReceiverName(recName);
		knowledgeShare = knowledgeShareDao.save(knowledgeShare);
		if(knowledgeShare == null){
			logger.debug("[ KnowledgeShareServiceImpl ] add knowledgeShare is fail");
			return null;
		}else{
			return knowledgeShare;
		}
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
		if(list!=null && list.size()>0){
			for(KnowledgeShare ks : list){
				long id = ks.getKnowledgeId();
				Article article = articleService.selectByPrimaryKey(id);
				User user =userService.selectByPrimaryKey(ks.getUserId());
				if(article!=null){
					ks.setArticle(article);
					ks.setUserName(user.getName());
				}
			}
		}else{
			logger.debug("[ KnowledgeShareServiceImpl ] set Article is fail, because list is null");
		}
		return list;
	}

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
