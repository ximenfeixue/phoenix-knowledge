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
		str = str.replaceAll("\\s*|\t|\r|\n", "");
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}

	public static String imgFilter(String str) throws PatternSyntaxException {
		String ret = "";
		String regxpForImgTag = "<\\s*[I,i][M,m][G,g]\\s+([^>]*)\\s*>"; // 找出IMG标签 
		Pattern p = Pattern.compile(regxpForImgTag);
		Matcher m = p.matcher(str);
//		ret =  m.replaceAll("").trim();
//		regxpForImgTag = "<\\s*img\\s+([^>]*)\\s*>"; // 找出IMG标签 
//		p = Pattern.compile(regxpForImgTag);
//		m = p.matcher(ret);
//		ret =  m.replaceAll("").trim();
		return m.replaceAll("").trim();
	}
	public static void main(String[] args) {
		System.out.println(Snippet.getExtensionName("asdf.doc.docx"));
		System.out.println(Snippet.getFileNameNoEx("asdf.doc.docx"));
		System.out.println(Snippet.stringFilter("adf/ l . [] /j      () *7  ^%$$#&!@*#($)%)__+_)(*&...dox.asjjpejpig.jda 发大水将阿斯顿 .exe"));
		System.out.println(Snippet.imgFilter("<html><body><div>&nbsp;<IMG SRC=\"/webserver/tomcat/phoenix-knowledge-web/ywxt/webserver/upload/knowledge/GENFILE/GENPATH_10001/HTMLTEMP/136860950536400049_test.html/136860950536400049_test_html_m61be27a3.png\" NAME=\"图片 1\" ALIGN=BOTTOM WIDTH=431 HEIGHT=278 BORDER=0></div><img src='' width=100></body></html>"));
	}
}
