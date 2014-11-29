package com.ginkgocap.ywxt.knowledge.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import com.ginkgocap.ywxt.knowledge.util.Constants;

/**
 * 把string 数组转换Long数组
 * 
 * @author Administrator
 * 
 */
public class KnowledgeUtil {

	private final static String split = ",";

	public static long[] formatString(String str) {
		String[] strs = str.split(split);
		return convertionToLong(strs);
	}

	public static long[] formatString(String str, int begin, int end) {
		String[] strs = str.substring(begin, end).split(split);
		return convertionToLong(strs);
	}

	public static long[] convertionToLong(String[] strs) {// 将String数组转换为Long类型数组
		long[] longs = new long[strs.length]; // 声明long类型的数组
		for (int i = 0; i < strs.length; i++) {
			String str = strs[i]; // 将strs字符串数组中的第i个值赋值给str
			long thelong = Long.valueOf(str);// 将str转换为long类型，并赋值给thelong
			longs[i] = thelong;// 将thelong赋值给 longs数组中对应的地方
		}

		return longs; // 返回long数组
	}

	public static void main(String[] args) {
		// String[] testStr = { "23", "33", "44" };
		// long[] longs = KnowledgeUtil.convertionToLong(testStr);
		// System.out.println("打印longs这个long数组对象：" + longs);
		// for (long longN : longs) {
		// System.out.println(longN);
		//
		// }

		String str = "{\"dule\":false,\"dales\":[1,2,3],\"xiaoles\":[34,7],\"zhongles\":[4]}";
		KnowledgeUtil.getPermissionList(str);
	}

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
		Map<Integer, Object> result = new HashMap<Integer, Object>();
		List<String> perList = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode rootNode = mapper.readTree(userPermissionStr);
			boolean dule = rootNode.get(Constants.dule).asBoolean();
			result.put(Constants.PermissionType.dule.v(), dule);
			if (!dule) {
				JsonNode dalesNodes = rootNode
						.get(Constants.PermissionType.dales.c());
				if (dalesNodes.size() > 0) {
					perList = new ArrayList<String>();
					for (JsonNode daleNode : dalesNodes) {
						perList.add(daleNode.asText());
					}

					result.put(Constants.PermissionType.dales.v(), perList);
					perList = null;
				}
				JsonNode zhongleNodes = rootNode
						.get(Constants.PermissionType.zhongles.c());
				if (zhongleNodes.size() > 0) {
					perList = new ArrayList<String>();
					for (JsonNode zhongle : zhongleNodes) {
						perList.add(zhongle.asText());
					}
					result.put(Constants.PermissionType.zhongles.v(), perList);
					perList = null;
				}

				JsonNode xiaoles = rootNode
						.get(Constants.PermissionType.xiaoles.c());
				if (xiaoles.size() > 0) {
					perList = new ArrayList<String>();
					for (JsonNode xiaole : xiaoles) {
						perList.add(xiaole.asText());
					}
					result.put(Constants.PermissionType.xiaoles.v(), perList);
					perList = null;
				}
			}
		} catch (Exception e) {
			return null;
		}

		return result;
	}

	public static List<String> parseTags(String tags) {
		List<String> tagList = null;
		if (StringUtils.isNotBlank(tags)) {
			tagList = new ArrayList<String>();
			String[] tag = tags.split(split);
			for (String t : tag) {
				tagList.add(t);
			}
		}
		return tagList;
	}

	public static List<Long> parseIds(String ids) {
		List<Long> idList = null;
		if (StringUtils.isNotBlank(ids)) {
			idList = new ArrayList<Long>();
			String[] idsArr = ids.split(split);
			for (String id : idsArr) {
				idList.add(Long.parseLong(id));
			}
		}
		return idList;
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
}
