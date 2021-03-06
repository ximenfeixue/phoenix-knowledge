package com.ginkgocap.ywxt.knowledge.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;

public class HtmlToText {
	
	/**
	 * html转化为text
	 * @date 2016年1月14日 下午3:57:29
	 * @param inputString
	 * @return
	 */
	public static String html2Text(String inputString) {
		String htmlStr = inputString; // 含html标签的字符串
		String textStr = "";

		try {
            java.util.regex.Pattern p_script;
            java.util.regex.Matcher m_script;
            java.util.regex.Pattern p_style;
            java.util.regex.Matcher m_style;
            java.util.regex.Pattern p_html;
            java.util.regex.Matcher m_html;

			String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; // 定义script的正则表达式{或<script>]*?>[\s\S]*?<\/script>
			// }
			String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; // 定义style的正则表达式{或<style>]*?>[\s\S]*?<\/style>
			// }
			String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式

			htmlStr = htmlStr.replaceAll("&amp;", "&");
			htmlStr = htmlStr.replaceAll("&lt;", "<");
			htmlStr = htmlStr.replaceAll("&gt;", ">");
			htmlStr = htmlStr.replaceAll("&nbsp;", "");
			htmlStr = htmlStr.replaceAll("&quot;", "");
			htmlStr = htmlStr.replaceAll("&emsp;", "");
			htmlStr = htmlStr.replaceAll("&emsp;", "");
			htmlStr = StringUtils.replace(htmlStr, "\n", "");
			htmlStr = StringUtils.replace(htmlStr, "\t", "");
			htmlStr = StringUtils.replace(htmlStr, " ", "");
			htmlStr = htmlStr.replaceAll("<span>", "").replaceAll("</span>", "");

			p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
			m_script = p_script.matcher(htmlStr);
			htmlStr = m_script.replaceAll(""); // 过滤script标签

			p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
			m_style = p_style.matcher(htmlStr);
			htmlStr = m_style.replaceAll(""); // 过滤style标签

			p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(htmlStr);
			htmlStr = m_html.replaceAll(""); // 过滤html标签
			htmlStr = htmlStr.replaceAll("&.[0,4];", "");
			textStr = htmlStr;

		} catch (Exception e) {
			System.err.println("Html2Text: " + e.getMessage());
		}

		return textStr;
	}

	public static String removeUnderLine(String inputString) {
        String htmlStr = inputString; // 含html标签的字符串
        String textStr = "";
		try {
            java.util.regex.Pattern p_script;
            java.util.regex.Matcher m_script;

            htmlStr = htmlStr.replaceAll("&amp;", "&");
            htmlStr = htmlStr.replaceAll("&lt;", "<");
            htmlStr = htmlStr.replaceAll("&gt;", ">");
            htmlStr = htmlStr.replaceAll("&nbsp;", "");
            htmlStr = htmlStr.replaceAll("&quot;", "");
            htmlStr = htmlStr.replaceAll("&emsp;", "");
            htmlStr = htmlStr.replaceAll("&emsp;", "");
            htmlStr = StringUtils.replace(htmlStr, "\n", "");
            htmlStr = StringUtils.replace(htmlStr, "\t", "");
//            htmlStr = StringUtils.replace(htmlStr, " ", "");
//            htmlStr = htmlStr.replaceAll("<span>", "").replaceAll("</span>", "");

            //final String http_regexp = "(<a href=)//([^/:]+)(:\\d*)?([^#\\s]*)";; // 定义script的正则表达式{或<script>]*?>[\s\S]*?<\/script>
            //String http_regexp = "<[\\s]*?style[^>]*?>(<a href=)*?(decoration: underline;)*?>"; // 定义script的正则表达式{或<script>]*?>[\s\S]*?<\/script>
            String regEx_html = "<a href=*(decoration: underline;)*[^>]+>"; // 定义HTML标签的正则表达式

            p_script = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll(""); // 过滤script标签

            htmlStr = htmlStr.replace("text-decoration:underline", "");
            htmlStr = htmlStr.replace("text-decoration: underline", "");
            htmlStr = htmlStr.replace("</p><hr/>", "");
		} catch (Exception e) {
			System.err.println("Html2Text: " + e.getMessage());
		}
		return htmlStr;
	}

    public static String htmlToText(String htmlContent)
    {
		if (htmlContent != null && htmlContent.trim().length() > 5) {
			if (htmlContent.indexOf("html>") >= 0 || htmlContent.indexOf("/html>") > 0
					|| htmlContent.indexOf("<style") >= 0 || htmlContent.indexOf("<div>") >= 0
					|| htmlContent.indexOf("<p>") >= 0 || htmlContent.indexOf("<br>") >= 0
					|| htmlContent.indexOf("<head>") >= 0 || htmlContent.indexOf("http-equiv") >= 0
					|| htmlContent.indexOf("<body>") >= 0 || htmlContent.indexOf("content-type") >= 0
					|| htmlContent.indexOf("http-equiv") >= 0 || htmlContent.indexOf("http-equiv") >= 0
					|| htmlContent.indexOf("h3 style") >= 0 || htmlContent.indexOf("span style") >= 0
					|| htmlContent.indexOf("class=") >= 0 || htmlContent.indexOf("align:center") >= 0
					|| htmlContent.indexOf("line-height") >= 0 || htmlContent.indexOf("font-family") >= 0
					|| htmlContent.indexOf("img src") >= 0 || htmlContent.indexOf("font-family") >= 0) {
				//Document doc = Jsoup.parse(htmlContent);
				//return doc.body().text();
				return html2Text(htmlContent);
			}
		}
        return htmlContent;
    }

	public static String filterHtml(String html) {
		Pattern pattern = Pattern.compile("<style[^>]*?>[\\D\\d]*?<\\/style>");
		Matcher matcher = pattern.matcher(html);
		String htmlStr = matcher.replaceAll("");
		pattern = Pattern.compile("<[^>]+>");
		String filterStr = pattern.matcher(htmlStr).replaceAll("");
		filterStr.replace("&nbsp;", " ");
		return filterStr;
	}

	/**
	 * 替换四个字节的字符 '\xF0\x9F\x98\x84\xF0\x9F）的解决方案
	 * @param content
	 * @return
	 */
	public static String removeFourChar(String content) {
        byte[] conbyte = content.getBytes();
        for (int i = 0; i < conbyte.length; i++) {
            if ((conbyte[i] & 0xF8) == 0xF0) {
                for (int j = 0; j < 4; j++) {
                    conbyte[i + j] = 0x30;
                }
                i += 3;
            }
        }
        content = new String(conbyte);
        return content.replaceAll("0000", "");
	}

	public static void main(String[] args) {
		String str = "<html>"
				+ "<head><meta http-equiv='content-type' content='text/html;charset=utf-8'></head> "
				+ "<body> " + "<p>ddddddddd</p> " + "</body> " + "</html>";
		System.out.println(htmlToText(str));

        //System.out.println(HtmlToText(str));
		// String strs[] = str.split(",");
		// for (int i = 0; i < strs.length; i++) {
		// String s = "'" + strs[i].trim() + "',";
		// System.out.println(s.trim());
		// }
		// System.out.println(strs.length);
	}
}
