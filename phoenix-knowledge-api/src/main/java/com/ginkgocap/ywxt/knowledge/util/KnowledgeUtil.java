package com.ginkgocap.ywxt.knowledge.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

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
		String[] testStr = { "23", "33", "44" };
		long[] longs = KnowledgeUtil.convertionToLong(testStr);
		System.out.println("打印longs这个long数组对象：" + longs);
		for (long longN : longs) {
			System.out.println(longN);

		}
	}

	public static List<String> getPermissionList(String userPermission) {
		List<String> perList = new ArrayList<String>();
		if (StringUtils.isBlank(userPermission)) {
			return null;
		}
		String[] perm = userPermission.split("&");
		for (String p : perm) {
			perList.add(p);
		}

		return perList;
	}
	
	public static List<String> parseTags(String tags) {
		List<String> tagList = null;
		if (StringUtils.isNotBlank(tags)) {
			tagList = new ArrayList<String>();
			String[] tag = tags.split(",");
			for (String t : tag) {
				tagList.add(t);
			}
		}
		return tagList;
	}
}
