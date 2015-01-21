package com.ginkgocap.ywxt.knowledge.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonUtil {
	private static Logger logger = LoggerFactory.getLogger(JsonUtil.class);

	public static Boolean checkKnowledgePermission(String str) {
		boolean flag = false;
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode rootNode = mapper.readTree(str);
			boolean dule = rootNode.get(Constants.dule).asBoolean();
			if (dule) {
				flag = true;
			}
		} catch (Exception e) {
			return null;
		}

		return flag;
	}

	public static Map<Integer, Object> getPermissionMap(String userPermissionStr) {
		logger.info("开始解析权限字符串,字符串:{}", userPermissionStr);
		Map<Integer, Object> result = new HashMap<Integer, Object>();
		JSONObject j = JSONObject.fromObject(userPermissionStr);
		JSONArray jsons = null;
		List<String> perList = null;
		JSONObject job = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode rootNode = mapper.readTree(userPermissionStr);
			boolean dule = rootNode.get(Constants.dule).asBoolean();
			result.put(Constants.PermissionType.dule.v(), dule);
			if (!dule) {
				String dales = j.get(Constants.PermissionType.dales.c())
						.toString();
				perList = new ArrayList<String>();
				jsons = JSONArray.fromObject(dales);
				for (int i = 0; i < jsons.size(); i++) {
					job = jsons.getJSONObject(i); // 遍历 jsonarray
					perList.add(job.get("id") + "");
				}
				result.put(Constants.PermissionType.dales.v(), perList);
				perList = null;

				String zhongles = j.get(Constants.PermissionType.zhongles.c())
						.toString();
				perList = new ArrayList<String>();
				jsons = JSONArray.fromObject(zhongles);
				for (int i = 0; i < jsons.size(); i++) {
					job = jsons.getJSONObject(i); // 遍历 jsonarray
					perList.add(job.get("id") + "");
				}
				result.put(Constants.PermissionType.zhongles.v(), perList);
				perList = null;

				String xiaoles = j.get(Constants.PermissionType.xiaoles.c())
						.toString();
				perList = new ArrayList<String>();
				jsons = JSONArray.fromObject(xiaoles);
				for (int i = 0; i < jsons.size(); i++) {
					job = jsons.getJSONObject(i); // 遍历 jsonarray
					perList.add(job.get("id") + "");
				}
				result.put(Constants.PermissionType.xiaoles.v(), perList);
				perList = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("解析权限字符串失败,字符串:{}", userPermissionStr);
			return result;
		}

		return result;
	}

	public static List<String> parseTags(String tags) {
		List<String> tagList = null;
		if (StringUtils.isNotBlank(tags)) {
			tagList = new ArrayList<String>();
			String[] tag = tags.split(" ");
			for (String t : tag) {
				tagList.add(t);
			}
		}
		return tagList;
	}

	public static List<String> getPermissionList(String perIds) {
		Map<Integer, Object> perResult = getPermissionMap(perIds);

		List<String> permList = new ArrayList<String>();
		Set<Integer> set = perResult.keySet();
		Iterator<Integer> iterator = set.iterator();

		StringBuffer sb = new StringBuffer();
		while (iterator.hasNext()) {
			Integer key = iterator.next();
			if (key == Constants.PermissionType.dule.v()) {
				continue;
			}
			sb = new StringBuffer();
			sb.append(key);
			sb.append(":");
			sb.append(perResult.get(key));
			permList.add(sb.toString());
			sb = null;
		}
		return permList;
	}

	public static boolean isAllPermission(String selectedIds) {

		boolean flag = true;
		JSONObject j = JSONObject.fromObject(selectedIds);

		String learray[] = { Constants.PermissionType.dales.c(),
				Constants.PermissionType.zhongles.c(),
				Constants.PermissionType.xiaoles.c() };
		for (int i = 0; i < learray.length; i++) {
			if (StringUtils.equals(j.get(learray[i]).toString(), "[]")) {
				continue;
			}
			String jsonstr = j.get(learray[i]).toString();
			if (StringUtils.equals(jsonstr, "-9")) {
				flag = false;
			}
		}

		return flag;
	}
	
	public static boolean isDude(String selectedIds) {

		boolean flag = true;
		JSONObject j = JSONObject.fromObject(selectedIds);

		String learray[] = { Constants.PermissionType.dales.c(),
				Constants.PermissionType.zhongles.c(),
				Constants.PermissionType.xiaoles.c() };
		for (int i = 0; i < learray.length; i++) {
			if (StringUtils.equals(j.get(learray[i]).toString(), "[]")) {
				continue;
			}
			String jsonstr = j.get(learray[i]).toString();
			if (StringUtils.equals(jsonstr, "-9")) {
				flag = false;
			}
		}

		return flag;
	}

	public static void main(String[] args) {
		// String[] testStr = { "23", "33", "44" };
		// long[] longs = KnowledgeUtil.convertionToLong(testStr);
		// System.out.println("打印longs这个long数组对象：" + longs);
		// for (long longN : longs) {
		// System.out.println(longN);
		//
		// }

		// String str =
		// "{\"dule\":false,\"dales\":[1,2,3],\"xiaoles\":[34,7],\"zhongles\":[4]}";
		// KnowledgeUtil.getPermissionList(str);
	}
}
