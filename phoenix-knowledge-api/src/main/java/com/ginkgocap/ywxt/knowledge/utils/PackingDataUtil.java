package com.ginkgocap.ywxt.knowledge.utils;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeMongo;
import com.ginkgocap.ywxt.user.form.EtUserInfo;
import com.ginkgocap.ywxt.user.form.ReceiversInfo;
import com.ginkgocap.ywxt.user.model.User;
import com.ginkgocap.ywxt.user.model.UserFeed;
import com.ginkgocap.ywxt.user.service.DiaryService;

/**
 * @Title: 数据包装转换类
 * @author 周仕奇
 * @date 2016年1月14日 下午4:28:19
 * @version V1.0.0
 */
public class PackingDataUtil {
	
	/**
	 * 包装发送给MQ的数据
	 * @author 周仕奇
	 * @date 2016年1月14日 下午4:33:26
	 * @param knowledgeMongo
	 * @param user
	 * @return
	 */
	public static String packingSendBigData(KnowledgeMongo knowledgeMongo,User user) {
		
		JSONObject json = new JSONObject();
		json.put("kid", knowledgeMongo.getId());
		json.put("cid", user.getId());
		json.put("cname", user.getName());
		json.put("title", knowledgeMongo.getTitle());
		json.put("cpathid", /*knowledgeMongo.getColumnId()*/"");
		json.put("pic", knowledgeMongo.getPictureId());
		json.put("selectedIds", /*vo.getSelectedIds()*/"");
		json.put("status", knowledgeMongo.getStatus());
		json.put("tags", /*vo.getTags()*/"");
		json.put("columnid", knowledgeMongo.getColumnId());
		json.put("columnType", /*vo.getColumnType()*/"");
		json.put("content", knowledgeMongo.getContent());
		json.put("desc", knowledgeMongo.getContentDesc());
		json.put("createtime", knowledgeMongo.getCreateDate());
		
		return json.toString();
		
	}
	
	/**
	 * 动态推送数据包装
	 * @author 周仕奇
	 * @date 2016年1月14日 下午5:04:52
	 * @param knowledgeMongo
	 * @param user
	 * @param diaryService
	 * @return
	 */
	public static UserFeed packingSendFeedData(KnowledgeMongo knowledgeMongo,User user,DiaryService diaryService) {
		if (knowledgeMongo.getColumnId() == 8) {
			UserFeed feed = new UserFeed();
			feed.setContent(knowledgeMongo.getContent());
			feed.setCreatedBy(user.getName());
			feed.setCreatedById(user.getId());
			feed.setCtime(knowledgeMongo.getCreateDate());
			feed.setGroupName("仅好友可见");
			feed.setScope(1);// 设置可见级别
			feed.setGroupName("");
			feed.setTargetId(knowledgeMongo.getId());
			feed.setTitle(knowledgeMongo.getTitle());
			feed.setType(1);
			feed.setImgPath("");// 长观点地址
			feed.setDelstatus(0);// 删除状态
			List<ReceiversInfo> receivers = diaryService.getReceiversInfo(knowledgeMongo.getContent(), -1, user.getId());
			feed.setReceivers(receivers);// 获取接收人信息
			feed.setDiaryType(1);
			List<EtUserInfo> etInfo = new ArrayList<EtUserInfo>();// 被@的信息
			feed.setEtInfo(etInfo);
			return feed;
		} else {
			return null;
		}
	}
	
}