package com.ginkgocap.ywxt.knowledge.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * java获取文件扩展名及文件名
 * */

public class Snippet {
	/**
	 * Java文件操作 获取文件扩展名
	 * 
	 * Author: lk
	 */
	public static String getExtensionName(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot > -1) && (dot < (filename.length() - 1))) {
				return filename.substring(dot + 1);
			}
		}
		return filename;
	}

	/**
	 * Java文件操作 获取不带扩展名的文件名
	 * 
	 * Author: lk
	 */
	public static String getFileNameNoEx(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot > -1) && (dot < (filename.length()))) {
				return filename.substring(0, dot);
			}
		}
		return filename;
	}

	public static String stringFilter(String str) throws PatternSyntaxException {
		String regEx = "[`~!@#$%^&*()+=|{}':;',//[//]<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}

	public static void main(String[] args) {
		System.out.println(Snippet.getExtensionName("asdf.doc.docx"));
		System.out.println(Snippet.getFileNameNoEx("asdf.doc.docx"));
		System.out.println(Snippet.stringFilter("adf/ l . [] /j  () *7  ^%$$#&!@*#($)%)__+_)(*&...dox.asjjpejpig.jda 发大水将阿斯顿 .exe"));
		
	}
}
