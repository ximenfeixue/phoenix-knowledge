package com.ginkgocap.ywxt.knowledge.utils;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeBase;
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
	public static String packingSendBigData(KnowledgeBase base, long userId)
	{
		JSONObject json = new JSONObject();
		json.put("kid", base.getId());
		json.put("cid", userId);
		json.put("cname", base.getCreateUserName());
		json.put("title", base.getTitle());
		json.put("cpathid", base.getCpath());
		json.put("pic", base.getCoverPic());
		json.put("publicFlag", base.getPrivated() == 1 ? 0 : 1);
		json.put("status", base.getStatus());
		json.put("tags", base.getTags());
		json.put("columnid", base.getColumnId());
		json.put("columnType", base.getType());
		json.put("content", base.getContent());
		json.put("desc", base.getContentDesc());
		json.put("createtime", base.getCreateDate());
		
		return json.toString();
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
}