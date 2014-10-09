package com.ginkgocap.ywxt.knowledge.util;

/**
 * 把string 数组转换Long数组
 * @author Administrator
 *
 */
public class KnowledgeUtil {

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
}
