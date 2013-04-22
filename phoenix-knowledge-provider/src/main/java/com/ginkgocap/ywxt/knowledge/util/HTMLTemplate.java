package com.ginkgocap.ywxt.knowledge.util;

public class HTMLTemplate {

	
	public static final String ARTICLE_TITLE = "HTMLTemplate.article.getArticleTitle.value";
	
	public static final String ARTICLE_CONTENT = "HTMLTemplate.article.getArticleContent.value";
	
	public static String getTemplate(){
		StringBuffer HTML = new StringBuffer();
		HTML.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">");
		HTML.append("<HTML><HEAD>");
		HTML.append("<META HTTP-EQUIV=\"CONTENT-TYPE\"");
		HTML.append(" CONTENT=\"text/html; charset=utf-8\"><BODY>");
		HTML.append("文章标题:" + ARTICLE_TITLE);
		HTML.append("<HR><BR/>");
		HTML.append(ARTICLE_CONTENT);
		HTML.append("</BODY></HTML>");
		return HTML.toString();
	}
}
