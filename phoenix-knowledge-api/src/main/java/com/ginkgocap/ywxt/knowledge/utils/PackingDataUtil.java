package com.ginkgocap.ywxt.knowledge.utils;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeBase;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeMongo;
import com.ginkgocap.ywxt.user.form.EtUserInfo;
import com.ginkgocap.ywxt.user.form.ReceiversInfo;
import com.ginkgocap.ywxt.user.model.User;
import com.ginkgocap.ywxt.user.model.UserFeed;
import com.ginkgocap.ywxt.user.service.DiaryService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Title: 数据包装转换类
 * @date 2016年1月14日 下午4:28:19
 * @version V1.0.0
 */
public class PackingDataUtil {
	
	/**
	 * 包装发送给MQ的数据
	 * @date 2016年1月14日 下午4:33:26
	 * @param base
	 * @param userId
	 * @return
	 */
	public static String packingSendBigData(KnowledgeBase base, long userId) {
		
		JSONObject json = new JSONObject();
		json.put("kid", base.getId());
		json.put("cid", userId);
		json.put("cname", base.getCreateUserName());
		json.put("title", base.getTitle());
		json.put("cpathid", /*knowledgeMongo.getColumnId()*/"");
		json.put("pic", base.getCoverPic());
		json.put("selectedIds", /*base.getSelectedIds()*/"");
		json.put("status", base.getStatus());
		json.put("tags", /*base.getTags()*/"");
		json.put("columnid", base.getColumnId());
		json.put("columnType", base.getType());
		json.put("content", base.getContent());
		json.put("desc", base.getContentDesc());
		json.put("createtime", base.getCreateDate());
		
		return json.toString();
		
	}
	
	/**
	 * 动态推送数据包装
	 * @date 2016年1月14日 下午5:04:52
	 * @param base
	 * @param user
	 * @param diaryService
	 * @return
	 */
	public static UserFeed packingSendFeedData(KnowledgeBase base,User user,DiaryService diaryService) {
		if (base.getColumnId() == 8) {
			UserFeed feed = new UserFeed();
			feed.setContent(base.getContent());
			feed.setCreatedBy(user.getName());
			feed.setCreatedById(user.getId());
            String cTime = DateUtil.formatWithYYYYMMDDHHMMSS(new Date(base.getCreateDate()));
			feed.setCtime(cTime);
			feed.setGroupName("仅好友可见");
			feed.setScope(1);// 设置可见级别
			feed.setGroupName("");
			feed.setTargetId(base.getId());
			feed.setTitle(base.getTitle());
			feed.setType(1);
			feed.setImgPath("");// 长观点地址
			feed.setDelstatus(0);// 删除状态
			List<ReceiversInfo> receivers = diaryService.getReceiversInfo(base.getContent(), -1, user.getId());
			feed.setReceivers(receivers);// 获取接收人信息
			feed.setDiaryType(1);
			List<EtUserInfo> etInfo = new ArrayList<EtUserInfo>();// 被@的信息
			feed.setEtInfo(etInfo);
			return feed;
		} else {
			return null;
		}
	}
	

	/**
	 * @date 2016年1月18日 上午10:54:14
	 * @param str
	 * @return
	 */
	public static List<JSONObject> getRecommendResult(String str) {
		List<JSONObject> maps = new ArrayList<JSONObject>();
		if (str != null && str.length() > 0) {
			JSONObject jo = JSONObject.fromObject(str);
			JSONArray jas = JSONArray.fromObject(jo.get("knos"));
			if (jas != null) {
				JSONObject sr = null;
				for (int i = 0; i < jas.size(); i++) {
					JSONObject ob = (JSONObject) jas.get(i);
					sr = new JSONObject();
					sr.put("title",ob.optString("name"));// 名称
					sr.put("id",ob.getString("id"));
					sr.put("knowledgeType",ob.optString("knoType"));
					sr.put("tagsScores",ob.optString("tagsScores"));
					sr.put("tags",ob.optString("tags"));
					sr.put("desc",ob.optString("desc"));
					maps.add(sr);
				}
			}
		}
		return maps;
	}
	
	/**
	 * 将文本包装为HTML
	 * @date 2016年1月18日 下午2:41:06
	 * @param html
	 * @return
	 */
	public static String getSpHtmlString(String html) {
		Pattern pattern = Pattern.compile("(?isu)<body[^>]*>(.*)</body>");
		Matcher matcher = pattern.matcher(html);
		String body = null;
		StringBuffer htmlsb = new StringBuffer(
				"<!DOCTYPE html><html><head><meta charset='utf-8' /><style>.gtrelated img{margin-top:10px;max-width:96%;margin-left:2%;height:auto;}.gtrelated{word-break: break-all;word-wrap: break-word; overflow-x: hidden; overflow-y:auto; } body { letter-spacing: 0.1em; line-height: 1.5em;} table{ width:100%; border-top: #bbb solid 1px;border-left: #bbb solid 1px; text-align: center;}table td{ border-right: #bbb solid 1px; border-bottom: #bbb solid 1px;} </style></head><body><div class='gtrelated'>");
		if (matcher.find()) {
			body = matcher.group(1);
			htmlsb.append(body);
		} else {
			htmlsb.append(html);
		}
		htmlsb.append("</div></body></html>");
		html = htmlsb.toString();
		return html;
	}
	
}